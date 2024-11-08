package com.example.vortex_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code SelectedEntrantsActivity} is an {@link AppCompatActivity} that displays a list of selected entrants
 * for an event. The entrants are displayed in a {@link RecyclerView} using a {@link SelectedEntrantAdapter}.
 * A "Draw" button is included to initiate a draw for finalizing selected entrants.
 */
public class SelectedEntrantsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSelectedEntrants;
    private SelectedEntrantAdapter selectedEntrantAdapter;
    private List<User> selectedEntrantList;
    private Button buttonDraw;

    /**
     * Called when the activity is created. Sets up the layout, initializes the list of selected entrants,
     * and configures the RecyclerView with a {@link SelectedEntrantAdapter} to display entrant data.
     * Also configures a button to initiate a draw when clicked.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_entrants);

        recyclerViewSelectedEntrants = findViewById(R.id.recyclerViewSelectedEntrants);
        buttonDraw = findViewById(R.id.buttonDraw);

        // Initialize entrant data
        selectedEntrantList = new ArrayList<>();

        // Placeholder for Firebase data (to be replaced with actual Firebase logic)
        /*
        selectedEntrantList.add(new User("Entrant Name 1", true));
        selectedEntrantList.add(new User("Entrant Name 2", true));
        selectedEntrantList.add(new User("Entrant Name 3", false));
        selectedEntrantList.add(new User("Entrant Name 4", true));
        */

        // Set up RecyclerView with layout manager and adapter
        recyclerViewSelectedEntrants.setLayoutManager(new LinearLayoutManager(this));
        selectedEntrantAdapter = new SelectedEntrantAdapter(selectedEntrantList);
        recyclerViewSelectedEntrants.setAdapter(selectedEntrantAdapter);

        // Set click event for the Draw button
        buttonDraw.setOnClickListener(v -> {
            Toast.makeText(this, "Draw initiated", Toast.LENGTH_SHORT).show();
        });
    }
}
