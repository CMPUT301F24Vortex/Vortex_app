package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final String TEST_USER_ID = "020422"; // Fixed user ID

    private FirebaseFirestore db;

    private TextView firstNameTextView, lastNameTextView, emailTextView, contactInfoTextView, deviceTextView;
    private ImageView profileImageView;
    private Button editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        profileImageView = findViewById(R.id.profileImageView);
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        contactInfoTextView = findViewById(R.id.contactInfoTextView);
        deviceTextView = findViewById(R.id.deviceTextView);
        editProfileButton = findViewById(R.id.editButton);

        // Load user profile data
        loadUserProfile(TEST_USER_ID);

        // Set up Edit Profile button
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("USER_ID", TEST_USER_ID); // Pass the user ID to EditProfileActivity
            startActivity(intent);
        });

        // Set up bottom navigation
        setupBottomNavigation();
    }

    private void loadUserProfile(String userId) {
        db.collection("user_profile").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Populate profile data
                        firstNameTextView.setText(documentSnapshot.getString("firstName"));
                        lastNameTextView.setText(documentSnapshot.getString("lastName"));
                        emailTextView.setText(documentSnapshot.getString("email"));
                        contactInfoTextView.setText(documentSnapshot.getString("contactInfo"));
                        deviceTextView.setText(documentSnapshot.getString("device"));

                        String avatarUrl = documentSnapshot.getString("avatarUrl");
                        if (avatarUrl != null && !avatarUrl.isEmpty()) {
                            Glide.with(this).load(avatarUrl).into(profileImageView);
                        } else {
                            profileImageView.setImageResource(R.drawable.profile); // Default image
                        }
                    } else {
                        Log.w(TAG, "No profile found for user ID: " + userId);
                        createDefaultProfile(userId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading profile", e);
                    clearProfileUI();
                });
    }

    // Creates a default profile document if none exists
    private void createDefaultProfile(String userId) {
        Map<String, Object> defaultProfile = new HashMap<>();
        defaultProfile.put("firstName", "Default First Name");
        defaultProfile.put("lastName", "Default Last Name");
        defaultProfile.put("email", "default@example.com");
        defaultProfile.put("contactInfo", "N/A");
        defaultProfile.put("device", "N/A");
        defaultProfile.put("avatarUrl", "");

        db.collection("user_profile").document(userId).set(defaultProfile)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Default profile created for user ID: " + userId))
                .addOnFailureListener(e -> Log.e(TAG, "Error creating default profile", e));
    }

    private void clearProfileUI() {
        firstNameTextView.setText("N/A");
        lastNameTextView.setText("N/A");
        emailTextView.setText("N/A");
        contactInfoTextView.setText("N/A");
        deviceTextView.setText("N/A");
        profileImageView.setImageResource(R.drawable.profile); // Default profile image
    }

    private void setupBottomNavigation() {
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, EntrantActivity.class));
                return true;
            } else if (id == R.id.nav_events) {
                startActivity(new Intent(this, EventsActivity.class));
                return true;
            } else if (id == R.id.nav_notifications) {
                startActivity(new Intent(this, NotificationsActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                return true; // Already on ProfileActivity
            }
            return false;
        });

        // Highlight the current tab
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
    }
}
