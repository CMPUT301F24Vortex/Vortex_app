package com.example.vortex_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SelectedEntrantsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSelectedEntrants;
    private SelectedEntrantAdapter selectedEntrantAdapter;
    private List<User> selectedEntrantList;
    private Button buttonDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_entrants);

        recyclerViewSelectedEntrants = findViewById(R.id.recyclerViewSelectedEntrants);
        buttonDraw = findViewById(R.id.buttonDraw);

        // initialize entrant data
        selectedEntrantList = new ArrayList<>();

        // insert firebase list
        /*selectedEntrantList.add(new SelectedEntrant("Entrant Name 1", true));
        selectedEntrantList.add(new SelectedEntrant("Entrant Name 2", true));
        selectedEntrantList.add(new SelectedEntrant("Entrant Name 3", false));
        selectedEntrantList.add(new SelectedEntrant("Entrant Name 4", true));
*/
        // set RecyclerView
        recyclerViewSelectedEntrants.setLayoutManager(new LinearLayoutManager(this));
        selectedEntrantAdapter = new SelectedEntrantAdapter(selectedEntrantList);
        recyclerViewSelectedEntrants.setAdapter(selectedEntrantAdapter);

        // set Draw button click event
        buttonDraw.setOnClickListener(v -> {
            Toast.makeText(this, "Draw initiated", Toast.LENGTH_SHORT).show();
        });
    }
}