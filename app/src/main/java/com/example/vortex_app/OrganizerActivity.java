package com.example.vortex_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * OrganizerActivity provides an interface for event organizers to view, add, and manage events.
 * It uses a RecyclerView to display a list of events and includes functionality to add new events.
 */
public class OrganizerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrganizerEventAdapter eventAdapter;
    private List<Event> eventList = new ArrayList<>();
    private Button buttonNavigate;


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
        setContentView(R.layout.organizer_events);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new OrganizerEventAdapter(eventList, new OrganizerEventAdapter.OnEventClickListener() {
            @Override
            public void onEventClick(Event event) {

                String eventID = event.getEventID();
                String eventName = event.getName();
                Intent intent = new Intent(OrganizerActivity.this, OrganizerMenu.class);
                intent.putExtra("EVENT_NAME", eventName);
                intent.putExtra("EVENT_ID", eventID);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(eventAdapter);


        buttonNavigate = findViewById(R.id.button_add_event);
        buttonNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OrganizerActivity.this, AddEvent.class);
                startActivity(intent);
            }
        });

        loadEvents();
    }
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
                            Event event = new Event(eventName, eventID );
                            eventList.add(event);
                        }
                        eventAdapter.notifyDataSetChanged();

                    }

                });
    }
}
