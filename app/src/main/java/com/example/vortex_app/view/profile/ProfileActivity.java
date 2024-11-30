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

public class ProfileActivity extends AppCompatActivity {
    private UserRepository userRepository;
    private static final String TAG = "ProfileActivity";

    private ImageView profileImageView;
    private TextView fullNameTextView, firstNameTextView, lastNameTextView, emailTextView, contactInfoTextView, deviceTextView;
    private Button editButton;

    private String androidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRepository = new UserRepository();

        profileImageView = findViewById(R.id.profileImage);
        fullNameTextView = findViewById(R.id.fullNameTextView);
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        contactInfoTextView = findViewById(R.id.contactInfoTextView);
        deviceTextView = findViewById(R.id.deviceTextView);
        editButton = findViewById(R.id.editButton);

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "ANDROID_ID: " + androidId);

        loadUserData(androidId);

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
                startActivity(new Intent(this, NotificationsActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }

    private void loadUserData(String androidId) {
        userRepository.getUser(androidId, new UserRepository.OnUserLoadedCallback() {
            @Override
            public void onUserLoaded(User user) {
                if (user != null) {
                    String firstName = user.getFirstName();
                    String lastName = user.getLastName();

                    firstNameTextView.setText(firstName);
                    lastNameTextView.setText(lastName);
                    emailTextView.setText(user.getEmail());
                    contactInfoTextView.setText(user.getContactInfo());
                    deviceTextView.setText(user.getDevice()); // This will be androidId now

                    fullNameTextView.setText(firstName + " " + lastName);

                    String avatarUrl = user.getAvatarUrl();
                    if (avatarUrl != null && !avatarUrl.isEmpty()) {
                        Glide.with(ProfileActivity.this)
                                .load(avatarUrl)
                                .placeholder(R.drawable.profile)
                                .into(profileImageView);
                    }
                } else {
                    Log.d(TAG, "No such document. Initializing user data...");
                    initializeUserData(androidId);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Failed to load user data", e);
            }
        });
    }

    private void initializeUserData(String androidId) {
        userRepository.initializeUserData(androidId, new UserRepository.OnUserSavedCallback() {
            @Override
            public void onUserSaved() {
                Log.d(TAG, "Default user created successfully");
                loadUserData(androidId);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error creating default user", e);
            }
        });
    }
}
