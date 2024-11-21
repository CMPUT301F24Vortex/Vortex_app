package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class EntrantActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EntrantEventAdapter eventAdapter; // New adapter for events
    private List<Event> eventList; // List of events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrant_new); // Use the new layout

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the data list and populate it
        eventList = new ArrayList<>();
        loadEventData();

        // Set up the RecyclerView adapter
        eventAdapter = new EntrantEventAdapter(this, eventList);
        recyclerView.setAdapter(eventAdapter);

        // Set up the "Scan QR Code" button
        Button scanQrButton = findViewById(R.id.button_scan_qr);
        scanQrButton.setOnClickListener(v -> {
            // TODO: Add functionality for scanning QR codes
            Log.d("EntrantActivity", "Scan QR Code button clicked");
        });

        // Bottom Navigation View Setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (itemId == R.id.nav_events) {
                startActivity(new Intent(this, EventsActivity.class));
                return true;
            } else if (itemId == R.id.nav_notifications) {
                startActivity(new Intent(this, NotificationsActivity.class));
                return true;
            } else if (itemId == R.id.nav_home) {
                return true;
            }
            return false;
        });
    }

    // Method to populate the event list
    private void loadEventData() {
        eventList.add(new Event("Class Name", "Difficulty: Beginner"));
        eventList.add(new Event("Class Name", "Difficulty: Beginner"));
        // Add more events as needed
    }
}



