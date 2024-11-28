package com.example.vortex_app.view.organizer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrgNotificationDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_notification_details);

        String date = getIntent().getStringExtra("DATE");
        String title = getIntent().getStringExtra("TITLE");
        String description = getIntent().getStringExtra("MESSAGE");

        System.out.println(date);
        System.out.println(title);
        System.out.println(description);

        TextView textDate = findViewById(R.id.text_notification_date);
        TextView textTitle = findViewById(R.id.text_notification_title);
        TextView textDescription = findViewById(R.id.text_notification_description);
        Button buttonBack = findViewById(R.id.button_back);
        Button buttonDelete = findViewById(R.id.button_delete);

        textDate.setText(date);
        textTitle.setText(title);
        textDescription.setText(description);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // deklete operation
                Toast.makeText(OrgNotificationDetailsActivity.this, "Notification deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}