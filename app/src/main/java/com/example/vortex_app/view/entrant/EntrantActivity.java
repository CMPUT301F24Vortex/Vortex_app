package com.example.vortex_app.view.entrant;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import com.example.vortex_app.model.Event;
import com.example.vortex_app.R;
import com.example.vortex_app.view.event.EventInfoActivity;
import com.example.vortex_app.view.event.ManageEventsActivity;
import com.example.vortex_app.view.notification.NotificationsActivity;
import com.example.vortex_app.view.profile.ProfileActivity;
import com.example.vortex_app.controller.adapter.EventAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntrantActivity extends AppCompatActivity {

    private ListView listView;
    private EventAdapter eventAdapter;
    private List<String> eventNames;
    private List<String> eventIDs;
    private List<String> eventImageUrls;
    private FirebaseFirestore db;
    private String currentUserID;
    private String collectionName;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrant_new);


        db = FirebaseFirestore.getInstance();
        currentUserID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        collectionName = "final";



        listView = findViewById(R.id.list_view_events);
        eventNames = new ArrayList<>();
        eventIDs = new ArrayList<>();
        eventImageUrls = new ArrayList<>();



        eventAdapter = new EventAdapter(this, eventNames, eventIDs, eventImageUrls);
        listView.setAdapter(eventAdapter);

        Button scanQrButton = findViewById(R.id.button_scan_qr);
        scanQrButton.setOnClickListener(v -> checkCameraPermission());

        // Set up bottom navigation and handle item selection
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_events) {
                startActivity(new Intent(this, ManageEventsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_notifications) {
                Intent intent = new Intent(this, NotificationsActivity.class);
                startActivity(intent);
                finish();
                return true;


            } else if (itemId == R.id.nav_home) {
                // Current activity; do nothing
                return true;
            }
            return false;
        });


        // Fetch all event IDs related to the user (selected, waitlisted, final)
        fetchUserEventIDs(currentUserID,collectionName);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String clickedEventID = eventIDs.get(position);
            Intent intent = new Intent(this, EventInfoActivity.class);
            intent.putExtra("EVENT_ID", clickedEventID);
            startActivity(intent);
        });
    }

    private void fetchUserEventIDs(String currentUserID, String collectionName) {
        Set<String> allEventIDs = new HashSet<>();

        db.collection(collectionName)
                .whereEqualTo("userID", currentUserID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String eventID = document.getString("eventID");
                            if (eventID != null) {
                                allEventIDs.add(eventID);
                            }
                        }
                        loadEventDetails(allEventIDs); // Now, load details only once
                    } else {
                        Toast.makeText(this, "Error fetching final events", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void loadEventDetails(Set<String> allEventIDs) {
        // Clear the lists to ensure fresh data
        eventNames.clear();
        eventIDs.clear();
        eventImageUrls.clear();

        // Iterate over the event IDs set and fetch details for each event
        for (String eventID : allEventIDs) {
            fetchEventDetails(eventID);
        }
    }

    private void fetchEventDetails(String eventID) {
        db.collection("events").document(eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String eventName = document.getString("eventName");
                            String eventImageUrl = document.getString("imageUrl");

                            // Only add event details if the event is not already in the list
                            if (!eventIDs.contains(eventID)) {
                                eventNames.add(eventName);
                                eventIDs.add(eventID);
                                eventImageUrls.add(eventImageUrl);
                                eventAdapter.notifyDataSetChanged(); // Notify adapter to update the UI
                            }
                        }
                    } else {
                        Toast.makeText(this, "Error fetching event details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startQRCodeScanner();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startQRCodeScanner();
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startQRCodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(true);
        integrator.setPrompt("Scan a QR code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedContent = result.getContents();
                Toast.makeText(this, "Scanned: " + scannedContent, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, EventInfoActivity.class);
                intent.putExtra("EVENT_ID", scannedContent);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No scan result!", Toast.LENGTH_SHORT).show();
        }
    }
}
