package com.example.vortex_app.view.facility;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.model.Facility;
import com.example.vortex_app.controller.adapter.AdminFacilityArrayAdapter;
import com.example.vortex_app.view.event.AdminEventScreen;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


/**
 * AdminFacilityScreen is an Activity class that allows an administrator to manage facilities in the system.
 * It provides functionalities to view, delete, and manage facilities and their associated events.
 * This screen includes:
 * <ul>
 *     <li>A list of facilities fetched from Firestore.</li>
 *     <li>The ability to view detailed events associated with a facility.</li>
 *     <li>The ability to delete a facility and all associated events.</li>
 * </ul>
 * <p>
 * The activity listens for changes to the facility data and updates the list accordingly.
 * </p>
 */
public class AdminFacilityScreen extends AppCompatActivity {

    private static final String TAG = "AdminFacilityScreen";

    private FirebaseFirestore db;
    private CollectionReference facilitiesRef;
    private CollectionReference eventsRef;

    private ImageButton buttonBack;
    private ListView facilityList;
    private ArrayList<Facility> facilityDataList;
    private AdminFacilityArrayAdapter facilityArrayAdapter;


    /**
     * Called when the activity is first created.
     * Initializes Firestore references, UI components, and sets up event listeners.
     * It also listens for changes in the facility data and updates the list dynamically.
     *
     * @param savedInstanceState A Bundle containing any saved instance state from a previous activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_facilityscreen);

        // Initialize Firestore references
        db = FirebaseFirestore.getInstance();
        facilitiesRef = db.collection("facility");
        eventsRef = db.collection("events");

        // Initialize facility data list
        facilityDataList = new ArrayList<>();

        // Set up UI components
        buttonBack = findViewById(R.id.button_back);
        facilityList = findViewById(R.id.listview_facilities);

        // Set up the facility list array adapter
        facilityArrayAdapter = new AdminFacilityArrayAdapter(this, facilityDataList);
        facilityList.setAdapter(facilityArrayAdapter);

        // Dynamically update the facility list from Firestore
        facilitiesRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w(TAG, "Listen failed", error);
                return;
            }

            // Clear the current list and add updated data
            facilityDataList.clear();
            for (QueryDocumentSnapshot doc : value) {
                Facility facility = doc.toObject(Facility.class);
                facility.setId(doc.getId()); // Set the document ID as the facility ID
                facilityDataList.add(facility);
            }
            facilityArrayAdapter.notifyDataSetChanged();
        });

        // Handle short clicks to navigate to AdminEventScreen
        facilityList.setOnItemClickListener((parent, view, position, id) -> {
            Facility selectedFacility = facilityDataList.get(position);
            String facilityName = selectedFacility.getFacilityName();

            Log.d(TAG, "Clicked facility: " + facilityName);

            Intent intent = new Intent(AdminFacilityScreen.this, AdminEventScreen.class);
            intent.putExtra("FACILITY_NAME", facilityName);
            startActivity(intent);
        });

        // Handle long clicks to delete the facility and associated events
        facilityList.setOnItemLongClickListener((parent, view, position, id) -> {
            Facility selectedFacility = facilityDataList.get(position);
            String facilityName = selectedFacility.getFacilityName();

            new AlertDialog.Builder(this)
                    .setTitle("Delete Facility")
                    .setMessage("Are you sure you want to delete the facility \"" + facilityName + "\" and all associated events?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteFacilityAndEvents(selectedFacility))
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });

        // Set back button click listener
        buttonBack.setOnClickListener(v -> finish());
    }

    /**
     * Deletes the selected facility and all associated events from the Firestore database.
     * <p>
     * The process is carried out in two steps:
     * <ul>
     *     <li>Deleting all events associated with the facility.</li>
     *     <li>Deleting the facility itself.</li>
     * </ul>
     * </p>
     *
     * @param facility The facility to delete.
     */
    private void deleteFacilityAndEvents(Facility facility) {
        // Step 1: Delete all events associated with the facility
        eventsRef.whereEqualTo("facilityName", facility.getFacilityName())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        eventsRef.document(doc.getId()).delete()
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Deleted event: " + doc.getId()))
                                .addOnFailureListener(e -> Log.e(TAG, "Failed to delete event: ", e));
                    }

                    // Step 2: Delete the facility itself
                    facilitiesRef.document(facility.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Facility and associated events deleted successfully.", Toast.LENGTH_SHORT).show();
                                facilityDataList.remove(facility);
                                facilityArrayAdapter.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to delete facility: ", e);
                                Toast.makeText(this, "Failed to delete facility.", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to retrieve associated events: ", e);
                    Toast.makeText(this, "Failed to retrieve associated events.", Toast.LENGTH_SHORT).show();
                });
    }
}
