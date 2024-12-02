package com.example.vortex_app.view.facility;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.controller.adapter.FacilityAdapter;
import com.example.vortex_app.model.Facility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * MyFacilityActivity displays a list of facilities created by the current organizer.
 * It allows the user to view and edit the details of each facility and create new facilities.
 * The facilities are loaded from Firestore and displayed in a RecyclerView. A FloatingActionButton
 * is provided for creating new facilities, which leads to the CreateFacilityActivity.
 */
public class MyFacilityActivity extends AppCompatActivity {

    private static final String TAG = "MyFacilityActivity";

    private FirebaseFirestore db;
    private String organizerID;

    private RecyclerView recyclerViewFacilities;
    private FacilityAdapter facilityAdapter;
    private List<Facility> facilityList;
    private FloatingActionButton fabCreateFacility;

    /**
     * Called when the activity is created. This method initializes the Firestore instance,
     * sets up the RecyclerView, and loads the facilities associated with the current organizer.
     * It also defines the FloatingActionButton for creating new facilities.
     *
     * @param savedInstanceState A Bundle containing the saved instance state from a previous activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_facility);

        db = FirebaseFirestore.getInstance();
        organizerID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        recyclerViewFacilities = findViewById(R.id.recyclerViewFacilities);
        fabCreateFacility = findViewById(R.id.fabCreateFacility);

        // Initialize RecyclerView and adapter
        facilityList = new ArrayList<>();
        facilityAdapter = new FacilityAdapter(facilityList, facility -> {
            // Handle item click to edit the facility
            Intent intent = new Intent(MyFacilityActivity.this, EditFacilityActivity.class);
            intent.putExtra("FACILITY_ID", facility.getId());
            intent.putExtra("FACILITY_NAME", facility.getFacilityName());
            intent.putExtra("FACILITY_ADDRESS", facility.getAddress());
            startActivity(intent);
        });
        recyclerViewFacilities.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFacilities.setAdapter(facilityAdapter);

        // Load the facility details from Firestore
        loadFacilityDetails();

        // Floating button to create a new facility
        fabCreateFacility.setOnClickListener(v -> {
            Intent intent = new Intent(MyFacilityActivity.this, CreateFacilityActivity.class);
            startActivityForResult(intent, 100);
        });
    }

    /**
     * Called when the activity resumes. This method reloads the list of facilities
     * from Firestore when returning to the activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadFacilityDetails(); // Reload the facility list when returning to this activity
    }

    /**
     * Loads the facility details from Firestore for the current organizer.
     * The facilities are filtered by the organizer's ID and displayed in the RecyclerView.
     */
    private void loadFacilityDetails() {
        db.collection("facility")
                .whereEqualTo("organizerId", organizerID) // Filter facilities by organizer ID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        facilityList.clear(); // Clear the previous list of facilities
                        task.getResult().forEach(document -> {
                            String id = document.getId();
                            String facilityName = document.getString("facilityName");
                            String address = document.getString("address");
                            Log.d(TAG, "Facility found: " + facilityName + ", " + address);
                            facilityList.add(new Facility(id, facilityName, address));
                        });
                        facilityAdapter.notifyDataSetChanged(); // Notify adapter of data change
                        Log.d(TAG, "Facilities loaded for organizer: " + facilityList.size());
                    } else {
                        Log.e(TAG, "Error fetching facilities", task.getException());
                    }
                });
    }

    /**
     * Called when a result is returned from an activity. In this case, it reloads the
     * list of facilities if a new facility was created.
     *
     * @param requestCode  The request code passed in startActivityForResult().
     * @param resultCode   The result code returned by the child activity.
     * @param data         The Intent returned by the child activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadFacilityDetails(); // Reload facilities after creating a new one
        }
    }
}
