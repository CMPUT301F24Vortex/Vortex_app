package com.example.vortex_app.view.location;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "LocationActivity";
    private GoogleMap mMap;
    private FirebaseFirestore db;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get event ID from intent
        eventID = getIntent().getStringExtra("EVENT_ID");
        if (eventID == null || eventID.isEmpty()) {
            Toast.makeText(this, "Event ID not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        fetchEntrantLocations();
    }

    private void fetchEntrantLocations() {
        db.collection("waitlisted")
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String firstName = document.getString("firstName");
                            String lastName = document.getString("lastName");
                            Double latitude = document.getDouble("latitude");
                            Double longitude = document.getDouble("longitude");

                            if (latitude != null && longitude != null) {
                                LatLng entrantLocation = new LatLng(latitude, longitude);
                                String entrantName = firstName + " " + lastName;

                                // Add marker for each entrant
                                mMap.addMarker(new MarkerOptions()
                                        .position(entrantLocation)
                                        .title(entrantName));
                            }
                        }

                        // Move camera to the first entrant's location
                        if (!querySnapshot.getDocuments().isEmpty()) {
                            DocumentSnapshot firstDocument = querySnapshot.getDocuments().get(0);
                            Double firstLat = firstDocument.getDouble("latitude");
                            Double firstLng = firstDocument.getDouble("longitude");
                            if (firstLat != null && firstLng != null) {
                                LatLng firstLocation = new LatLng(firstLat, firstLng);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10));
                            }
                        }
                    } else {
                        Toast.makeText(this, "No entrants found for this event.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching entrant locations: ", e);
                    Toast.makeText(this, "Failed to fetch entrant locations.", Toast.LENGTH_SHORT).show();
                });
    }
}
