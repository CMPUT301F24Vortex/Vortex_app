package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        String eventName = getIntent().getStringExtra("EVENT_NAME");
        String eventID  = getIntent().getStringExtra("EVENT_ID");



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


        buttonOrganizerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OrganizerMenu.this, OrganizerInfo.class);
                intent.putExtra("EVENT_ID", eventID);

                startActivity(intent);

            }
        });

        buttonOrganizerWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerMenu.this, OrgWaitingListActivity.class);
                intent.putExtra("EVENT_ID", eventID);
                startActivity(intent);


            }
        });

        buttonOrganizerQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerMenu.this, OrgQRCodeActivity.class);
                intent.putExtra("EVENT_ID", eventID);
                startActivity(intent);


            }
        });

        buttonOrganizerSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerMenu.this, SelectedEntrantsActivity.class);
                intent.putExtra("EVENT_ID", eventID);
                startActivity(intent);


            }
        });
        buttonOrganizerMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        buttonOrganizerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerMenu.this, CancelledEntrantsActivity.class);
                intent.putExtra("EVENT_ID", eventID);
                startActivity(intent);


            }
        });
        buttonOrganizerLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerMenu.this, LocationActivity.class);
                intent.putExtra("EVENT_ID", eventID);
                startActivity(intent);

            }
        });
        buttonOrganizerFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerMenu.this, FinalEntrantsActivity.class);
                intent.putExtra("EVENT_ID", eventID);
                startActivity(intent);

            }
        });

    }
}
