package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

/**
 * EventActivity is responsible for displaying a list of events associated with a specific center.
 * The center name is passed from the previous activity, and relevant event data is displayed in a RecyclerView.
 */
public class EventActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    /**
     * Called when the activity is first created. Sets up the layout, initializes views, and populates the
     * event data for the specified center.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). Note: Otherwise, it is null.
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

        // Load event data based on the center (replaceable with dynamic data loading)
        loadEventData(centerName);

        // Set up the RecyclerView adapter
        eventAdapter = new EventAdapter(this, eventList);  // Pass 'this' (the context) and the event list
        recyclerView.setAdapter(eventAdapter);

        // Bottom Navigation View setup and event handling
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_events) {
                Intent intent = new Intent(this, EventsActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_notifications) {
                Intent intent = new Intent(this, NotificationsActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_home) {
                return true;
            }
            return false;
        });
    }

    /**
     * Loads event data based on the provided center name.
     * This method can be expanded to load data dynamically from a database or API.
     *
     * @param centerName The name of the center for which events are to be loaded.
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
