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
import com.example.vortex_app.controller.adapter.FinalEntrantAdapter;
import com.example.vortex_app.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
/**
 * Activity class for displaying the final entrants for a specific event.
 * The activity retrieves and displays a list of users who are final entrants for the event.
 * The event ID is passed through an intent, and a RecyclerView is used to display the entrants.
 *
 * Includes functionality to go back to the previous activity, passing back the event ID.
 *
 * <p>Uses Firebase Firestore to fetch data about the final entrants for the event.</p>
 *
 * <p>Requires the following permissions:
 * - Access to Firebase Firestore</p>
 *
 * @author Gyurim
 * @version 1.0
 * @since 2024-12-02
 */
public class FinalEntrantsActivity extends AppCompatActivity {

    /** RecyclerView to display the final entrants list. */
    private RecyclerView recyclerViewEntrants;

    /** Adapter for displaying the list of final entrants. */
    private FinalEntrantAdapter finalEntrantAdapter;

    /** List to store the final entrants fetched from Firestore. */
    private List<User> entrantList;

    /** Firestore instance to fetch the final entrants data. */
    private FirebaseFirestore db;

    /** Event ID used to filter the final entrants. */
    private String eventId;

    /**
     * Called when the activity is created. Initializes the UI components and fetches the event ID from the intent.
     * Sets up the RecyclerView and the back button functionality. Fetches the final entrants data from Firestore.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_entrants);

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
        finalEntrantAdapter = new FinalEntrantAdapter(entrantList);

        recyclerViewEntrants.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEntrants.setAdapter(finalEntrantAdapter);

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
        fetchFinalEntrants();
    }

    /**
     * Fetches the final entrants data from Firebase Firestore based on the event ID.
     * Updates the RecyclerView with the list of entrants or displays a Toast if no entrants are found.
     *
     * Logs any errors encountered during the Firestore data retrieval.
     */
    private void fetchFinalEntrants() {
        db.collection("final")
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
                            String firstName = document.getString("firstName") != null ? document.getString("firstName") : "Unknown";
                            String lastName = document.getString("lastName") != null ? document.getString("lastName") : "User";
                            String userID = document.getString("userID");

                            if (userID != null) {
                                entrantList.add(new User(firstName, lastName, userID));
                            } else {
                                Log.d("FirestoreError", "Document missing required fields: " + document.getId());
                            }
                        }
                        finalEntrantAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirestoreError", "Error fetching data: ", task.getException());
                        Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error fetching final entrants", e);
                    Toast.makeText(this, "Error fetching data. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }
}
