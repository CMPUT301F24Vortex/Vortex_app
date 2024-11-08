package com.example.vortex_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * LocationActivity displays a list of users with location information using a RecyclerView.
 * This activity sets up and initializes the RecyclerView to display geolocation data for users.
 */
public class LocationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLocation;
    private LocationAdapter locationAdapter;
    private List<User> locationEntrants;

    /**
     * Called when the activity is created. Sets up the layout, initializes the RecyclerView,
     * and configures the adapter to display user location data.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        recyclerViewLocation = findViewById(R.id.recyclerViewLocation);

        // Initialize location entrant data list
        locationEntrants = new ArrayList<>();

        // Set up the RecyclerView with a LinearLayoutManager and the adapter
        recyclerViewLocation.setLayoutManager(new LinearLayoutManager(this));
        locationAdapter = new LocationAdapter(this, locationEntrants);
        recyclerViewLocation.setAdapter(locationAdapter);
    }
}
