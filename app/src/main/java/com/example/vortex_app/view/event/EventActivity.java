package com.example.vortex_app.view.event;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.Event;
import com.example.vortex_app.controller.adapter.EntrantEventAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EntrantEventAdapter eventAdapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Retrieve the center name from the intent
        String centerName = getIntent().getStringExtra("CENTER_NAME");

        // Set the center name in the TextView
        TextView centerNameTextView = findViewById(R.id.center_name);
        if (centerName != null && !centerName.isEmpty()) {
            centerNameTextView.setText(centerName);
        } else {
            centerNameTextView.setText("Unknown Center");
        }

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recycler_view_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load event data based on the center
        eventList = loadEventData(centerName);

        // Set up the RecyclerView adapter
        eventAdapter = new EntrantEventAdapter(this, eventList);
        recyclerView.setAdapter(eventAdapter);
    }

    /**
     * Method to load event data based on the center name.
     *
     * @param centerName The name of the center to filter events for.
     * @return A list of events associated with the specified center.
     */
    private List<Event> loadEventData(String centerName) {
        List<Event> events = new ArrayList<>();

        if ("Center 1".equals(centerName)) {
            events.add(new Event(
                    "Event 1 at Center 1",
                    R.drawable.sample_event_image,
                    "Monday",
                    "3:00pm - 5:00pm",
                    "2025-03-01 ~ 2025-06-05",
                    "2025-01-28",
                    "2025-01-01",
                    "60",
                    "8621 112st NW, Alberta",
                    20,
                    "Beginner",
                    true
            ));
            events.add(new Event(
                    "Event 2 at Center 1",
                    R.drawable.sample_event_image,
                    "Tuesday",
                    "1:00pm - 3:00pm",
                    "2025-03-01 ~ 2025-06-05",
                    "2025-01-28",
                    "2025-01-01",
                    "50",
                    "8621 112st NW, Alberta",
                    15,
                    "Intermediate",
                    false
            ));
        } else if ("Center 2".equals(centerName)) {
            events.add(new Event(
                    "Event 1 at Center 2",
                    R.drawable.sample_event_image,
                    "Wednesday",
                    "10:00am - 12:00pm",
                    "2025-03-01 ~ 2025-06-05",
                    "2025-01-28",
                    "2025-01-01",
                    "70",
                    "8711 120st NW, Alberta",
                    25,
                    "Advanced",
                    true
            ));
        } else {
            // Fallback for unknown centers
            events.add(new Event(
                    "General Event",
                    R.drawable.sample_event_image,
                    "Friday",
                    "2:00pm - 4:00pm",
                    "2025-03-01 ~ 2025-06-05",
                    "2025-01-28",
                    "2025-01-01",
                    "40",
                    "8621 112st NW, Alberta",
                    10,
                    "All Levels",
                    false
            ));
        }

        return events;
    }
}
