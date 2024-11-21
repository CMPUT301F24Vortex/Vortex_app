package com.example.vortex_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_CODE = 1;
    private static final String TAG = "MainActivity";
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        // Create Notification Channel
        NotificationHelper.createNotificationChannel(this);

        // Request notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }

        // Initialize buttons
        Button buttonMainscreenEntrant = findViewById(R.id.button_mainscreen_entrant);
        Button buttonMainscreenOrganizer = findViewById(R.id.button_mainscreen_organizer);
        Button buttonMainscreenAdmin = findViewById(R.id.button_mainscreen_admin);

        // Set up Entrant role
        buttonMainscreenEntrant.setOnClickListener(view -> handleEntrantRole());

        // Set up Organizer role
        buttonMainscreenOrganizer.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, OrganizerActivity.class);
            startActivity(intent);
        });

        // Set up Admin role
        buttonMainscreenAdmin.setOnClickListener(view -> {
//            // Navigate to AdminActivity
//            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
//            startActivity(intent);
        });
    }

    // Handle Entrant Role
    private void handleEntrantRole() {
        if (auth.getCurrentUser() == null) {
            // Log in anonymously
            auth.signInAnonymously().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Anonymous sign-in successful");
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        createRandomUserProfile(user.getUid());
                    }
                } else {
                    Log.e(TAG, "Anonymous sign-in failed", task.getException());
                    Toast.makeText(this, "Failed to log in. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // User already logged in
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                navigateToEntrantActivity(user.getUid());
            }
        }
    }

    // Generate Random User Profile
    private void createRandomUserProfile(String userID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("firstName", "User" + UUID.randomUUID().toString().substring(0, 5));
        userProfile.put("lastName", "Guest");
        userProfile.put("email", "guest" + UUID.randomUUID().toString().substring(0, 5) + "@example.com");
        userProfile.put("contactInfo", "N/A");
        userProfile.put("device", "Android");

        db.collection("user_profile").document(userID)
                .set(userProfile)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Random user profile created successfully");
                    navigateToEntrantActivity(userID);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to create random user profile", e);
                    Toast.makeText(this, "Failed to create user profile. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }

    // Navigate to Entrant Activity
    private void navigateToEntrantActivity(String userID) {
        Intent intent = new Intent(MainActivity.this, EntrantActivity.class);
        intent.putExtra("USER_ID", userID);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
