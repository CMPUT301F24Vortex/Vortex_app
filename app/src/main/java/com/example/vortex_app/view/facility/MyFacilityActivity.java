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

public class MyFacilityActivity extends AppCompatActivity {

    private static final String TAG = "MyFacilityActivity";

    private FirebaseFirestore db;
    private String organizerID;

    private RecyclerView recyclerViewFacilities;
    private FacilityAdapter facilityAdapter;
    private List<Facility> facilityList;
    private FloatingActionButton fabCreateFacility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_facility);

        db = FirebaseFirestore.getInstance();
        organizerID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        recyclerViewFacilities = findViewById(R.id.recyclerViewFacilities);
        fabCreateFacility = findViewById(R.id.fabCreateFacility);

        // Initialize RecyclerView
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

        // Load Facilities
        loadFacilityDetails();

        // Floating button to create a new facility
        fabCreateFacility.setOnClickListener(v -> {
            Intent intent = new Intent(MyFacilityActivity.this, CreateFacilityActivity.class);
            startActivityForResult(intent, 100);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFacilityDetails(); // Reload facilities when returning to this activity
    }

    private void loadFacilityDetails() {
        db.collection("facility")
                .whereEqualTo("organizerId", organizerID) // Filter by organizerID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        facilityList.clear();
                        task.getResult().forEach(document -> {
                            String id = document.getId();
                            String facilityName = document.getString("facilityName");
                            String address = document.getString("address");
                            Log.d(TAG, "Facility found: " + facilityName + ", " + address);
                            facilityList.add(new Facility(id, facilityName, address));
                        });
                        facilityAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Facilities loaded for organizer: " + facilityList.size());
                    } else {
                        Log.e(TAG, "Error fetching facilities", task.getException());
                    }
                });
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadFacilityDetails(); // Reload facilities after creating a new one
        }
    }
}
