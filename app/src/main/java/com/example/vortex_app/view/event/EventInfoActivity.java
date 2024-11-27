package com.example.vortex_app.view.event;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.view.entrant.EntrantActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EventInfoActivity extends AppCompatActivity {

    private static final String TAG = "EventInfoActivity";

    private TextView eventNameTextView, classDayTextView, timeTextView, periodTextView, locationTextView, priceTextView;
    private Button joinWaitingListButton;

    private String eventID;
    private String deviceID; // Use device ID as the user identifier
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve device ID
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "Device ID: " + deviceID);

        // Initialize views
        eventNameTextView = findViewById(R.id.text_event_name);
        classDayTextView = findViewById(R.id.text_class_day);
        timeTextView = findViewById(R.id.text_time);
        periodTextView = findViewById(R.id.text_period);
        locationTextView = findViewById(R.id.text_location);
        priceTextView = findViewById(R.id.text_price);
        joinWaitingListButton = findViewById(R.id.button_join_waiting_list);

        // Get event ID from the intent
        eventID = getIntent().getStringExtra("EVENT_ID");

        if (eventID == null || eventID.isEmpty()) {
            Toast.makeText(this, "Event ID not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Load event details
        loadEventDetails();

        // Set up "Join Waiting List" button
        joinWaitingListButton.setOnClickListener(view -> checkIfAlreadyJoined());
    }

    private void loadEventDetails() {
        db.collection("events")
                .document(eventID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String eventName = documentSnapshot.getString("eventName");
                        String classDay = documentSnapshot.getString("classDay");
                        String time = documentSnapshot.getString("time");
                        String period = documentSnapshot.getString("period");
                        String location = documentSnapshot.getString("eventLocation");
                        String price = documentSnapshot.getString("price");

                        eventNameTextView.setText(eventName != null ? eventName : "Details unavailable");
                        classDayTextView.setText(classDay != null ? classDay : "Details unavailable");
                        timeTextView.setText(time != null ? time : "Details unavailable");
                        periodTextView.setText(period != null ? period : "Details unavailable");
                        locationTextView.setText(location != null ? location : "Details unavailable");
                        priceTextView.setText(price != null ? "$" + price : "Details unavailable");
                    } else {
                        Toast.makeText(this, "Event details not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching event details", e);
                    Toast.makeText(this, "Failed to load event details.", Toast.LENGTH_SHORT).show();
                });
    }

    private void checkIfAlreadyJoined() {
        Log.d(TAG, "Checking waiting list for eventID: " + eventID + ", deviceID: " + deviceID);

        DocumentReference waitingListRef = db.collection("events")
                .document(eventID)
                .collection("waitingLists")
                .document(deviceID);

        waitingListRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Log.d(TAG, "User already in waiting list.");
                Toast.makeText(this, "You have already joined this event's waiting list.", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "User not in waiting list, proceeding...");
                joinWaitingList();
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error checking waiting list status: ", e);
            Toast.makeText(this, "Failed to check waiting list status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }



    private void joinWaitingList() {
        DocumentReference userDocRef = db.collection("user_profile").document(deviceID);

        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot userDoc = task.getResult();
                if (userDoc != null && userDoc.exists()) {
                    String firstName = userDoc.getString("firstName");
                    String lastName = userDoc.getString("lastName");
                    String email = userDoc.getString("email");

                    // Prepare the waiting list entry
                    Map<String, Object> waitingListEntry = new HashMap<>();
                    waitingListEntry.put("userID", deviceID);
                    waitingListEntry.put("firstName", firstName);
                    waitingListEntry.put("lastName", lastName);
                    waitingListEntry.put("email", email);
                    waitingListEntry.put("timestamp", FieldValue.serverTimestamp());

                    // Add to the waiting list
                    db.collection("events")
                            .document(eventID)
                            .collection("waitingLists")
                            .document(deviceID)
                            .set(waitingListEntry)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Successfully joined the waiting list.", Toast.LENGTH_SHORT).show();

                                // Navigate to EntrantActivity
                                Intent intent = new Intent(EventInfoActivity.this, EntrantActivity.class);
                                startActivity(intent);
                                finish(); // Close the current activity
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to join waiting list: ", e);
                                Toast.makeText(this, "Failed to join waiting list.", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(this, "User profile not found.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e(TAG, "Error fetching user profile: ", task.getException());
                Toast.makeText(this, "Failed to fetch user profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}