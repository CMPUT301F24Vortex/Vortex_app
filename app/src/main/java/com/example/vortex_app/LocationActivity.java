package com.example.vortex_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code LocationActivity} is an {@link AppCompatActivity} that displays a list of entrants with
 * their location and timestamp details in a {@link RecyclerView}. The list is populated with placeholder
 * data but can be replaced with data from a database in the future.
 */
public class LocationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLocation;
    private LocationAdapter locationAdapter;
    private List<User> locationEntrants;

    /**
     * Called when the activity is created. Sets up the layout, initializes the list of entrants with
     * location details, and configures the RecyclerView with a {@link LocationAdapter} to display the data.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        recyclerViewLocation = findViewById(R.id.recyclerViewLocation);

        // Initialize the list of entrants with geolocation data
        locationEntrants = new ArrayList<>();

        // Placeholder data; replace with data from a database
        /*
        locationEntrants.add(new LocationEntrant("Entrant 1", "8621 112st NW, Alberta", "18:23 - 2025-01-28"));
        locationEntrants.add(new LocationEntrant("Entrant 2", "8621 112st NW, Alberta", "18:23 - 2025-01-28"));
        locationEntrants.add(new LocationEntrant("Entrant 3", "8621 112st NW, Alberta", "18:23 - 2025-01-28"));
        locationEntrants.add(new LocationEntrant("Entrant 4", "8621 112st NW, Alberta", "18:23 - 2025-01-28"));
        */

        // Set up RecyclerView with a LinearLayoutManager and LocationAdapter
        recyclerViewLocation.setLayoutManager(new LinearLayoutManager(this));
        locationAdapter = new LocationAdapter(this, locationEntrants);
        recyclerViewLocation.setAdapter(locationAdapter);
    }
}
