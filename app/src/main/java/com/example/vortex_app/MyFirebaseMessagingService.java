package com.example.vortex_app;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.Manifest;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();

            // Call the method to create the notification
            createNotification(title, message);
        }

        // Check if message contains a data payload (if applicable)
        if (remoteMessage.getData().size() > 0) {
            // Handle the data payload
            Log.d(TAG, "Message data: " + remoteMessage.getData());
            // You can also handle data messages here
        }
    }

    private void createNotification(String title, String message) {

        // Check for Notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Handle lack of permission (e.g., show a message or log)
                Log.e(TAG, "Notification permission not granted");
                return; // Exit if permission is not granted
            }
        }
        // Define intents for Accept and Decline actions
        Intent acceptIntent = new Intent(this, NotificationActionReceiver.class);
        acceptIntent.setAction("ACTION_ACCEPT");
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(
                this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        Intent declineIntent = new Intent(this, NotificationActionReceiver.class);
        declineIntent.setAction("ACTION_DECLINE");
        PendingIntent declinePendingIntent = PendingIntent.getBroadcast(
                this, 1, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "INVITATION_CHANNEL_ID")
                .setSmallIcon(R.drawable.notification) // Replace with your notification icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.icon_accept, "Accept", acceptPendingIntent)
                .addAction(R.drawable.icon_decline, "Decline", declinePendingIntent)
                .setAutoCancel(true); // Dismiss the notification when tapped

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(1, builder.build());
    }
}
