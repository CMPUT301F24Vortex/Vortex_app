package com.example.vortex_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;

/**
 * EditProfileActivity allows users to edit their profile details, including their avatar.
 * Data is stored and retrieved from Firebase Firestore and Firebase Storage.
 */
public class EditProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private static final String TAG = "EditProfileActivity";

    private EditText firstNameEditText, lastNameEditText, emailEditText, contactInfoEditText, deviceEditText;
    private Button saveButton;
    private ImageView editIcon, avatarImageView;
    private Uri imageUri;
    private String oldAvatarUrl;

    // Launcher for file chooser intent
    private final ActivityResultLauncher<Intent> fileChooserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    avatarImageView.setImageURI(imageUri);

                    // Upload new avatar and update Firestore
                    if (imageUri != null) {
                        if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
                            StorageReference oldAvatarRef = storage.getReferenceFromUrl(oldAvatarUrl);
                            oldAvatarRef.delete().addOnSuccessListener(aVoid ->
                                            Log.d(TAG, "Old avatar successfully deleted"))
                                    .addOnFailureListener(e ->
                                            Log.w(TAG, "Failed to delete old avatar", e));
                        }

                        StorageReference storageRef = storage.getReference()
                                .child("user_avatars/XKcYtstm0zrzcdIgvLwb.jpg");

                        storageRef.putFile(imageUri)
                                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String downloadUrl = uri.toString();
                                    DocumentReference docRef = db.collection("user_profile").document("XKcYtstm0zrzcdIgvLwb");
                                    docRef.update("avatarUrl", downloadUrl)
                                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Avatar URL successfully updated in Firestore!"))
                                            .addOnFailureListener(e -> Log.w(TAG, "Error updating avatar URL in Firestore", e));
                                    Toast.makeText(EditProfileActivity.this, "Avatar uploaded successfully!", Toast.LENGTH_SHORT).show();
                                }))
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error uploading image to Firebase Storage", e);
                                    Toast.makeText(EditProfileActivity.this, "Failed to upload avatar. Please try again.", Toast.LENGTH_SHORT).show();
                                });
                    }
                }
            });

    /**
     * Called when the activity is created. Initializes Firebase instances,
     * UI components, and loads user data.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Initialize UI components
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        contactInfoEditText = findViewById(R.id.contactInfoEditText);
        deviceEditText = findViewById(R.id.deviceEditText);
        saveButton = findViewById(R.id.saveButton);
        editIcon = findViewById(R.id.editAvatarIcon);
        avatarImageView = findViewById(R.id.avatarImageView);

        loadUserData();

        saveButton.setOnClickListener(view -> {
            saveUserData();
            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });

        editIcon.setOnClickListener(v -> {
            editIcon.setVisibility(View.GONE);
            showPopupMenu(v);
        });
    }

    /**
     * Loads user data from Firestore and populates the UI fields.
     */
    private void loadUserData() {
        DocumentReference docRef = db.collection("user_profile").document("XKcYtstm0zrzcdIgvLwb");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    firstNameEditText.setText(document.getString("firstName"));
                    lastNameEditText.setText(document.getString("lastName"));
                    emailEditText.setText(document.getString("email"));
                    contactInfoEditText.setText(document.getString("contactInfo"));
                    deviceEditText.setText(document.getString("device"));

                    oldAvatarUrl = document.getString("avatarUrl");
                    if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
                        Glide.with(this)
                                .load(oldAvatarUrl)
                                .placeholder(R.drawable.profile)
                                .into(avatarImageView);
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    /**
     * Saves updated user data to Firestore.
     */
    private void saveUserData() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String contactInfo = contactInfoEditText.getText().toString();
        String device = deviceEditText.getText().toString();

        DocumentReference docRef = db.collection("user_profile").document("XKcYtstm0zrzcdIgvLwb");
        docRef.update("firstName", firstName,
                        "lastName", lastName,
                        "email", email,
                        "contactInfo", contactInfo,
                        "device", device)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    /**
     * Displays a popup menu with edit options for the avatar.
     *
     * @param view The view that triggered the popup menu.
     */
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_edit_options, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
        popupMenu.show();
    }

    /**
     * Handles item selection from the popup menu.
     *
     * @param item The selected menu item.
     * @return True if the event was handled, false otherwise.
     */
    private boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.option_edit) {
            openFileChooser();
            return true;
        } else if (itemId == R.id.option_remove) {
            avatarImageView.setImageResource(R.drawable.profile);
            DocumentReference docRef = db.collection("user_profile").document("XKcYtstm0zrzcdIgvLwb");
            docRef.update("avatarUrl", null)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Avatar URL removed from Firestore"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error removing avatar URL from Firestore", e));
            Toast.makeText(this, "Avatar removed", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Opens a file chooser to select an image for the avatar.
     */
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fileChooserLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }
}
