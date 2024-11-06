package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationModel> notificationList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the selected item to Events to highlight it correctly
        bottomNavigationView.setSelectedItemId(R.id.nav_events);

        // Set a new navigation item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Navigate to HomeActivity (EntrantActivity) when the Home icon is clicked
                Intent intent = new Intent(this, EntrantActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Navigate to ProfileActivity when the Profile icon is clicked
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_notifications) {
                // Already in Notifs, do nothing
                return true;
            } else if (itemId == R.id.nav_events) {
                // Navigate to NotificationsActivity when the Notifications icon is clicked
                Intent intent = new Intent(this, NotificationsActivity.class);
                startActivity(intent);
            }
            return false;
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Pass context and click handler to open detail view on click
        adapter = new NotificationAdapter(notificationList, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchNotifications();
    }

    private void fetchNotifications() {
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

    private void handleNotificationClick(NotificationModel notification) {
        // Update status in Firestore
        db.collection("notifications").document(notification.getId())
                .update("status", "read")
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Notification marked as read.");
                    fetchNotifications(); // Refresh the list
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error updating status", e));
    }

    private void openNotificationDetail(NotificationModel notification) {
        // Mark the notification as read and open NotificationDetailActivity
        db.collection("notifications").document(notification.getId())
                .update("status", "read")
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Notification marked as read."))
                .addOnFailureListener(e -> Log.e("Firestore", "Error updating status", e));

        Intent intent = new Intent(this, NotificationDetailActivity.class);
        intent.putExtra("title", notification.getTitle());
        intent.putExtra("message", notification.getMessage());
        intent.putExtra("status", notification.getStatus());
        startActivity(intent);
    }

}
