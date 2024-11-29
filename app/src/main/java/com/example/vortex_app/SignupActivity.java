package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, emailInput, passwordInput, confirmPasswordInput, phoneInput;
    private Button signUpButton, loginButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize input fields
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        phoneInput = findViewById(R.id.phone_input);
        signUpButton = findViewById(R.id.button_signup);
        loginButton = findViewById(R.id.button_login); // Make sure this button exists in your layout

        // Set up sign-up button's click listener
        signUpButton.setOnClickListener(v -> {
            if (validateInputs()) {  // Ensure inputs are valid before proceeding
                createAccount();  // Create user account with FirebaseAuth
            }
        });


        // Set up login button's click listener
        loginButton.setOnClickListener(v -> {
            // Redirect to LoginActivity
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }


    // Validate user inputs (simple example)
    private boolean validateInputs() {
        if (firstNameInput.getText().toString().isEmpty() ||
                lastNameInput.getText().toString().isEmpty() ||
                emailInput.getText().toString().isEmpty() ||
                passwordInput.getText().toString().isEmpty() ||
                confirmPasswordInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!passwordInput.getText().toString().equals(confirmPasswordInput.getText().toString())) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    // Create user account with FirebaseAuth
    private void createAccount() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-up successful, user is authenticated
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d(TAG, "createUserWithEmail:success, userID: " + user.getUid());

                        // Save user data to Firestore
                        saveUserData(user.getUid());

                        // Show a confirmation message
                        Toast.makeText(SignupActivity.this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();

                        // Navigate to MainActivity
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();  // Close SignupActivity
                    } else {
                        // If sign-up fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignupActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Save user data to Firestore
    private void saveUserData(String userID) {
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String contactInfo = phoneInput.getText().toString().trim();  // Assuming this maps to contactInfo

        // Create a map with your friend's collection structure
        // Create a map with user data
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("email", email);
        user.put("contactInfo", contactInfo);
        user.put("device", "");  // Leave as empty or add logic as needed
        user.put("avatarUrl", "");  // Leave empty if no avatar URL for now

        // Save to Firestore using userID as document ID
        db.collection("user_profile")
                .document(userID)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User profile saved to Firestore");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error saving user profile", e);
                });
    }

}
