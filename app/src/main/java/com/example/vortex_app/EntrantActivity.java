package com.example.vortex_app;


import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class EntrantActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CenterAdapter centerAdapter;
    private List<Center> centerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_entrant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_centers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the data list and populate the data
        centerList = new ArrayList<>();
        loadCenterData();

        // Set up the RecyclerView adapter and handle click events
        centerAdapter = new CenterAdapter(centerList, new CenterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Center center) {
                // When an item is clicked, start EventActivity and pass the center's name
                Intent intent = new Intent(EntrantActivity.this, EventActivity.class);
                intent.putExtra("CENTER_NAME", center.getName());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(centerAdapter);

        // Bottom Navigation View Setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set a new navigation item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_events) {
                Intent intent = new Intent(this, EventsActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_notifications) {
                Intent intent = new Intent(this, NotificationsActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_home) {
                return true;
            }
            return false;
        });
    }

    // Method to populate the list with data (this can be dynamic or static for now)
    private void loadCenterData() {
        centerList.add(new Center("Center 1333", "123 Main St"));
        centerList.add(new Center("Center 2", "456 Oak St"));
        centerList.add(new Center("Center 3", "789 Pine St"));
        centerList.add(new Center("Center 4", "101 Maple Ave"));
        centerList.add(new Center("Center 5", "202 Elm St"));
    }
}
