package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class ProfileActivity extends AppCompatActivity {

    // Firestore and Storage instances
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private static final String TAG = "ProfileActivity";

    // UI components
    private ImageView profileImageView;
    private TextView fullNameTextView, firstNameTextView, lastNameTextView, emailTextView, contactInfoTextView, deviceTextView;
    private Button editButton;

    // User ID to fetch and pass
    private String userID = "XKcYtstm0zrzcdIgvLwb"; // Replace with dynamic user ID from authentication or configuration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firestore and Storage
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

        // Set up Edit button to navigate to EditProfileActivity
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("USER_ID", userID); // Pass userID to EditProfileActivity
            startActivity(intent);
        });

        // Load user data from Firestore
        loadUserData();

        // Set up Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, EntrantActivity.class));
                return true;
            } else if (itemId == R.id.nav_events) {
                startActivity(new Intent(this, EventsActivity.class));
                return true;
            } else if (itemId == R.id.nav_notifications) {
                startActivity(new Intent(this, NotificationsActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Do nothing as the user is already on the profile screen
                return true;
            }
            return false;
        });

    }

    // Load user data from Firestore
    private void loadUserData() {
        DocumentReference docRef = db.collection("user_profile").document(userID);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    String firstName = document.getString("firstName");
                    String lastName = document.getString("lastName");

                    // Set data to TextView fields
                    firstNameTextView.setText(firstName != null ? firstName : "N/A");
                    lastNameTextView.setText(lastName != null ? lastName : "N/A");
                    emailTextView.setText(document.getString("email"));
                    contactInfoTextView.setText(document.getString("contactInfo"));
                    deviceTextView.setText(document.getString("device"));

                    // Update full name TextView
                    fullNameTextView.setText((firstName != null ? firstName : "") + " " +
                            (lastName != null ? lastName : ""));

                    // Load profile image
                    String avatarUrl = document.getString("avatarUrl");
                    if (avatarUrl != null && !avatarUrl.isEmpty()) {
                        Glide.with(this)
                                .load(avatarUrl)
                                .placeholder(R.drawable.profile) // Placeholder image
                                .into(profileImageView);
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
}
