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

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";

    private EditText firstNameEditText, lastNameEditText, emailEditText, contactInfoEditText, deviceEditText;
    private Button saveButton;
    private ImageView editIcon, avatarImageView;
    private Uri imageUri;
    private String oldAvatarUrl;
    private String androidId;

    private UserRepository userRepository;

    private final ActivityResultLauncher<Intent> fileChooserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    avatarImageView.setImageURI(imageUri);

                    if (imageUri != null) {
                        userRepository.uploadAvatar(androidId, imageUri, oldAvatarUrl, new UserRepository.OnAvatarUploadedCallback() {
                            @Override
                            public void onAvatarUploaded(String downloadUrl) {
                                Log.d(TAG, "Avatar uploaded successfully!");
                                Toast.makeText(EditProfileActivity.this, "Avatar uploaded successfully!", Toast.LENGTH_SHORT).show();
                                oldAvatarUrl = downloadUrl;
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

        userRepository = new UserRepository();
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

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

    private void loadUserData() {
        userRepository.getUser(androidId, new UserRepository.OnUserLoadedCallback() {
            @Override
            public void onUserLoaded(User user) {
                if (user != null) {
                    firstNameEditText.setText(user.getFirstName());
                    lastNameEditText.setText(user.getLastName());
                    emailEditText.setText(user.getEmail());
                    contactInfoEditText.setText(user.getContactInfo());
                    deviceEditText.setText(user.getDevice()); // Should be androidId now

                    oldAvatarUrl = user.getAvatarUrl();
                    if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
                        Glide.with(EditProfileActivity.this)
                                .load(oldAvatarUrl)
                                .placeholder(R.drawable.profile)
                                .into(avatarImageView);
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Failed to load user data", e);
            }
        });
    }

    private void saveUserData() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String contactInfo = contactInfoEditText.getText().toString();
        String device = deviceEditText.getText().toString();

        User user = new User(firstName, lastName, email, contactInfo, device);
        user.setAvatarUrl(oldAvatarUrl);

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
            avatarImageView.setImageResource(R.drawable.profile);
            userRepository.deleteAvatar(androidId, oldAvatarUrl, new UserRepository.OnAvatarDeletedCallback() {
                @Override
                public void onAvatarDeleted() {
                    Log.d(TAG, "Avatar removed");
                    oldAvatarUrl = null;
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

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fileChooserLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }
}