package com.example.vortex_app.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * {@code NotificationActionReceiver} is a {@link BroadcastReceiver} that handles user actions
 * from notifications, such as accepting, declining, staying on, or leaving a waiting list.
 * It updates the corresponding invitation status in Firestore based on the user's response.
 *
 * <p>This receiver performs actions in response to user interactions with notifications, such as
 * updating the status of invitations or providing feedback, all without the need for the user
 * to open an activity.</p>
 */
public class NotificationActionReceiver extends BroadcastReceiver {
    private FirebaseFirestore db;

    /**
     * Called when the receiver receives a broadcasted intent. It determines the action taken
     * by the user (e.g., accept, decline, stay, leave) and calls the appropriate method to handle
     * that action by updating Firestore or showing a confirmation message.
     *
     * @param context The {@link Context} in which the receiver is running.
     * @param intent  The {@link Intent} being received, containing the action and invitation ID.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        String action = intent.getAction();
        String invitationId = intent.getStringExtra("invitationId"); // Retrieve the invitation ID

        switch (action) {
            case "ACTION_ACCEPT":
                handleAccept(context, invitationId);
                break;
            case "ACTION_DECLINE":
                handleDecline(context, invitationId);
                break;
            case "ACTION_STAY":
                handleStay(context);
                break;
            case "ACTION_LEAVE":
                handleLeave(context, invitationId);
                break;
            default:
                break;
        }
    }

    /**
     * Handles the "Accept" action for an invitation. Updates the invitation status in Firestore to "accepted".
     *
     * @param context      The {@link Context} in which the receiver is running.
     * @param invitationId The ID of the invitation to be updated.
     */
    private void handleAccept(Context context, String invitationId) {
        db.collection("invitations").document(invitationId)
                .update("status", "accepted")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Invitation Accepted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error accepting invitation", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Handles the "Decline" action for an invitation. Updates the invitation status in Firestore to "declined".
     *
     * @param context      The {@link Context} in which the receiver is running.
     * @param invitationId The ID of the invitation to be updated.
     */
    private void handleDecline(Context context, String invitationId) {
        db.collection("invitations").document(invitationId)
                .update("status", "declined")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Invitation Declined", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error declining invitation", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Handles the "Stay" action, which keeps the user on the waiting list. This method displays
     * a confirmation message to the user but does not require a Firestore update.
     *
     * @param context The {@link Context} in which the receiver is running.
     */
    private void handleStay(Context context) {
        Toast.makeText(context, "You have chosen to stay on the waiting list.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Handles the "Leave" action for a waiting list. Updates the invitation status in Firestore to "left_waitlist".
     *
     * @param context      The {@link Context} in which the receiver is running.
     * @param invitationId The ID of the invitation to be updated.
     */
    private void handleLeave(Context context, String invitationId) {
        db.collection("invitations").document(invitationId)
                .update("status", "left_waitlist")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "You have left the waiting list", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error leaving the waiting list", Toast.LENGTH_SHORT).show();
                });
    }
}
