package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrganizerMenu extends AppCompatActivity {

    private TextView eventNameTextView;

    // Log tag for debugging
    private static final String TAG = "OrganizerMenu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_main);

        // Apply Edge-to-Edge design
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve event name and ID from the intent
        String eventName = getIntent().getStringExtra("EVENT_NAME");
        String eventID = getIntent().getStringExtra("EVENT_ID");

        // Log the event details
        if (eventName == null || eventID == null || eventID.isEmpty()) {
            Log.e(TAG, "Invalid event data: Event Name = " + eventName + ", Event ID = " + eventID);
            finish(); // Close activity if event data is invalid
            return;
        } else {
            Log.d(TAG, "Received Event Name: " + eventName + ", Event ID: " + eventID);
        }

        // Initialize views
        eventNameTextView = findViewById(R.id.text_event_name);
        eventNameTextView.setText(eventName);

        // Initialize buttons
        Button buttonOrganizerInfo = findViewById(R.id.button_info);
        Button buttonOrganizerWaiting = findViewById(R.id.button_waiting);
        Button buttonOrganizerQr = findViewById(R.id.button_Qrcode);
        Button buttonOrganizerSelected = findViewById(R.id.button_selected);
        Button buttonOrganizerMedia = findViewById(R.id.button_media);
        Button buttonOrganizerCancel = findViewById(R.id.button_cancellation);
        Button buttonOrganizerLocal = findViewById(R.id.button_location);
        Button buttonOrganizerFinal = findViewById(R.id.button_final);

        // Set up button listeners
        buttonOrganizerInfo.setOnClickListener(view -> navigateToActivity(OrganizerInfo.class, eventID, eventName));
        buttonOrganizerWaiting.setOnClickListener(view -> navigateToActivity(OrgWaitingListActivity.class, eventID, eventName));
        buttonOrganizerQr.setOnClickListener(view -> navigateToActivity(OrgQRCodeActivity.class, eventID, eventName));
        buttonOrganizerSelected.setOnClickListener(view -> navigateToActivity(SelectedEntrantsActivity.class, eventID, eventName));
        buttonOrganizerMedia.setOnClickListener(view -> {
            Log.d(TAG, "Media button clicked, no activity assigned yet.");
            // Future functionality placeholder
        });
        buttonOrganizerCancel.setOnClickListener(view -> navigateToActivity(CancelledEntrantsActivity.class, eventID, eventName));
        buttonOrganizerLocal.setOnClickListener(view -> navigateToActivity(LocationActivity.class, eventID, eventName));
        buttonOrganizerFinal.setOnClickListener(view -> navigateToActivity(FinalEntrantsActivity.class, eventID, eventName));
    }

    /**
     * Navigate to the specified activity with event ID and name passed as extras.
     *
     * @param targetActivity The target activity class.
     * @param eventID        The event ID to pass to the target activity.
     * @param eventName      The event name to pass to the target activity (optional).
     */
    private void navigateToActivity(Class<?> targetActivity, String eventID, String eventName) {
        if (eventID == null || eventID.isEmpty()) {
            Log.e(TAG, "Event ID is null or empty. Cannot navigate.");
            return;
        }

        Intent intent = new Intent(OrganizerMenu.this, targetActivity);
        intent.putExtra("EVENT_ID", eventID);
        if (eventName != null) {
            intent.putExtra("EVENT_NAME", eventName);
        }
        startActivity(intent);

        Log.d(TAG, "Navigating to " + targetActivity.getSimpleName() + " with Event ID: " + eventID);
    }
}
