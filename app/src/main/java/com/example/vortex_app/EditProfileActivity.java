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

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private EditText firstNameEditText, lastNameEditText, emailEditText, contactInfoEditText, deviceEditText;
    private ImageView avatarImageView, editAvatarIcon;
    private Button saveButton;

    private Uri imageUri;
    private String oldAvatarUrl;

    // Static User ID
    private static final String USER_ID = "020422";

    private final ActivityResultLauncher<Intent> fileChooserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    avatarImageView.setImageURI(imageUri);

                    // Upload the new avatar to Firebase Storage
                    uploadAvatar();
                }
            });

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
        avatarImageView = findViewById(R.id.avatarImageView);
        editAvatarIcon = findViewById(R.id.editAvatarIcon);
        saveButton = findViewById(R.id.saveButton);

        // Load user data from Firestore
        loadUserData();

        // Save user data on button click
        saveButton.setOnClickListener(v -> saveUserData());

        // Open avatar editing options
        editAvatarIcon.setOnClickListener(v -> showPopupMenu(v));
    }

    private void loadUserData() {
        DocumentReference docRef = db.collection("user_profile").document(USER_ID);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Map<String, Object> document = task.getResult().getData();
                if (document != null) {
                    // Load user data into the fields
                    firstNameEditText.setText((String) document.getOrDefault("firstName", ""));
                    lastNameEditText.setText((String) document.getOrDefault("lastName", ""));
                    emailEditText.setText((String) document.getOrDefault("email", ""));
                    contactInfoEditText.setText((String) document.getOrDefault("contactInfo", ""));
                    deviceEditText.setText((String) document.getOrDefault("device", ""));

                    oldAvatarUrl = (String) document.get("avatarUrl");
                    if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
                        Glide.with(this)
                                .load(oldAvatarUrl)
                                .placeholder(R.drawable.profile)
                                .into(avatarImageView);
                    }
                } else {
                    Log.d(TAG, "No document found.");
                }
            } else {
                Log.e(TAG, "Error fetching user data", task.getException());
            }
        });
    }

    private void saveUserData() {
        DocumentReference docRef = db.collection("user_profile").document(USER_ID);

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("firstName", firstNameEditText.getText().toString());
        updatedData.put("lastName", lastNameEditText.getText().toString());
        updatedData.put("email", emailEditText.getText().toString());
        updatedData.put("contactInfo", contactInfoEditText.getText().toString());
        updatedData.put("device", deviceEditText.getText().toString());

        docRef.update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    // Navigate back to ProfileActivity
                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating profile", e);
                    Toast.makeText(this, "Error updating profile", Toast.LENGTH_SHORT).show();
                });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_edit_options, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
        popupMenu.show();
    }

    private boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.option_edit) {
            openFileChooser();
            return true;
        } else if (itemId == R.id.option_remove) {
            removeAvatar();
            return true;
        } else {
            return false;
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fileChooserLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void uploadAvatar() {
        if (imageUri == null) return;

        // Delete old avatar if it exists
        if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
            StorageReference oldAvatarRef = storage.getReferenceFromUrl(oldAvatarUrl);
            oldAvatarRef.delete().addOnSuccessListener(aVoid ->
                            Log.d(TAG, "Old avatar successfully deleted"))
                    .addOnFailureListener(e ->
                            Log.w(TAG, "Failed to delete old avatar", e));
        }

        // Upload new avatar
        StorageReference storageRef = storage.getReference().child("user_avatars/" + USER_ID + ".jpg");
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    db.collection("user_profile").document(USER_ID)
                            .update("avatarUrl", downloadUrl)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Avatar URL updated in Firestore"))
                            .addOnFailureListener(e -> Log.w(TAG, "Error updating avatar URL in Firestore", e));
                    Toast.makeText(this, "Avatar uploaded successfully!", Toast.LENGTH_SHORT).show();
                }))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error uploading avatar", e);
                    Toast.makeText(this, "Failed to upload avatar. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }

    private void removeAvatar() {
        avatarImageView.setImageResource(R.drawable.profile);
        db.collection("user_profile").document(USER_ID)
                .update("avatarUrl", null)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Avatar URL removed from Firestore");
                    Toast.makeText(this, "Avatar removed", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error removing avatar URL", e));
    }
}
