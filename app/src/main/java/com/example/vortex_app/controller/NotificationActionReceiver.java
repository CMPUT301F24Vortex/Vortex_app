package com.example.vortex_app.controller;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Map;

/**
 * {@code NotificationActionReceiver} is a {@link BroadcastReceiver} that handles actions
 * from notification buttons, such as accepting or declining an invitation, staying on or leaving a waiting list.
 * It updates the entrant's status in Firestore based on the user's response.
 */
public class NotificationActionReceiver extends BroadcastReceiver {
    private FirebaseFirestore db;

    // Define action strings (should match those in NotificationsActivity)
    public static final String ACTION_ACCEPT = "ACTION_ACCEPT";
    public static final String ACTION_DECLINE = "ACTION_DECLINE";
    public static final String ACTION_STAY = "ACTION_STAY";
    public static final String ACTION_LEAVE = "ACTION_LEAVE";

    @Override
    public void onReceive(Context context, Intent intent) {
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        String action = intent.getAction();
        String userID = intent.getStringExtra("userID");
        String eventID = intent.getStringExtra("eventID");

        if (userID == null || eventID == null) {
            Log.e("NotificationActionReceiver", "Missing userID or eventID.");
            return;
        }

        switch (action) {
            case ACTION_ACCEPT:
                handleAccept(context, userID, eventID);
                break;
            case ACTION_DECLINE:
                handleDecline(context, userID, eventID);
                break;
            case ACTION_STAY:
                handleStay(context, userID, eventID);
                break;
            case ACTION_LEAVE:
                handleLeave(context, userID, eventID);
                break;
            default:
                Log.w("NotificationActionReceiver", "Unknown action received: " + action);
                break;
        }
    }

    /**
     * Handles the "Accept" action. Moves the entrant from 'selected' to 'enrolled'.
     *
     * @param context  The {@link Context} in which the receiver is running.
     * @param userID   The ID of the user.
     * @param eventID  The ID of the event.
     */
    private void handleAccept(Context context, String userID, String eventID) {
        // Query to find the user's document in 'selected' collection
        db.collection("selected")
                .whereEqualTo("userID", userID)
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot selectedDoc = querySnapshot.getDocuments().get(0);
                        DocumentReference selectedRef = selectedDoc.getReference();
                        DocumentReference enrolledRef = db.collection("enrolled").document();

                        WriteBatch batch = db.batch();
                        batch.set(enrolledRef, selectedDoc.getData());
                        batch.delete(selectedRef);

                        batch.commit()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "You have been enrolled!", Toast.LENGTH_SHORT).show();
                                    Log.d("NotificationActionReceiver", "User moved to 'enrolled'.");
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Error enrolling in the event.", Toast.LENGTH_SHORT).show();
                                    Log.e("NotificationActionReceiver", "Transaction failed: " + e.getMessage());
                                });
                    } else {
                        Toast.makeText(context, "Selected document not found.", Toast.LENGTH_SHORT).show();
                        Log.e("NotificationActionReceiver", "Selected document not found for userID: " + userID + ", eventID: " + eventID);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error fetching selected document.", Toast.LENGTH_SHORT).show();
                    Log.e("NotificationActionReceiver", "Error fetching selected document: ", e);
                });
    }

    /**
     * Handles the "Decline" action. Moves the entrant from 'selected' to 'cancelled'.
     *
     * @param context  The {@link Context} in which the receiver is running.
     * @param userID   The ID of the user.
     * @param eventID  The ID of the event.
     */
    private void handleDecline(Context context, String userID, String eventID) {
        // Query to find the user's document in 'selected' collection
        db.collection("selected")
                .whereEqualTo("userID", userID)
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot selectedDoc = querySnapshot.getDocuments().get(0);
                        DocumentReference selectedRef = selectedDoc.getReference();
                        DocumentReference cancelledRef = db.collection("cancelled").document();

                        WriteBatch batch = db.batch();
                        batch.set(cancelledRef, selectedDoc.getData());
                        batch.delete(selectedRef);

                        batch.commit()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "You have declined the invitation.", Toast.LENGTH_SHORT).show();
                                    Log.d("NotificationActionReceiver", "User moved to 'cancelled'.");
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Error declining the invitation.", Toast.LENGTH_SHORT).show();
                                    Log.e("NotificationActionReceiver", "Transaction failed: " + e.getMessage());
                                });
                    } else {
                        Toast.makeText(context, "Selected document not found.", Toast.LENGTH_SHORT).show();
                        Log.e("NotificationActionReceiver", "Selected document not found for userID: " + userID + ", eventID: " + eventID);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error fetching selected document.", Toast.LENGTH_SHORT).show();
                    Log.e("NotificationActionReceiver", "Error fetching selected document: ", e);
                });
    }

    /**
     * Handles the "Stay" action. Keeps the entrant in the 'waitlisted' collection.
     *
     * @param context  The {@link Context} in which the receiver is running.
     * @param userID   The ID of the user.
     * @param eventID  The ID of the event.
     */
    private void handleStay(Context context, String userID, String eventID) {
        // No action needed as the user remains in 'waitlisted'
        Toast.makeText(context, "You remain on the waiting list.", Toast.LENGTH_SHORT).show();
        Log.d("NotificationActionReceiver", "User chose to stay on the waiting list.");
    }

    /**
     * Handles the "Leave" action. Moves the entrant from 'waitlisted' to 'cancelled'.
     *
     * @param context  The {@link Context} in which the receiver is running.
     * @param userID   The ID of the user.
     * @param eventID  The ID of the event.
     */
    private void handleLeave(Context context, String userID, String eventID) {
        // Query to find the user's document in 'waitlisted' collection
        db.collection("waitlisted")
                .whereEqualTo("userID", userID)
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot waitlistedDoc = querySnapshot.getDocuments().get(0);
                        DocumentReference waitlistedRef = waitlistedDoc.getReference();
                        DocumentReference cancelledRef = db.collection("cancelled").document();

                        WriteBatch batch = db.batch();
                        batch.set(cancelledRef, waitlistedDoc.getData());
                        batch.delete(waitlistedRef);

                        batch.commit()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "You have left the waiting list.", Toast.LENGTH_SHORT).show();
                                    Log.d("NotificationActionReceiver", "User moved to 'cancelled'.");
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Error leaving the waiting list.", Toast.LENGTH_SHORT).show();
                                    Log.e("NotificationActionReceiver", "Transaction failed: " + e.getMessage());
                                });
                    } else {
                        Toast.makeText(context, "Waitlisted document not found.", Toast.LENGTH_SHORT).show();
                        Log.e("NotificationActionReceiver", "Waitlisted document not found for userID: " + userID + ", eventID: " + eventID);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error fetching waitlisted document.", Toast.LENGTH_SHORT).show();
                    Log.e("NotificationActionReceiver", "Error fetching waitlisted document: ", e);
                });
    }
}
