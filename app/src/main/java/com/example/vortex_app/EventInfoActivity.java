package com.example.vortex_app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EventInfoActivity extends AppCompatActivity {

    private static final String TAG = "EventInfoActivity";

    private TextView eventNameTextView, classDayTextView, timeTextView, periodTextView, locationTextView, priceTextView;
    private Button joinWaitingListButton;

    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        // Initialize views
        eventNameTextView = findViewById(R.id.text_event_name);
        classDayTextView = findViewById(R.id.text_class_day);
        timeTextView = findViewById(R.id.text_time);
        periodTextView = findViewById(R.id.text_period);
        locationTextView = findViewById(R.id.text_location);
        priceTextView = findViewById(R.id.text_price);
        joinWaitingListButton = findViewById(R.id.button_join_waiting_list);

        // Get eventID from intent
        eventID = getIntent().getStringExtra("EVENT_ID");
        if (eventID == null || eventID.isEmpty()) {
            Toast.makeText(this, "Event ID not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Load event details
        loadEventDetails(eventID);

        // Set up Join Waiting List button
        joinWaitingListButton.setOnClickListener(view -> joinWaitingList());
    }

    private void loadEventDetails(String eventID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("events")
                .document(eventID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Populate the UI with event details
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

    private void joinWaitingList() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "You must be signed in to join the waiting list.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userID = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> waitingListEntry = new HashMap<>();
        waitingListEntry.put("userID", userID);
        waitingListEntry.put("timestamp", FieldValue.serverTimestamp());

        db.collection("events")
                .document(eventID)
                .collection("waitingLists")
                .document(userID)
                .set(waitingListEntry)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Successfully joined the waiting list.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to join waiting list", e);
                    Toast.makeText(this, "Failed to join the waiting list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
