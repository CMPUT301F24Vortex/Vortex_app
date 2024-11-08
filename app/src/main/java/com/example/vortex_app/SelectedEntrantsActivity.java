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
 * SelectedEntrantsActivity displays a list of selected entrants and provides a button
 * to initiate a draw or perform an action. The list is displayed using a RecyclerView.
 */
public class SelectedEntrantsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSelectedEntrants;
    private SelectedEntrantAdapter selectedEntrantAdapter;
    private List<User> selectedEntrantList;
    private Button buttonDraw;

    /**
     * Called when the activity is created. Sets up the layout, initializes the RecyclerView,
     * and handles user interactions, including a draw action through a button click.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_entrants);

        // Initialize the RecyclerView and Button
        recyclerViewSelectedEntrants = findViewById(R.id.recyclerViewSelectedEntrants);
        buttonDraw = findViewById(R.id.buttonDraw);

        // Initialize entrant data (this can be replaced with data from Firebase or other data sources)
        selectedEntrantList = new ArrayList<>();

        // Set up the RecyclerView with a LinearLayoutManager and the adapter
        recyclerViewSelectedEntrants.setLayoutManager(new LinearLayoutManager(this));
        selectedEntrantAdapter = new SelectedEntrantAdapter(selectedEntrantList);
        recyclerViewSelectedEntrants.setAdapter(selectedEntrantAdapter);

        // Set up click event for the Draw button
        buttonDraw.setOnClickListener(v -> {
            // Display a Toast message when the button is clicked
            Toast.makeText(this, "Draw initiated", Toast.LENGTH_SHORT).show();
        });
    }
}
