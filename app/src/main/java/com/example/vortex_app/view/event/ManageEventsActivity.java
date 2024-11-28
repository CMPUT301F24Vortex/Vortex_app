package com.example.vortex_app.view.event;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.controller.adapter.EventListAdapter;
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
    private String deviceID; // Use deviceID as the unique user identifier

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_events);

        db = FirebaseFirestore.getInstance();

        // Retrieve the unique device ID
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "Device ID: " + deviceID);

        listView = findViewById(R.id.list_view_events);
        eventList = new ArrayList<>();

        adapter = new EventListAdapter(this, eventList);
        listView.setAdapter(adapter);

        // Load the events for both waitlisted and canceled categories
        loadWaitlistedEvents();
        loadCancelledEvents();

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
                    for (DocumentSnapshot eventDoc : querySnapshot.getDocuments()) {
                        String eventId = eventDoc.getId();
                        db.collection("events")
                                .document(eventId)
                                .collection("waitingLists")
                                .document(deviceID) // Check for this device ID in the waiting list
                                .get()
                                .addOnSuccessListener(waitlistDoc -> {
                                    if (waitlistDoc.exists()) {
                                        String eventName = eventDoc.getString("eventName");

                                        Map<String, String> eventData = new HashMap<>();
                                        eventData.put("eventID", eventId);
                                        eventData.put("eventName", eventName);
                                        eventData.put("status", "Waitlisted");

                                        eventList.add(eventData);
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error checking waiting list for event: " + eventId, e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching events", e);
                    Toast.makeText(this, "Failed to load waitlisted events.", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadCancelledEvents() {
        db.collection("cancelled")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot cancelledDoc : querySnapshot.getDocuments()) {
                        String eventId = cancelledDoc.getString("eventID");
                        String userId = cancelledDoc.getString("userID");

                        if (userId != null && userId.equals(deviceID)) {
                            Map<String, String> eventData = new HashMap<>();
                            eventData.put("eventID", eventId);
                            eventData.put("eventName", "Cancelled Event " + eventId); // Placeholder for event name
                            eventData.put("status", "Cancelled");

                            eventList.add(eventData);
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching cancelled events", e);
                    Toast.makeText(this, "Failed to load cancelled events.", Toast.LENGTH_SHORT).show();
                });
    }

    private void showUnjoinDialog(String eventId, String eventName) {
        new AlertDialog.Builder(this)
                .setTitle("Unjoin the Event?")
                .setMessage("Do you want to unjoin from " + eventName + "?")
                .setPositiveButton("Yes", (dialog, which) -> unjoinEvent(eventId))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void unjoinEvent(String eventId) {
        db.collection("events")
                .document(eventId)
                .collection("waitingLists")
                .document(deviceID) // Remove the document using the device ID
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Successfully unjoined from the event.", Toast.LENGTH_SHORT).show();

                    // Remove the unjoined event from the local list
                    for (int i = 0; i < eventList.size(); i++) {
                        if (eventList.get(i).get("eventID").equals(eventId)) {
                            eventList.remove(i);
                            adapter.notifyDataSetChanged(); // Update the adapter to refresh the UI
                            break;
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error unjoining from the event", e);
                    Toast.makeText(this, "Failed to unjoin from the event.", Toast.LENGTH_SHORT).show();
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
