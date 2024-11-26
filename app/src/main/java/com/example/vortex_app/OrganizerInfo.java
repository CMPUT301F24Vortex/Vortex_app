package com.example.vortex_app;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.bumptech.glide.Glide;

public class OrganizerInfo extends AppCompatActivity {

    private TextView eventNameTextView;
    private TextView classDayTextView;
    private TextView timeTextView;
    private TextView periodTextView;
    private TextView registrationDueDateTextView;
    private TextView registrationOpenDateTextView;
    private TextView priceTextView;
    private TextView maxPeopleTextView;
    private ImageView posterImageView;
    private Button editButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_events_info);


        String eventID = getIntent().getStringExtra("EVENT_ID");
        editButton = findViewById(R.id.edit_button_org);
        eventNameTextView = findViewById(R.id.text_event_name);
        classDayTextView = findViewById(R.id.text_class_day);
        timeTextView = findViewById(R.id.text_time);
        periodTextView= findViewById(R.id.text_period);
        registrationDueDateTextView = findViewById(R.id.text_registration_due_date);
        registrationOpenDateTextView = findViewById(R.id.text_registration_open_date);
        priceTextView = findViewById(R.id.text_price);
        maxPeopleTextView = findViewById(R.id.text_max_people);
        posterImageView = findViewById(R.id.image_event);
        loadEventDetails(eventID);


        editButton.setOnClickListener(v -> {
            // Pass event details to AddEvent Activity for editing
            Intent intent = new Intent(OrganizerInfo.this, AddEvent.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });
    }

    private void loadEventDetails(String eventID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("events").document(eventID);

        // Fetch the document
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {

                    String eventName = document.getString("eventName");
                    String classDay = document.getString("classDay");
                    String time = document.getString("time");
                    String startPeriod = document.getString("startPeriod");
                    String endPeriod = document.getString("endPeriod");
                    String registrationDueDate = document.getString("regDueDate");
                    String registrationOpenDate = document.getString("regOpenDate");
                    String price = document.getString("price");
                    String poster = document.getString("imageUrl");
                    if (poster != null && !poster.isEmpty()) {
                        Glide.with(this)
                                .load(poster)  // the URL of the image
                                .into(posterImageView);  // The ImageView to load the image into
                    }
                    int maxPeople = document.getLong("maxPeople") != null ? document.getLong("maxPeople").intValue() : 0;


                    eventNameTextView.setText(eventName);
                    classDayTextView.setText(classDay);
                    timeTextView.setText(time);
                    periodTextView.setText(startPeriod+"~"+ endPeriod);
                    registrationDueDateTextView.setText(registrationDueDate);
                    registrationOpenDateTextView.setText(registrationOpenDate);
                    priceTextView.setText(String.valueOf(price));
                    maxPeopleTextView.setText(String.valueOf(maxPeople));



                }

            }



        });
    }


}