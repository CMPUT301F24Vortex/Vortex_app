package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code OrganizerActivity} is an {@link AppCompatActivity} that allows organizers to view and manage events.
 * It displays a list of events in a {@link RecyclerView} and provides options to navigate to the event management menu
 * or add new events.
 */
public class OrganizerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrganizerEventAdapter eventAdapter;
    private List<Event> eventList = new ArrayList<>();
    private Button buttonNavigate;

    /**
     * Called when the activity is created. Sets up the layout, initializes the RecyclerView for displaying events,
     * and configures a button to navigate to the event addition screen.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_events);

        // Initialize RecyclerView for event display
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the adapter and handle event click to navigate to OrganizerMenu
        eventAdapter = new OrganizerEventAdapter(eventList, event -> {
            String eventID = event.getEventID();
            String eventName = event.getName();
            Intent intent = new Intent(OrganizerActivity.this, OrganizerMenu.class);
            intent.putExtra("EVENT_NAME", eventName);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });
        recyclerView.setAdapter(eventAdapter);

        // Initialize button to navigate to AddEvent activity
        buttonNavigate = findViewById(R.id.button_add_event);
        buttonNavigate.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerActivity.this, AddEvent.class);
            startActivity(intent);
        });

        loadEvents(); // Load events from Firestore
    }

    /**
     * Loads events from Firestore and updates the RecyclerView with the retrieved event data.
     * The event list is cleared before loading new data to avoid duplication.
     */
    private void loadEvents() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        eventList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String eventName = document.getString("eventName");
                            String eventID = document.getId();
                            Event event = new Event(eventName, eventID);
                            eventList.add(event);
                        }
                        eventAdapter.notifyDataSetChanged(); // Update the adapter with new data
                    }
                });
    }
}
