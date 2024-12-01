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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.vortex_app.R;
import com.example.vortex_app.view.organizer.OrganizerInfo;
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

    private FirebaseFirestore db;
    private StorageReference storageReference;
    private EditText eventNameInput, eventTimeInput, eventStartPeriodInput, eventEndPeriodInput,
            eventRegDueDateInput, eventRegOpenDateInput, eventPriceInput, eventMaxPeopleInput, eventLimitInput, eventClassDayInput;
    private ImageView imageUploadBox;
    private Button addButton, uploadButton;
    private Uri imageUri = null;
    private String eventID = null;
    private String existingImage;

    // Multi-choice dialog variables
    private String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private boolean[] selectedDays = new boolean[daysOfWeek.length];
    private List<String> selectedDayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        initializeUI();

        // 检查是否是编辑已有事件
        eventID = getIntent().getStringExtra("EVENT_ID");
        if (eventID != null) {
            loadEventDetails(eventID);
        }

        // 设置按钮监听器
        uploadButton.setOnClickListener(v -> openImageChooser());
        addButton.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadImageToFirebase();
            } else {
                addOrUpdateEvent(existingImage);
            }
        });

        // 设置日期和时间选择器
        eventRegDueDateInput.setOnClickListener(v -> showDatePickerDialog(eventRegDueDateInput));
        eventRegOpenDateInput.setOnClickListener(v -> showDatePickerDialog(eventRegOpenDateInput));
        eventStartPeriodInput.setOnClickListener(v -> showDatePickerDialog(eventStartPeriodInput));
        eventEndPeriodInput.setOnClickListener(v -> showDatePickerDialog(eventEndPeriodInput));
        eventTimeInput.setOnClickListener(v -> showTimePickerDialog());

        // 多选对话框用于选择 Class Day
        eventClassDayInput.setOnClickListener(v -> showMultiChoiceDialog());
    }

    private void initializeUI() {
        eventNameInput = findViewById(R.id.event_name_input);
        eventClassDayInput = findViewById(R.id.event_class_day_input); // 对应 XML 中的 Spinner
        eventTimeInput = findViewById(R.id.event_time_input);
        eventStartPeriodInput = findViewById(R.id.event_start_period_input);
        eventEndPeriodInput = findViewById(R.id.event_end_period_input);
        eventRegDueDateInput = findViewById(R.id.event_reg_due_date_input);
        eventRegOpenDateInput = findViewById(R.id.event_reg_open_date_input);
        eventPriceInput = findViewById(R.id.event_price_input);
        eventMaxPeopleInput = findViewById(R.id.event_max_people_input);
        eventLimitInput = findViewById(R.id.event_waitlist_limit_input);
        imageUploadBox = findViewById(R.id.image_upload_box);
        addButton = findViewById(R.id.add_event_button);
        uploadButton = findViewById(R.id.upload_button);
    }

    private void loadEventDetails(String eventID) {
        db.collection("events").document(eventID).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        eventNameInput.setText(document.getString("eventName"));
                        eventTimeInput.setText(document.getString("time"));
                        eventStartPeriodInput.setText(document.getString("startPeriod"));
                        eventEndPeriodInput.setText(document.getString("endPeriod"));
                        eventRegDueDateInput.setText(document.getString("regDueDate"));
                        eventRegOpenDateInput.setText(document.getString("regOpenDate"));
                        eventPriceInput.setText(document.getString("price"));
                        eventMaxPeopleInput.setText(document.getString("maxPeople"));
                        eventLimitInput.setText(document.getString("waitlistLimit"));

                        // 加载选中的 Class Days
                        List<String> classDays = (List<String>) document.get("classDays");
                        if (classDays != null) {
                            selectedDayList.clear();
                            selectedDayList.addAll(classDays);
                            for (int i = 0; i < daysOfWeek.length; i++) {
                                selectedDays[i] = selectedDayList.contains(daysOfWeek[i]);
                            }
                            eventClassDayInput.setText(String.join(", ", selectedDayList));
                        }

                        String imageUrl = document.getString("imageUrl");
                        if (imageUrl != null) {
                            Glide.with(this).load(imageUrl).into(imageUploadBox);
                            existingImage = imageUrl;
                        }
                    }
                });
    }

    private void showMultiChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Class Days");
        builder.setMultiChoiceItems(daysOfWeek, selectedDays, (dialog, which, isChecked) -> {
            if (isChecked) {
                if (!selectedDayList.contains(daysOfWeek[which])) {
                    selectedDayList.add(daysOfWeek[which]);
                }
            } else {
                selectedDayList.remove(daysOfWeek[which]);
            }
        });
        builder.setPositiveButton("OK", (dialog, which) -> eventClassDayInput.setText(String.join(", ", selectedDayList)));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showDatePickerDialog(EditText dateInput) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            String formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.getTime());
            dateInput.setText(formattedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String period = hourOfDay >= 12 ? "PM" : "AM";
            if (hourOfDay > 12) hourOfDay -= 12;
            else if (hourOfDay == 0) hourOfDay = 12;
            eventTimeInput.setText(String.format(Locale.US, "%02d:%02d %s", hourOfDay, minute, period));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
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
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> addOrUpdateEvent(uri.toString())))
                    .addOnFailureListener(e -> Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show());
        }
    }

    private void addOrUpdateEvent(String imageUrl) {
        String eventName = eventNameInput.getText().toString();
        String time = eventTimeInput.getText().toString();
        String startPeriod = eventStartPeriodInput.getText().toString();
        String endPeriod = eventEndPeriodInput.getText().toString();
        String regDueDate = eventRegDueDateInput.getText().toString();
        String regOpenDate = eventRegOpenDateInput.getText().toString();
        String price = eventPriceInput.getText().toString();
        String maxPeople = eventMaxPeopleInput.getText().toString();
        String eventLimit = eventLimitInput.getText().toString();
        String organizerID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Map<String, Object> event = new HashMap<>();
        event.put("eventName", eventName);
        event.put("classDays", selectedDayList);
        event.put("time", time);
        event.put("startPeriod", startPeriod);
        event.put("endPeriod", endPeriod);
        event.put("regDueDate", regDueDate);
        event.put("regOpenDate", regOpenDate);
        event.put("price", price);
        event.put("maxPeople", maxPeople);
        event.put("waitlistLimit", eventLimit);
        event.put("organizerId", organizerID);
        event.put("imageUrl", imageUrl);

        if (eventID != null) {
            db.collection("events").document(eventID).set(event)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show();
                        navigateBackToOrganizerInfo(eventID, eventName);
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to update event", Toast.LENGTH_SHORT).show());
        } else {
            db.collection("events").add(event)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();
                        navigateBackToOrganizerInfo(documentReference.getId(), eventName);
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to add event", Toast.LENGTH_SHORT).show());
        }
    }

    private void navigateBackToOrganizerInfo(String eventID, String eventName) {
        Intent intent = new Intent(this, OrganizerInfo.class);
        intent.putExtra("EVENT_ID", eventID);
        intent.putExtra("EVENT_NAME", eventName);
        startActivity(intent);
        finish();
    }
}
