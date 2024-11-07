package com.example.vortex_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddEvent extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private OrganizerEventAdapter eventAdapter;
    private ArrayList<Event> eventList;
    private EditText eventNameInput;
    private EditText eventLocationInput;
    private EditText eventClassDayInput; // Add more fields for inputs
    private EditText eventTimeInput;
    private EditText eventPeriodInput;
    private EditText eventRegDueDateInput;
    private EditText eventRegOpenDateInput;
    private EditText eventPriceInput;
    private EditText eventMaxPeopleInput;
    private EditText eventDifficultyInput;
    private Button addButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventNameInput = findViewById(R.id.event_name_input);
        eventLocationInput = findViewById(R.id.event_location_input);
        eventClassDayInput = findViewById(R.id.event_class_day_input);
        eventTimeInput = findViewById(R.id.event_time_input);
        eventPeriodInput = findViewById(R.id.event_period_input);
        eventRegDueDateInput = findViewById(R.id.event_reg_due_date_input);
        eventRegOpenDateInput = findViewById(R.id.event_reg_open_date_input);
        eventPriceInput = findViewById(R.id.event_price_input);
        eventMaxPeopleInput = findViewById(R.id.event_max_people_input);
        eventDifficultyInput = findViewById(R.id.event_difficulty_input);
        addButton = findViewById(R.id.add_event_button);
        //eventList = (ArrayList<Event>) getIntent().getSerializableExtra("EVENT_LIST");

        if (eventList == null) {
            eventList = new ArrayList<>();
        }

        addButton.setOnClickListener(v -> addEvent());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addEvent() {
        String eventName = eventNameInput.getText().toString();
        String eventLocation = eventLocationInput.getText().toString();
        String classDay = eventClassDayInput.getText().toString();
        String time = eventTimeInput.getText().toString();
        String period = eventPeriodInput.getText().toString();
        String regDueDate = eventRegDueDateInput.getText().toString();
        String regOpenDate = eventRegOpenDateInput.getText().toString();
        String price;
        int maxPeople;

        // Input validation
        if (eventName.isEmpty() || eventLocation.isEmpty() || classDay.isEmpty() || time.isEmpty() ||
                period.isEmpty() || regDueDate.isEmpty() || regOpenDate.isEmpty() ||
                eventPriceInput.getText().toString().isEmpty() || eventMaxPeopleInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse price and max people
        try {
            price = String.valueOf(Double.parseDouble(eventPriceInput.getText().toString()));
            maxPeople = Integer.parseInt(eventMaxPeopleInput.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> event = new HashMap<>();
        event.put("eventName", eventName);
        event.put("eventLocation", eventLocation);
        event.put("classDay", classDay);
        event.put("time", time);
        event.put("period", period);
        event.put("regDueDate", regDueDate);
        event.put("regOpenDate", regOpenDate);
        event.put("price", price);
        event.put("maxPeople", maxPeople);
        event.put("difficulty", eventDifficultyInput.getText().toString());
        event.put("geoLoc", true);

        db.collection("events")
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    Event newEvent = new Event(eventName, R.drawable.sample_event_image, classDay, time, period, regDueDate, regOpenDate, price, eventLocation, maxPeople, eventDifficultyInput.getText().toString(), true);
                    eventList.add(newEvent);
                    eventAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Event added", Toast.LENGTH_SHORT).show();
                    clearInputFields();
                    launchOrganizerActivity();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error adding event", Toast.LENGTH_SHORT).show();
                });
    }




    private void clearInputFields() {
        eventNameInput.setText("");
        eventLocationInput.setText("");
        eventClassDayInput.setText("");
        eventTimeInput.setText("");
        eventPeriodInput.setText("");
        eventRegDueDateInput.setText("");
        eventRegOpenDateInput.setText("");
        eventPriceInput.setText("");
        eventMaxPeopleInput.setText("");
        eventDifficultyInput.setText("");

    }




    private void launchOrganizerActivity() {
        Intent intent = new Intent(AddEvent.this, OrganizerActivity.class);
        //intent.putExtra("EVENT_LIST", eventList); // Pass the event list to EventListActivity
        startActivity(intent);
    }


}