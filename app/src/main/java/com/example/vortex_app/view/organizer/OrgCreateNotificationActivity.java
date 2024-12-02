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

/**
 * OrgCreateNotificationActivity allows the event organizer to create and send notifications.
 * The notifications include details such as title, content, and type, and are stored in Firestore.
 */
public class OrgCreateNotificationActivity extends AppCompatActivity {

    private EditText notificationTitle;
    private EditText notificationContent;
    private Spinner notificationType;
    private String eventId; // The event ID to associate with the notification

    /**
     * Called when the activity is first created. Initializes the UI components, sets up the spinner for
     * notification types, and defines actions for the back and send buttons.
     *
     * @param savedInstanceState a Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);

        // Retrieve the event ID passed from the previous activity
        eventId = getIntent().getStringExtra("EVENT_ID");

        // Check if event ID is missing
        if (eventId == null) {
            Toast.makeText(this, "Event ID is missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI elements
        notificationTitle = findViewById(R.id.edit_text_notification_title);
        notificationContent = findViewById(R.id.edit_text_notification_content);
        notificationType = findViewById(R.id.spinner_notification_type);
        Button backButton = findViewById(R.id.button_back);
        Button sendButton = findViewById(R.id.button_send);

        // Set up spinner for selecting notification type
        String[] types = {"Final", "Cancelled", "Waitlist", "Chosen"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notificationType.setAdapter(adapter);

        // Back button functionality
        backButton.setOnClickListener(view -> finish());

        // Send button functionality
        sendButton.setOnClickListener(view -> sendNotification());
    }

    /**
     * Sends a notification by storing the notification data in Firebase Firestore.
     * The notification includes the title, content, type, event ID, and a timestamp.
     * A unique notification ID is generated for each notification.
     */
    private void sendNotification() {
        String title = notificationTitle.getText().toString().trim();
        String content = notificationContent.getText().toString().trim();
        String type = notificationType.getSelectedItem().toString();

        // Validate input fields
        if (title.isEmpty()) {
            Toast.makeText(this, "Notification title cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.isEmpty()) {
            Toast.makeText(this, "Notification content cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique notification ID
        String notificationId = UUID.randomUUID().toString();

        // Create the notification data to be stored in Firestore
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("notificationId", notificationId); // Unique notification ID
        notificationData.put("eventId", eventId); // Event ID to associate with the notification
        notificationData.put("title", title); // Title of the notification
        notificationData.put("message", content); // Content of the notification
        notificationData.put("type", type); // Type of notification (e.g., Final, Cancelled)
        notificationData.put("timestamp", Timestamp.now()); // Timestamp of when the notification was created

        // Store the notification data in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notifications")
                .document(notificationId) // Use the generated notificationId as the document ID
                .set(notificationData)
                .addOnSuccessListener(aVoid -> {
                    // Display success message
                    Toast.makeText(this, "Notification sent successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity and return to the previous screen
                })
                .addOnFailureListener(e -> {
                    // Display error message in case of failure
                    Toast.makeText(this, "Failed to send notification!", Toast.LENGTH_SHORT).show();
                });
    }
}
