package com.example.vortex_app.view.organizer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrgNotificationDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String notificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_notification_details);

        notificationId = getIntent().getStringExtra("notificationId");

        if (notificationId == null) {
            Toast.makeText(this, "Notification ID is missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();

        TextView textDate = findViewById(R.id.text_notification_date);
        TextView textTitle = findViewById(R.id.text_notification_title);
        TextView textDescription = findViewById(R.id.text_notification_description);
        Button buttonBack = findViewById(R.id.button_back);
        Button buttonDelete = findViewById(R.id.button_delete);

        textDate.setText(getIntent().getStringExtra("DATE"));
        textTitle.setText(getIntent().getStringExtra("TITLE"));
        textDescription.setText(getIntent().getStringExtra("MESSAGE"));

        buttonBack.setOnClickListener(view -> finish());

        buttonDelete.setOnClickListener(view -> deleteNotification());
    }

    private void deleteNotification() {
        if (notificationId == null) {
            Toast.makeText(this, "Cannot delete notification: ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("notifications").document(notificationId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Notification deleted successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete notification!", Toast.LENGTH_SHORT).show();
                });
        finish();
    }
}