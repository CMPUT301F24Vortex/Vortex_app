package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrganizerActivity extends AppCompatActivity {
    private ListView listView;
    private OrgEventAdapter customAdapter;
    private List<String> eventNames = new ArrayList<>();
    private List<String> eventIDs = new ArrayList<>();
    private List<String> eventImageUrls = new ArrayList<>();
    private Button buttonNavigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_events);

        listView = findViewById(R.id.listView);
        buttonNavigate = findViewById(R.id.button_add_event);

        loadEvents();


       customAdapter = new OrgEventAdapter(this, eventNames, eventIDs, eventImageUrls);
        listView.setAdapter(customAdapter);


        listView.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {

            String eventID = eventIDs.get(position);
            String eventName  = eventNames.get(position);


            Intent intent = new Intent(OrganizerActivity.this, OrganizerMenu.class);
            intent.putExtra("EVENT_NAME", eventName);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });

        buttonNavigate.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerActivity.this, AddEvent.class);
            startActivity(intent);
        });


    }

    private void loadEvents() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        eventNames.clear();
                        eventIDs.clear();
                        eventImageUrls.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String eventName = document.getString("eventName");
                            String eventID = document.getId();
                            String eventImageUrl = document.getString("imageUrl");

                            // Add event name and document ID to respective lists
                            eventNames.add(eventName);
                            eventIDs.add(eventID);
                            eventImageUrls.add(eventImageUrl);
                        }

                        // Notify adapter to update the ListView
                        customAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load events", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
