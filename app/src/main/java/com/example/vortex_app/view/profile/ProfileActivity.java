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
import com.example.vortex_app.model.UserRepository;
import com.example.vortex_app.view.event.ManageEventsActivity;
import com.example.vortex_app.view.notification.NotificationsActivity;
import com.example.vortex_app.view.entrant.EntrantActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * ProfileActivity displays the user's profile information.
 * It attempts to load the user's data, including the avatar image,
 * and provides an option to edit the profile.
 */
public class ProfileActivity extends AppCompatActivity {
    private UserRepository userRepository;  // Repository for user data operations
    private static final String TAG = "ProfileActivity";  // Tag for logging

    // UI components
    private ImageView profileImageView;
    private TextView fullNameTextView, firstNameTextView, lastNameTextView, emailTextView, contactInfoTextView, deviceTextView;
    private Button editButton;

    private String androidId;  // Unique device identifier

    // Variables for retry mechanism when loading the avatar
    private int retryCount = 0;          // Current retry attempt
    private final int MAX_RETRY = 5;     // Maximum number of retries

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Enables edge-to-edge display
        setContentView(R.layout.activity_profile);

        // Adjust padding to account for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRepository = new UserRepository();  // Initialize the user repository

        // Initialize UI components
        profileImageView = findViewById(R.id.profileImage);
        fullNameTextView = findViewById(R.id.fullNameTextView);
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        contactInfoTextView = findViewById(R.id.contactInfoTextView);
        deviceTextView = findViewById(R.id.deviceTextView);
        editButton = findViewById(R.id.editButton);

        // Set up the edit button click listener to navigate to EditProfileActivity
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        // Retrieve the unique Android ID for the device
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "ANDROID_ID: " + androidId);

        // Load user data from the repository
        loadUserData(androidId);

        // Set up bottom navigation and handle item selection
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, EntrantActivity.class));
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


            } else if (itemId == R.id.nav_profile) {
                // Current activity; do nothing
                return true;
            }
            return false;
        });
    }

    /**
     * Loads the user's data from the repository.
     * Implements a retry mechanism to handle asynchronous updates of the avatar URL.
     *
     * @param androidId The unique identifier for the user's device.
     */
    private void loadUserData(String androidId) {
        userRepository.getUser(androidId, new UserRepository.OnUserLoadedCallback() {
            @Override
            public void onUserLoaded(User user) {
                if (user != null) {
                    // Update UI with user information
                    String firstName = user.getFirstName();
                    String lastName = user.getLastName();

                    firstNameTextView.setText(firstName);
                    lastNameTextView.setText(lastName);
                    emailTextView.setText(user.getEmail());
                    contactInfoTextView.setText(user.getContactInfo());
                    deviceTextView.setText(user.getDevice()); // Should now be the androidId

                    fullNameTextView.setText(firstName + " " + lastName);

                    // Attempt to load the avatar image
                    String avatarUrl = user.getAvatarUrl();
                    if (avatarUrl != null && !avatarUrl.isEmpty()) {
                        // Avatar URL is available; load the image
                        Glide.with(ProfileActivity.this)
                                .load(avatarUrl)
                                .placeholder(R.drawable.profile)  // Placeholder image
                                .into(profileImageView);
                        retryCount = 0;  // Reset retry count upon successful load
                    } else {
                        // Avatar URL not yet available; retry loading after a delay
                        if (retryCount < MAX_RETRY) {
                            retryCount++;
                            Log.d(TAG, "Avatar URL not available, retrying... (" + retryCount + ")");
                            profileImageView.postDelayed(() -> loadUserData(androidId), 1000); // Retry after 1 second
                        } else {
                            // Maximum retries reached; handle accordingly
                            Log.w(TAG, "Max retries reached, avatar not updated.");
                            retryCount = 0;  // Reset retry count
                            // Optionally display default avatar or inform the user
                            profileImageView.setImageResource(R.drawable.profile);
                        }
                    }
                } else {
                    // User document does not exist; initialize default user data
                    Log.d(TAG, "No such document. Initializing user data...");
                    initializeUserData(androidId);
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure to load user data
                Log.e(TAG, "Failed to load user data", e);
            }
        });
    }

    /**
     * Initializes default user data in the repository when no user data exists.
     *
     * @param androidId The unique identifier for the user's device.
     */
    private void initializeUserData(String androidId) {
        userRepository.initializeUserData(androidId, new UserRepository.OnUserSavedCallback() {
            @Override
            public void onUserSaved() {
                // Default user data saved successfully; reload user data
                Log.d(TAG, "Default user created successfully");
                loadUserData(androidId);
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure to save default user data
                Log.e(TAG, "Error creating default user", e);
            }
        });
    }
}
