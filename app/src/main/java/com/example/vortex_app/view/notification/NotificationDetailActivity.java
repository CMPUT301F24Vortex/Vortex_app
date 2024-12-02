package com.example.vortex_app.view.notification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.controller.NotificationActionReceiver;

/**
 * {@code NotificationDetailActivity} displays detailed information about a specific notification.
 * It retrieves the notification's title, message, date, and other relevant data passed through an intent
 * and displays them within the activity's layout.
 *
 * <p>This activity also includes a back button in the action bar to allow users to return to the previous screen.
 * Additionally, it displays action buttons (e.g., Accept, Decline, Stay, Leave) based on the notification type.
 */
public class NotificationDetailActivity extends AppCompatActivity {

    // Constants for logging
    private static final String TAG = "NotificationDetailActivity";

    // Notification data
    private String title;
    private String message;
    private String userID;
    private String eventID;
    private String notificationType;
    private String Date;

    // UI Components
    private TextView titleTextView, messageTextView, dateTextView;
    private Button buttonAction1, buttonAction2;
    private View actionButtonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        // Enable back button in the action bar
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        titleTextView = findViewById(R.id.titleTextView);
        messageTextView = findViewById(R.id.messageTextView);
        dateTextView = findViewById(R.id.dateTextView);
        buttonAction1 = findViewById(R.id.detailButtonPrimary);
        buttonAction2 = findViewById(R.id.detailButtonSecondary);
        actionButtonsLayout = findViewById(R.id.actionButtonsLayout);

        // Retrieve data from the intent and assign to class variables
        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            message = intent.getStringExtra("message");
            Date = intent.getStringExtra("date"); // Optional: Handle if not provided
            userID = intent.getStringExtra("userID");
            eventID = intent.getStringExtra("eventID");
            notificationType = intent.getStringExtra("notificationType"); // Ensure this key matches sender

            // Debugging: Log the received data
            Log.d(TAG, "Title: " + title);
            Log.d(TAG, "Message: " + message);
            Log.d(TAG, "UserID: " + userID);
            Log.d(TAG, "EventID: " + eventID);
            Log.d(TAG, "NotificationType: " + notificationType);

            // Optional: Show a Toast for debugging
            Toast.makeText(this, "Type: " + notificationType, Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "No intent received.");
            Toast.makeText(this, "No notification data received.", Toast.LENGTH_SHORT).show();
        }

        // Set data to TextViews
        titleTextView.setText(title != null ? title : "No Title");
        messageTextView.setText(message != null ? message : "No Message");

        if (intent != null && intent.hasExtra("date")) {
            dateTextView.setText(intent.getStringExtra("date"));
        } else {
            // Set to current date or a default value if date is not provided
            dateTextView.setText(Date);
        }

        // Configure action buttons based on notification type
        configureActionButtons();
    }

    /**
     * Configures the action buttons based on the notification type.
     * Shows the buttons and sets their text and click listeners accordingly.
     */
    private void configureActionButtons() {
        if ("selected".equalsIgnoreCase(notificationType)) {
            // Show Accept and Decline buttons
            actionButtonsLayout.setVisibility(View.VISIBLE);
            buttonAction1.setText("Accept");
            buttonAction2.setText("Decline");

            buttonAction1.setOnClickListener(v -> sendActionBroadcast(NotificationActionReceiver.ACTION_ACCEPT, userID, eventID));
            buttonAction2.setOnClickListener(v -> sendActionBroadcast(NotificationActionReceiver.ACTION_DECLINE, userID, eventID));

            Log.d(TAG, "Configured buttons for 'selected' type.");
            Toast.makeText(this, "Configured buttons for 'selected' type.", Toast.LENGTH_SHORT).show();
        } else if ("lost".equalsIgnoreCase(notificationType)) {
            // Show Stay and Leave buttons
            actionButtonsLayout.setVisibility(View.VISIBLE);
            buttonAction1.setText("Stay");
            buttonAction2.setText("Leave");

            buttonAction1.setOnClickListener(v -> sendActionBroadcast(NotificationActionReceiver.ACTION_STAY, userID, eventID));
            buttonAction2.setOnClickListener(v -> sendActionBroadcast(NotificationActionReceiver.ACTION_LEAVE, userID, eventID));

            Log.d(TAG, "Configured buttons for 'lost' type.");
            Toast.makeText(this, "Configured buttons for 'lost' type.", Toast.LENGTH_SHORT).show();
        } else {
            // Hide buttons if notification type is unknown or not provided
            actionButtonsLayout.setVisibility(View.GONE);
            Log.d(TAG, "Unknown notification type. Buttons hidden.");
            Toast.makeText(this, "Unknown notification type. Buttons hidden.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sends a broadcast intent to NotificationActionReceiver with the specified action.
     *
     * @param action   The action string to be handled by the receiver.
     * @param userID   The ID of the user.
     * @param eventID  The ID of the event.
     */
    private void sendActionBroadcast(String action, String userID, String eventID) {
        Intent intent = new Intent(this, NotificationActionReceiver.class);
        intent.setAction(action);
        intent.putExtra("userID", userID);
        intent.putExtra("eventID", eventID);
        sendBroadcast(intent);

        // Provide user feedback
        Toast.makeText(this, action.replace("ACTION_", "") + " clicked.", Toast.LENGTH_SHORT).show();

        // Optionally, finish the activity or navigate elsewhere
        finish();
    }

    /**
     * Handles the back button functionality in the action bar.
     * When the back button is pressed, it calls {@link #onBackPressed()} to return to the previous screen.
     *
     * @return {@code true} if the back action was handled.
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Handle back button in action bar
        return true;
    }
}
