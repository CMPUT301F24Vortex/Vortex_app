package com.example.vortex_app.view.entrant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.controller.adapter.CancelledEntrantAdapter;
import com.example.vortex_app.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CancelledEntrantsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEntrants;
    private CancelledEntrantAdapter entrantAdapter;
    private List<User> entrantList;
    private FirebaseFirestore db;
    private String eventId; // Store event ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_entrants); // Your layout file for this activity

        // Retrieve event ID from the intent
        eventId = getIntent().getStringExtra("EVENT_ID");

        // Ensure event ID is not null or empty
        if (eventId == null || eventId.isEmpty()) {
            Toast.makeText(this, "Event ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize RecyclerView and adapter
        recyclerViewEntrants = findViewById(R.id.recyclerViewEntrants);
        db = FirebaseFirestore.getInstance();
        entrantList = new ArrayList<>();
        entrantAdapter = new CancelledEntrantAdapter(entrantList);

        recyclerViewEntrants.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEntrants.setAdapter(entrantAdapter);

        // Back Button Functionality
        ImageView backButton = findViewById(R.id.imageViewBack);
        backButton.setOnClickListener(view -> {
            // Create an intent and add the event ID
            Intent intent = new Intent();
            intent.putExtra("EVENT_ID", eventId);
            setResult(RESULT_OK, intent); // Pass result back to the calling activity
            finish(); // Finish this activity and go back
        });

        // Fetch data from Firestore
        fetchCancelledEntrants();
    }

    private void fetchCancelledEntrants() {
        Toast.makeText(this, "Event ID: " + eventId, Toast.LENGTH_SHORT).show(); // Debugging
        db.collection("cancelled")
                .whereEqualTo("eventID", eventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        entrantList.clear();
                        if (task.getResult().isEmpty()) {
                            Toast.makeText(this, "No cancelled users found for this event", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String firstName = document.getString("firstName") != null ? document.getString("firstName") : "Unknown";
                            String lastName = document.getString("lastName") != null ? document.getString("lastName") : "User";
                            String userID = document.getString("userID");

                            if (userID != null) {
                                entrantList.add(new User(firstName, lastName, userID));
                            } else {
                                Log.d("FirestoreError", "Document missing required fields: " + document.getId());
                            }
                        }
                        entrantAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirestoreError", "Error fetching data: ", task.getException());
                        Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error fetching cancelled entrants", e);
                    Toast.makeText(this, "Error fetching data. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }

}
