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

/**
 * EventInfoActivity displays detailed information about a specific event.
 * It also allows users to join a waiting list, with optional geolocation warnings.
 */
public class EventInfoActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created. Sets up the layout, retrieves event data from the intent,
     * and initializes UI components.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
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

        // Display the event name
        TextView eventNameTextView = findViewById(R.id.text_event_name);
        eventNameTextView.setText(eventName);

        // Additional logic to handle other fields and UI components
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

    /**
     * Shows a warning dialog if geolocation is required.
     */
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

    /**
     * Navigates the user to the WaitingListActivity.
     */
    private void navigateToWaitingList() {
        Intent intent = new Intent(this, WaitingListActivity.class);
        startActivity(intent);
    }
}
