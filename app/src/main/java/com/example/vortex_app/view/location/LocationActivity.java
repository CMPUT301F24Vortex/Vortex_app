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

/**
 * LocationActivity displays the location of event entrants on a Google Map and in a RecyclerView.
 * It fetches entrant data from Firestore, displaying their names and locations (latitude and longitude).
 * Entrants are shown on the map with markers, and clicking on an entrant in the RecyclerView
 * zooms the map to their location.
 */
public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "LocationActivity";
    private GoogleMap mMap;
    private FirebaseFirestore db;
    private String eventID;

    private RecyclerView recyclerViewEntrants;
    private EntrantAdapter entrantAdapter;
    private List<Entrant> entrantList;

    /**
     * Called when the activity is created. This method initializes Firestore, sets up the RecyclerView,
     * initializes the map fragment, and fetches entrant data from Firestore.
     *
     * @param savedInstanceState A Bundle containing the saved instance state from a previous activity.
     */
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

    /**
     * Initializes the RecyclerView for displaying entrants.
     */
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

    /**
     * Initializes the map fragment and sets up the callback for when the map is ready.
     */
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

    /**
     * Called when the map is ready. This method stores the map object for later use.
     *
     * @param googleMap The GoogleMap instance that is ready to use.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Log.d(TAG, "Google Map is ready.");
    }

    /**
     * Fetches entrant data from multiple Firestore collections based on the event ID.
     * The collections include "waitlisted", "selected_but_not_confirmed", and "final".
     */
    private void fetchAllEntrantLocations() {
        fetchEntrantLocationsFromCollection("waitlisted");
        fetchEntrantLocationsFromCollection("selected_but_not_confirmed");
        fetchEntrantLocationsFromCollection("final");
    }

    /**
     * Fetches entrant data from a specific Firestore collection and adds valid entrants to the list.
     *
     * @param collectionName The name of the Firestore collection to fetch entrant data from.
     */
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

    /**
     * Parses a value to a Double safely.
     *
     * @param value The value to parse.
     * @return The parsed Double value, or null if the value is invalid.
     */
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

    /**
     * Handles the click event on an entrant. When an entrant is clicked, the map is moved to their location
     * and a marker is added to the map showing their name.
     *
     * @param entrant The entrant whose location is being clicked.
     */
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

    /**
     * Displays all entrants on the map with markers showing their names and locations.
     */
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

    /**
     * Displays a toast message on the screen.
     *
     * @param message The message to display in the toast.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
