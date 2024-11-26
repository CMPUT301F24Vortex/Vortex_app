package com.example.vortex_app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageEventsActivity extends AppCompatActivity {

    private static final String TAG = "ManageEventsActivity";

    private ListView listView;
    private EventListAdapter adapter;
    private ArrayList<Map<String, String>> eventList; // List to hold event data

    private FirebaseFirestore db;
    private static final String USER_ID = "020422"; // Fixed user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_events);

        db = FirebaseFirestore.getInstance();
        listView = findViewById(R.id.list_view_events);
        eventList = new ArrayList<>();

        adapter = new EventListAdapter(this, eventList);
        listView.setAdapter(adapter);

        // Load the events the user is waitlisted for
        loadWaitlistedEvents();

        // Handle item click for unjoining
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Map<String, String> selectedEvent = eventList.get(position);
            String eventId = selectedEvent.get("eventID");
            String eventName = selectedEvent.get("eventName");

            // Show confirmation dialog
            showUnjoinDialog(eventId, eventName);
        });

        // Set up bottom navigation
        setupBottomNavigation();
    }

    private void loadWaitlistedEvents() {
        db.collection("events")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    eventList.clear();

                    for (DocumentSnapshot eventDoc : querySnapshot.getDocuments()) {
                        db.collection("events")
                                .document(eventDoc.getId())
                                .collection("waitingLists")
                                .document(USER_ID)
                                .get()
                                .addOnSuccessListener(waitlistDoc -> {
                                    if (waitlistDoc.exists()) {
                                        String eventID = eventDoc.getId();
                                        String eventName = eventDoc.getString("eventName");

                                        Map<String, String> eventData = new HashMap<>();
                                        eventData.put("eventID", eventID);
                                        eventData.put("eventName", eventName);

                                        eventList.add(eventData);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching waitlisted events", e);
                    Toast.makeText(this, "Failed to load waitlisted events.", Toast.LENGTH_SHORT).show();
                });
    }

    private void showUnjoinDialog(String eventId, String eventName) {
        new AlertDialog.Builder(this)
                .setTitle("Unjoin the Waiting List?")
                .setMessage("Do you want to unjoin from " + eventName + "?")
                .setPositiveButton("Yes", (dialog, which) -> unjoinEvent(eventId))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void unjoinEvent(String eventId) {
        db.collection("events")
                .document(eventId)
                .collection("waitingLists")
                .document(USER_ID)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Successfully unjoined from the waiting list.", Toast.LENGTH_SHORT).show();
                    loadWaitlistedEvents();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error unjoining from waiting list", e);
                    Toast.makeText(this, "Failed to unjoin from the waiting list.", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupBottomNavigation() {
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Redirect to Home Activity
                return true;
            } else if (id == R.id.nav_events) {
                // Redirect to Events Activity
                return true;
            } else if (id == R.id.nav_notifications) {
                // Redirect to Notifications Activity
                return true;
            } else if (id == R.id.nav_profile) {
                // Redirect to Profile Activity
                return true;
            }
            return false;
        });

        // Highlight the current tab
        bottomNavigationView.setSelectedItemId(R.id.nav_events);
    }
}
