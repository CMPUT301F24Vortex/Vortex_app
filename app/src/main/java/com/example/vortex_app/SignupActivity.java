package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, emailInput, passwordInput, confirmPasswordInput, phoneInput;
    private Button signUpButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firestore
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        // Initialize input fields
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        phoneInput = findViewById(R.id.phone_input);
        signUpButton = findViewById(R.id.button_signup);

        // Set up sign-up button's click listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {  // Ensure inputs are valid before proceeding
                    saveUserData();  // Save user data to Firestore

                    // Show a confirmation message
                    Toast.makeText(SignupActivity.this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();

                    // Navigate to EntrantActivity
                    Intent intent = new Intent(SignupActivity.this, EntrantActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();  // Close SignupActivity
                }
            }
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

    // Save user data to Firestore
    private void saveUserData() {
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String email = emailInput.getText().toString();
        String contactInfo = phoneInput.getText().toString();  // Assuming this maps to contactInfo

        // Create a map with your friend's collection structure
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("email", email);
        user.put("contactInfo", contactInfo);
        user.put("device", "");  // Leave as empty or add logic as needed
        user.put("avatarUrl", "");  // Leave empty if no avatar URL for now

        db.collection("user_profile")
                .document(email)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignupActivity.this, "User saved in shared database", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignupActivity.this, "Error saving user: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

}
