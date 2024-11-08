package com.example.vortex_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * {@code EventInfoActivity} is an {@link AppCompatActivity} that displays detailed information
 * about a specific event. Users can view details such as event name, time, location, and more.
 * If the event requires geolocation, a warning dialog is shown when users try to join the waiting list.
 */
public class EventInfoActivity extends AppCompatActivity {

    /**
     * Called when the activity is created. Retrieves event details from the intent,
     * sets them in the UI components, and initializes the button to join the waiting list.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
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

        // Set the data to TextViews or other UI components in the activity
        TextView eventNameTextView = findViewById(R.id.text_event_name);
        eventNameTextView.setText(eventName);

        // Display warning if geolocation is required
        if (requiresGeolocation) {
            // Logic for geolocation requirement warning
        }

        // Set up the Join Waiting List button
        Button joinWaitingListButton = findViewById(R.id.button_join_waiting_list);
        joinWaitingListButton.setOnClickListener(view -> {
            if (requiresGeolocation) {
                showGeolocationWarningDialog();
            } else {
                navigateToWaitingList();
            }
        });
    }

    /**
     * Shows a warning dialog if the event requires geolocation for joining the waiting list.
     * The dialog contains options to confirm understanding or quit.
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
            dialog.dismiss();
            navigateToWaitingList();
        });

        dialog.show();
    }

    /**
     * Navigates to {@link WaitingListActivity} when the user confirms joining the waiting list.
     */
    private void navigateToWaitingList() {
        Intent intent = new Intent(this, WaitingListActivity.class);
        startActivity(intent);
    }
}
