package com.example.vortex_app.view.organizer;

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

import com.example.vortex_app.OrganizerInfo;
import com.example.vortex_app.R;
import com.example.vortex_app.view.location.LocationActivity;
import com.example.vortex_app.view.entrant.CancelledEntrantsActivity;
import com.example.vortex_app.view.entrant.FinalEntrantsActivity;
import com.example.vortex_app.view.entrant.SelectedEntrantsActivity;
import com.example.vortex_app.view.qrcode.OrgQRCodeActivity;
import com.example.vortex_app.view.waitinglist.OrgWaitingListActivity;

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
                intent.putExtra("EVENT_NAME", eventName);
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
