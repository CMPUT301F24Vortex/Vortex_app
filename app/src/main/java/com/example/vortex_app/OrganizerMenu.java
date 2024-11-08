package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * OrganizerMenu provides options for an organizer to manage a specific event.
 * It offers navigation to different activities such as viewing event details, handling waiting lists,
 * generating QR codes, viewing selected entrants, and more.
 */
public class OrganizerMenu extends AppCompatActivity {

    private TextView eventNameTextView;

    /**
     * Called when the activity is created. Sets up the layout, retrieves event details from the intent,
     * initializes buttons for different actions, and handles navigation to various activities related to the event.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.organizer_main);

        // Apply window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve data for the specific event
        String eventName = getIntent().getStringExtra("EVENT_NAME");
        String classDay = getIntent().getStringExtra("CLASS_DAY");
        String time = getIntent().getStringExtra("TIME");
        String period = getIntent().getStringExtra("PERIOD");
        String regDueDate = getIntent().getStringExtra("REG_DUE_DATE");
        String regOpenDate = getIntent().getStringExtra("REG_OPEN_DATE");
        double price = getIntent().getDoubleExtra("PRICE", 0);
        String location = getIntent().getStringExtra("LOCATION");
        int maxPeople = getIntent().getIntExtra("MAX_PEOPLE", 0);
        String difficulty = getIntent().getStringExtra("DIFFICULTY");
        String eventID = getIntent().getStringExtra("EVENTID");

        // Initialize the TextView for event name and set its value
        eventNameTextView = findViewById(R.id.text_event_name);
        eventNameTextView.setText(eventName);

        // Initialize buttons for different actions
        Button buttonOrganizerInfo = findViewById(R.id.button_info);
        Button buttonOrganizerWaiting = findViewById(R.id.button_waiting);
        Button buttonOrganizerQr = findViewById(R.id.button_Qrcode);
        Button buttonOrganizerSelected = findViewById(R.id.button_selected);
        Button buttonOrganizerMedia = findViewById(R.id.button_media);
        Button buttonOrganizerCancel = findViewById(R.id.button_cancellation);
        Button buttonOrganizerLocal = findViewById(R.id.button_location);
        Button buttonOrganizerFinal = findViewById(R.id.button_final);

        // Set up onClickListeners for each button to handle navigation to various activities
        buttonOrganizerInfo.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, OrganizerInfo.class);
            intent.putExtra("EVENT_NAME", eventName);
            intent.putExtra("CLASS_DAY", classDay);
            intent.putExtra("TIME", time);
            intent.putExtra("PERIOD", period);
            intent.putExtra("REG_DUE_DATE", regDueDate);
            intent.putExtra("REG_OPEN_DATE", regOpenDate);
            intent.putExtra("PRICE", String.valueOf(price));
            intent.putExtra("LOCATION", location);
            intent.putExtra("MAX_PEOPLE", maxPeople);
            intent.putExtra("DIFFICULTY", difficulty);
            startActivity(intent);
        });

        buttonOrganizerWaiting.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, OrgWaitingListActivity.class);
            intent.putExtra("EVENTID", eventID);
            startActivity(intent);
        });

        buttonOrganizerQr.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, OrgQRCodeActivity.class);
            intent.putExtra("EVENTID", eventID);
            startActivity(intent);
        });

        buttonOrganizerSelected.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, SelectedEntrantsActivity.class);
            intent.putExtra("EVENTID", eventID);
            startActivity(intent);
        });

        buttonOrganizerCancel.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, CancelledEntrantsActivity.class);
            intent.putExtra("EVENTID", eventID);
            startActivity(intent);
        });

        buttonOrganizerLocal.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, LocationActivity.class);
            intent.putExtra("EVENTID", eventID);
            startActivity(intent);
        });

        buttonOrganizerFinal.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, FinalEntrantsActivity.class);
            intent.putExtra("EVENTID", eventID);
            startActivity(intent);
        });

        // ButtonOrganizerMedia is not implemented yet
        buttonOrganizerMedia.setOnClickListener(view -> {
            // Placeholder for future implementation
        });
    }
}
