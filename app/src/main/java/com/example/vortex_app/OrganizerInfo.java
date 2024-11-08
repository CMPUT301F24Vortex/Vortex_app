package com.example.vortex_app;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * {@code OrganizerInfo} is an {@link AppCompatActivity} that displays detailed information about a specific event.
 * It fetches event details from Firestore using the event ID and displays them in a structured format.
 */
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

    /**
     * Called when the activity is created. Sets up the layout, retrieves the event ID from the intent,
     * and initializes the TextViews for displaying event details. Calls {@link #loadEventDetails(String)}
     * to load and display event information from Firestore.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_events_info);

        // Retrieve event ID from intent
        String eventID = getIntent().getStringExtra("EVENT_ID");

        // Initialize TextViews
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

        loadEventDetails(eventID); // Load and display event details
    }

    /**
     * Loads event details from Firestore using the specified event ID and updates the UI with the event information.
     * This includes the event name, schedule, registration dates, location, and other details.
     *
     * @param eventID The ID of the event to retrieve details for.
     */
    private void loadEventDetails(String eventID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("events").document(eventID);

        // Fetch the document
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // Retrieve event details from document
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

                    // Set text for each TextView with event details
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
