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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_notifications);

        listView = findViewById(R.id.list_notifications);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            OrgNotification notification = notificationList.get(position);
            Intent intent = new Intent(OrgNotificationsActivity.this, OrgNotificationDetailsActivity.class);
            System.out.println("test");
            System.out.println(notification.getDate());
            System.out.println(notification.getTitle());
            System.out.println(notification.getMessage());
            intent.putExtra("DATE", notification.getDate());
            intent.putExtra("TITLE", notification.getTitle());
            intent.putExtra("MESSAGE", notification.getMessage());
            startActivity(intent);
        });
        notificationAdapter = new OrgNotificationAdapter(this, notificationList);
        listView.setAdapter(notificationAdapter);
        Button buttonCreate = findViewById(R.id.button_create_notification);

        buttonCreate.setOnClickListener(v -> {
            Intent intent = new Intent(OrgNotificationsActivity.this, OrgCreateNotificationActivity.class);
            startActivity(intent);
        });

        loadNotifications();
    }

    private void loadNotifications() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("notifications")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notificationList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String message = document.getString("message");

                            Date date = new Date();
                            try {
                                Timestamp timestamp = document.getTimestamp("date");
                                if (timestamp != null) {
                                    date = timestamp.toDate();
                                }
                            } catch (Exception e) {
                                Log.e("ERROR", "Date parsing error: " + e.getMessage());
                            }

                            OrgNotification notification = new OrgNotification(title, message, date);
                            System.out.println("notification");
                            System.out.println(notification);
                            notificationList.add(notification);
                        }

                        notificationAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load notifications", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}