package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@code NotificationsActivity} displays a list of notifications for the user.
 * This activity retrieves notification data from Firestore, organizes it in a {@link RecyclerView},
 * and supports user interactions for marking notifications as read and navigating to detailed views.
 *
 * <p>This activity is accessible via the app's bottom navigation bar, enabling users to view
 * and manage notifications related to events or updates within the application.
 */
public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationModel> notificationList = new ArrayList<>();
    private FirebaseFirestore db;

    /**
     * Called when the activity is first created.
     * Sets up the bottom navigation, initializes the RecyclerView for displaying notifications,
     * and loads notifications from Firestore.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_events);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, EntrantActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_notifications) {
                return true;
            } else if (itemId == R.id.nav_events) {
                startActivity(new Intent(this, NotificationsActivity.class));
            }
            return false;
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(notificationList, this);
        recyclerView.setAdapter(adapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        fetchNotifications();


    }


    /**
         * Fetches notifications from Firestore and updates the {@link RecyclerView} with the retrieved data.
         * This method orders notifications by timestamp in descending order and refreshes the UI upon data retrieval.
         */
        private void fetchNotifications () {
            db.collection("notifications")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            notificationList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String message = document.getString("message");
                                String status = document.getString("status");
                                Date Date = document.getDate("timestamp");
                                String eventID = document.getString("eventID");
                                String id = document.getId();


                                NotificationModel notification = new NotificationModel(title, message, status, id, Date);
                                notificationList.add(notification);

                            }


                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w("Firestore", "Error getting notifications.", task.getException());
                        }
                    });
        }
    }

