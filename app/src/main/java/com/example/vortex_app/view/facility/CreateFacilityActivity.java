package com.example.vortex_app.view.facility;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateFacilityActivity extends AppCompatActivity {

    private static final String TAG = "CreateFacilityActivity";

    private FirebaseFirestore db;
    private String organizerID;

    private EditText editTextFacilityName;
    private EditText editTextFacilityAddress;
    private Button buttonSaveFacility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_facility);

        db = FirebaseFirestore.getInstance();
        organizerID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        editTextFacilityName = findViewById(R.id.editTextFacilityName);
        editTextFacilityAddress = findViewById(R.id.editTextFacilityAddress);
        buttonSaveFacility = findViewById(R.id.buttonSaveFacility);

        buttonSaveFacility.setOnClickListener(v -> {
            String facilityName = editTextFacilityName.getText().toString().trim();
            String facilityAddress = editTextFacilityAddress.getText().toString().trim();

            if (facilityName.isEmpty() || facilityAddress.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                saveFacilityToFirebase(facilityName, facilityAddress);
            }
        });
    }

    private void saveFacilityToFirebase(String facilityName, String facilityAddress) {
        Map<String, Object> facilityData = new HashMap<>();
        facilityData.put("facilityName", facilityName);
        facilityData.put("address", facilityAddress);
        facilityData.put("organizerId", organizerID); // Include organizerID in the document

        db.collection("facility")
                .add(facilityData)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Facility created with ID: " + documentReference.getId());
                    Toast.makeText(this, "Facility created successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish(); // Close the activity after saving
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating facility", e);
                    Toast.makeText(this, "Failed to create facility: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
