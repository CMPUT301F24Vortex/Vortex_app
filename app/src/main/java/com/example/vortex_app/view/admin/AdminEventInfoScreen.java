package com.example.vortex_app.view.admin;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AdminEventInfoScreen extends AppCompatActivity {

    String TAG = "AdminEventScreen";

    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    ImageButton buttonBack;
    Button buttonQRCode;
    TextView eventDays;
    TextView eventTime;
    TextView eventPeriod;
    TextView eventRegDueDate;
    TextView eventRegOpenDate;
    TextView eventPrice;
    TextView eventLocation;
    TextView eventSpots;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_admin_eventinfoscreen);

        //Get info from intent
        String eventID = getIntent().getStringExtra("EVENTID");

        //Set Firestore references
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        //Set ui component references
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

        //Get event info from firestore
        eventsRef.document(eventID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed", error);
                    return;
                }

                if (value != null && value.exists()) {
                    Event event = value.toObject(Event.class);
                    //Set all textViews to reflect user info
                    eventDays.setText(event.getClassDay());
                    eventTime.setText(event.getTime());
                    eventPeriod.setText(event.getPeriod());
                    eventRegDueDate.setText(event.getRegistrationDueDate());
                    eventRegOpenDate.setText(event.getRegistrationOpenDate());
                    eventPrice.setText(Double.toString(event.getPrice()));
                    eventLocation.setText(event.getLocation());
                    eventSpots.setText(Integer.toString(event.getMaxPeople()));

                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(AdminEventInfoScreen.this, );
                //startActivity(intent);
            }
        });


    }
}
