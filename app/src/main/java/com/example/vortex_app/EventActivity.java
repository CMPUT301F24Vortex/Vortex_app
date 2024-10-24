package com.example.vortex_app;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Get the center name passed from the previous activity
        String centerName = getIntent().getStringExtra("CENTER_NAME");

        // Set the center name in the TextView
        TextView centerNameTextView = findViewById(R.id.center_name);
        centerNameTextView.setText(centerName);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load event data based on the center (you can replace this with dynamic data)
        loadEventData(centerName);

        // Set up the RecyclerView adapter
        eventAdapter = new EventAdapter(this, eventList);  // Pass 'this' (the context) and the event list
        recyclerView.setAdapter(eventAdapter);
    }

    // Method to load event data based on the center
    private void loadEventData(String centerName) {
        eventList = new ArrayList<>();

        if ("Center 1".equals(centerName)) {
            eventList.add(new Event("Event 1 at Center 1", R.drawable.sample_event_image, "Monday", "3:00pm - 5:00pm", "2025-03-01 ~ 2025-06-05", "2025-01-28", "2025-01-01", "$60", "8621 112st NW, Alberta", 20, "Beginner"));
            eventList.add(new Event("Event 2 at Center 1", R.drawable.sample_event_image, "Tuesday", "1:00pm - 3:00pm", "2025-03-01 ~ 2025-06-05", "2025-01-28", "2025-01-01", "$50", "8621 112st NW, Alberta", 20, "Intermediate"));
        } else if ("Center 2".equals(centerName)) {
            // Make sure you provide all fields for Center 2 as well
            eventList.add(new Event("Event 1 at Center 2", R.drawable.sample_event_image, "Wednesday", "10:00am - 12:00pm", "2025-03-01 ~ 2025-06-05", "2025-01-28", "2025-01-01", "$70", "1234 Oak St, Alberta", 25, "Advanced"));
            eventList.add(new Event("Event 2 at Center 2", R.drawable.sample_event_image, "Thursday", "2:00pm - 4:00pm", "2025-03-01 ~ 2025-06-05", "2025-01-28", "2025-01-01", "$80", "1234 Oak St, Alberta", 30, "Beginner"));
        } else {
            // Default events for other centers
            eventList.add(new Event("General Event 1", R.drawable.sample_event_image, "Friday", "9:00am - 11:00am", "2025-03-01 ~ 2025-06-05", "2025-01-28", "2025-01-01", "$40", "5678 Maple St, Alberta", 15, "Beginner"));
            eventList.add(new Event("General Event 2", R.drawable.sample_event_image, "Saturday", "11:00am - 1:00pm", "2025-03-01 ~ 2025-06-05", "2025-01-28", "2025-01-01", "$45", "5678 Maple St, Alberta", 18, "Intermediate"));
        }

    }
}

