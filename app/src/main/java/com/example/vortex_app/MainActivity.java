package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        // Entrant button setup
        Button buttonEntrant = findViewById(R.id.button_mainscreen_entrant);
        buttonEntrant.setOnClickListener(v -> setupEntrant());

        // Organizer button setup
        Button buttonOrganizer = findViewById(R.id.button_mainscreen_organizer);
        buttonOrganizer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrganizerActivity.class);
            startActivity(intent);
        });

        // Admin button setup
        Button buttonAdmin = findViewById(R.id.button_mainscreen_admin);
        buttonAdmin.setOnClickListener(v -> {
            // Navigate to AdminActivity (not implemented yet)
            Toast.makeText(this, "Admin functionality coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupEntrant() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            Log.d(TAG, "Anonymous login successful. User ID: " + user.getUid());
                            generateRandomUserProfile(user.getUid());
                        } else {
                            Log.e(TAG, "User is null after anonymous login.");
                            Toast.makeText(MainActivity.this, "Failed to retrieve user information.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Anonymous login failed", task.getException());
                        Toast.makeText(MainActivity.this, "Failed to login as Entrant. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void generateRandomUserProfile(String userId) {
        // Generate random user details
        String[] firstNames = {"John", "Jane", "Alex", "Emily", "Chris", "Taylor"};
        String[] lastNames = {"Doe", "Smith", "Johnson", "Brown", "Williams", "Davis"};
        String[] devices = {"Android", "iPhone", "Tablet", "Windows"};
        Random random = new Random();

        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";
        String contactInfo = "+1" + (random.nextInt(900000000) + 100000000);
        String device = devices[random.nextInt(devices.length)];

        // Create user profile map
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("firstName", firstName);
        userProfile.put("lastName", lastName);
        userProfile.put("email", email);
        userProfile.put("contactInfo", contactInfo);
        userProfile.put("device", device);

        // Save profile to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user_profile")
                .document(userId)
                .set(userProfile)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Random user profile created successfully.");
                    Toast.makeText(MainActivity.this, "Profile created for: " + firstName + " " + lastName, Toast.LENGTH_SHORT).show();

                    // Navigate to EntrantActivity
                    Intent intent = new Intent(MainActivity.this, EntrantActivity.class);
                    intent.putExtra("USER_ID", userId); // Pass the generated user ID
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to create user profile", e);
                    Toast.makeText(MainActivity.this, "Failed to create profile", Toast.LENGTH_SHORT).show();
                });
    }
}
