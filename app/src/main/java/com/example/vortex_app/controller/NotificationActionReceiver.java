package com.example.vortex_app.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.vortex_app.controller.adapter.NotificationActionHandler;

public class NotificationActionReceiver extends BroadcastReceiver {
    // Define unique action strings
    public static final String ACTION_ACCEPT = "com.example.vortex_app.ACTION_ACCEPT";
    public static final String ACTION_DECLINE = "com.example.vortex_app.ACTION_DECLINE";
    public static final String ACTION_STAY = "com.example.vortex_app.ACTION_STAY";
    public static final String ACTION_LEAVE = "com.example.vortex_app.ACTION_LEAVE";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String userID = intent.getStringExtra("userID");
        String eventID = intent.getStringExtra("eventID");

        Log.d("NotificationActionReceiver", "Received action: " + action + ", userID: " + userID + ", eventID: " + eventID);

        if (userID == null || eventID == null) {
            Log.e("NotificationActionReceiver", "Missing userID or eventID.");
            return;
        }

        NotificationActionHandler handler = new NotificationActionHandler();

        switch (action) {
            case ACTION_ACCEPT:
                Log.d("NotificationActionReceiver", "Handling ACTION_ACCEPT");
                handler.moveDocument(
                        "selected_but_not_confirmed",
                        "final",
                        userID,
                        eventID,
                        context,
                        "You have been enrolled!",
                        "Error enrolling in the event."
                );
                break;
            case ACTION_DECLINE:
                Log.d("NotificationActionReceiver", "Handling ACTION_DECLINE");
                handler.moveDocument(
                        "selected_but_not_confirmed",
                        "waitlisted",
                        userID,
                        eventID,
                        context,
                        "You have declined the invitation.",
                        "Error declining the invitation."
                );
                break;
            case ACTION_STAY:
                Log.d("NotificationActionReceiver", "Handling ACTION_STAY");
                Toast.makeText(context, "You remain on the waiting list.", Toast.LENGTH_SHORT).show();
                Log.d("NotificationActionReceiver", "User chose to stay on the waiting list.");
                break;
            case ACTION_LEAVE:
                Log.d("NotificationActionReceiver", "Handling ACTION_LEAVE");
                handler.moveDocument(
                        "waitlisted",
                        "cancelled",
                        userID,
                        eventID,
                        context,
                        "You have left the waiting list.",
                        "Error leaving the waiting list."
                );
                break;
            default:
                Log.w("NotificationActionReceiver", "Unknown action received: " + action);
                break;
        }
    }

}
