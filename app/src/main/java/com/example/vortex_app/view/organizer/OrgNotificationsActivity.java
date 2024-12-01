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

public class OrgNotificationsActivity extends AppCompatActivity {
    private ListView listView;
    private OrgNotificationAdapter notificationAdapter;
    private List<OrgNotification> notificationList = new ArrayList<>();
    private String eventId; // Used to store the Event ID passed from the previous page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_notifications);

        // get Event ID from Intent
        eventId = getIntent().getStringExtra("EVENT_ID");

        if (eventId == null) {
            Toast.makeText(this, "Event ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        listView = findViewById(R.id.list_notifications);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            OrgNotification notification = notificationList.get(position);
            Intent intent = new Intent(OrgNotificationsActivity.this, OrgNotificationDetailsActivity.class);
            intent.putExtra("DATE", notification.getDate());
            intent.putExtra("TITLE", notification.getTitle());
            intent.putExtra("MESSAGE", notification.getMessage());
            intent.putExtra("notificationId", notification.getNotificationId());
            System.out.println(notification.getTitle());
            System.out.println(notification.getMessage());
            System.out.println(notification.getDate());

            startActivity(intent);
        });

        notificationAdapter = new OrgNotificationAdapter(this, notificationList);
        listView.setAdapter(notificationAdapter);

        Button buttonCreate = findViewById(R.id.button_create_notification);
        buttonCreate.setOnClickListener(v -> {
            Intent intent = new Intent(OrgNotificationsActivity.this, OrgCreateNotificationActivity.class);
            intent.putExtra("EVENT_ID", eventId); // need to pass the Event ID when creating a notification
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotifications();
    }

    private void loadNotifications() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // search documents in the notifications collection that matches eventId
        db.collection("notifications")
                .whereEqualTo("eventId", eventId) // use eventId as condition for search
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notificationList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            System.out.println(document);
                            String title = document.getString("title");
                            String message = document.getString("message");
                            String notificationID = document.getString("notificationId");

                            Date date = new Date();
                            try {
                                Timestamp timestamp = document.getTimestamp("timestamp");
                                if (timestamp != null) {
                                    date = timestamp.toDate();
                                }
                            } catch (Exception e) {
                                Log.e("ERROR", "Date parsing error: " + e.getMessage());
                            }

                            OrgNotification notification = new OrgNotification(title, message, date, notificationID);
                            notificationList.add(notification);
                        }

                        notificationAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load notifications", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}