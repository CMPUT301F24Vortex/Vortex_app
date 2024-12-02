package com.example.vortex_app.controller.adapter;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

public class NotificationActionHandler {

    private FirebaseFirestore db;

    public NotificationActionHandler() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Moves a document from sourceCollection to targetCollection.
     *
     * @param sourceCollection The source Firestore collection.
     * @param targetCollection The target Firestore collection.
     * @param userID           The user ID.
     * @param eventID          The event ID.
     * @param context          The context to show Toast messages.
     * @param successMessage   The message to show on success.
     * @param failureMessage   The message to show on failure.
     */
    public void moveDocument(String sourceCollection, String targetCollection, String userID, String eventID, Context context, String successMessage, String failureMessage) {
        db.collection(sourceCollection)
                .whereEqualTo("userID", userID)
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        DocumentReference sourceRef = doc.getReference();
                        DocumentReference targetRef = db.collection(targetCollection).document();

                        WriteBatch batch = db.batch();
                        batch.set(targetRef, doc.getData());
                        batch.delete(sourceRef);

                        batch.commit()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show();
                                    Log.d("NotificationActionHandler", "User moved to '" + targetCollection + "'.");
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show();
                                    Log.e("NotificationActionHandler", "Transaction failed: " + e.getMessage());
                                });
                    } else {
                        Toast.makeText(context, "Document not found.", Toast.LENGTH_SHORT).show();
                        Log.e("NotificationActionHandler", "Document not found for userID: " + userID + ", eventID: " + eventID);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error fetching document.", Toast.LENGTH_SHORT).show();
                    Log.e("NotificationActionHandler", "Error fetching document: ", e);
                });
    }
}
