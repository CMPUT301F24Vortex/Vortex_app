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

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get event ID from intent
        eventID = getIntent().getStringExtra("EVENT_ID");
        if (eventID == null || eventID.isEmpty()) {
            showToast("Event ID not found.");
            finish();
            return;
        }

        Log.d(TAG, "Received Event ID: " + eventID);

        // Initialize RecyclerView
        initializeRecyclerView();

        // Initialize map fragment
        initializeMapFragment();

        // Back Button Functionality
        ImageView backButton = findViewById(R.id.imageViewBack);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("EVENT_ID", eventID);
            setResult(RESULT_OK, intent);
            finish();
        });

        // Fetch entrant data
        fetchAllEntrantLocations();
    }

    private void initializeRecyclerView() {
        recyclerViewEntrants = findViewById(R.id.recyclerViewEntrants);
        recyclerViewEntrants.setLayoutManager(new LinearLayoutManager(this));
        entrantList = new ArrayList<>();
        entrantAdapter = new EntrantAdapter(entrantList, (entrant, position) -> {
            entrantAdapter.setSelectedPosition(position);
            onEntrantClicked(entrant);
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
        mMap = googleMap;
        Log.d(TAG, "Google Map is ready.");
    }

    private void fetchAllEntrantLocations() {
        fetchEntrantLocationsFromCollection("waitlisted");
        fetchEntrantLocationsFromCollection("selected_but_not_confirmed");
        fetchEntrantLocationsFromCollection("final");
    }

    private void fetchEntrantLocationsFromCollection(String collectionName) {
        Log.d(TAG, "Fetching users from collection: " + collectionName);

        db.collection(collectionName)
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            try {
                                String firstName = document.getString("firstName");
                                String lastName = document.getString("lastName");
                                Double latitude = parseDoubleSafe(document.get("latitude"));
                                Double longitude = parseDoubleSafe(document.get("longitude"));

                                Log.d(TAG, "Fetched document ID: " + document.getId());
                                Log.d(TAG, "Data: firstName=" + firstName + ", lastName=" + lastName +
                                        ", latitude=" + latitude + ", longitude=" + longitude);

                                if (firstName != null && lastName != null && latitude != null && longitude != null) {
                                    entrantList.add(new Entrant(firstName, lastName, latitude, longitude));
                                    Log.d(TAG, "Valid Entrant: " + firstName + " " + lastName);
                                } else {
                                    Log.e(TAG, "Missing or invalid data in document: " + document.getId());
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error processing document: " + document.getId(), e);
                            }
                        }
                        Log.d(TAG, "Entrants loaded from " + collectionName + ": " + entrantList.size());
                        entrantAdapter.notifyDataSetChanged();
                        displayAllEntrantsOnMap();
                    } else {
                        Log.d(TAG, "No entrants found in " + collectionName);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching entrant locations from " + collectionName, e);
                    showToast("Failed to fetch entrant locations from " + collectionName);
                });
    }

    private Double parseDoubleSafe(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Invalid latitude/longitude value: " + value, e);
            }
        }
        return null;
    }

    private void onEntrantClicked(Entrant entrant) {
        if (mMap != null) {
            LatLng location = new LatLng(entrant.getLatitude(), entrant.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(entrant.getFirstName() + " " + entrant.getLastName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            Log.d(TAG, "Moved camera to: Latitude=" + entrant.getLatitude() + ", Longitude=" + entrant.getLongitude());
        } else {
            Log.e(TAG, "Google Map is not ready.");
            showToast("Map is not ready.");
        }
    }

    private void displayAllEntrantsOnMap() {
        if (mMap != null) {
            mMap.clear();
            for (Entrant entrant : entrantList) {
                LatLng location = new LatLng(entrant.getLatitude(), entrant.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(entrant.getFirstName() + " " + entrant.getLastName()));
                Log.d(TAG, "Added marker for: " + entrant.getFirstName() + " " + entrant.getLastName() +
                        " at Latitude=" + entrant.getLatitude() + ", Longitude=" + entrant.getLongitude());
            }
            if (!entrantList.isEmpty()) {
                LatLng firstLocation = new LatLng(entrantList.get(0).getLatitude(), entrantList.get(0).getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10));
                Log.d(TAG, "Camera moved to first entrant's location.");
            }
        } else {
            Log.e(TAG, "Google Map is not ready.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
