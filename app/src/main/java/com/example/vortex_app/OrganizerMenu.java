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

public class OrganizerMenu extends AppCompatActivity {

    private TextView eventNameTextView;

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



        //Retrieve data for specific Event
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

        //add other necessary data


        // Get references to button objects on the screen
        Button buttonOrganizerInfo = findViewById(R.id.button_info);
        Button buttonOrganizerWaiting = findViewById(R.id.button_waiting);
        Button buttonOrganizerQr = findViewById(R.id.button_Qrcode);
        Button buttonOrganizerSelected = findViewById(R.id.button_selected);
        Button buttonOrganizerMedia = findViewById(R.id.button_media);
        Button buttonOrganizerCancel = findViewById(R.id.button_cancellation);
        Button buttonOrganizerLocal = findViewById(R.id.button_location);
        Button buttonOrganizerFinal = findViewById(R.id.button_final);

        // Initialize the TextView
        eventNameTextView = findViewById(R.id.text_event_name);
        // Set the text of the TextView to the event name
        eventNameTextView.setText(eventName);


        // Initialize onClickListeners for the 3 buttons
        buttonOrganizerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OrganizerMenu.this, OrganizerInfo.class);
                // Pass the event details to OrganizerInfo activity
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


            }
        });

        buttonOrganizerWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerMenu.this, OrgWaitingListActivity.class);
                intent.putExtra("EVENTID", eventID);
                startActivity(intent);


            }
        });

        buttonOrganizerQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerMenu.this, OrgQRCodeActivity.class);
                intent.putExtra("EVENTID", eventID);
                startActivity(intent);


            }
        });

        buttonOrganizerSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerMenu.this, SelectedEntrantsActivity.class);
                intent.putExtra("EVENTID", eventID);
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
                intent.putExtra("EVENTID", eventID);
                startActivity(intent);


            }
        });
        buttonOrganizerLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerMenu.this, LocationActivity.class);
                intent.putExtra("EVENTID", eventID);
                startActivity(intent);

            }
        });
        buttonOrganizerFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerMenu.this, FinalEntrantsActivity.class);
                intent.putExtra("EVENTID", eventID);
                startActivity(intent);

            }
        });

    }
}
