package com.example.vortex_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;



/**
 * The CancelledEntrantsActivity class represents an activity for displaying a list of cancelled entrants.
 * This activity sets up a RecyclerView to show user data related to cancelled entrants.
 */
public class CancelledEntrantsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEntrants;
    private CancelledEntrantAdapter entrantAdapter;
    private List<User> entrantList;



    /**
     * Called when the activity is first created. Sets up the layout, initializes data, and configures
     * the RecyclerView to display a list of cancelled entrants.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_entrants);

        recyclerViewEntrants = findViewById(R.id.recyclerViewEntrants);

        // initialize entrants' data
        entrantList = new ArrayList<>();

        //replace with firestore
        /*entrantList.add(new Entrant("Entrant Name 1"));
        entrantList.add(new Entrant("Entrant Name 2"));
        entrantList.add(new Entrant("Entrant Name 3"));
        entrantList.add(new Entrant("Entrant Name 4"));
        entrantList.add(new Entrant("Entrant Name 5"));*/

        // set RecyclerView
        recyclerViewEntrants.setLayoutManager(new LinearLayoutManager(this));
        entrantAdapter = new CancelledEntrantAdapter(entrantList);
        recyclerViewEntrants.setAdapter(entrantAdapter);
    }
}