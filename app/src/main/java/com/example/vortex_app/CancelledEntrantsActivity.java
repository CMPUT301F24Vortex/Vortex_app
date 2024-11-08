package com.example.vortex_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code CancelledEntrantsActivity} is an {@link AppCompatActivity} that displays a list of cancelled entrants.
 * This activity initializes a RecyclerView to show a list of users who have cancelled their event participation.
 *
 * <p>The data for cancelled entrants is stored in a {@link List} and displayed using {@link CancelledEntrantAdapter}.
 * Future implementations could replace this static list with dynamic data from Firestore.
 */
public class CancelledEntrantsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEntrants;
    private CancelledEntrantAdapter entrantAdapter;
    private List<User> entrantList;

    /**
     * Called when the activity is first created. Initializes the RecyclerView, sets up the list
     * of cancelled entrants, and configures the adapter.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_entrants);

        recyclerViewEntrants = findViewById(R.id.recyclerViewEntrants);

        // Initialize entrants' data
        entrantList = new ArrayList<>();

        // TODO: Replace this with Firestore data fetching for cancelled entrants
        /*
        entrantList.add(new User("Entrant Name 1"));
        entrantList.add(new User("Entrant Name 2"));
        entrantList.add(new User("Entrant Name 3"));
        */

        // Set up RecyclerView
        recyclerViewEntrants.setLayoutManager(new LinearLayoutManager(this));
        entrantAdapter = new CancelledEntrantAdapter(entrantList);
        recyclerViewEntrants.setAdapter(entrantAdapter);
    }
}
