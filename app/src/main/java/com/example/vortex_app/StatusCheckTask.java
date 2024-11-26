package com.example.vortex_app;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StatusCheckTask {

    public static void performStatusCheck(Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // User is not logged in; exit
            return;
        }
        String currentUserId = user.getUid();

        // Collections to check
        String[] statusCollections = {"Waitlisted", "Selected", "Enrolled", "Canceled"};
        SharedPreferences prefs = context.getSharedPreferences("status_prefs", Context.MODE_PRIVATE);

        for (String collectionName : statusCollections) {
            long lastCheckTime = prefs.getLong("lastCheckTime_" + collectionName, 0);

            db.collection(collectionName)
                    .whereEqualTo("userID", currentUserId)
                    .whereGreaterThan("timestamp", new Date(lastCheckTime))
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Generate notification based on the collection
                                generateStatusNotification(context, collectionName, document);
                                // Save notification to Firestore
                                saveNotificationToFirestore(db, collectionName, document);
                            }

                            // Update last check time
                            prefs.edit().putLong("lastCheckTime_" + collectionName, System.currentTimeMillis()).apply();
                        } else {
                            Log.w("StatusCheckTask", "Error checking " + collectionName, task.getException());
                        }
                    });
        }
    }

    private static void generateStatusNotification(Context context, String statusCollection, DocumentSnapshot document) {
        // Create notification channel if needed
        createNotificationChannel(context);

        NotificationData notificationData = getNotificationData(statusCollection, document);

        Intent intent = new Intent(context, NotificationDetailActivity.class);
        intent.putExtra("eventID", notificationData.getEventID());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "status_channel_id")
                .setSmallIcon(notificationData.getIconResId())
                .setContentTitle(notificationData.getTitle())
                .setContentText(notificationData.getMessage())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = document.getId().hashCode();

        // Check if the permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(notificationId, builder.build());
            } else {
                Log.w("StatusCheckTask", "POST_NOTIFICATIONS permission not granted, cannot display notification.");
            }
        } else {
            notificationManager.notify(notificationId, builder.build());
        }
    }

    private static void saveNotificationToFirestore(FirebaseFirestore db, String statusCollection, DocumentSnapshot document) {
        NotificationData notificationData = getNotificationData(statusCollection, document);

        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("userID", notificationData.getUserID());
        notificationMap.put("eventID", notificationData.getEventID());
        notificationMap.put("title", notificationData.getTitle());
        notificationMap.put("message", notificationData.getMessage());
        notificationMap.put("timestamp", new Date());
        notificationMap.put("status", "unread");
        notificationMap.put("type", notificationData.getType());

        db.collection("notifications")
                .add(notificationMap)
                .addOnSuccessListener(documentReference -> {
                    Log.d("StatusCheckTask", "Notification saved to Firestore");
                })
                .addOnFailureListener(e -> {
                    Log.w("StatusCheckTask", "Error saving notification to Firestore", e);
                });
    }

    private static NotificationData getNotificationData(String statusCollection, DocumentSnapshot document) {
        String title = "";
        String message = "";
        int iconResId = R.drawable.notification; // Default icon
        String type = "";
        String eventID = document.getString("eventID");
        String userID = document.getString("userID");

        switch (statusCollection) {
            case "Waitlisted":
                title = "Waitlisted";
                message = "You have been added to the waitlist for an event.";
                iconResId = R.drawable.notification; // Custom icon for waitlisted
                type = "waitlisted";
                break;
            case "Selected":
                title = "Selected";
                message = "Congratulations! You have been selected for the event.";
                iconResId = R.drawable.notification;
                type = "selected";
                break;
            case "Enrolled":
                title = "Enrolled";
                message = "You are now enrolled in the event.";
                iconResId = R.drawable.notification;
                type = "enrolled";
                break;
            case "Canceled":
                title = "Canceled";
                message = "Your enrollment has been canceled.";
                iconResId = R.drawable.notification;
                type = "canceled";
                break;
        }

        return new NotificationData(title, message, eventID, userID, iconResId, type);
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Status Updates";
            String description = "Notifications for status changes in events";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("status_channel_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
