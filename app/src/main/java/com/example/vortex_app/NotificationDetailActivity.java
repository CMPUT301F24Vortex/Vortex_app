package com.example.vortex_app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * {@code NotificationDetailActivity} displays detailed information about a specific notification.
 * It retrieves the notification's title, message, and other relevant data passed through an intent
 * and displays it within the activity's layout.
 *
 * <p>This activity also includes a back button in the action bar to allow users to return to the previous screen.
 */
public class NotificationDetailActivity extends AppCompatActivity {

    /**
     * Initializes the activity and sets up the view to display notification details.
     * Retrieves the title and message data from the intent and assigns them to the appropriate TextViews.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        // Enable back button in the action bar
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Retrieve data from the intent
        String title = getIntent().getStringExtra("title");
        String message = getIntent().getStringExtra("message");

        // Set data to TextViews
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView messageTextView = findViewById(R.id.messageTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);

        titleTextView.setText(title);
        messageTextView.setText(message);
        dateTextView.setText("2025-01-01"); // Example date, replace with actual date if available
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
