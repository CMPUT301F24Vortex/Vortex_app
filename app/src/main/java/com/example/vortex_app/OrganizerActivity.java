package com.example.vortex_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

/**
 * OrganizerActivity provides an interface for event organizers to view, add, and manage events.
 * It uses a RecyclerView to display a list of events and includes functionality to add new events.
 */
public class OrganizerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrganizerEventAdapter eventAdapter;
    private ArrayList<Event> eventList;
    private Button addEventButton;

    /**
     * Called when the activity is created. Sets up the layout, initializes the RecyclerView and adapter,
     * and handles interactions such as adding events and item clicks.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_events); // Ensure this layout exists

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the event list from the Intent
        eventList = (ArrayList<Event>) getIntent().getSerializableExtra("EVENT_LIST");

        // If event list is null, create a new empty list
        if (eventList == null) {
            eventList = new ArrayList<>();
            Toast.makeText(this, "No events to display.", Toast.LENGTH_SHORT).show();
        }

        // Set up the RecyclerView adapter
        eventAdapter = new OrganizerEventAdapter(this, eventList);
        recyclerView.setAdapter(eventAdapter);

        // Button to add a new event
        addEventButton = findViewById(R.id.button_add_event);
        addEventButton.setOnClickListener(v -> {
            // Navigate to AddEvent activity to add a new event
            Intent intent = new Intent(OrganizerActivity.this, AddEvent.class);
            intent.putExtra("EVENT_LIST", eventList);
            startActivity(intent);
        });

        // Set up item click listener for the adapter
        eventAdapter.setOnItemClickListener(new OrganizerEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                // Handle event click (Add custom behavior as needed)
            }
        });
    }
}
