package com.example.vortex_app.view.facility;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditFacilityActivity extends AppCompatActivity {

    private static final String TAG = "EditFacilityActivity";

    private FirebaseFirestore db;

    private EditText editTextFacilityName;
    private EditText editTextFacilityAddress;
    private Button buttonSaveChanges;

    private String facilityId; // ID of the facility to edit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_facility);

        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        editTextFacilityName = findViewById(R.id.editTextFacilityName);
        editTextFacilityAddress = findViewById(R.id.editTextFacilityAddress);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);

        // Retrieve facility details from the intent
        facilityId = getIntent().getStringExtra("FACILITY_ID");
        String facilityName = getIntent().getStringExtra("FACILITY_NAME");
        String facilityAddress = getIntent().getStringExtra("FACILITY_ADDRESS");

        // Set the existing facility details in the fields
        editTextFacilityName.setText(facilityName);
        editTextFacilityAddress.setText(facilityAddress);

        // Handle Save Changes button
        buttonSaveChanges.setOnClickListener(v -> {
            String updatedName = editTextFacilityName.getText().toString().trim();
            String updatedAddress = editTextFacilityAddress.getText().toString().trim();

            if (updatedName.isEmpty() || updatedAddress.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                saveChangesToFirebase(updatedName, updatedAddress);
            }
        });
    }

    private void saveChangesToFirebase(String updatedName, String updatedAddress) {
        db.collection("facility")
                .document(facilityId)
                .update("facilityName", updatedName, "address", updatedAddress)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Facility updated successfully");
                    Toast.makeText(this, "Facility updated successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after saving
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating facility", e);
                    Toast.makeText(this, "Failed to update facility: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
