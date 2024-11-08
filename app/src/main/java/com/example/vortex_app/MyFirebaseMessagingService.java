package com.example.vortex_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code MyFirebaseMessagingService} is a Firebase Cloud Messaging (FCM) service that handles
 * incoming FCM messages and token management. It processes notification messages, saves tokens to Firestore,
 * and creates various types of notifications on the device.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final int REQUEST_CODE_NOTIFICATION_PERMISSION = 1;
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when the FCM token is refreshed. Saves the token to shared preferences and Firestore.
     *
     * @param token The new FCM token.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM Token", "Refreshed token: " + token);
        saveTokenToPreferences(token);
        saveTokenToFirestore(token);
    }

    /**
     * Saves the FCM token to shared preferences for local access.
     *
     * @param token The FCM token to be saved.
     */
    private void saveTokenToPreferences(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("FCM_Prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fcm_token", token);
        editor.apply();
    }

    /**
     * Saves the FCM token to Firestore under the current user's document.
     *
     * @param token The FCM token to be saved in Firestore.
     */
    private void saveTokenToFirestore(String token) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = getCurrentUserId();
        db.collection("users").document(userId).update("fcmToken", token)
                .addOnSuccessListener(aVoid -> Log.d("FCM", "Token saved successfully"))
                .addOnFailureListener(e -> Log.e("FCM", "Error saving token", e));
    }

    /**
     * Retrieves the current Firebase authenticated user's ID.
     *
     * @return The user ID if available, otherwise {@code null}.
     */
    public String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    /**
     * Called when an FCM message is received. Extracts the notification data, stores it in Firestore,
     * and displays a notification on the device.
     *
     * @param remoteMessage The received {@link RemoteMessage} containing notification data.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Notification permission not granted. Notification not shown.");
            return;
        }

        super.onMessageReceived(remoteMessage);

        // Extract notification data
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String notificationId = remoteMessage.getMessageId();

        // Save notification to Firestore and show it on the device
        saveNotificationToFirestore(title, message, notificationId);
        showNotification(title, message);
    }

    /**
     * Saves a notification message to Firestore with details including title, message, and status.
     *
     * @param title          The title of the notification.
     * @param message        The message content of the notification.
     * @param notificationId A unique identifier for the notification.
     */
    private void saveNotificationToFirestore(String title, String message, String notificationId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("title", title);
        notificationData.put("message", message);
        notificationData.put("status", "unread");
        notificationData.put("timestamp", FieldValue.serverTimestamp());

        db.collection("notifications")
                .document(notificationId)
                .set(notificationData)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Notification stored successfully"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error storing notification", e));
    }

    /**
     * Displays a notification on the device with the given title and message.
     *
     * @param title   The title of the notification.
     * @param message The message content of the notification.
     */
    private void showNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Check for notification permission
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(0, builder.build());
        } else {
            Log.e(TAG, "Notification permission not granted");
        }
    }

    /**
     * Creates a notification for a winning invitation with accept and decline options.
     *
     * @param title        The title of the invitation notification.
     * @param message      The message content of the invitation notification.
     * @param invitationId The unique ID for the invitation.
     */
    private void createWinNotification(String title, String message, String invitationId) {
        Intent acceptIntent = new Intent(this, NotificationActionReceiver.class);
        acceptIntent.setAction("ACTION_ACCEPT");
        acceptIntent.putExtra("invitationId", invitationId);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent declineIntent = new Intent(this, NotificationActionReceiver.class);
        declineIntent.setAction("ACTION_DECLINE");
        declineIntent.putExtra("invitationId", invitationId);
        PendingIntent declinePendingIntent = PendingIntent.getBroadcast(this, 1, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "INVITATION_CHANNEL_ID")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.icon_accept, "Accept", acceptPendingIntent)
                .addAction(R.drawable.icon_accept, "Decline", declinePendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(this).notify(1, builder.build());
        } else {
            Log.e(TAG, "Notification permission not granted");
        }
    }

    /**
     * Creates a notification for a losing invitation with options to stay or leave the waiting list.
     *
     * @param title        The title of the waiting list notification.
     * @param message      The message content of the notification.
     * @param invitationId The unique ID for the invitation.
     */
    private void createLoseNotification(String title, String message, String invitationId) {
        Intent stayIntent = new Intent(this, NotificationActionReceiver.class);
        stayIntent.setAction("ACTION_STAY");
        stayIntent.putExtra("invitationId", invitationId);
        PendingIntent stayPendingIntent = PendingIntent.getBroadcast(this, 2, stayIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent leaveIntent = new Intent(this, NotificationActionReceiver.class);
        leaveIntent.setAction("ACTION_LEAVE");
        leaveIntent.putExtra("invitationId", invitationId);
        PendingIntent leavePendingIntent = PendingIntent.getBroadcast(this, 3, leaveIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "INVITATION_CHANNEL_ID")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.icon_accept, "Stay", stayPendingIntent)
                .addAction(R.drawable.icon_accept, "Leave", leavePendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(this).notify(2, builder.build());
        } else {
            Log.e(TAG, "Notification permission not granted");
        }
    }
}
