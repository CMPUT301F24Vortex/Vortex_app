package com.example.vortex_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.vortex_app.WaitingListActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EventInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);



        // Retrieve the event details from the intent
        String eventName = getIntent().getStringExtra("EVENT_NAME");
        String classDay = getIntent().getStringExtra("CLASS_DAY");
        String time = getIntent().getStringExtra("TIME");
        String period = getIntent().getStringExtra("PERIOD");
        String registrationDueDate = getIntent().getStringExtra("REGISTRATION_DUE_DATE");
        String registrationOpenDate = getIntent().getStringExtra("REGISTRATION_OPEN_DATE");
        String price = getIntent().getStringExtra("PRICE");
        String location = getIntent().getStringExtra("LOCATION");
        int maxPeople = getIntent().getIntExtra("MAX_PEOPLE", 0);
        String difficulty = getIntent().getStringExtra("DIFFICULTY");
        boolean requiresGeolocation = getIntent().getBooleanExtra("REQUIRES_GEOLOCATION", false);

        // Set the data to TextViews or other UI components in the activity
        // Example:
        TextView eventNameTextView = findViewById(R.id.text_event_name);
        eventNameTextView.setText(eventName);

        // Repeat for other fields and display them on the UI

        // Example: show warning if geolocation is required
        if (requiresGeolocation) {
            // Display warning message about geolocation requirement
            // Add your logic here
        }

        // Set up the Join Waiting List button
        Button joinWaitingListButton = findViewById(R.id.button_join_waiting_list);
        joinWaitingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show warning dialog if geolocation is required
                if (requiresGeolocation) {
                    showGeolocationWarningDialog();
                } else {
                    navigateToWaitingList();
                }
            }
        });
    }


    private void showGeolocationWarningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_geolocation_warning, null);
        builder.setView(dialogView);

        Button quitButton = dialogView.findViewById(R.id.button_quit);
        Button gotItButton = dialogView.findViewById(R.id.button_got_it);

        AlertDialog dialog = builder.create();

        // Quit button logic
        quitButton.setOnClickListener(v -> dialog.dismiss());

        // Got it button logic
        gotItButton.setOnClickListener(v -> {
            // Handle "Got it" action
            dialog.dismiss();
            navigateToWaitingList();
        });

        dialog.show();
    }

    // Method to navigate to WaitingListActivity
    private void navigateToWaitingList() {
        Intent intent = new Intent(this, WaitingListActivity.class);
        startActivity(intent);
    }
}
