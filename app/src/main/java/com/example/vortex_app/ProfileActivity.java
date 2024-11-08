package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * ProfileActivity displays the user's profile information, including their avatar, full name, contact details,
 * and other attributes fetched from a Firestore database. It also allows users to navigate to different sections
 * of the app through a bottom navigation bar and provides an option to edit their profile.
 */
public class ProfileActivity extends AppCompatActivity {

    // Firestore and Storage instances
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private static final String TAG = "ProfileActivity";

    // UI components
    private ImageView profileImageView;
    private TextView fullNameTextView, firstNameTextView, lastNameTextView, emailTextView, contactInfoTextView, deviceTextView;
    private Button editButton;

    /**
     * Called when the activity is created. Sets up the layout, initializes UI components,
     * fetches user data from Firestore, and sets up navigation through a bottom navigation bar.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Set up padding for system bars to ensure proper layout display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        // Set up the Edit button to navigate to EditProfileActivity
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        // Load user data from Firestore
        loadUserData();

        // Set up the Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        // Set a new navigation item selected listener
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
                // Already in Profile, do nothing
                return true;
            }
            return false;
        });
    }

    /**
     * Fetches user data from Firestore and updates the UI components with the retrieved data.
     */
    private void loadUserData() {
        DocumentReference docRef = db.collection("user_profile").document("XKcYtstm0zrzcdIgvLwb");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    String firstName = document.getString("firstName");
                    String lastName = document.getString("lastName");

                    // Update UI components with data
                    firstNameTextView.setText(firstName);
                    lastNameTextView.setText(lastName);
                    emailTextView.setText(document.getString("email"));
                    contactInfoTextView.setText(document.getString("contactInfo"));
                    deviceTextView.setText(document.getString("device"));

                    // Set full name
                    fullNameTextView.setText(firstName + " " + lastName);

                    // Load and display the profile image from Firebase Storage
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
