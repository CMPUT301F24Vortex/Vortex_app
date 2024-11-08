package com.example.vortex_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * MainActivity serves as the main entry point for the Vortex application. It provides
 * buttons to navigate to different sections of the app, such as Entrant, Organizer, and Admin screens.
 * This activity also applies edge-to-edge display configurations.
 */
public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_SIGNED_UP = "hasSignedUp";

    /**
     * Called when the activity is created. Sets up the layout, configures the UI,
     * and handles button click events to navigate to different screens.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Apply window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get references to button objects on the screen
        Button buttonMainscreenEntrant = findViewById(R.id.button_mainscreen_entrant);
        Button buttonMainscreenOrganizer = findViewById(R.id.button_mainscreen_organizer);
        Button buttonMainscreenAdmin = findViewById(R.id.button_mainscreen_admin);

        // Set onClickListeners for buttons
        buttonMainscreenEntrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to EntrantActivity
                Intent intent = new Intent(MainActivity.this, EntrantActivity.class);
                startActivity(intent);
            }
        });

        buttonMainscreenOrganizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to OrganizerActivity
                Intent intent = new Intent(MainActivity.this, OrganizerActivity.class);
                startActivity(intent);
            }
        });

        buttonMainscreenAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to AdminActivity (to be implemented)
            }
        });
    }
}
