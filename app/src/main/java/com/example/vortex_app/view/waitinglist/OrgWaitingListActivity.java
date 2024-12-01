package com.example.vortex_app.view.waitinglist;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.controller.adapter.OrgWaitingListAdapter;
import com.example.vortex_app.R;
import com.example.vortex_app.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

                            for (User user : selectedUsers) {
                                // Save the User object directly to the Firestore
                                db.collection("selected_but_not_confirmed")
                                        .add(user)
                                        .addOnSuccessListener(documentReference -> removeFromWaitlisted(user))
                                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to store selected user", Toast.LENGTH_SHORT).show());
                            }

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
