package com.example.vortex_app.view.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.vortex_app.R;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * UserDetailActivity displays detailed information about a selected user and provides an option to delete the user.
 */
public class UserDetailActivity extends AppCompatActivity {

    private static final String TAG = "UserDetailActivity";

    private ImageView profileImageView;
    private TextView fullNameTextView, firstNameTextView, lastNameTextView, emailTextView, contactInfoTextView, deviceTextView;
    private Button deleteUserButton;

    private String userId; // User ID to identify the user in Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Initialize UI components
        profileImageView = findViewById(R.id.profileImage);
        fullNameTextView = findViewById(R.id.fullNameTextView);
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        contactInfoTextView = findViewById(R.id.contactInfoTextView);
        deviceTextView = findViewById(R.id.deviceTextView);
        deleteUserButton = findViewById(R.id.deleteUserButton);

        // Get user details from the intent
        userId = getIntent().getStringExtra("USER_ID");
        String firstName = getIntent().getStringExtra("FIRST_NAME");
        String lastName = getIntent().getStringExtra("LAST_NAME");
        String email = getIntent().getStringExtra("EMAIL");
        String contactInfo = getIntent().getStringExtra("CONTACT_INFO");
        String device = getIntent().getStringExtra("DEVICE");
        String avatarUrl = getIntent().getStringExtra("AVATAR_URL");

        // Set user details in the UI
        firstNameTextView.setText(firstName);
        lastNameTextView.setText(lastName);
        fullNameTextView.setText(firstName + " " + lastName);
        emailTextView.setText(email);
        contactInfoTextView.setText(contactInfo);
        deviceTextView.setText(device);

        // Load the user's avatar image using Glide
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Glide.with(this)
                    .load(avatarUrl)
                    .placeholder(R.drawable.profile)
                    .into(profileImageView);
        } else {
            // Set a default avatar if no URL is provided
            profileImageView.setImageResource(R.drawable.profile);
        }

        // Set up the delete button click listener
        deleteUserButton.setOnClickListener(v -> deleteUser());
    }

    /**
     * Deletes the user from Firebase Firestore.
     */
    private void deleteUser() {
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user_profile").document(userId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UserDetailActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "User deleted successfully");
                    finish(); // Close the activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UserDetailActivity.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error deleting user", e);
                });
    }
}
