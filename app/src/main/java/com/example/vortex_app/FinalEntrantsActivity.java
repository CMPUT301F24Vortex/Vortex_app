package com.example.vortex_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * FinalEntrantsActivity displays a list of final entrants using a RecyclerView.
 * This activity sets up and initializes the RecyclerView with a list of User objects.
 */
public class FinalEntrantsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFinalEntrants;
    private FinalEntrantAdapter finalEntrantAdapter;
    private List<User> finalEntrantList;

    /**
     * Called when the activity is created. Sets up the layout, initializes the RecyclerView,
     * and configures the adapter to display the list of final entrants.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_entrants);

        recyclerViewFinalEntrants = findViewById(R.id.recyclerViewFinalEntrants);

        // Initialize entrant data list
        finalEntrantList = new ArrayList<>();

        // Set up the RecyclerView with a LinearLayoutManager and the adapter
        recyclerViewFinalEntrants.setLayoutManager(new LinearLayoutManager(this));
        finalEntrantAdapter = new FinalEntrantAdapter(finalEntrantList);
        recyclerViewFinalEntrants.setAdapter(finalEntrantAdapter);
    }
}
