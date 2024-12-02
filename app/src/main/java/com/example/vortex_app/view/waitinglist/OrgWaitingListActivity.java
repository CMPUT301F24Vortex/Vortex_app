package com.example.vortex_app.view.waitinglist;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.controller.adapter.OrgWaitingListAdapter;
import com.example.vortex_app.R;
import com.example.vortex_app.model.User;
import com.example.vortex_app.model.orgList;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrgWaitingListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewWaitingList;
    private OrgWaitingListAdapter waitingListAdapter;
    private List<User> waitingListEntrants;
    private FirebaseFirestore db;
    private String eventID;
    private Button waitlistDrawButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_waiting_list);

        // Initialize UI elements
        recyclerViewWaitingList = findViewById(R.id.recyclerViewWaitingList);
        waitingListEntrants = new ArrayList<>();
        recyclerViewWaitingList.setLayoutManager(new LinearLayoutManager(this));
        waitingListAdapter = new OrgWaitingListAdapter(waitingListEntrants);
        recyclerViewWaitingList.setAdapter(waitingListAdapter);

        waitlistDrawButton = findViewById(R.id.waitlist_draw_button);
        db = FirebaseFirestore.getInstance();
        eventID = getIntent().getStringExtra("EVENT_ID");

        // Fetch the waiting list and set the draw button's functionality
        fetchWaitingList(eventID);
        waitlistDrawButton.setOnClickListener(v -> selectAndStoreUsers());
    }

    private void fetchWaitingList(String eventID) {
        db.collection("waitlisted")
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            waitingListEntrants.clear();
                            for (DocumentSnapshot document : querySnapshot) {
                                String firstName = document.getString("firstName");
                                String lastName = document.getString("lastName");
                                String userID = document.getString("userID");
                                String eventIDFromDoc = document.getString("eventID");

                                if (firstName != null && lastName != null && userID != null) {
                                    User user = new User(firstName, lastName, userID);
                                    user.setEventID(eventIDFromDoc);
                                    waitingListEntrants.add(user);
                                } else {
                                    Toast.makeText(this, "Missing data for a user.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            waitingListAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "No users found in the waiting list.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Failed to fetch waiting list.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Selects users from the waiting list based on the event's maxPeople limit,
     * moves them to the 'selected' collection, removes them from the 'waitlisted' collection,
     * and logs notifications for each selected and non-selected user based on their preference.
     */
    private void selectAndStoreUsers() {
        db.collection("events")
                .document(eventID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String maxPeopleStr = documentSnapshot.getString("maxPeople");
                        int maxPeople = maxPeopleStr != null ? Integer.parseInt(maxPeopleStr) : 0;

                        // If there are fewer users than maxPeople, select everyone
                        int usersToSelect = Math.min(maxPeople, waitingListEntrants.size());
                        if (usersToSelect > 0) {
                            List<User> shuffledList = new ArrayList<>(waitingListEntrants);
                            Collections.shuffle(shuffledList);
                            List<User> selectedUsers = shuffledList.subList(0, usersToSelect);
                            List<User> nonSelectedUsers = shuffledList.subList(usersToSelect, shuffledList.size());

                            WriteBatch batch = db.batch();

                            // Process selected users
                            for (User user : selectedUsers) {
                                String userID = user.getUserID();
                                // Save the User object directly to the Firestore
                                db.collection("selected_but_not_confirmed")
                                        .add(user)
                                        .addOnSuccessListener(documentReference -> removeFromWaitlisted(user))
                                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to store selected user", Toast.LENGTH_SHORT).show());

                                // Reference to log notification
                                Map<String, Object> selectedNotificationData = new HashMap<>();
                                selectedNotificationData.put("eventID", eventID);
                                selectedNotificationData.put("timestamp", new Date());
                                selectedNotificationData.put("title", "Congratulations!");
                                selectedNotificationData.put("message", "You've been selected for the event.");
                                selectedNotificationData.put("userID", userID);
                                selectedNotificationData.put("status", "unread");
                                selectedNotificationData.put("notificationType", "selected");

                                // storing in notifications collection using batch operations
                                DocumentReference selectedNotificationRef = db.collection("notifications").document();
                                batch.set(selectedNotificationRef, selectedNotificationData);
                            }

                            // Process non-selected users
                            for (User user : nonSelectedUsers) {
                                String userID = user.getUserID();

                                // Log "Better Luck Next Time" notification
                                Map<String, Object> lostNotificationData = new HashMap<>();
                                lostNotificationData.put("eventID", eventID);
                                lostNotificationData.put("timestamp", new Date());
                                lostNotificationData.put("title", "Better Luck Next Time");
                                lostNotificationData.put("message", "You have not been selected for the event.");
                                lostNotificationData.put("userID", userID);
                                lostNotificationData.put("status", "unread");
                                lostNotificationData.put("notificationType", "lost");

                                DocumentReference lostNotificationRef = db.collection("notifications").document();
                                batch.set(lostNotificationRef, lostNotificationData);
                            }

                            // Commit the batch for notifications
                            batch.commit()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Notifications sent successfully.", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to send notifications.", Toast.LENGTH_SHORT).show();
                                        Log.e("OrgWaitingListActivity", "Error committing batch: ", e);
                                    });

                            // Refresh the waiting list
                            fetchWaitingList(eventID);
                            Toast.makeText(this, "Selected users stored successfully.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "No users to select or invalid maxPeople.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Event details not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch event details.", Toast.LENGTH_SHORT).show());
    }

    private void removeFromWaitlisted(User user) {
        db.collection("waitlisted")
                .whereEqualTo("userID", user.getUserID())
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        waitingListEntrants.remove(user);
                                        waitingListAdapter.notifyDataSetChanged();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to remove user from waitlist.", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        Toast.makeText(this, "Failed to fetch waitlist user for removal.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
