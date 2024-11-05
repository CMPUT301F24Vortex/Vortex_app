package com.example.vortex_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationActionReceiver extends BroadcastReceiver {
    private FirebaseFirestore db;

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

    private void handleAccept(Context context, String invitationId) {
        // Update Firestore to set accepted to true
        db.collection("invitations").document(invitationId)
                .update("status", "accepted")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Invitation Accepted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error accepting invitation", Toast.LENGTH_SHORT).show();
                });
    }

    private void handleDecline(Context context, String invitationId) {
        // Update Firestore to set status to declined
        db.collection("invitations").document(invitationId)
                .update("status", "declined")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Invitation Declined", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error declining invitation", Toast.LENGTH_SHORT).show();
                });
    }

    private void handleStay(Context context) {
        // For "Stay" action, no Firestore update may be needed; show confirmation to the user
        Toast.makeText(context, "You have chosen to stay on the waiting list.", Toast.LENGTH_SHORT).show();
    }

    private void handleLeave(Context context, String invitationId) {
        // Update Firestore to indicate the user has left the waiting list
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
