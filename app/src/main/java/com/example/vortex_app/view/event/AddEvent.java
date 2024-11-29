package com.example.vortex_app.view.event;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vortex_app.R;
import com.example.vortex_app.view.organizer.OrganizerActivity;
import com.example.vortex_app.view.organizer.OrganizerMenu;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddEvent extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private Spinner eventClassDayInput, geoLocationSpinner, facilitySpinner;
    private EditText eventNameInput, eventLocationInput, eventTimeInput, eventStartPeriodInput, eventEndPeriodInput,
            eventRegDueDateInput, eventRegOpenDateInput, eventPriceInput, eventMaxPeopleInput, eventLimitInput;
    private Button addButton, uploadButton;
    private Uri imageUri = null;
    private ImageView imageUploadBox;
    private String eventID = null;
    private String existingImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventNameInput = findViewById(R.id.event_name_input);
        eventLocationInput = findViewById(R.id.event_location_input);
        eventClassDayInput = findViewById(R.id.event_class_day_input);
        eventTimeInput = findViewById(R.id.event_time_input);
        eventStartPeriodInput = findViewById(R.id.event_start_period_input);
        eventEndPeriodInput = findViewById(R.id.event_end_period_input);
        eventRegDueDateInput = findViewById(R.id.event_reg_due_date_input);
        eventRegOpenDateInput = findViewById(R.id.event_reg_open_date_input);
        eventPriceInput = findViewById(R.id.event_price_input);
        eventMaxPeopleInput = findViewById(R.id.event_max_people_input);
        eventLimitInput = findViewById(R.id.event_waitlist_limit_input);
        geoLocationSpinner = findViewById(R.id.spinnerGeoLocation);
        facilitySpinner = findViewById(R.id.facility_spinner); // Facility spinner
        addButton = findViewById(R.id.add_event_button);
        uploadButton = findViewById(R.id.upload_button);

        imageUploadBox = findViewById(R.id.image_upload_box);

        // Load facilities into spinner
        loadFacilitiesIntoSpinner(facilitySpinner);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("EVENT_ID");

        if (eventID != null) {
            loadEventDetails(eventID); // Load event data if editing
        }

        uploadButton.setOnClickListener(v -> openImageChooser());

        addButton.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadImageToFirebase();
            } else {
                String selectedFacility = facilitySpinner.getSelectedItem().toString();
                addEvent(existingImage, selectedFacility);
            }
        });

        // Click listeners for date and time pickers
        eventRegDueDateInput.setOnClickListener(v -> showDatePickerDialog(eventRegDueDateInput));
        eventRegOpenDateInput.setOnClickListener(v -> showDatePickerDialog(eventRegOpenDateInput));
        eventStartPeriodInput.setOnClickListener(v -> showDatePickerDialog(eventStartPeriodInput));
        eventEndPeriodInput.setOnClickListener(v -> showDatePickerDialog(eventEndPeriodInput));
        eventTimeInput.setOnClickListener(v -> showTimePickerDialog());
    }

    private void loadFacilitiesIntoSpinner(Spinner facilitySpinner) {
        // Use organizer ID to fetch only the facilities created by this user
        String organizerID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        db.collection("facility") // Updated collection name
                .whereEqualTo("organizerId", organizerID) // Fetch facilities specific to this organizer
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<String> facilityNames = new ArrayList<>();
                        for (DocumentSnapshot doc : task.getResult()) {
                            String facilityName = doc.getString("facilityName");
                            if (facilityName != null) {
                                facilityNames.add(facilityName);
                            }
                        }
                        if (facilityNames.isEmpty()) {
                            facilityNames.add("No Facilities Found"); // Default message if no facilities
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, facilityNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        facilitySpinner.setAdapter(adapter);
                    } else {
                        Toast.makeText(this, "Failed to load facilities", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadEventDetails(String eventID) {
        DocumentReference eventRef = db.collection("events").document(eventID);

        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    String eventName = document.getString("eventName");
                    String classDay = document.getString("classDay");
                    String eventLocation = document.getString("eventLocation");
                    String time = document.getString("time");
                    String startPeriod = document.getString("startPeriod");
                    String endPeriod = document.getString("endPeriod");
                    String regDueDate = document.getString("regDueDate");
                    String regOpenDate = document.getString("regOpenDate");
                    String price = document.getString("price");
                    String poster = document.getString("imageUrl");
                    String maxPeople = document.getString("maxPeople");

                    eventNameInput.setText(eventName);
                    eventLocationInput.setText(eventLocation);

                    if (classDay != null) {
                        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) eventClassDayInput.getAdapter();
                        int spinnerPosition = adapter.getPosition(classDay);
                        eventClassDayInput.setSelection(spinnerPosition);
                    }

                    eventTimeInput.setText(time);
                    eventStartPeriodInput.setText(startPeriod);
                    eventEndPeriodInput.setText(endPeriod);
                    eventRegDueDateInput.setText(regDueDate);
                    eventRegOpenDateInput.setText(regOpenDate);
                    eventPriceInput.setText(price);
                    eventMaxPeopleInput.setText(maxPeople);

                    if (poster != null) {
                        Glide.with(this).load(poster).into(imageUploadBox);
                        existingImage = poster;
                    }
                }
            }
        });
    }

    private void showDatePickerDialog(EditText dateEditText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth1) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year1, monthOfYear, dayOfMonth1);
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                    dateEditText.setText(sdf.format(selectedDate.getTime()));
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minuteOfHour) -> {
                    String period = hourOfDay >= 12 ? "PM" : "AM";
                    if (hourOfDay > 12) hourOfDay -= 12;
                    else if (hourOfDay == 0) hourOfDay = 12;

                    String time = String.format(Locale.US, "%02d:%02d %s", hourOfDay, minuteOfHour, period);
                    eventTimeInput.setText(time);
                },
                hour, minute, false);
        timePickerDialog.show();
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            imageUploadBox.setImageURI(imageUri);
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            StorageReference fileRef = storageReference.child("event_images/" + System.currentTimeMillis() + ".jpg");
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        addEvent(imageUrl, facilitySpinner.getSelectedItem().toString());
                    }))
                    .addOnFailureListener(e -> Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show());
        }
    }

    private void addEvent(String imageUrl, String facilityName) {
        String eventName = eventNameInput.getText().toString();
        String eventLocation = eventLocationInput.getText().toString();
        String classDay = eventClassDayInput.getSelectedItem().toString();
        String time = eventTimeInput.getText().toString();
        String startPeriod = eventStartPeriodInput.getText().toString();
        String endPeriod = eventEndPeriodInput.getText().toString();
        String regDueDate = eventRegDueDateInput.getText().toString();
        String regOpenDate = eventRegOpenDateInput.getText().toString();
        String price = eventPriceInput.getText().toString();
        String maxPeople = eventMaxPeopleInput.getText().toString();
        String eventLimit = eventLimitInput.getText().toString();

        Map<String, Object> event = new HashMap<>();
        event.put("eventName", eventName);
        event.put("eventLocation", eventLocation);
        event.put("classDay", classDay);
        event.put("time", time);
        event.put("startPeriod", startPeriod);
        event.put("endPeriod", endPeriod);
        event.put("regDueDate", regDueDate);
        event.put("regOpenDate", regOpenDate);
        event.put("price", price);
        event.put("maxPeople", maxPeople);
        event.put("waitlistLimit", eventLimit);
        event.put("facilityName", facilityName);

        if (imageUrl != null) {
            event.put("imageUrl", imageUrl);
        }

        if (eventID != null) {
            db.collection("events").document(eventID).set(event)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show();
                        navigateToOrganizerMenu(eventID, eventName);
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to update event", Toast.LENGTH_SHORT).show());
        } else {
            db.collection("events").add(event)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();
                        navigateToOrganizerActivity();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to add event", Toast.LENGTH_SHORT).show());
        }
    }

    private void navigateToOrganizerActivity() {
        Intent intent = new Intent(this, OrganizerActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToOrganizerMenu(String eventID, String eventName) {
        Intent intent = new Intent(this, OrganizerMenu.class);
        intent.putExtra("EVENT_ID", eventID);
        intent.putExtra("EVENT_NAME", eventName);
        startActivity(intent);
        finish();
    }



}
