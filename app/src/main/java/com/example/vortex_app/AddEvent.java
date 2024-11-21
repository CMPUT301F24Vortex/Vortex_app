package com.example.vortex_app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddEvent extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText eventNameInput;
    private EditText eventLocationInput;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        db = FirebaseFirestore.getInstance();

        eventNameInput = findViewById(R.id.event_name_input);
        eventLocationInput = findViewById(R.id.event_location_input);
        addButton = findViewById(R.id.add_event_button);

        addButton.setOnClickListener(v -> addEvent());
    }

    private void addEvent() {
        String eventName = eventNameInput.getText().toString();
        String eventLocation = eventLocationInput.getText().toString();

        if (eventName.isEmpty() || eventLocation.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> event = new HashMap<>();
        event.put("eventName", eventName);
        event.put("eventLocation", eventLocation);

        db.collection("events")
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    String eventID = documentReference.getId();
                    Log.d("AddEvent", "Event added with ID: " + eventID);
                    Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close this activity
                })
                .addOnFailureListener(e -> {
                    Log.e("AddEvent", "Error adding event", e);
                    Toast.makeText(this, "Error adding event", Toast.LENGTH_SHORT).show();
                });
    }
}
