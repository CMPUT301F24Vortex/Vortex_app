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

        // Initialize the RecyclerView and the adapter
        recyclerViewWaitingList = findViewById(R.id.recyclerViewWaitingList);
        waitingListEntrants = new ArrayList<>();
        recyclerViewWaitingList.setLayoutManager(new LinearLayoutManager(this));
        waitingListAdapter = new OrgWaitingListAdapter(waitingListEntrants);
        recyclerViewWaitingList.setAdapter(waitingListAdapter);

        waitlistDrawButton = findViewById(R.id.waitlist_draw_button);
        db = FirebaseFirestore.getInstance();
        eventID = getIntent().getStringExtra("EVENT_ID");

        // Fetch the waiting list
        fetchWaitingList(eventID);

        // Set up the draw button
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
                                String userID = document.getString("userID"); // Assuming userID is stored

                                User user = new User(firstName, lastName, userID);
                                waitingListEntrants.add(user);
                            }
                            waitingListAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(this, "Failed to fetch waiting list", Toast.LENGTH_SHORT).show();
                        Log.e("OrgWaitingListActivity", "Error fetching waitlisted users: ", task.getException());
                    }
                });
    }




    /**
     * Selects users from the waiting list based on the event's maxPeople limit,
     * moves them to the 'selected' collection, removes them from the 'waitlisted' collection,
     * and logs notifications for each selected and non-selected user based on their preference.
     */
    private void selectAndStoreUsers() {
        // Fetch maxPeople for the event
        db.collection("events")
                .document(eventID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String maxPeopleStr = documentSnapshot.getString("maxPeople");
                        int maxPeople = 0;
                        try {
                            maxPeople = maxPeopleStr != null ? Integer.parseInt(maxPeopleStr) : 0;
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Invalid maxPeople value.", Toast.LENGTH_SHORT).show();
                            Log.e("OrgWaitingListActivity", "Invalid maxPeople value: ", e);
                            return;
                        }

                        if (maxPeople > 0 && waitingListEntrants.size() >= maxPeople) {
                            // Shuffle the list to randomize selection
                            List<User> shuffledList = new ArrayList<>(waitingListEntrants);
                            Collections.shuffle(shuffledList);
                            List<User> selectedUsers = shuffledList.subList(0, maxPeople);
                            List<User> nonSelectedUsers = shuffledList.subList(maxPeople, shuffledList.size());

                            // Initialize Firestore WriteBatch
                            WriteBatch batch = db.batch();

                            // Process selected users
                            for (User user : selectedUsers) {
                                String userID = user.getUserID();

                                // Query to find the specific document in 'waitlisted' for this user and event
                                db.collection("waitlisted")
                                        .whereEqualTo("userID", userID)
                                        .whereEqualTo("eventID", eventID)
                                        .get()
                                        .addOnSuccessListener(querySnapshot -> {
                                            if (!querySnapshot.isEmpty()) {
                                                DocumentSnapshot waitlistedDoc = querySnapshot.getDocuments().get(0);
                                                DocumentReference waitlistedRef = waitlistedDoc.getReference();

                                                // Reference to add to 'selected'
                                                DocumentReference selectedRef = db.collection("selected").document();
                                                orgList selectedUserObj = new orgList(userID, eventID);
                                                batch.set(selectedRef, selectedUserObj);

                                                // Reference to delete from 'waitlisted'
                                                batch.delete(waitlistedRef);

                                                // Reference to log notification
                                                Map<String, Object> selectedNotificationData = new HashMap<>();
                                                selectedNotificationData.put("eventID", eventID);
                                                selectedNotificationData.put("timestamp", new Date());
                                                selectedNotificationData.put("title", "Congratulations!");
                                                selectedNotificationData.put("message", "You've been selected for the event.");
                                                selectedNotificationData.put("userID", userID);
                                                selectedNotificationData.put("status", "unread");

                                                DocumentReference selectedNotificationRef = db.collection("notifications").document();
                                                batch.set(selectedNotificationRef, selectedNotificationData);

                                                // Commit the batch after all selected users are processed
                                                // (This simplistic approach works if the number is small)
                                                batch.commit()
                                                        .addOnSuccessListener(aVoid -> {
                                                            Toast.makeText(this, "Draw completed successfully.", Toast.LENGTH_SHORT).show();
                                                            // Refresh the waiting list
                                                            fetchWaitingList(eventID);
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Toast.makeText(this, "Failed to perform the draw.", Toast.LENGTH_SHORT).show();
                                                            Log.e("OrgWaitingListActivity", "Error committing batch: ", e);
                                                        });
                                            } else {
                                                Toast.makeText(this, "Waitlisted document not found for user: " + userID, Toast.LENGTH_SHORT).show();
                                                Log.e("OrgWaitingListActivity", "Waitlisted document not found for userID: " + userID);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Error fetching waitlisted document.", Toast.LENGTH_SHORT).show();
                                            Log.e("OrgWaitingListActivity", "Error fetching waitlisted document: ", e);
                                        });
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

                                DocumentReference lostNotificationRef = db.collection("notifications").document();
                                batch.set(lostNotificationRef, lostNotificationData);
                            }

                            // Commit the batch for non-selected users
                            batch.commit()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Draw completed successfully.", Toast.LENGTH_SHORT).show();
                                        // Refresh the waiting list
                                        fetchWaitingList(eventID);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to perform the draw.", Toast.LENGTH_SHORT).show();
                                        Log.e("OrgWaitingListActivity", "Error committing batch: ", e);
                                    });

                        } else {
                            Toast.makeText(this, "Not enough users in the waiting list or invalid maxPeople.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Event details not found.", Toast.LENGTH_SHORT).show();
                        Log.e("OrgWaitingListActivity", "Event document does not exist.");
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch event details.", Toast.LENGTH_SHORT).show();
                    Log.e("OrgWaitingListActivity", "Error fetching event details: ", e);
                });
    }


}
