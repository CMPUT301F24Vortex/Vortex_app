package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get a reference to the Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Notifications as the default selected item when this activity is opened
        bottomNavigationView.setSelectedItemId(R.id.nav_notifications);

        // Set a new navigation item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Navigate to Home (EntrantActivity)
                Intent intent = new Intent(this, EntrantActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_events) {
                // Navigate to EventsActivity
                Intent intent = new Intent(this, EventsActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Navigate to ProfileActivity
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_notifications) {
                // Already in Notifications, do nothing
                return true;
            }
            return false;
        });
    }
}
