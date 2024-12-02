package com.example.vortex_app.view.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.view.MainActivity;
import com.example.vortex_app.view.profile.AdminProfileScreen;

public class AdminMainscreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mainscreen);

        //Set button id's
        Button profileButton = findViewById(R.id.button_profile);
        Button facilityEventButton = findViewById(R.id.button_facility_event);
        Button imageButton = findViewById(R.id.button_image);
        Button notificationButton = findViewById(R.id.button_notification);
        Button logoutButton = findViewById(R.id.button_logout);


        //Set click listeners for the buttons
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainscreen.this, AdminProfileScreen.class);
                startActivity(intent);
            }
        });

        facilityEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainscreen.this, AdminFacilityScreen.class);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainscreen.this, AdminImageScreen.class);
                startActivity(intent);
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainscreen.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
