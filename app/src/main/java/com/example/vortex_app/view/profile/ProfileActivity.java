package com.example.vortex_app.view.profile;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import com.example.vortex_app.R;
import com.example.vortex_app.model.User;
import com.example.vortex_app.view.event.ManageEventsActivity;
import com.example.vortex_app.view.notification.NotificationsActivity;
import com.example.vortex_app.view.entrant.EntrantActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class ProfileActivity extends AppCompatActivity {
    // Firebase Firestore and Storage instances
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private static final String TAG = "ProfileActivity";

    // UI components
    private ImageView profileImageView;
    private TextView fullNameTextView, firstNameTextView, lastNameTextView, emailTextView, contactInfoTextView, deviceTextView;
    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Configure UI components with padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Firestore and Storage
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Initialize UI components
        profileImageView = findViewById(R.id.profileImage);
        fullNameTextView = findViewById(R.id.fullNameTextView);
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        contactInfoTextView = findViewById(R.id.contactInfoTextView);
        deviceTextView = findViewById(R.id.deviceTextView);
        editButton = findViewById(R.id.editButton);

        // Set up a click listener for the Edit button to navigate to EditProfileActivity
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        // Retrieve the device's unique Android ID
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "ANDROID_ID: " + androidId); // Debug log for verification

        // Load user data from Firestore using androidId
        loadUserData(androidId);

        // Configure the Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        // Set up navigation item selection listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, EntrantActivity.class)); // Navigate to Home
                return true;
            } else if (itemId == R.id.nav_events) {
                startActivity(new Intent(this, ManageEventsActivity.class)); // Navigate to Events
                return true;
            } else if (itemId == R.id.nav_notifications) {
                startActivity(new Intent(this, NotificationsActivity.class)); // Navigate to Notifications
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true; // Already in Profile
            }
            return false;
        });
    }

    /**
     * Loads user data from Firestore using the device's androidId.
     * If the document doesn't exist, it initializes user data.
     */
    private void loadUserData(String androidId) {
        DocumentReference docRef = db.collection("user_profile").document(androidId);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // Populate UI components with Firestore data
                    String firstName = document.getString("firstName");
                    String lastName = document.getString("lastName");

                    firstNameTextView.setText(firstName);
                    lastNameTextView.setText(lastName);
                    emailTextView.setText(document.getString("email"));
                    contactInfoTextView.setText(document.getString("contactInfo"));
                    deviceTextView.setText(document.getString("device"));

                    // Update the full name TextView
                    fullNameTextView.setText(firstName + " " + lastName);

                    // Load avatar from Firebase Storage
                    String avatarUrl = document.getString("avatarUrl");
                    if (avatarUrl != null && !avatarUrl.isEmpty()) {
                        Glide.with(this)
                                .load(avatarUrl)
                                .placeholder(R.drawable.profile) // Placeholder image
                                .into(profileImageView);
                    }
                } else {
                    // Document does not exist, create default user data
                    Log.d(TAG, "No such document. Initializing user data...");
                    initializeUserData(androidId);
                }
            } else {
                // Log Firestore query error
                Log.e(TAG, "Failed to load user data", task.getException());
            }
        });
    }

    /**
     * Initializes default user data in Firestore if the document doesn't exist.
     */
    private void initializeUserData(String androidId) {
        DocumentReference docRef = db.collection("user_profile").document(androidId);

        // Retrieve device information (brand and model)
        String brand = android.os.Build.BRAND; // e.g., "Samsung"
        String model = android.os.Build.MODEL; // e.g., "Galaxy S21"
        String deviceInfo = brand + " " + model; // Combine brand and model

        // Create default user data
        User defaultUser = new User(
                "Default First Name",  // Default first name
                "Default Last Name",   // Default last name
                "default@example.com", // Default email
                "123-456-7890",        // Default contact information
                deviceInfo             // Device information
        );

        // Save the default user data to Firestore
        docRef.set(defaultUser)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Default user created successfully");
                    // Reload user data after creating the document
                    loadUserData(androidId);
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error creating default user", e));
    }
}