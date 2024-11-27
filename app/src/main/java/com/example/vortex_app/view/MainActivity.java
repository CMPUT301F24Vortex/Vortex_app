package com.example.vortex_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.view.entrant.EntrantActivity;
import com.example.vortex_app.view.organizer.OrganizerActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TEST_USER_ID = "020422"; // Fixed user ID for login
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Buttons for selecting roles
        Button buttonEntrant = findViewById(R.id.button_mainscreen_entrant);
        Button buttonOrganizer = findViewById(R.id.button_mainscreen_organizer);
        Button buttonAdmin = findViewById(R.id.button_mainscreen_admin);

        // Ensure the user profile exists in Firebase
        ensureUserProfileExists(TEST_USER_ID);

        // Navigate to the relevant activity
        buttonEntrant.setOnClickListener(v -> navigateToEntrant());
        buttonOrganizer.setOnClickListener(v -> navigateToOrganizer());
        buttonAdmin.setOnClickListener(v -> navigateToAdmin());
    }

    private void ensureUserProfileExists(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDoc = db.collection("user_profile").document(userId);

        userDoc.get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                // Create a new document with default values
                Map<String, Object> defaultProfile = new HashMap<>();
                defaultProfile.put("firstName", "DefaultFirstName");
                defaultProfile.put("lastName", "DefaultLastName");
                defaultProfile.put("email", "default@example.com");
                defaultProfile.put("contactInfo", "N/A");
                defaultProfile.put("device", "N/A");
                defaultProfile.put("avatarUrl", null);

                userDoc.set(defaultProfile)
                        .addOnSuccessListener(aVoid -> Log.d("MainActivity", "User profile created successfully"))
                        .addOnFailureListener(e -> Log.e("MainActivity", "Error creating user profile", e));
            } else {
                Log.d("MainActivity", "User profile already exists");
            }
        }).addOnFailureListener(e -> Log.e("MainActivity", "Error checking user profile", e));
    }


    private void navigateToEntrant() {
        Intent intent = new Intent(this, EntrantActivity.class);
        intent.putExtra("USER_ID", TEST_USER_ID); // Pass the user ID to EntrantActivity
        startActivity(intent);
    }

    private void navigateToOrganizer() {
        Toast.makeText(this, "Organizer role selected", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, OrganizerActivity.class);
        startActivity(intent);
    }

    private void navigateToAdmin() {
        Toast.makeText(this, "Admin role selected", Toast.LENGTH_SHORT).show();
    }
}
