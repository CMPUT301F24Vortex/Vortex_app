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
        String invitationId = intent.getStringExtra("INVITATION_ID"); // Retrieve the invitation ID

        if ("ACTION_ACCEPT".equals(action)) {
            handleAccept(context, invitationId);
        } else if ("ACTION_DECLINE".equals(action)) {
            handleDecline(context, invitationId);
        }
    }

    private void handleAccept(Context context, String invitationId) {
        // Update Firestore to set accepted to true
        db.collection("invitations").document(invitationId)
                .update("accepted", true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Invitation Accepted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error accepting invitation", Toast.LENGTH_SHORT).show();
                });
    }

    private void handleDecline(Context context, String invitationId) {
        // Update Firestore to set accepted to false or delete the invitation
        db.collection("invitations").document(invitationId)
                .update("accepted", false)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Invitation Declined", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error declining invitation", Toast.LENGTH_SHORT).show();
                });
    }
}
