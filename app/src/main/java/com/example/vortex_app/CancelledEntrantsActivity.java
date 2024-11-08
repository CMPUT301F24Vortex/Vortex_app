package com.example.vortex_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CancelledEntrantsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEntrants;
    private CancelledEntrantAdapter entrantAdapter;
    private List<User> entrantList;

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