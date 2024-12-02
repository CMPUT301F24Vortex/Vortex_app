package com.example.vortex_app.view.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.model.Event;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AdminEventInfoScreen extends AppCompatActivity {

    private static final String TAG = "AdminEventInfoScreen";

    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    private ImageButton buttonBack;
    private Button buttonQRCode;
    private TextView eventDays;
    private TextView eventTime;
    private TextView eventPeriod;
    private TextView eventRegDueDate;
    private TextView eventRegOpenDate;
    private TextView eventPrice;
    private TextView eventLocation;
    private TextView eventSpots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_eventinfoscreen);

        // Get event ID from intent
        String eventID = getIntent().getStringExtra("EVENTID");
        if (eventID == null || eventID.isEmpty()) {
            Log.e(TAG, "Event ID is missing in the intent.");
            finish();
            return;
        }

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        // Initialize UI components
        buttonBack = findViewById(R.id.button_back);
        buttonQRCode = findViewById(R.id.button_qrcode);
        eventDays = findViewById(R.id.textView_eventdays);
        eventTime = findViewById(R.id.textView_time);
        eventPeriod = findViewById(R.id.textView_period);
        eventRegDueDate = findViewById(R.id.textView_regenddate);
        eventRegOpenDate = findViewById(R.id.textView_regopendate);
        eventPrice = findViewById(R.id.textView_price);
        eventLocation = findViewById(R.id.textView_location);
        eventSpots = findViewById(R.id.textView_maxpeople);

        // Fetch event info from Firestore
        eventsRef.document(eventID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e(TAG, "Error fetching event data", error);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Event event = snapshot.toObject(Event.class);
                    if (event != null) {
                        // Populate UI components
                        eventDays.setText(event.getClassDays() != null ? String.join(", ", event.getClassDays()) : "N/A");
                        eventTime.setText(event.getTime() != null ? event.getTime() : "N/A");
                        eventPeriod.setText(event.getPeriod() != null ? event.getPeriod() : "N/A");
                        eventRegDueDate.setText(event.getRegistrationDueDate() != null ? event.getRegistrationDueDate() : "N/A");
                        eventRegOpenDate.setText(event.getRegistrationOpenDate() != null ? event.getRegistrationOpenDate() : "N/A");
                        eventPrice.setText(event.getPrice() != null ? event.getPrice() : "N/A");
                        eventLocation.setText(event.getLocation() != null ? event.getLocation() : "N/A");
                        eventSpots.setText(event.getMaxPeople() != null ? event.getMaxPeople() : "N/A");
                    } else {
                        Log.e(TAG, "Event data is null.");
                    }
                } else {
                    Log.w(TAG, "No such document for event ID: " + eventID);
                }
            }
        });

        // Set button listeners
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Placeholder for QR Code functionality
                Log.d(TAG, "QR Code button clicked.");
            }
        });
    }
}
