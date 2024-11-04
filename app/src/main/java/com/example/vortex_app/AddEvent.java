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
import java.util.ArrayList;

public class AddEvent extends AppCompatActivity {

    private RecyclerView recyclerView;
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
    private RecyclerView.Adapter OrganizerEventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event); // Ensure this layout is created

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

        eventList = new ArrayList<>();
        eventAdapter = new OrganizerEventAdapter(this, eventList);
        recyclerView.setAdapter(OrganizerEventAdapter);

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

        // Create a new Event object
        Event newEvent = new Event(
                eventName,
                R.drawable.sample_event_image, // Placeholder for image resource
                classDay,
                time,
                period,
                regDueDate,
                regOpenDate,
                price,
                eventLocation,
                maxPeople,
                eventDifficultyInput.getText().toString()
        );

        // Add the new event to the list
        eventList.add(newEvent);
        eventAdapter.notifyDataSetChanged();


        // Clear input fields
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

        launchOrganizerActivity();


    }

    private void launchOrganizerActivity() {
        Intent intent = new Intent(AddEvent.this, OrganizerActivity.class);
        intent.putExtra("EVENT_LIST", eventList); // Pass the event list to EventListActivity
        startActivity(intent);
    }


}
