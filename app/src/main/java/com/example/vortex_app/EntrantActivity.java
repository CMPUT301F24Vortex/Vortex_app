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

/**
 * EntrantActivity is an activity for displaying a list of centers
 * and handling navigation between different app sections using a bottom navigation view.
 */
public class EntrantActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CenterAdapter centerAdapter;
    private List<Center> centerList;

    /**
     * Called when the activity is created. Sets up the UI, initializes the RecyclerView, and configures
     * bottom navigation behavior.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_entrant);

        // Apply window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_centers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize and populate the data list
        centerList = new ArrayList<>();
        loadCenterData();

        // Set up the RecyclerView adapter with click handling
        centerAdapter = new CenterAdapter(centerList, new CenterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Center center) {
                // Handle item click and navigate to EventActivity
                Intent intent = new Intent(EntrantActivity.this, EventActivity.class);
                intent.putExtra("CENTER_NAME", center.getName());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(centerAdapter);

        // Bottom Navigation View setup and event handling
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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

    /**
     * Populates the list of centers with sample data.
     * This method can be replaced or expanded with dynamic data in a real application.
     */
    private void loadCenterData() {
        centerList.add(new Center("Center 1333", "123 Main St"));
        centerList.add(new Center("Center 2", "456 Oak St"));
        centerList.add(new Center("Center 3", "789 Pine St"));
        centerList.add(new Center("Center 4", "101 Maple Ave"));
        centerList.add(new Center("Center 5", "202 Elm St"));
    }
}
