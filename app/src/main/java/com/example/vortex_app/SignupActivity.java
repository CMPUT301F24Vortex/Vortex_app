package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code SignupActivity} is an {@link AppCompatActivity} that allows new users to register
 * by entering personal details. It validates the user inputs, saves the data to Firestore, and
 * navigates to the main entrant activity upon successful registration.
 */
public class SignupActivity extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, emailInput, passwordInput, confirmPasswordInput, phoneInput;
    private Button signUpButton;
    private FirebaseFirestore db;

    /**
     * Called when the activity is created. Sets up the layout, initializes Firestore, input fields, and
     * configures the sign-up button to handle the registration process by validating inputs and saving data.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
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
        signUpButton.setOnClickListener(v -> {
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
        });
    }

    /**
     * Validates user inputs for required fields and checks if the password matches the confirmation.
     * Provides feedback through a {@link Toast} message if validation fails.
     *
     * @return {@code true} if inputs are valid; {@code false} otherwise.
     */
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

    /**
     * Saves the user's data to Firestore under the "user_profile" collection.
     * The email is used as the document ID, and fields include name, email, contact info, device, and avatar URL.
     */
    private void saveUserData() {
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String email = emailInput.getText().toString();
        String contactInfo = phoneInput.getText().toString();

        // Create a map with user data
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("email", email);
        user.put("contactInfo", contactInfo);
        user.put("device", "");  // Leave empty or add logic as needed
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
