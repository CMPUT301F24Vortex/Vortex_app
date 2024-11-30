package com.example.vortex_app.view.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.model.User;
import com.example.vortex_app.model.UserRepository;
import com.bumptech.glide.Glide;

/**
 * EditProfileActivity allows the user to edit their profile information,
 * including first name, last name, email, contact info, device info, and avatar.
 * Users can upload a new avatar or remove the existing one.
 */
public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";  // Tag for logging

    // UI components
    private EditText firstNameEditText, lastNameEditText, emailEditText, contactInfoEditText, deviceEditText;
    private Button saveButton;
    private ImageView editIcon, avatarImageView;

    private Uri imageUri;         // URI of the selected image for avatar
    private String oldAvatarUrl;  // URL of the existing avatar in Firebase Storage
    private String androidId;     // Unique device identifier

    private UserRepository userRepository;  // Repository for user data operations

    // Launcher for the activity result when choosing an image file
    private final ActivityResultLauncher<Intent> fileChooserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Handle the result of the file chooser intent
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    avatarImageView.setImageURI(imageUri);  // Display the selected image

                    if (imageUri != null) {
                        // Upload the new avatar image to Firebase Storage
                        userRepository.uploadAvatar(androidId, imageUri, oldAvatarUrl, new UserRepository.OnAvatarUploadedCallback() {
                            @Override
                            public void onAvatarUploaded(String downloadUrl) {
                                Log.d(TAG, "Avatar uploaded successfully!");
                                Toast.makeText(EditProfileActivity.this, "Avatar uploaded successfully!", Toast.LENGTH_SHORT).show();
                                oldAvatarUrl = downloadUrl;  // Update the oldAvatarUrl with the new one
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.e(TAG, "Error uploading avatar", e);
                                Toast.makeText(EditProfileActivity.this, "Failed to upload avatar. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userRepository = new UserRepository();  // Initialize the user repository
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);  // Get the Android ID

        // Initialize UI components
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        contactInfoEditText = findViewById(R.id.contactInfoEditText);
        deviceEditText = findViewById(R.id.deviceEditText);
        saveButton = findViewById(R.id.saveButton);
        editIcon = findViewById(R.id.editAvatarIcon);
        avatarImageView = findViewById(R.id.avatarImageView);

        // Load existing user data to populate the fields
        loadUserData();

        // Set up the save button click listener
        saveButton.setOnClickListener(view -> {
            saveUserData();  // Save the updated user data
            // Navigate back to ProfileActivity
            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up the edit icon click listener to show options for editing the avatar
        editIcon.setOnClickListener(v -> {
            editIcon.setVisibility(View.GONE);  // Hide the edit icon when menu is shown
            showPopupMenu(v);                  // Display the popup menu
        });
    }

    /**
     * Loads the user's existing data from the repository and populates the UI fields.
     */
    private void loadUserData() {
        userRepository.getUser(androidId, new UserRepository.OnUserLoadedCallback() {
            @Override
            public void onUserLoaded(User user) {
                if (user != null) {
                    // Populate the edit texts with user information
                    firstNameEditText.setText(user.getFirstName());
                    lastNameEditText.setText(user.getLastName());
                    emailEditText.setText(user.getEmail());
                    contactInfoEditText.setText(user.getContactInfo());
                    deviceEditText.setText(user.getDevice()); // Should be androidId now

                    oldAvatarUrl = user.getAvatarUrl();  // Get the current avatar URL
                    if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
                        // Load the existing avatar image
                        Glide.with(EditProfileActivity.this)
                                .load(oldAvatarUrl)
                                .placeholder(R.drawable.profile)
                                .into(avatarImageView);
                    }
                } else {
                    // User data does not exist
                    Log.d(TAG, "No such document");
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
     * Saves the updated user data to the repository.
     * Collects data from input fields and updates the user object.
     */
    private void saveUserData() {
        // Retrieve updated information from input fields
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String contactInfo = contactInfoEditText.getText().toString();
        String device = deviceEditText.getText().toString();

        // Create a new User object with the updated data
        User user = new User(firstName, lastName, email, contactInfo, device);
        user.setAvatarUrl(oldAvatarUrl);  // Set the avatar URL

        // Save the user data to the repository
        userRepository.saveUser(androidId, user, new UserRepository.OnUserSavedCallback() {
            @Override
            public void onUserSaved() {
                Log.d(TAG, "User data successfully updated!");
            }

            @Override
            public void onFailure(Exception e) {
                Log.w(TAG, "Error updating user data", e);
            }
        });
    }

    /**
     * Displays a popup menu when the user clicks the edit icon on the avatar.
     * Provides options to edit or remove the avatar.
     *
     * @param view The view that triggered the popup menu.
     */
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_edit_options, popupMenu.getMenu());  // Inflate the menu resource
        popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);      // Set the menu item click listener
        popupMenu.show();  // Show the popup menu
    }

    /**
     * Handles clicks on the popup menu items.
     *
     * @param item The selected menu item.
     * @return True if the event was handled, false otherwise.
     */
    private boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.option_edit) {
            // User chose to edit the avatar; open the file chooser
            openFileChooser();
            return true;
        } else if (itemId == R.id.option_remove) {
            // User chose to remove the avatar
            avatarImageView.setImageResource(R.drawable.profile);  // Set default avatar image
            userRepository.deleteAvatar(androidId, oldAvatarUrl, new UserRepository.OnAvatarDeletedCallback() {
                @Override
                public void onAvatarDeleted() {
                    Log.d(TAG, "Avatar removed");
                    oldAvatarUrl = null;  // Clear the avatar URL
                    Toast.makeText(EditProfileActivity.this, "Avatar removed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.w(TAG, "Error removing avatar", e);
                }
            });
            return true;
        } else {
            return false;
        }
    }

    /**
     * Opens a file chooser to allow the user to select an image for their avatar.
     */
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");                  // Specify that only images can be selected
        intent.setAction(Intent.ACTION_GET_CONTENT); // Open the content picker
        // Launch the file chooser and wait for the result
        fileChooserLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }
}
