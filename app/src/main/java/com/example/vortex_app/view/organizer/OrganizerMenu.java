package com.example.vortex_app.view.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.vortex_app.view.entrant.SelectedEntrantsActivity;
import com.example.vortex_app.view.qrcode.OrgQRCodeActivity;
import com.example.vortex_app.view.waitinglist.OrgWaitingListActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OrganizerMenu extends AppCompatActivity {

    private TextView eventNameTextView;
    private String eventName;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.organizer_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton fabNotification = findViewById(R.id.fab_notification);

        // Retrieve eventName and eventID from the intent
        eventName = getIntent().getStringExtra("EVENT_NAME");
        eventID = getIntent().getStringExtra("EVENT_ID");

        Button buttonOrganizerInfo = findViewById(R.id.button_info);
        Button buttonOrganizerWaiting = findViewById(R.id.button_waiting);
        Button buttonOrganizerQr = findViewById(R.id.button_Qrcode);
        Button buttonOrganizerSelected = findViewById(R.id.button_selected);
        Button buttonOrganizerMedia = findViewById(R.id.button_media);
        Button buttonOrganizerCancel = findViewById(R.id.button_cancellation);
        Button buttonOrganizerLocal = findViewById(R.id.button_location);
        Button buttonOrganizerFinal = findViewById(R.id.button_final);

        eventNameTextView = findViewById(R.id.text_event_name);
        eventNameTextView.setText(eventName);

        // Organizer Info
        buttonOrganizerInfo.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, OrganizerInfo.class);
            intent.putExtra("EVENT_ID", eventID);
            intent.putExtra("EVENT_NAME", eventName);
            startActivity(intent);
        });

        // Waiting List
        buttonOrganizerWaiting.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, OrgWaitingListActivity.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });

        // QR Code
        buttonOrganizerQr.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, OrgQRCodeActivity.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });

        // Selected Entrants
        buttonOrganizerSelected.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, SelectedEntrantsActivity.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });

        // Cancelled Entrants
        buttonOrganizerCancel.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, CancelledEntrantsActivity.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivityForResult(intent, 101); // Start with request code 101
        });

        // Location
        buttonOrganizerLocal.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, LocationActivity.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });

        // Final Entrants
        buttonOrganizerFinal.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, FinalEntrantsActivity.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivityForResult(intent, 102); // Start with request code 102
        });

        // Notifications
        fabNotification.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerMenu.this, OrgNotificationsActivity.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });
    }

    // Handle results from CancelledEntrantsActivity and FinalEntrantsActivity
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