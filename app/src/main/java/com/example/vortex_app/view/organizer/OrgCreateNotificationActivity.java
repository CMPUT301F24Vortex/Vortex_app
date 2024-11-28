package com.example.vortex_app.view.organizer;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;

public class OrgCreateNotificationActivity extends AppCompatActivity {

    private EditText notificationContent;
    private Spinner notificationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);

        notificationContent = findViewById(R.id.edit_text_notification_content);
        notificationType = findViewById(R.id.spinner_notification_type);
        Button backButton = findViewById(R.id.button_back);
        Button sendButton = findViewById(R.id.button_send);

        String[] types = {"Final", "Cancelled", "Waitlist", "Chosen"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notificationType.setAdapter(adapter);

        backButton.setOnClickListener(view -> finish());

        sendButton.setOnClickListener(view -> sendNotification());
    }

    private void sendNotification() {
        String content = notificationContent.getText().toString().trim();
        String type = notificationType.getSelectedItem().toString();

        if (content.isEmpty()) {
            Toast.makeText(this, "Notification content cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // sending notification
        Toast.makeText(this, "Notification sent to " + type, Toast.LENGTH_SHORT).show();
    }
}