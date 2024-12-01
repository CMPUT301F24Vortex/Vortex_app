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
import com.example.vortex_app.controller.adapter.SelectedEntrantAdapter;
import com.example.vortex_app.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class OrgSelectedEntrantsActivity extends AppCompatActivity {

    private static final String TAG = "SelectedEntrantsActivity";
    private RecyclerView recyclerViewFinalEntrants, recyclerViewSelectedButNotConfirmed, recyclerViewCanceledEntrants;
    private SelectedEntrantAdapter finalEntrantAdapter, selectedButNotConfirmedAdapter, canceledEntrantAdapter;
    private List<User> finalEntrantsList, selectedButNotConfirmedList, canceledEntrantsList;
    private FirebaseFirestore db;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_selected_entrants);

        db = FirebaseFirestore.getInstance();

        // Get the eventID from the Intent
        eventID = getIntent().getStringExtra("EVENT_ID");
        if (eventID == null || eventID.isEmpty()) {
            Toast.makeText(this, "Event ID not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "Received eventID: " + eventID);

        // Initialize RecyclerViews and Adapters
        initializeRecyclerViews();


        // Back Button Functionality
        ImageView backButton = findViewById(R.id.imageViewBack);
        backButton.setOnClickListener(view -> {
            // Create an intent and add the event ID
            Intent intent = new Intent();
            intent.putExtra("EVENT_ID", eventID);
            setResult(RESULT_OK, intent); // Pass result back to the calling activity
            finish(); // Finish this activity and go back
        });


        // Fetch data from Firestore
        fetchFinalEntrants();
        fetchSelectedButNotConfirmedEntrants();
        fetchCanceledEntrants();
    }

    private void initializeRecyclerViews() {
        recyclerViewFinalEntrants = findViewById(R.id.recyclerViewFinalEntrants);
        recyclerViewSelectedButNotConfirmed = findViewById(R.id.recyclerViewSelectedButNotConfirmed);
        recyclerViewCanceledEntrants = findViewById(R.id.recyclerViewCanceledEntrants);

        finalEntrantsList = new ArrayList<>();
        selectedButNotConfirmedList = new ArrayList<>();
        canceledEntrantsList = new ArrayList<>();

        finalEntrantAdapter = new SelectedEntrantAdapter(finalEntrantsList);
        selectedButNotConfirmedAdapter = new SelectedEntrantAdapter(selectedButNotConfirmedList);
        canceledEntrantAdapter = new SelectedEntrantAdapter(canceledEntrantsList);

        recyclerViewFinalEntrants.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFinalEntrants.setAdapter(finalEntrantAdapter);

        recyclerViewSelectedButNotConfirmed.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSelectedButNotConfirmed.setAdapter(selectedButNotConfirmedAdapter);

        recyclerViewCanceledEntrants.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCanceledEntrants.setAdapter(canceledEntrantAdapter);
    }

    private void fetchFinalEntrants() {
        db.collection("final")
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    finalEntrantsList.clear();
                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            addUserToList(document, finalEntrantsList);
                        }
                    } else {
                        Log.d(TAG, "No final entrants found for eventID: " + eventID);
                    }
                    finalEntrantAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> logAndToastError("final entrants", e));
    }

    private void fetchSelectedButNotConfirmedEntrants() {
        db.collection("selected_but_not_confirmed")
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    selectedButNotConfirmedList.clear();
                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            addUserToList(document, selectedButNotConfirmedList);
                        }
                    } else {
                        Log.d(TAG, "No selected but not confirmed entrants found for eventID: " + eventID);
                    }
                    selectedButNotConfirmedAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> logAndToastError("selected but not confirmed entrants", e));
    }

    private void fetchCanceledEntrants() {
        db.collection("cancelled")
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    canceledEntrantsList.clear();
                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            addUserToList(document, canceledEntrantsList);
                        }
                    } else {
                        Log.d(TAG, "No canceled entrants found for eventID: " + eventID);
                    }
                    canceledEntrantAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> logAndToastError("canceled entrants", e));
    }

    private void addUserToList(DocumentSnapshot document, List<User> list) {
        String firstName = document.getString("firstName");
        String lastName = document.getString("lastName");
        String userID = document.getString("userID");

        if (firstName != null && lastName != null && userID != null) {
            list.add(new User(firstName, lastName, userID));
        } else {
            Log.d(TAG, "Document missing required fields: " + document.getId());
        }
    }

    private void logAndToastError(String type, Exception e) {
        Log.e(TAG, "Error fetching " + type + ": ", e);
        Toast.makeText(this, "Failed to fetch " + type + ".", Toast.LENGTH_SHORT).show();
    }
}
