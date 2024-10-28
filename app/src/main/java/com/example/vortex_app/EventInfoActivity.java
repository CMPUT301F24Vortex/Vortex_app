package com.example.vortex_app;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EventInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        // Get event data from the intent
        String classDay = getIntent().getStringExtra("CLASS_DAY");
        String time = getIntent().getStringExtra("TIME");
        String period = getIntent().getStringExtra("PERIOD");
        String regDueDate = getIntent().getStringExtra("REG_DUE_DATE");
        String regOpenDate = getIntent().getStringExtra("REG_OPEN_DATE");
        String price = getIntent().getStringExtra("PRICE");
        String location = getIntent().getStringExtra("LOCATION");
        int maxPeople = getIntent().getIntExtra("MAX_PEOPLE", 0);
        String difficulty = getIntent().getStringExtra("DIFFICULTY");

        // Set the event details in the TextViews
        ((TextView) findViewById(R.id.text_class_day)).setText(classDay);
        ((TextView) findViewById(R.id.text_time)).setText(time);
        ((TextView) findViewById(R.id.text_period)).setText(period);
        ((TextView) findViewById(R.id.text_registration_due_date)).setText(regDueDate);
        ((TextView) findViewById(R.id.text_registration_open_date)).setText(regOpenDate);
        ((TextView) findViewById(R.id.text_price)).setText(price);
        ((TextView) findViewById(R.id.text_location)).setText(location);
        ((TextView) findViewById(R.id.text_max_people)).setText(String.valueOf(maxPeople));
        ((TextView) findViewById(R.id.text_difficulty)).setText(difficulty);
    }
}
