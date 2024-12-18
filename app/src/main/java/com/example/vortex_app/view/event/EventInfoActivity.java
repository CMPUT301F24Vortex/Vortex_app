package com.example.vortex_app.view.event;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.vortex_app.R;
import com.example.vortex_app.view.entrant.EntrantActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

/**
 * EventInfoActivity is the screen that displays detailed information about a specific event.
 * It fetches event details from Firebase Firestore and displays the event's name, schedule,
 * location, price, registration dates, and other relevant data. The activity also includes
 * functionality for checking if the user can join a waiting list based on geolocation requirements.
 *
 * <p>This activity allows users to:
 * <ul>
 *   <li>View event details, including the poster image, using Glide.</li>
 *   <li>Join the waiting list for the event (if applicable).</li>
 *   <li>See if a geolocation check is required before joining the waiting list.</li>
 * </ul>
 *
 * <p>The activity is initialized by retrieving the event ID passed from the previous screen
 * (either the list of events or EntrantActivity) and loading event details from Firestore.
 * If the event requires geolocation, the user is warned before they can join the waiting list.
 *
 * @see EntrantActivity
 * @see com.bumptech.glide.Glide
 * @see FirebaseFirestore
 */
public class EventInfoActivity extends AppCompatActivity {

    private static final String TAG = "EventInfoActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private TextView eventNameTextView, classDayTextView, timeTextView, periodTextView,
            locationTextView, priceTextView, maxPeopleTextView, regOpenDateTextView,
            regDueDateTextView, waitlistLimitTextView, geolocationRequirementTextView,
            facilityNameTextView;
    private ImageView posterImageView;
    private Button joinWaitingListButton;

