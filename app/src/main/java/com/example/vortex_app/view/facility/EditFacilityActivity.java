package com.example.vortex_app.view.facility;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * EditFacilityActivity allows the user to edit the details of an existing facility.
 * The user can update the facility's name and address, and the changes are saved to Firestore.
 * The activity retrieves the existing facility details from the Intent, and upon saving,
 * updates the data in Firestore.
 */
public class EditFacilityActivity extends AppCompatActivity {

    private static final String TAG = "EditFacilityActivity";

    private FirebaseFirestore db;

    private EditText editTextFacilityName;
    private EditText editTextFacilityAddress;
    private Button buttonSaveChanges;

    private String facilityId; // ID of the facility to edit

    /**
     * Called when the activity is created.
     * This method initializes the Firebase Firestore instance, retrieves the existing facility details
     * from the Intent, and sets up the UI components. It also defines the action for the "Save Changes" button,
     * which validates the input and updates the facility data in Firestore.
     *
     * @param savedInstanceState A Bundle containing any saved instance state from a previous activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_facility);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Set up UI components
        editTextFacilityName = findViewById(R.id.editTextFacilityName);
        editTextFacilityAddress = findViewById(R.id.editTextFacilityAddress);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);

        // Retrieve the facility details from the Intent
        facilityId = getIntent().getStringExtra("FACILITY_ID");
        String facilityName = getIntent().getStringExtra("FACILITY_NAME");
        String facilityAddress = getIntent().getStringExtra("FACILITY_ADDRESS");

        // Set the existing facility details in the EditText fields
        editTextFacilityName.setText(facilityName);
        editTextFacilityAddress.setText(facilityAddress);

        // Set the listener for the "Save Changes" button
        buttonSaveChanges.setOnClickListener(v -> {
            String updatedName = editTextFacilityName.getText().toString().trim();
            String updatedAddress = editTextFacilityAddress.getText().toString().trim();

            // Validate the input fields
            if (updatedName.isEmpty() || updatedAddress.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Save the changes to Firestore if valid
                saveChangesToFirebase(updatedName, updatedAddress);
            }
        });
    }

    /**
     * Updates the facility's name and address in Firestore.
     * If the update is successful, a success message is shown, and the activity is closed.
     * If the update fails, an error message is displayed to the user.
     *
     * @param updatedName    The updated facility name.
     * @param updatedAddress The updated facility address.
     */
    private void saveChangesToFirebase(String updatedName, String updatedAddress) {
        // Update the facility details in Firestore
        db.collection("facility")
                .document(facilityId)
                .update("facilityName", updatedName, "address", updatedAddress)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Facility updated successfully");
                    Toast.makeText(this, "Facility updated successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after saving the changes
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating facility", e);
                    Toast.makeText(this, "Failed to update facility: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
