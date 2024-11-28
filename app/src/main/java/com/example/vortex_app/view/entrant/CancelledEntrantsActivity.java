package com.example.vortex_app.view.entrant;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_entrants);

        recyclerViewEntrants = findViewById(R.id.recyclerViewEntrants);
//        ImageView backButton = findViewById(R.id.imageViewBack);

        // Initialize Firestore and entrant list
        db = FirebaseFirestore.getInstance();
        entrantList = new ArrayList<>();
        entrantAdapter = new CancelledEntrantAdapter(entrantList);

        recyclerViewEntrants.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEntrants.setAdapter(entrantAdapter);

        // Back button functionality
//        backButton.setOnClickListener(view -> finish());

        // Fetch data from Firestore
        fetchCancelledEntrants();
    }

    private void fetchCancelledEntrants() {
        String eventId = getIntent().getStringExtra("EVENT_ID"); // Retrieve the event ID
        if (eventId == null || eventId.isEmpty()) {
            Toast.makeText(this, "Event ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

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
                            String userName = document.getString("userName");
                            String userID = document.getString("userID");

                            if (userName != null && userID != null) {
                                entrantList.add(new User(userName, userID)); // Updated constructor
                            } else {
                                Log.d("FirestoreError", "Document missing required fields: " + document.getId());
                            }
                        }
                        entrantAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
