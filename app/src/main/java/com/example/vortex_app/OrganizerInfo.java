package com.example.vortex_app;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class OrganizerInfo extends AppCompatActivity {

    private TextView eventNameTextView;
    private TextView classDayTextView;
    private TextView timeTextView;
    private TextView periodTextView;
    private TextView registrationDueDateTextView;
    private TextView registrationOpenDateTextView;
    private TextView priceTextView;
    private TextView locationTextView;
    private TextView maxPeopleTextView;
    private TextView difficultyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_events_info);


        String eventID = getIntent().getStringExtra("EVENT_ID");
        eventNameTextView = findViewById(R.id.text_event_name);
        classDayTextView = findViewById(R.id.text_class_day);
        timeTextView = findViewById(R.id.text_time);
        periodTextView = findViewById(R.id.text_period);
        registrationDueDateTextView = findViewById(R.id.text_registration_due_date);
        registrationOpenDateTextView = findViewById(R.id.text_registration_open_date);
        priceTextView = findViewById(R.id.text_price);
        locationTextView = findViewById(R.id.text_location);
        maxPeopleTextView = findViewById(R.id.text_max_people);
        difficultyTextView = findViewById(R.id.text_difficulty);
        loadEventDetails(eventID);
    }

    private void loadEventDetails(String eventID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("events").document(eventID);

        // Fetch the document
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // Get the event details from the document
                    String eventName = document.getString("eventName");
                    String classDay = document.getString("classDay");
                    String time = document.getString("time");
                    String period = document.getString("period");
                    String registrationDueDate = document.getString("regDueDate");
                    String registrationOpenDate = document.getString("regOpenDate");
                    String price = document.getString("price");
                    String location = document.getString("eventLocation");
                    int maxPeople = document.getLong("maxPeople") != null ? document.getLong("maxPeople").intValue() : 0;
                    String difficulty = document.getString("difficulty");


                    eventNameTextView.setText(eventName);
                    classDayTextView.setText(classDay);
                    timeTextView.setText(time);
                    periodTextView.setText(period);
                    registrationDueDateTextView.setText(registrationDueDate);
                    registrationOpenDateTextView.setText(registrationOpenDate);
                    priceTextView.setText(String.valueOf(price));
                    locationTextView.setText(location);
                    maxPeopleTextView.setText(String.valueOf(maxPeople));
                    difficultyTextView.setText(difficulty);

                }

            }


        });
    }
}