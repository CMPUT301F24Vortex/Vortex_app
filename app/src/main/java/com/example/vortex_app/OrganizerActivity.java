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

public class OrganizerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrganizerEventAdapter eventAdapter;
    private ArrayList<Event> eventList;
    private Button addEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_events); // Ensure this layout is created

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the event list from the Intent
        eventList = (ArrayList<Event>) getIntent().getSerializableExtra("EVENT_LIST");

        if (eventList == null) {
            eventList = new ArrayList<>(); // Create an empty list if null
            Toast.makeText(this, "No events to display.", Toast.LENGTH_SHORT).show();
        }

        eventAdapter = new OrganizerEventAdapter(this, eventList);
        recyclerView.setAdapter(eventAdapter);

        // Button to add a new event
        addEventButton = findViewById(R.id.button_add_event);
        addEventButton.setOnClickListener(v -> {
            // Navigate to AddEvent activity
            Intent intent = new Intent(OrganizerActivity.this, AddEvent.class);
            startActivity(intent);
        });

        // Set up item click listener for the adapter
        eventAdapter.setOnItemClickListener(new OrganizerEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                // Handle the event click

            }
        });
    }

}