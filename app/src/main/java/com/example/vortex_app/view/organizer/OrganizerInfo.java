package com.example.vortex_app.view.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.vortex_app.R;
import com.example.vortex_app.view.event.AddEvent;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * The OrganizerInfo activity is responsible for displaying detailed information about a specific event.
 * It retrieves event details from Firebase Firestore, including event name, class days, time, price,
 * registration dates, and the associated facility's information such as its name and location.
 * The user can also edit the event or navigate back to the event list.
 */
public class OrganizerInfo extends AppCompatActivity {

    private TextView eventNameTextView;
    private TextView classDayTextView;
    private TextView timeTextView;
    private TextView periodTextView;
    private TextView registrationDueDateTextView;
    private TextView registrationOpenDateTextView;
    private TextView priceTextView;
    private TextView maxPeopleTextView;
    private TextView facilityNameTextView; // To display the facility name
    private ImageView posterImageView;
    private Button editButton, doneButton;

    private TextView locationTextView;

    /**
     * Called when the activity is created.
     * Initializes the UI elements and loads event details from Firebase Firestore.
     * Sets up listeners for the edit and done buttons.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
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
        periodTextView = findViewById(R.id.text_period);
        registrationDueDateTextView = findViewById(R.id.text_registration_due_date);
        registrationOpenDateTextView = findViewById(R.id.text_registration_open_date);
        priceTextView = findViewById(R.id.text_price);
        maxPeopleTextView = findViewById(R.id.text_max_people);
        facilityNameTextView = findViewById(R.id.text_facility_name); // Initialize the facility name text view
        posterImageView = findViewById(R.id.image_event);
        locationTextView = findViewById(R.id.text_location_name);

        loadEventDetails(eventID);

        // Set up listeners for the buttons
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

    /**
     * Loads the details of the event from Firebase Firestore and displays them in the respective UI elements.
     * It retrieves the event name, class days, time, period, registration dates, price, maximum people,
     * facility name, and event poster.
     *
     * @param eventID The ID of the event whose details are to be loaded.
     */
    private void loadEventDetails(String eventID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("events").document(eventID);

        // Fetch the event document
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {

                    String eventName = document.getString("eventName");
                    eventNameTextView.setText(eventName);

                    // Check if classDay is an array or list
                    Object classDayObject = document.get("classDays");
                    if (classDayObject instanceof List) {
                        List<String> classDays = (List<String>) classDayObject;
                        classDayTextView.setText(String.join(", ", classDays)); // Join array items with commas
                    } else {
                        classDayTextView.setText("No class days available");
                    }

                    String eventTime = document.getString("time");
                    timeTextView.setText(eventTime);

                    String startPeriod = document.getString("startPeriod");
                    String endPeriod = document.getString("endPeriod");
                    periodTextView.setText(startPeriod + " ~ " + endPeriod);

                    String regDueDate = document.getString("regDueDate");
                    registrationDueDateTextView.setText(regDueDate);

                    String regOpenDate = document.getString("regOpenDate");
                    registrationOpenDateTextView.setText(regOpenDate);

                    String price = document.getString("price");
                    priceTextView.setText("$" + price);

                    String maxPeople = document.getString("maxPeople");
                    maxPeopleTextView.setText(maxPeople);

                    // Load facility name
                    String facilityName = document.getString("facilityName");
                    if (facilityName != null && !facilityName.isEmpty()) {
                        facilityNameTextView.setText(facilityName);

                        // Fetch location information from the facility collection
                        fetchFacilityLocation(db, facilityName);
                    } else {
                        facilityNameTextView.setText("No Facility Assigned");
                    }

                    // Load poster image
                    String poster = document.getString("imageUrl");
                    if (poster != null && !poster.isEmpty()) {
                        Glide.with(this)
                                .load(poster)
                                .into(posterImageView);
                    }
                }
            } else {
                Log.e("OrganizerInfo", "Error fetching event details", task.getException());
            }
        });
    }

    /**
     * Fetches the location of the facility associated with the event from Firebase Firestore
     * and updates the UI to display the location address.
     *
     * @param db           The FirebaseFirestore instance.
     * @param facilityName The name of the facility whose location is to be fetched.
     */
    private void fetchFacilityLocation(FirebaseFirestore db, String facilityName) {
        db.collection("facility")
                .whereEqualTo("facilityName", facilityName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot facilityDoc = task.getResult().getDocuments().get(0);
                        String locationAddress = facilityDoc.getString("address");

                        if (locationAddress != null) {
                            // Display the location address
                            locationTextView.setText(locationAddress);
                        } else {
                            locationTextView.setText("Location not available");
                        }
                    } else {
                        Log.e("OrganizerInfo", "Error fetching facility location: Facility not found");
                        locationTextView.setText("Location not available");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("OrganizerInfo", "Error fetching facility location", e);
                    locationTextView.setText("Failed to load location");
                });
    }
}
