package com.example.vortex_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code FinalEntrantsActivity} is an {@link AppCompatActivity} that displays a list of final entrants
 * who have been selected for an event. It uses a {@link RecyclerView} to present the entrants' names.
 * The data is currently static but can be replaced with data from a database.
 */
public class FinalEntrantsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFinalEntrants;
    private FinalEntrantAdapter finalEntrantAdapter;
    private List<User> finalEntrantList;

    /**
     * Called when the activity is created. Sets up the layout, initializes the list of final entrants,
     * and configures the RecyclerView with a {@link FinalEntrantAdapter} to display the entrant data.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_entrants);

        recyclerViewFinalEntrants = findViewById(R.id.recyclerViewFinalEntrants);

        // Initialize entrant data
        finalEntrantList = new ArrayList<>();

        // Example static data - replace with data from a database
        /*
        finalEntrantList.add(new User("Gyurim Do"));
        finalEntrantList.add(new User("Peter Kim"));
        finalEntrantList.add(new User("Peyton"));
        finalEntrantList.add(new User("Alex S"));
        finalEntrantList.add(new User("Chr234"));
        */

        // Set up RecyclerView
        recyclerViewFinalEntrants.setLayoutManager(new LinearLayoutManager(this));
        finalEntrantAdapter = new FinalEntrantAdapter(finalEntrantList);
        recyclerViewFinalEntrants.setAdapter(finalEntrantAdapter);
    }
}
