package com.example.vortex_app.view.organizer;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID; // import UUID

public class OrgCreateNotificationActivity extends AppCompatActivity {

    private EditText notificationTitle;
    private EditText notificationContent;
    private Spinner notificationType;
    private String eventId; // assign Event ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);

        // get Event ID
        eventId = getIntent().getStringExtra("EVENT_ID");

        if (eventId == null) {
            Toast.makeText(this, "Event ID is missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // initialize view
        notificationTitle = findViewById(R.id.edit_text_notification_title);
        notificationContent = findViewById(R.id.edit_text_notification_content);
        notificationType = findViewById(R.id.spinner_notification_type);
        Button backButton = findViewById(R.id.button_back);
        Button sendButton = findViewById(R.id.button_send);

        // Set notification type of Spinner
        String[] types = {"Final", "Cancelled", "Waitlist", "Chosen"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notificationType.setAdapter(adapter);

        // backbutton
        backButton.setOnClickListener(view -> finish());

        // send notification button
        sendButton.setOnClickListener(view -> sendNotification());
    }

    private void sendNotification() {
        String title = notificationTitle.getText().toString().trim();
        String content = notificationContent.getText().toString().trim();
        String type = notificationType.getSelectedItem().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Notification title cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.isEmpty()) {
            Toast.makeText(this, "Notification content cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // generate unique notificationId
        String notificationId = UUID.randomUUID().toString();

        // create notification data
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("notificationId", notificationId); // add notificationId
        notificationData.put("eventId", eventId); // link Event ID
        notificationData.put("title", title);
        notificationData.put("message", content);
        notificationData.put("type", type);
        notificationData.put("timestamp", Timestamp.now());

        // store data to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notifications")
                .document(notificationId) // Use the generated notificationId as the document ID
                .set(notificationData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Notification sent successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // back to previous screen
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to send notification!", Toast.LENGTH_SHORT).show();
                });
        finish();
    }
}