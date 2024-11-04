package com.example.vortex_app;


import android.app.Activity;

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

public class MainActivity extends AppCompatActivity {


    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_SIGNED_UP = "hasSignedUp";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get references to button objects on the screen
        Button buttonMainscreenEntrant = findViewById(R.id.button_mainscreen_entrant);
        Button buttonMainscreenOrganizer = findViewById(R.id.button_mainscreen_organizer);
        Button buttonMainscreenAdmin = findViewById(R.id.button_mainscreen_admin);

        // Initialize onClickListeners for the 3 buttons
        buttonMainscreenEntrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the user has already signed up
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                boolean hasSignedUp = prefs.getBoolean(KEY_SIGNED_UP, false);

                if (hasSignedUp) {
                    // If signed up, go to EntrantActivity
                    Intent intent = new Intent(MainActivity.this, EntrantActivity.class);
                    startActivity(intent);
                } else {
                    // If not signed up, go to SignupActivity
                    Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            }
        });

        buttonMainscreenOrganizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OrganizerActivity.class);
                startActivity(intent);
            }
        });

        buttonMainscreenAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to navigate to AdminActivity can be added here
            }
        });
    }
}
