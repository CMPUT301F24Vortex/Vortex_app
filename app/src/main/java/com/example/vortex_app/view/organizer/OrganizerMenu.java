package com.example.vortex_app.view.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vortex_app.R;
import com.example.vortex_app.view.location.LocationActivity;
import com.example.vortex_app.view.entrant.CancelledEntrantsActivity;
import com.example.vortex_app.view.entrant.FinalEntrantsActivity;
import com.example.vortex_app.view.entrant.OrgSelectedEntrantsActivity;
import com.example.vortex_app.view.qrcode.OrgQRCodeActivity;
import com.example.vortex_app.view.waitinglist.OrgWaitingListActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * OrganizerMenu class handles the main activity for event organizers.
 * It provides access to various functionalities such as viewing entrants, managing
 * cancellations, generating QR codes, and accessing location-related information.
 */
public class OrganizerMenu extends AppCompatActivity {

    private TextView eventNameTextView;
    private String eventName;
    private String eventID;

    /**
     * Called when the activity is first created. It initializes UI components
     * and sets up click listeners for various buttons in the organizer menu.
     *
     * @param savedInstanceState a Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.organizer_main);

        // Adjusts padding based on system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton fabNotification = findViewById(R.id.fab_notification);

        // Retrieve eventName and eventID passed from previous activity
        eventName = getIntent().getStringExtra("EVENT_NAME");
        eventID = getIntent().getStringExtra("EVENT_ID");

        Button buttonOrganizerInfo = findViewById(R.id.button_info);
        Button buttonOrganizerWaiting = findViewById(R.id.button_waiting);
        Button buttonOrganizerQr = findViewById(R.id.button_Qrcode);
        Button buttonOrganizerSelected = findViewById(R.id.button_selected);
        Button buttonOrganizerCancel = findViewById(R.id.button_cancellation);
        Button buttonOrganizerLocal = findViewById(R.id.button_location);
        Button buttonOrganizerFinal = findViewById(R.id.button_final);

        eventNameTextView = findViewById(R.id.text_event_name);
        eventNameTextView.setText(eventName);

        // Setup click listeners for buttons
        buttonOrganizerInfo.setOnClickListener(view -> openOrganizerInfo());
        buttonOrganizerWaiting.setOnClickListener(view -> openWaitingList());
        buttonOrganizerQr.setOnClickListener(view -> openQRCodeActivity());
        buttonOrganizerSelected.setOnClickListener(view -> openSelectedEntrants());
        buttonOrganizerCancel.setOnClickListener(view -> openCancelledEntrants());
        buttonOrganizerLocal.setOnClickListener(view -> openLocationActivity());
        buttonOrganizerFinal.setOnClickListener(view -> openFinalEntrants());

        // Notifications button click listener
        fabNotification.setOnClickListener(view -> openNotifications());

        // Back button to return to OrganizerActivity
        ImageView backButton = findViewById(R.id.imageViewBack);
        backButton.setOnClickListener(view -> goBack());
    }

    /**
     * Opens the OrganizerInfo activity with the event details.
     */
    private void openOrganizerInfo() {
        Intent intent = new Intent(OrganizerMenu.this, OrganizerInfo.class);
        intent.putExtra("EVENT_ID", eventID);
        intent.putExtra("EVENT_NAME", eventName);
        startActivity(intent);
    }

    /**
     * Opens the Waiting List activity with the event details.
     */
    private void openWaitingList() {
        Intent intent = new Intent(OrganizerMenu.this, OrgWaitingListActivity.class);
        intent.putExtra("EVENT_ID", eventID);
        startActivity(intent);
    }

    /**
     * Opens the QR Code activity with the event details.
     */
    private void openQRCodeActivity() {
        Intent intent = new Intent(OrganizerMenu.this, OrgQRCodeActivity.class);
        intent.putExtra("EVENT_ID", eventID);
        startActivity(intent);
    }

    /**
     * Opens the Selected Entrants activity with the event details.
     */
    private void openSelectedEntrants() {
        Intent intent = new Intent(OrganizerMenu.this, OrgSelectedEntrantsActivity.class);
        intent.putExtra("EVENT_ID", eventID);
        startActivity(intent);
    }

    /**
     * Opens the Cancelled Entrants activity with the event details.
     */
    private void openCancelledEntrants() {
        Intent intent = new Intent(OrganizerMenu.this, CancelledEntrantsActivity.class);
        intent.putExtra("EVENT_ID", eventID);
        startActivityForResult(intent, 101); // Start with request code 101
    }

    /**
     * Opens the Location activity with the event details.
     */
    private void openLocationActivity() {
        Intent intent = new Intent(OrganizerMenu.this, LocationActivity.class);
        intent.putExtra("EVENT_ID", eventID);
        startActivity(intent);
    }

    /**
     * Opens the Final Entrants activity with the event details.
     */
    private void openFinalEntrants() {
        Intent intent = new Intent(OrganizerMenu.this, FinalEntrantsActivity.class);
        intent.putExtra("EVENT_ID", eventID);
        startActivityForResult(intent, 102); // Start with request code 102
    }

    /**
     * Opens the Notifications activity with the event details.
     */
    private void openNotifications() {
        Intent intent = new Intent(OrganizerMenu.this, OrgNotificationsActivity.class);
        intent.putExtra("EVENT_ID", eventID);
        startActivity(intent);
    }

    /**
     * Navigates back to the OrganizerActivity and passes the event details.
     */
    private void goBack() {
        Intent intent = new Intent(OrganizerMenu.this, OrganizerActivity.class);
        intent.putExtra("EVENT_ID", eventID); // Pass the event ID back if needed
        intent.putExtra("EVENT_NAME", eventName); // Pass the event name back if needed
        startActivity(intent);
        finish(); // Finish the current activity
    }

    /**
     * Handles results from CancelledEntrantsActivity and FinalEntrantsActivity.
     * It updates the event ID if returned from those activities.
     *
     * @param requestCode the request code used to start the activity.
     * @param resultCode  the result code returned by the child activity.
     * @param data        the Intent returned by the child activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String returnedEventId = data.getStringExtra("EVENT_ID");
            if (returnedEventId != null) {
                eventID = returnedEventId; // Update the event ID
                Toast.makeText(this, "Returned Event ID: " + eventID, Toast.LENGTH_SHORT).show();
            }
        }

        switch (requestCode) {
            case 101: // CancelledEntrantsActivity
                Log.d("OrganizerMenu", "Returned from Cancelled Entrants");
                break;
            case 102: // FinalEntrantsActivity
                Log.d("OrganizerMenu", "Returned from Final Entrants");
                break;
            default:
                break;
        }
    }
}
