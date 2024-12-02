package com.example.vortex_app.view.organizer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * OrgNotificationDetailsActivity displays the details of a specific notification.
 * The activity allows the user to view the notification's date, title, and message.
 * Additionally, the user can delete the notification if necessary.
 */
public class OrgNotificationDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String notificationId;

    /**
     * Called when the activity is first created. Initializes the UI components and fetches
     * the notification details (ID, date, title, message) from the intent passed to this activity.
     * It also sets up the click listeners for the back and delete buttons.
     *
     * @param savedInstanceState a Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_notification_details);

        // Retrieve the notification ID from the intent
        notificationId = getIntent().getStringExtra("notificationId");

        // Check if notification ID is missing
        if (notificationId == null) {
            Toast.makeText(this, "Notification ID is missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firestore database instance
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        TextView textDate = findViewById(R.id.text_notification_date);
        TextView textTitle = findViewById(R.id.text_notification_title);
        TextView textDescription = findViewById(R.id.text_notification_description);
        Button buttonBack = findViewById(R.id.button_back);
        Button buttonDelete = findViewById(R.id.button_delete);

        // Set text fields with data from the intent
        textDate.setText(getIntent().getStringExtra("DATE"));
        textTitle.setText(getIntent().getStringExtra("TITLE"));
        textDescription.setText(getIntent().getStringExtra("MESSAGE"));

        // Set up the back button to close the activity
        buttonBack.setOnClickListener(view -> finish());

        // Set up the delete button to delete the notification
        buttonDelete.setOnClickListener(view -> deleteNotification());
    }

    /**
     * Deletes the notification from Firestore based on the notification ID.
     * If the deletion is successful, a success message is shown, and the activity is finished.
     * If the deletion fails, an error message is shown.
     */
    private void deleteNotification() {
        // Check if the notification ID is missing
        if (notificationId == null) {
            Toast.makeText(this, "Cannot delete notification: ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Delete the notification from Firestore
        db.collection("notifications").document(notificationId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Notification deleted successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete notification!", Toast.LENGTH_SHORT).show();
                });

        finish(); // Close the activity after attempting to delete
    }
}
