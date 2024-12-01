package com.example.vortex_app.view.location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.controller.adapter.EntrantAdapter;
import com.example.vortex_app.model.Entrant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "LocationActivity";
    private GoogleMap mMap;
    private FirebaseFirestore db;
    private String eventID;

    private RecyclerView recyclerViewEntrants;
    private EntrantAdapter entrantAdapter;
    private List<Entrant> entrantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        try {
            // Initialize Firestore
            db = FirebaseFirestore.getInstance();

            // Get event ID from intent
            eventID = getIntent().getStringExtra("EVENT_ID");
            if (eventID == null || eventID.isEmpty()) {
                showToast("Event ID not found.");
                finish();
                return;
            }

            // Initialize RecyclerView
            initializeRecyclerView();

            // Initialize map fragment
            initializeMapFragment();

            // Back Button Functionality
            ImageView backButton = findViewById(R.id.imageViewBack);
            backButton.setOnClickListener(view -> {
                // Create an intent and add the event ID
                Intent intent = new Intent();
                intent.putExtra("EVENT_ID", eventID);
                setResult(RESULT_OK, intent); // Pass result back to the calling activity
                finish(); // Finish this activity and go back
            });

            // Fetch entrant data
            fetchEntrantLocations();

        } catch (Exception e) {
            Log.e(TAG, "Error initializing activity: ", e);
            showToast("Unexpected error occurred. Please try again.");
        }
    }

    private void initializeRecyclerView() {
        recyclerViewEntrants = findViewById(R.id.recyclerViewEntrants);
        recyclerViewEntrants.setLayoutManager(new LinearLayoutManager(this));
        entrantList = new ArrayList<>();
        entrantAdapter = new EntrantAdapter(entrantList, (entrant, position) -> {
            entrantAdapter.setSelectedPosition(position); // Highlight selected item
            onEntrantClicked(entrant); // Show entrant location on map
        });
        recyclerViewEntrants.setAdapter(entrantAdapter);
    }

    private void initializeMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e(TAG, "Map fragment is null.");
            showToast("Failed to load map.");
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "Google Map is ready.");
        mMap = googleMap;
    }

    private void fetchEntrantLocations() {
        Log.d(TAG, "Fetching entrant locations for eventID: " + eventID);
        db.collection("waitlisted")
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    Log.d(TAG, "Entrant locations fetched successfully.");
                    if (!querySnapshot.isEmpty()) {
                        entrantList.clear();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            try {
                                String firstName = document.getString("firstName");
                                String lastName = document.getString("lastName");
                                Object latitudeObj = document.get("latitude");
                                Object longitudeObj = document.get("longitude");

                                if (latitudeObj instanceof Double && longitudeObj instanceof Double) {
                                    double latitude = (Double) latitudeObj;
                                    double longitude = (Double) longitudeObj;

                                    if (firstName != null && lastName != null) {
                                        entrantList.add(new Entrant(firstName, lastName, latitude, longitude));
                                    }
                                } else {
                                    Log.e(TAG, "Invalid latitude/longitude data: " + document.getId());
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error processing document: " + document.getId(), e);
                            }
                        }
                        entrantAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "No entrants found for this event.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching entrant locations: ", e);
                    Toast.makeText(this, "Failed to fetch entrant locations.", Toast.LENGTH_SHORT).show();
                });
    }

    private void onEntrantClicked(Entrant entrant) {
        if (mMap != null) {
            LatLng location = new LatLng(entrant.getLatitude(), entrant.getLongitude());
            mMap.clear(); // Clear previous markers
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(entrant.getFirstName() + " " + entrant.getLastName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        } else {
            Log.e(TAG, "Google Map is not ready.");
            showToast("Map is not ready.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
