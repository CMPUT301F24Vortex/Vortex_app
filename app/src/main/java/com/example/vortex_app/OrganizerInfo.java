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
    private Button editButton, doneButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_events_info);


        String eventID = getIntent().getStringExtra("EVENT_ID");
        String eventName = getIntent().getStringExtra("EVENT_NAME");
        editButton = findViewById(R.id.edit_button_org);
        doneButton = findViewById(R.id.done_button);
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

            Intent intent = new Intent(OrganizerInfo.this, AddEvent.class);
            intent.putExtra("EVENT_ID", eventID);
            startActivity(intent);
        });

        doneButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerInfo.this, OrganizerMenu.class);
            intent.putExtra("EVENT_ID", eventID);
            intent.putExtra("EVENT_NAME", eventName);
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
                    eventNameTextView.setText(eventName);
                    String eventDay = document.getString("classDay");
                    classDayTextView.setText(eventDay);
                    String eventTime = document.getString("time");
                    timeTextView.setText(eventTime);
                    String startPeriod = document.getString("startPeriod");
                    String endPeriod  =  document.getString("endPeriod");
                    periodTextView.setText(startPeriod + " ~ " + endPeriod);
                    String regDueDate = document.getString("regDueDate");
                    registrationDueDateTextView.setText(regDueDate);
                    String regOpenDate =  document.getString("regOpenDate");
                    registrationOpenDateTextView.setText(regOpenDate);
                    String price  = document.getString("price");
                    priceTextView.setText("$"+price);
                    String maxPeople =  document.getString("maxPeople");
                    maxPeopleTextView.setText(maxPeople);
                    String poster = document.getString("imageUrl");
                    if (poster != null && !poster.isEmpty()) {
                        Glide.with(this)
                                .load(poster)
                                .into(posterImageView);
                    }





                }

            }



        });
    }


}