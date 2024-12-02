package com.example.vortex_app.view.entrant;

import static android.content.ContentValues.TAG;

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
import com.example.vortex_app.view.MainActivity;
import com.example.vortex_app.view.event.EventInfoActivity;
import com.example.vortex_app.view.event.ManageEventsActivity;
import com.example.vortex_app.view.notification.NotificationsActivity;
import com.example.vortex_app.view.organizer.OrganizerActivity;
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

/**
 * EntrantActivity displays a list of events that the current user has joined (based on their user ID).
 * The activity also allows the user to scan a QR code for event details, navigate between different screens via bottom navigation,
 * and change their user role by navigating to the MainActivity.
 */
public class EntrantActivity extends AppCompatActivity {

    private ListView listView;
    private EventAdapter eventAdapter;
    private List<String> eventNames;
    private List<String> eventIDs;
    private List<String> eventImageUrls;
    private FirebaseFirestore db;
    private String currentUserID;
    private String collectionName;
    private Button buttonChangeRole;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    /**
     * Called when the activity is created. Initializes the UI components, fetches event details,
     * and handles bottom navigation and button clicks.
     *
     * @param savedInstanceState The saved instance state (if any) when the activity is recreated.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrant_new);

        db = FirebaseFirestore.getInstance();
        currentUserID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        collectionName = "final";

        // Initialize UI components
        listView = findViewById(R.id.list_view_events);
        eventNames = new ArrayList<>();
        eventIDs = new ArrayList<>();
        eventImageUrls = new ArrayList<>();
        buttonChangeRole = findViewById(R.id.button_change_role);

        eventAdapter = new EventAdapter(this, eventNames, eventIDs, eventImageUrls);
        listView.setAdapter(eventAdapter);

        // Set up QR scan button click listener
        Button scanQrButton = findViewById(R.id.button_scan_qr);
        scanQrButton.setOnClickListener(v -> checkCameraPermission());

        // Set up bottom navigation bar
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
                return true;
            }
            return false;
        });

        // Handle "Change Role" button click
        buttonChangeRole.setOnClickListener(v -> {
            Log.d(TAG, "Change Role button clicked!");
            Intent intent = new Intent(EntrantActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Fetch events the user has joined
        fetchUserEventIDs(currentUserID, collectionName);

        // Set up item click listener for event details
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String clickedEventID = eventIDs.get(position);
            Intent intent = new Intent(this, EventInfoActivity.class);
            intent.putExtra("EVENT_ID", clickedEventID);
            intent.putExtra("FROM_ENTRANT", true);
            startActivity(intent);
        });
    }

    /**
     * Fetches the event IDs that the current user has joined (from the Firestore database).
     * It retrieves events that the user has selected, waitlisted, or marked as final.
     *
     * @param currentUserID The ID of the current user.
     * @param collectionName The name of the Firestore collection to query.
     */
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
                        loadEventDetails(allEventIDs);
                    } else {
                        Toast.makeText(this, "Error fetching final events", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Loads the event details for the given set of event IDs. The event details include the event name and image URL.
     *
     * @param allEventIDs A set of event IDs to fetch details for.
     */
    private void loadEventDetails(Set<String> allEventIDs) {
        // Clear previous event data
        eventNames.clear();
        eventIDs.clear();
        eventImageUrls.clear();

        // Fetch details for each event
        for (String eventID : allEventIDs) {
            fetchEventDetails(eventID);
        }
    }

    /**
     * Fetches the details of a specific event from the Firestore database.
     *
     * @param eventID The ID of the event to fetch details for.
     */
    private void fetchEventDetails(String eventID) {
        db.collection("events").document(eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String eventName = document.getString("eventName");
                            String eventImageUrl = document.getString("imageUrl");

                            // Only add event details if not already in the list
                            if (!eventIDs.contains(eventID)) {
                                eventNames.add(eventName);
                                eventIDs.add(eventID);
                                eventImageUrls.add(eventImageUrl);
                                eventAdapter.notifyDataSetChanged(); // Update the adapter
                            }
                        }
                    } else {
                        Toast.makeText(this, "Error fetching event details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Checks if the app has permission to access the camera. If not, requests camera permission.
     */
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startQRCodeScanner();
        }
    }

    /**
     * Handles the result of the camera permission request. If permission is granted, starts the QR code scanner.
     *
     * @param requestCode The request code passed in the permission request.
     * @param permissions The list of permissions requested.
     * @param grantResults The results of the permission request.
     */
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

    /**
     * Starts the QR code scanner by initializing an IntentIntegrator.
     */
    private void startQRCodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(true);
        integrator.setPrompt("Scan a QR code");
        integrator.initiateScan();
    }

    /**
     * Handles the result of a QR code scan. If a valid result is found, navigates to the event info page.
     *
     * @param requestCode The request code for the activity result.
     * @param resultCode The result code for the activity result.
     * @param data The intent data returned by the scan.
     */
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
