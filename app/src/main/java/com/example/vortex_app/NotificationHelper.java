package com.example.vortex_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

/**
 * {@code NotificationHelper} is a utility class that provides functionality to create a notification
 * channel for the app. This channel is required for displaying notifications on devices running
 * Android 8.0 (Oreo) and above.
 *
 * <p>This class ensures that notifications are categorized under a specific channel with custom settings,
 * improving the user experience and notification management.
 */
public class NotificationHelper {

    /**
     * Creates a notification channel for invitation notifications if the device is running Android 8.0 (API level 26) or higher.
     * This channel is used to display high-priority notifications for invitations and related actions.
     *
     * @param context The {@link Context} used to access system services and create the notification channel.
     */
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "INVITATION_CHANNEL_ID",
                    "Invitation Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for invitation notifications");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