    private String eventID;
    private String deviceID;
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double userLatitude = 0.0;
    private double userLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        // Initialize Firebase Firestore and Location Services
        db = FirebaseFirestore.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Retrieve device ID
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "Device ID: " + deviceID);

        // Initialize UI components
        eventNameTextView = findViewById(R.id.text_event_name);
        classDayTextView = findViewById(R.id.text_class_day);
        timeTextView = findViewById(R.id.text_time);
        periodTextView = findViewById(R.id.text_period);
        locationTextView = findViewById(R.id.text_location);
        priceTextView = findViewById(R.id.text_price);
        maxPeopleTextView = findViewById(R.id.text_max_people);
        regOpenDateTextView = findViewById(R.id.text_reg_open_date);
        regDueDateTextView = findViewById(R.id.text_reg_due_date);
        waitlistLimitTextView = findViewById(R.id.text_waitlist_limit);
        geolocationRequirementTextView = findViewById(R.id.text_geolocation_requirement);
        facilityNameTextView = findViewById(R.id.text_facility_name);
        posterImageView = findViewById(R.id.image_event_poster);
        joinWaitingListButton = findViewById(R.id.button_join_waiting_list);

        // Retrieve event ID from the intent
        eventID = getIntent().getStringExtra("EVENT_ID");

        if (eventID == null || eventID.isEmpty()) {
            Toast.makeText(this, "Event ID not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Check if the activity was opened from EntrantActivity
        boolean fromEntrantActivity = getIntent().getBooleanExtra("FROM_ENTRANT", false);

        // Hide the "Join Waiting List" button if from EntrantActivity
        if (fromEntrantActivity) {
            joinWaitingListButton.setVisibility(View.GONE);
        }

        // Load event details
        loadEventDetails();

        // Set up "Join Waiting List" button
        joinWaitingListButton.setOnClickListener(view -> checkIfAlreadyJoined());
    }



    /**
     * Fetches and displays event details from Firebase Firestore.
     * Sets the UI elements with event-specific information including name, class day,
     * schedule, location, price, registration details, and the event's poster image.
     *
     * If the event is associated with a facility, its location is also fetched and displayed.
     *
     * @see FirebaseFirestore
     */
    private void loadEventDetails() {
        db.collection("events")
                .document(eventID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String eventName = documentSnapshot.getString("eventName");
                        String classDay = documentSnapshot.getString("classDay");
                        String time = documentSnapshot.getString("time");
                        String startPeriod = documentSnapshot.getString("startPeriod");
                        String endPeriod = documentSnapshot.getString("endPeriod");
                        String facilityName = documentSnapshot.getString("facilityName");
                        String price = documentSnapshot.getString("price");
                        String maxPeople = documentSnapshot.getString("maxPeople");
                        String regOpenDate = documentSnapshot.getString("regOpenDate");
                        String regDueDate = documentSnapshot.getString("regDueDate");
                        String waitlistLimit = documentSnapshot.getString("waitlistLimit");
                        String geolocationRequirement = documentSnapshot.getString("geolocationRequirement");
                        String imageUrl = documentSnapshot.getString("imageUrl");

                        eventNameTextView.setText(eventName != null ? eventName : "Details unavailable");
                        classDayTextView.setText(classDay != null ? classDay : "Details unavailable");
                        timeTextView.setText(time != null ? time : "Details unavailable");
                        periodTextView.setText((startPeriod != null && endPeriod != null)
                                ? startPeriod + " ~ " + endPeriod
                                : "Details unavailable");
                        priceTextView.setText(price != null ? "$" + price : "Details unavailable");
                        maxPeopleTextView.setText(maxPeople != null ? maxPeople : "Details unavailable");
                        regOpenDateTextView.setText(regOpenDate != null ? regOpenDate : "Details unavailable");
                        regDueDateTextView.setText(regDueDate != null ? regDueDate : "Details unavailable");
                        waitlistLimitTextView.setText(waitlistLimit != null ? waitlistLimit : "Details unavailable");
                        geolocationRequirementTextView.setText(geolocationRequirement != null ? geolocationRequirement : "Details unavailable");
                        facilityNameTextView.setText(facilityName != null ? facilityName : "Details unavailable");

                        // Fetch and display location from facility collection
                        fetchLocationFromFacility(facilityName);

                        // Load poster using Glide safely
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Log.d(TAG, "Image URL: " + imageUrl);
                            if (!isDestroyed() && !isFinishing()) {
                                Glide.with(this)
                                        .load(imageUrl)
                                        .placeholder(R.drawable.placeholder_image) // Placeholder image
                                        .error(R.drawable.placeholder_image)      // Error fallback
                                        .into(posterImageView);
                            } else {
                                Log.w(TAG, "Activity is destroyed or finishing. Skipping Glide image loading.");
                            }
                        } else {
                            Log.w(TAG, "Image URL is null or empty, loading placeholder image.");
                            posterImageView.setImageResource(R.drawable.placeholder_image);
                        }
                    } else {
                        Toast.makeText(this, "Event details not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching event details", e);
                    Toast.makeText(this, "Failed to load event details.", Toast.LENGTH_SHORT).show();
                });
    }


    /**
     * Fetches and displays the location (address) of the facility from the "facility" collection.
     * Updates the location UI element with the fetched address.
     *
     * @param facilityName The name of the facility to fetch the location for.
     */
    private void fetchLocationFromFacility(String facilityName) {
        if (facilityName == null || facilityName.isEmpty()) {
            locationTextView.setText("Facility not specified");
            return;
        }

        db.collection("facility")
                .whereEqualTo("facilityName", facilityName)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot facilityDoc = querySnapshot.getDocuments().get(0);
                        String address = facilityDoc.getString("address");
                        locationTextView.setText(address != null ? address : "Address not available");
                        Log.d(TAG, "Facility Address Fetched: " + address);
                    } else {
                        locationTextView.setText("Facility not found");
                        Log.e(TAG, "No facility found with the given name: " + facilityName);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching facility location", e);
                    locationTextView.setText("Failed to load location");
                });
    }


    /**
     * Checks whether the event has a geolocation requirement. If it does, prompts the user to
     * grant location access, otherwise proceeds to check the user's status on the waiting list.
     */
    private void checkIfAlreadyJoined() {
        db.collection("events")
                .document(eventID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String geolocationRequirement = documentSnapshot.getString("geolocationRequirement");
                    if ("Yes".equalsIgnoreCase(geolocationRequirement)) {
                        showGeolocationWarning();
                    } else {
                        checkWaitingListStatus();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching geolocation requirement", e);
                });
    }




    /**
     * Displays a warning dialog if the event requires geolocation to register.
     */
    private void showGeolocationWarning() {
        new AlertDialog.Builder(this)
                .setTitle("Warning!")
                .setMessage("This class requires geolocation to register!")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Got it", (dialog, which) -> requestUserLocation())
                .setNegativeButton("Quit", (dialog, which) -> dialog.dismiss())
                .show();
    }


    /**
     * Requests the user's location permission if not already granted. If permission is granted,
     * the user's location is retrieved.
     */
    private void requestUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getUserLocation();
        }
    }


    /**
     * Retrieves the user's last known location and checks their status on the event's waiting list.
     * If the location is unavailable, a message is displayed to the user.
     */
    private void getUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Get the last known location
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            userLatitude = location.getLatitude();
                            userLongitude = location.getLongitude();
                            Log.d(TAG, "User Location: " + userLatitude + ", " + userLongitude);
                            checkWaitingListStatus();
                        } else {
                            Toast.makeText(this, "Unable to get location.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error getting location", e);
                        Toast.makeText(this, "Failed to get location.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }



    /**
     * Checks whether the user has already joined the event's waiting list. If the user is not on
     * the list, they are added to it.
     */
    private void checkWaitingListStatus() {
        db.collection("waitlisted")
                .document(eventID + "_" + deviceID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Toast.makeText(this, "You have already joined this event's waiting list.", Toast.LENGTH_SHORT).show();
                    } else {
                        joinWaitingList();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error checking waiting list status", e);
                    Toast.makeText(this, "Failed to check waiting list status.", Toast.LENGTH_SHORT).show();
                });
    }



    /**
     * Adds the user to the event's waiting list by retrieving their profile and saving the
     * necessary information to the database, including their location coordinates.
     */
    private void joinWaitingList() {
        db.collection("user_profile")
                .document(deviceID)
                .get()
                .addOnSuccessListener(userDoc -> {
                    if (userDoc.exists()) {
                        String firstName = userDoc.getString("firstName");
                        String lastName = userDoc.getString("lastName");
                        String email = userDoc.getString("email");

                        Map<String, Object> waitingListEntry = new HashMap<>();
                        waitingListEntry.put("userID", deviceID);
                        waitingListEntry.put("firstName", firstName);
                        waitingListEntry.put("lastName", lastName);
                        waitingListEntry.put("email", email);
                        waitingListEntry.put("timestamp", FieldValue.serverTimestamp());
                        waitingListEntry.put("eventID", eventID);
                        waitingListEntry.put("latitude", userLatitude);
                        waitingListEntry.put("longitude", userLongitude);

                        db.collection("waitlisted")
                                .document(eventID + "_" + deviceID)
                                .set(waitingListEntry)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Successfully joined the waiting list.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EventInfoActivity.this, EntrantActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error adding user to the waiting list", e);
                                    Toast.makeText(this, "Failed to join waiting list.", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "User profile not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user profile", e);
                    Toast.makeText(this, "Failed to fetch user profile.", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Handles the result of a location permission request. If permission is granted, the user's
     * location is retrieved; otherwise, a message is displayed indicating that location permission
     * is required.
     *
     * @param requestCode The request code passed in requestPermissions().
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getUserLocation();
        } else {
            Toast.makeText(this, "Location permission is required to join the event.", Toast.LENGTH_SHORT).show();
        }
    }
}
