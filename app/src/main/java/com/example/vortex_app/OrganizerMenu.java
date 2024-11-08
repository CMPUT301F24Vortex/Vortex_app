package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * {@code OrganizerMenu} is an {@link AppCompatActivity} that provides a menu interface for organizers to manage
 * various aspects of an event. This includes viewing event information, accessing the waiting list,
 * scanning QR codes, managing selected and final entrants, and checking the event location.
 */
public class OrganizerMenu extends AppCompatActivity {

    private TextView eventNameTextView;

    /**
     * Called when the activity is created. Sets up the layout, retrieves event information from the intent,
     * and initializes buttons that navigate to different organizer management screens.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.organizer_main);

        // Adjust layout for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve event name and ID from intent
        String eventName = getIntent().getStringExtra("EVENT_NAME");
        String eventID = getIntent().getStringExtra("EVENT_ID");

        // Initialize TextView for displaying event name
        eventNameTextView = findViewById(R.id.text_event_name);
        eventNameTextView.setText(eventName);

        // Initialize and set up button listeners for navigation
        Button buttonOrganizerInfo = findViewById(R.id.button_info);
        Button buttonOrganizerWaiting = findViewById(R.id.button_waiting);
        Button buttonOrganizerQr = findViewById(R.id.button_Qrcode);
        Button buttonOrganizerSelected = findViewById(R.id.button_selected);
        Button buttonOrganizerMedia = findViewById(R.id.button_media);
        Button buttonOrganizerCancel = findViewById(R.id.button_cancellation);
        Button buttonOrganizerLocal = findViewById(R.id.button_location);
        Button buttonOrganizerFinal = findViewById(R.id.button_final);

        // Navigate to OrganizerInfo screen
        buttonOrganizerInfo.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, OrganizerInfo.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });

        // Navigate to OrgWaitingListActivity screen
        buttonOrganizerWaiting.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, OrgWaitingListActivity.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });

        // Navigate to OrgQRCodeActivity screen
        buttonOrganizerQr.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, OrgQRCodeActivity.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });

        // Navigate to SelectedEntrantsActivity screen
        buttonOrganizerSelected.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, SelectedEntrantsActivity.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });

        // Placeholder for Media Management screen (currently no action defined)
        buttonOrganizerMedia.setOnClickListener(view -> {
            // TODO: Add code for Media Management
        });

        // Navigate to CancelledEntrantsActivity screen
        buttonOrganizerCancel.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, CancelledEntrantsActivity.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });

        // Navigate to LocationActivity screen
        buttonOrganizerLocal.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, LocationActivity.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });

        // Navigate to FinalEntrantsActivity screen
        buttonOrganizerFinal.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, FinalEntrantsActivity.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });
    }
}
