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
import com.example.vortex_app.model.orgList;
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
                    }
                });
    }

    private void selectAndStoreUsers() {
        // Fetch maxPeople for the event
        db.collection("events")
                .document(eventID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String maxPeopleStr = documentSnapshot.getString("maxPeople");
                        int maxPeople = maxPeopleStr != null ? Integer.parseInt(maxPeopleStr) : 0;


                        if (maxPeople > 0 && waitingListEntrants.size() >= maxPeople) {
                            List<User> shuffledList = new ArrayList<>(waitingListEntrants);
                            Collections.shuffle(shuffledList);
                            List<User> selectedUsers = shuffledList.subList(0, maxPeople);


                            for (User user : selectedUsers) {
                                orgList selectedUser = new orgList(user.getUserID(), eventID);
                                db.collection("selected")
                                        .add(selectedUser)
                                        .addOnSuccessListener(documentReference -> {
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Failed to store selected user", Toast.LENGTH_SHORT).show();
                                        });
                            }
                            Toast.makeText(this, "Selected users stored successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Not enough users in the waiting list or invalid maxPeople", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Event details not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch event details", Toast.LENGTH_SHORT).show());
    }
}
