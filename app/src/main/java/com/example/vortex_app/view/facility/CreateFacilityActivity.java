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

/**
 * CreateFacilityActivity allows the user to create a new facility by entering its name and address.
 * This activity collects the information and saves it to Firestore under the "facility" collection.
 * It requires the user to provide both the facility name and address before proceeding.
 * The activity also includes the organizer's ID (retrieved from the device) in the saved data.
 */
public class CreateFacilityActivity extends AppCompatActivity {

    private static final String TAG = "CreateFacilityActivity";

    private FirebaseFirestore db;
    private String organizerID;

    private EditText editTextFacilityName;
    private EditText editTextFacilityAddress;
    private Button buttonSaveFacility;

    /**
     * Called when the activity is created.
     * This method initializes the Firebase Firestore instance, retrieves the device's organizer ID,
     * and sets up the user interface components, including the edit texts and save button.
     * It also defines the action for the save button, which validates the input and saves the facility to Firestore.
     *
     * @param savedInstanceState A Bundle containing any saved instance state from a previous activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_facility);

        // Initialize Firestore and retrieve organizer ID
        db = FirebaseFirestore.getInstance();
        organizerID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Set up UI components
        editTextFacilityName = findViewById(R.id.editTextFacilityName);
        editTextFacilityAddress = findViewById(R.id.editTextFacilityAddress);
        buttonSaveFacility = findViewById(R.id.buttonSaveFacility);

        // Set up the save button click listener
        buttonSaveFacility.setOnClickListener(v -> {
            String facilityName = editTextFacilityName.getText().toString().trim();
            String facilityAddress = editTextFacilityAddress.getText().toString().trim();

            // Validate input fields
            if (facilityName.isEmpty() || facilityAddress.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Save the facility data to Firestore if inputs are valid
                saveFacilityToFirebase(facilityName, facilityAddress);
            }
        });
    }

    /**
     * Saves the facility data (name, address, and organizer ID) to Firestore in the "facility" collection.
     * If the facility is successfully saved, a success message is shown and the activity is closed.
     * If an error occurs during the save process, an error message is shown.
     *
     * @param facilityName  The name of the facility to be saved.
     * @param facilityAddress The address of the facility to be saved.
     */
    private void saveFacilityToFirebase(String facilityName, String facilityAddress) {
        // Prepare the facility data to be stored in Firestore
        Map<String, Object> facilityData = new HashMap<>();
        facilityData.put("facilityName", facilityName);
        facilityData.put("address", facilityAddress);
        facilityData.put("organizerId", organizerID); // Include organizer ID in the document

        // Save the facility data to Firestore
        db.collection("facility")
                .add(facilityData)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Facility created with ID: " + documentReference.getId());
                    Toast.makeText(this, "Facility created successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Set result to notify the calling activity
                    finish(); // Close the activity after saving
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating facility", e);
                    Toast.makeText(this, "Failed to create facility: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

