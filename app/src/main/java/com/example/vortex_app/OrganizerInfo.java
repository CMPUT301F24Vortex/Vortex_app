package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OrganizerInfo extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_events_info);
        // Retrieve the event details from the intent
        String eventName = getIntent().getStringExtra("EVENT_NAME");
        String classDay = getIntent().getStringExtra("CLASS_DAY");
        String time = getIntent().getStringExtra("TIME");
        String period = getIntent().getStringExtra("PERIOD");
        String registrationDueDate = getIntent().getStringExtra("REG_DUE_DATE");
        String registrationOpenDate = getIntent().getStringExtra("REG_OPEN_DATE");
        String price = getIntent().getStringExtra("PRICE");
        String location = getIntent().getStringExtra("LOCATION");
        int maxPeople = getIntent().getIntExtra("MAX_PEOPLE", 0);
        String difficulty = getIntent().getStringExtra("DIFFICULTY");


        // Set the data to TextViews or other UI components in the activity
        TextView eventNameTextView = findViewById(R.id.text_event_name);
        TextView classDayTextView = findViewById(R.id.text_class_day);
        TextView timeTextView = findViewById(R.id.text_time);
        TextView periodTextView = findViewById(R.id.text_period);
        TextView registrationDueDateTextView = findViewById(R.id.text_registration_due_date);
        TextView registrationOpenDateTextView = findViewById(R.id.text_registration_open_date);
        TextView priceTextView = findViewById(R.id.text_price);
        TextView locationTextView = findViewById(R.id.text_location);
        TextView maxPeopleTextView = findViewById(R.id.text_max_people);
        TextView difficultyTextView = findViewById(R.id.text_difficulty);

        // Set values to the TextViews
        eventNameTextView.setText(eventName);
        classDayTextView.setText(classDay);
        timeTextView.setText(time);
        periodTextView.setText(period);
        registrationDueDateTextView.setText(registrationDueDate);
        registrationOpenDateTextView.setText(registrationOpenDate);
        priceTextView.setText(price);
        locationTextView.setText(location);
        maxPeopleTextView.setText(String.valueOf(maxPeople));
        difficultyTextView.setText(difficulty);



    }


}
