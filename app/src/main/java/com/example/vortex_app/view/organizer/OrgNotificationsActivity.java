package com.example.vortex_app.view.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.controller.adapter.OrgNotificationAdapter;
import com.example.vortex_app.model.OrgNotification;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OrgNotificationsActivity is responsible for displaying a list of notifications related to a specific event.
 * This activity allows the organizer to view the details of existing notifications and create new ones.
 */
public class OrgNotificationsActivity extends AppCompatActivity {

    private ListView listView;
    private OrgNotificationAdapter notificationAdapter;
    private List<OrgNotification> notificationList = new ArrayList<>();
    private String eventId; // Used to store the Event ID passed from the previous page

    /**
     * Called when the activity is first created. Initializes the UI components and fetches
     * the list of notifications related to the specific event from Firestore.
     *
     * @param savedInstanceState a Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_notifications);

        // Retrieve Event ID from the Intent
        eventId = getIntent().getStringExtra("EVENT_ID");

        // If Event ID is missing, show an error message and close the activity
        if (eventId == null) {
            Toast.makeText(this, "Event ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ListView to display notifications
        listView = findViewById(R.id.list_notifications);

        // Set an item click listener for the ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            OrgNotification notification = notificationList.get(position);
            Intent intent = new Intent(OrgNotificationsActivity.this, OrgNotificationDetailsActivity.class);
            intent.putExtra("DATE", notification.getDate());
            intent.putExtra("TITLE", notification.getTitle());
            intent.putExtra("MESSAGE", notification.getMessage());
            intent.putExtra("notificationId", notification.getNotificationId());

            // Start OrgNotificationDetailsActivity to show notification details
            startActivity(intent);
        });

        // Set up the adapter for the ListView
        notificationAdapter = new OrgNotificationAdapter(this, notificationList);
        listView.setAdapter(notificationAdapter);

        // Set up the button to create a new notification
        Button buttonCreate = findViewById(R.id.button_create_notification);
        buttonCreate.setOnClickListener(v -> {
            Intent intent = new Intent(OrgNotificationsActivity.this, OrgCreateNotificationActivity.class);
            intent.putExtra("EVENT_ID", eventId); // Pass the Event ID to the notification creation activity
            startActivity(intent);
        });
    }

    /**
     * Called when the activity is resumed. Reloads the list of notifications from Firestore.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadNotifications();
    }

    /**
     * Fetches the list of notifications related to the event from Firestore.
     * The notifications are loaded based on the event ID, and displayed in the ListView.
     */
    private void loadNotifications() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Fetch notifications from Firestore where the event ID matches
        db.collection("notifications")
                .whereEqualTo("eventId", eventId) // Filter by eventId
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notificationList.clear();

                        // Process each document retrieved from Firestore
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String message = document.getString("message");
                            String notificationID = document.getString("notificationId");

                            Date date = new Date();
                            try {
                                Timestamp timestamp = document.getTimestamp("timestamp");
                                if (timestamp != null) {
                                    date = timestamp.toDate(); // Convert Timestamp to Date
                                }
                            } catch (Exception e) {
                                Log.e("ERROR", "Date parsing error: " + e.getMessage());
                            }

                            // Create a new OrgNotification object and add it to the list
                            OrgNotification notification = new OrgNotification(title, message, date, notificationID);
                            notificationList.add(notification);
                        }

                        // Notify the adapter that the data has changed
                        notificationAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load notifications", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
