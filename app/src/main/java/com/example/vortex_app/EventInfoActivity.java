package com.example.vortex_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class EventInfoActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private FirebaseFirestore firestore;
    private User currentUser; // Object to hold the user's profile data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        // Retrieve event details passed via Intent
        String eventID = getIntent().getStringExtra("EVENT_ID");
        String eventName = getIntent().getStringExtra("EVENT_NAME");

        // Set up views
        TextView eventNameTextView = findViewById(R.id.text_event_name);
        Button joinWaitingListButton = findViewById(R.id.button_join_waiting_list);

        eventNameTextView.setText(eventName != null ? eventName : "No Event Name");

        // Initialize Firebase database reference for the event's waiting list
        if (eventID != null) {
            databaseRef = FirebaseDatabase.getInstance().getReference("events").child(eventID).child("waiting_list");
        } else {
            Toast.makeText(this, "Event ID is missing. Cannot join waiting list.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialize Firestore instance
        firestore = FirebaseFirestore.getInstance();

        // Fetch and store user profile data
        fetchUserProfile();

        // Set up the Join Waiting List button
        joinWaitingListButton.setOnClickListener(v -> {
            if (currentUser != null) {
                addUserToWaitingList(currentUser, eventID);
            } else {
                Toast.makeText(EventInfoActivity.this, "User profile not loaded. Cannot join waiting list.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Fetches the user's profile data from Firestore.
     */
    private void fetchUserProfile() {
        DocumentReference docRef = firestore.collection("user_profile").document("XKcYtstm0zrzcdIgvLwb");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    currentUser = new User(
                            document.getString("firstName"),
                            document.getString("lastName"),
                            document.getString("email"),
                            document.getString("contactInfo"),
                            document.getId() // Use Firestore document ID as the unique user ID
                    );
                }
            } else {
                Toast.makeText(this, "Failed to load user profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Adds a user to the event's waiting list in Firebase.
     *
     * @param user    The user to add.
     * @param eventID The event ID.
     */
    private void addUserToWaitingList(User user, String eventID) {
        if (user == null || user.getUserID() == null) {
            Toast.makeText(this, "Invalid user data. Cannot join waiting list.", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseRef.child(user.getUserID()).setValue(user).addOnSuccessListener(aVoid -> {
            Toast.makeText(EventInfoActivity.this, "You have joined the waiting list for event: " + eventID, Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(EventInfoActivity.this, "Failed to join the waiting list. Please try again.", Toast.LENGTH_SHORT).show();
        });
    }
}
