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
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final int REQUEST_CODE_NOTIFICATION_PERMISSION = 1;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        //super.onNewToken(token);
        Log.d("FCM Token", "Refreshed token: " + token);
        //sendTokenToServer(token);
        saveTokenToPreferences(token);
        // Save the token to Firestore for each user
        saveTokenToFirestore(token);
    }

    private void saveTokenToPreferences(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("FCM_Prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fcm_token", token);
        editor.apply();
    }

    private void saveTokenToFirestore(String token) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = getCurrentUserId(); // retrieve the current user's ID

        db.collection("users").document(userId).update("fcmToken", token)
                .addOnSuccessListener(aVoid -> Log.d("FCM", "Token saved successfully"))
                .addOnFailureListener(e -> Log.e("FCM", "Error saving token", e));
    }

    public String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            // User is not signed in
            return null;
        }
    }



    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check for notification permission (Android 13 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Notification permission not granted. Notification not shown.");
            return;  // Exit if permission is not granted
        }

        super.onMessageReceived(remoteMessage);

        // Extract the notification data


        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String notificationId = remoteMessage.getMessageId();  // Unique identifier

        // Store notification in Firestore
        saveNotificationToFirestore(title, message, notificationId);
        showNotification(title, message);


        //String title = remoteMessage.getNotification().getTitle();
        //String message = remoteMessage.getNotification().getBody();
        //String eventId = remoteMessage.getData().get("event_id");

        // Store notification in Firestore
        //storeNotificationInFirestore(title, message, eventId);

        // Show the notification on the device
        //sendNotification(title, message);


    }

    private void sendNotification(String title, String messageBody) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create notification channel (for Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("your_channel_id",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "your_channel_id")
                .setSmallIcon(R.drawable.notification) // replace with your app icon
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());
    }
    private void saveNotificationToFirestore(String title, String message, String notificationId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("title", title);
        notificationData.put("message", message);
        notificationData.put("status", "unread");
        notificationData.put("timestamp", FieldValue.serverTimestamp());

        db.collection("notifications")
                .document(notificationId) // Use notification ID as the document ID
                .set(notificationData)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Notification stored successfully"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error storing notification", e));
    }


    private void showNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            notificationManager.notify(0, builder.build());
        } else {
            Log.e(TAG, "Notification permission not granted");
        }
    }



    private void createWinNotification(String title, String message, String invitationId) {
        Intent acceptIntent = new Intent(this, NotificationActionReceiver.class);
        acceptIntent.setAction("ACTION_ACCEPT");
        acceptIntent.putExtra("invitationId", invitationId);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(
                this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent declineIntent = new Intent(this, NotificationActionReceiver.class);
        declineIntent.setAction("ACTION_DECLINE");
        declineIntent.putExtra("invitationId", invitationId);
        PendingIntent declinePendingIntent = PendingIntent.getBroadcast(
                this, 1, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "INVITATION_CHANNEL_ID")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.icon_accept, "Accept", acceptPendingIntent)
                //change the icon_accept to icon_decline
                .addAction(R.drawable.icon_accept, "Decline", declinePendingIntent)
                .setAutoCancel(true);


        // Check for notification permission
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            manager.notify(1, builder.build());
        } else {
            Log.e(TAG, "Notification permission not granted");
        }
    }


    private void createLoseNotification(String title, String message,String invitationId) {
        Intent stayIntent = new Intent(this, NotificationActionReceiver.class);
        stayIntent.setAction("ACTION_STAY");
        stayIntent.putExtra("invitationId", invitationId);
        PendingIntent stayPendingIntent = PendingIntent.getBroadcast(
                this, 2, stayIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent leaveIntent = new Intent(this, NotificationActionReceiver.class);
        leaveIntent.setAction("ACTION_LEAVE");
        leaveIntent.putExtra("invitationId", invitationId);
        PendingIntent leavePendingIntent = PendingIntent.getBroadcast(
                this, 3, leaveIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "INVITATION_CHANNEL_ID")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                //change the icon_accept to icon_stay_in_waiting_list
                .addAction(R.drawable.icon_accept, "Stay", stayPendingIntent)
                //and change icon_accept icon_leave_waiting_list
                .addAction(R.drawable.icon_accept, "Leave", leavePendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            manager.notify(2, builder.build());
        } else {
            Log.e(TAG, "Notification permission not granted");
        }

    }

}

