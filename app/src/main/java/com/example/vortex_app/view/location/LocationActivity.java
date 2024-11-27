package com.example.vortex_app.view.location;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.controller.adapter.LocationAdapter;
import com.example.vortex_app.R;
import com.example.vortex_app.model.User;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLocation;
    private LocationAdapter locationAdapter;
    private List<User> locationEntrants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        recyclerViewLocation = findViewById(R.id.recyclerViewLocation);

        // initialize geolocation data
        locationEntrants = new ArrayList<>();

        // replace with database
        /*locationEntrants.add(new LocationEntrant("Entrant 1", "8621 112st NW, Alberta", "18:23 - 2025-01-28"));
        locationEntrants.add(new LocationEntrant("Entrant 2", "8621 112st NW, Alberta", "18:23 - 2025-01-28"));
        locationEntrants.add(new LocationEntrant("Entrant 3", "8621 112st NW, Alberta", "18:23 - 2025-01-28"));
        locationEntrants.add(new LocationEntrant("Entrant 4", "8621 112st NW, Alberta", "18:23 - 2025-01-28"));*/

        // set RecyclerView
        recyclerViewLocation.setLayoutManager(new LinearLayoutManager(this));
        locationAdapter = new LocationAdapter(this, locationEntrants);
        recyclerViewLocation.setAdapter(locationAdapter);
    }
}