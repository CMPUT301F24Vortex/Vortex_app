package com.example.vortex_app.view.entrant;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.controller.adapter.FinalEntrantAdapter;
import com.example.vortex_app.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FinalEntrantsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEntrants;
    private FinalEntrantAdapter entrantAdapter;
    private List<User> entrantList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_entrants); // Update with your layout file name

        recyclerViewEntrants = findViewById(R.id.recyclerViewEntrants);

        // Initialize Firestore and entrant list
        db = FirebaseFirestore.getInstance();
        entrantList = new ArrayList<>();
        entrantAdapter = new FinalEntrantAdapter(entrantList);

        recyclerViewEntrants.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEntrants.setAdapter(entrantAdapter);

        // Fetch data from Firestore
        fetchFinalEntrants();
    }

    private void fetchFinalEntrants() {
        String eventId = getIntent().getStringExtra("EVENT_ID"); // Retrieve the event ID
        if (eventId == null || eventId.isEmpty()) {
            Toast.makeText(this, "Event ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Event ID: " + eventId, Toast.LENGTH_SHORT).show(); // Debugging
        db.collection("final") // Fetch from the "final" collection
                .whereEqualTo("eventID", eventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        entrantList.clear();
                        if (task.getResult().isEmpty()) {
                            Toast.makeText(this, "No final users found for this event", Toast.LENGTH_SHORT).show();
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
