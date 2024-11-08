package com.example.vortex_app;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code EventActivity} is an {@link AppCompatActivity} that displays a list of events associated with a specific center.
 * It retrieves the center name from the intent, displays it, and loads a list of events associated with the center.
 * The events are displayed in a {@link RecyclerView} using the {@link EventAdapter}.
 */
public class EventActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    /**
     * Called when the activity is first created. Retrieves the center name passed from the previous activity,
     * initializes the RecyclerView, loads event data for the specified center, and sets up the adapter.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
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

        // Load event data based on the center
        loadEventData(centerName);

        // Set up the RecyclerView adapter
        eventAdapter = new EventAdapter(this, eventList);
        recyclerView.setAdapter(eventAdapter);
    }

    /**
     * Loads event data for the specified center. This is currently static data but can be replaced with dynamic data.
     * The method populates the {@code eventList} with events associated with the specified center.
     *
     * @param centerName The name of the center for which to load events.
     */
    private void loadEventData(String centerName) {
        eventList = new ArrayList<>();
        if ("Center 1".equals(centerName)) {
            eventList.add(new Event("Event 1 at Center 1", R.drawable.sample_event_image,
                    "Monday", "3:00pm - 5:00pm", "2025-03-01 ~ 2025-06-05",
                    "2025-01-28", "2025-01-01", "60",
                    "8621 112st NW, Alberta", 20, "Beginner", true));  // Geolocation required
            eventList.add(new Event("Event 2 at Center 1", R.drawable.sample_event_image,
                    "Tuesday", "1:00pm - 3:00pm", "2025-03-01 ~ 2025-06-05",
                    "2025-01-28", "2025-01-01", "50",
                    "8621 112st NW, Alberta", 20, "Intermediate", false));  // Geolocation not required
        }
    }
}
