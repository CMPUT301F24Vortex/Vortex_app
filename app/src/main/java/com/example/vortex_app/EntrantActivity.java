package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class EntrantActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CenterAdapter centerAdapter;
    private List<Center> centerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrant);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_centers); // Ensure ID matches XML
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the data list and populate the data
        centerList = new ArrayList<>();
        loadCenterData();

        // Set up the RecyclerView adapter and handle item click events
        centerAdapter = new CenterAdapter(centerList, new CenterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Center center) {
                // When a center is clicked, start EventActivity and pass the center name
                Intent intent = new Intent(EntrantActivity.this, EventActivity.class);
                intent.putExtra("CENTER_NAME", center.getName()); // Pass the center name
                startActivity(intent);  // Start EventActivity
            }
        });

        recyclerView.setAdapter(centerAdapter);
    }

    // Populate the data
    private void loadCenterData() {
        centerList.add(new Center("Center 1", "123 Main St"));
        centerList.add(new Center("Center 2", "456 Oak St"));
        centerList.add(new Center("Center 3", "789 Pine St"));
        // Add more centers if needed
    }
}

