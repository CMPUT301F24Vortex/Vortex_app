package com.example.vortex_app.view.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.view.MainActivity;
import com.example.vortex_app.view.facility.AdminFacilityScreen;
import com.example.vortex_app.view.image.AdminImageScreen;
import com.example.vortex_app.view.profile.AdminProfileScreen;

/**
 * AdminMainscreen class provides the main interface for the admin user.
 * This screen contains buttons to navigate to different admin sections such as Profile, Facility, Event, Notifications, and Logout.
 */
public class AdminMainscreen extends AppCompatActivity {

    /**
     * Called when the activity is created.
     * Initializes the UI elements and sets up the button click listeners.
     *
     * @param savedInstanceState The saved instance state (if any) when the activity is recreated.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mainscreen);

        // Initialize button references
        Button profileButton = findViewById(R.id.button_profile);
        Button facilityEventButton = findViewById(R.id.button_facility_event);
        Button notificationButton = findViewById(R.id.button_notification);
        Button logoutButton = findViewById(R.id.button_logout);

        // Set click listener for the profile button
        profileButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Starts the AdminProfileScreen activity when the profile button is clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainscreen.this, AdminProfileScreen.class);
                startActivity(intent);
            }
        });

        // Set click listener for the facility/event button
        facilityEventButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Starts the AdminFacilityScreen activity when the facility/event button is clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainscreen.this, AdminFacilityScreen.class);
                startActivity(intent);
            }
        });

        // Set click listener for the notification button
        notificationButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Placeholder for notification button functionality.
             * Currently does nothing when clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                // Placeholder action
            }
        });

        // Set click listener for the logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Starts the MainActivity (login screen) when the logout button is clicked.
             * This effectively logs the admin out and returns to the login screen.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainscreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
