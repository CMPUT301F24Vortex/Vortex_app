package com.example.vortex_app.view.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.controller.adapter.EventAdapter;
import com.example.vortex_app.view.MainActivity;
import com.example.vortex_app.view.event.AddEvent;
import com.example.vortex_app.view.facility.MyFacilityActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizerActivity extends AppCompatActivity {

    private static final String TAG = "OrganizerActivity";

    private ListView listView;
    private EventAdapter customAdapter;
    private List<String> eventNames = new ArrayList<>();
    private List<String> eventIDs = new ArrayList<>();
    private List<String> eventImageUrls = new ArrayList<>();
    private Button buttonNavigate;
    private Button buttonMyFacility;

    private FirebaseFirestore db;
    private String organizerID;

    private Button buttonChangeRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_events);

        // Initialize Firebase Firestore and UI components
        db = FirebaseFirestore.getInstance();
        organizerID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "Organizer ID: " + organizerID);

        listView = findViewById(R.id.listView);
        buttonNavigate = findViewById(R.id.button_add_event);
        buttonMyFacility = findViewById(R.id.button_my_facility);
        buttonChangeRole = findViewById(R.id.button_change_role); // New button


        // Check for an existing facility or create a default one
        ensureDefaultFacility();

        // Load organizer events
        loadEvents();

        // Set up custom adapter for ListView
        customAdapter = new EventAdapter(this, eventNames, eventIDs, eventImageUrls);
        listView.setAdapter(customAdapter);

        // Handle item clicks in ListView
        listView.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            String eventID = eventIDs.get(position);
            String eventName = eventNames.get(position);

            Intent intent = new Intent(OrganizerActivity.this, OrganizerMenu.class);
            intent.putExtra("EVENT_NAME", eventName);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });

        // Handle "Add Event" button click
        buttonNavigate.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerActivity.this, AddEvent.class);
            startActivity(intent);
        });

        // Handle "My Facility" button click
        buttonMyFacility.setOnClickListener(v -> {
            Log.d(TAG, "My Facility button clicked!");
            Intent intent = new Intent(OrganizerActivity.this, MyFacilityActivity.class);
            startActivity(intent);
        });


        // Handle "Change Role" button click
        buttonChangeRole.setOnClickListener(v -> {
            Log.d(TAG, "Change Role button clicked!");
            Intent intent = new Intent(OrganizerActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        });
    }

    private void loadEvents() {
        db.collection("events")
                .whereEqualTo("organizerId", organizerID) // Only fetch events created by this organizer
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        eventNames.clear();
                        eventIDs.clear();
                        eventImageUrls.clear();

                        for (DocumentSnapshot document : task.getResult()) {
                            String eventName = document.getString("eventName");
                            String eventID = document.getId();
                            String eventImageUrl = document.getString("imageUrl");

                            if (eventName != null && eventID != null) {
                                eventNames.add(eventName);
                                eventIDs.add(eventID);
                                eventImageUrls.add(eventImageUrl != null ? eventImageUrl : "");
                            } else {
                                Log.e(TAG, "Invalid event data: " + document.getId());
                            }
                        }

                        customAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Error loading events", task.getException());
                        Toast.makeText(this, "Failed to load events", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void ensureDefaultFacility() {
        db.collection("facility")
                .whereEqualTo("organizerId", organizerID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            Log.d(TAG, "No facility found. Creating default facility...");
                            createDefaultFacility();
                        } else {
                            Log.d(TAG, "Facility already exists for organizer ID: " + organizerID);
                        }
                    } else {
                        Log.e(TAG, "Error checking facility existence", task.getException());
                        Toast.makeText(this, "Error checking facility: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createDefaultFacility() {
        Map<String, Object> facilityData = new HashMap<>();
        facilityData.put("facilityName", "Default Facility Name");
        facilityData.put("address", "Default Location");
        facilityData.put("organizerId", organizerID);

        db.collection("facilities")
                .add(facilityData)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Default facility created with ID: " + documentReference.getId());
                    Toast.makeText(this, "Default facility created successfully.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating default facility", e);
                    Toast.makeText(this, "Failed to create default facility: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}