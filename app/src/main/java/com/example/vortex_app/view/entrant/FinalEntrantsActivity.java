package com.example.vortex_app.view.entrant;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.User;
import com.example.vortex_app.controller.adapter.FinalEntrantAdapter;

import java.util.ArrayList;
import java.util.List;

public class FinalEntrantsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFinalEntrants;
    private FinalEntrantAdapter finalEntrantAdapter;
    private List<User> finalEntrantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_entrants);

        recyclerViewFinalEntrants = findViewById(R.id.recyclerViewFinalEntrants);

        // initialize entrant data
        finalEntrantList = new ArrayList<>();

        // replace with database
        /*finalEntrantList.add(new FinalEntrant("Gyurim Do"));
        finalEntrantList.add(new FinalEntrant("Peter Kim"));
        finalEntrantList.add(new FinalEntrant("Peyton"));
        finalEntrantList.add(new FinalEntrant("Alex S"));
        finalEntrantList.add(new FinalEntrant("Chr234"));*/

        // set RecyclerView
        recyclerViewFinalEntrants.setLayoutManager(new LinearLayoutManager(this));
        finalEntrantAdapter = new FinalEntrantAdapter(finalEntrantList);
        recyclerViewFinalEntrants.setAdapter(finalEntrantAdapter);
    }
}