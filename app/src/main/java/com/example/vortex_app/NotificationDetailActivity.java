package com.example.vortex_app;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;


public class NotificationDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        // Enable back button in the action bar
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Retrieve data from intent
        String title = getIntent().getStringExtra("title");
        String message = getIntent().getStringExtra("message");

        // Set data to TextViews
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView messageTextView = findViewById(R.id.messageTextView);

        titleTextView.setText(title);
        messageTextView.setText(message);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Handle back button in action bar
        return true;
    }
}
