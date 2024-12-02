package com.example.vortex_app.model;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * UserRepository handles all user-related data operations,
 * including retrieving, saving, and updating user data in Firestore,
 * as well as uploading and deleting avatar images in Firebase Storage.
 */
public class UserRepository {
    private static final String TAG = "UserRepository";  // Tag for logging

    private FirebaseFirestore db;       // Firestore database instance
    private FirebaseStorage storage;    // Firebase Storage instance

    /**
     * Constructor initializes the Firestore and Firebase Storage instances.
     */
    public UserRepository() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    /**
     * Callback interface for when a user is loaded from Firestore.
     */
    public interface OnUserLoadedCallback {
        void onUserLoaded(User user);   // Called when user data is successfully loaded
        void onFailure(Exception e);    // Called when there is an error loading user data
    }

    /**
     * Retrieves a user document from Firestore based on the provided Android ID.
     *
     * @param androidId The unique identifier for the user's device.
     * @param callback  The callback to handle success or failure.
     */
    public void getUser(String androidId, OnUserLoadedCallback callback) {
        DocumentReference docRef = db.collection("user_profile").document(androidId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // User document exists; convert it to a User object
                    User user = document.toObject(User.class);
                    callback.onUserLoaded(user);
                } else {
                    // User document does not exist
                    callback.onUserLoaded(null);
                }
            } else {
                // Error occurred while fetching the document
                callback.onFailure(task.getException());
            }
        });
    }

    /**
     * Callback interface for when a user is saved to Firestore.
     */
    public interface OnUserSavedCallback {
        void onUserSaved();            // Called when user data is successfully saved
        void onFailure(Exception e);   // Called when there is an error saving user data
    }

    /**
     * Saves or updates a user document in Firestore with the provided user data.
     *
     * @param androidId The unique identifier for the user's device.
     * @param user      The User object containing user data to be saved.
     * @param callback  The callback to handle success or failure.
     */
    public void saveUser(String androidId, User user, OnUserSavedCallback callback) {
        DocumentReference docRef = db.collection("user_profile").document(androidId);
        docRef.set(user)
                .addOnSuccessListener(aVoid -> callback.onUserSaved())
                .addOnFailureListener(e -> callback.onFailure(e));
    }

    /**
     * Callback interface for when an avatar image is uploaded to Firebase Storage.
     */
    public interface OnAvatarUploadedCallback {
        void onAvatarUploaded(String downloadUrl);  // Called when avatar upload is successful
        void onFailure(Exception e);               // Called when there is an error uploading the avatar
    }

    /**
     * Uploads a new avatar image to Firebase Storage and updates the user's avatar URL in Firestore.
     * If an old avatar exists, it deletes the old avatar from storage.
     *
     * @param androidId    The unique identifier for the user's device.
     * @param imageUri     The URI of the new avatar image to upload.
     * @param oldAvatarUrl The URL of the old avatar image to delete (if any).
     * @param callback     The callback to handle success or failure.
     */
    public void uploadAvatar(String androidId, Uri imageUri, String oldAvatarUrl, OnAvatarUploadedCallback callback) {
        if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
            // Delete the old avatar image from Firebase Storage
            StorageReference oldAvatarRef = storage.getReferenceFromUrl(oldAvatarUrl);
            oldAvatarRef.delete()
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Old avatar successfully deleted"))
                    .addOnFailureListener(e -> Log.w(TAG, "Failed to delete old avatar", e));
        }

        // Create a reference for the new avatar image in Firebase Storage
        StorageReference storageRef = storage.getReference()
                .child("user_avatars/" + androidId + ".jpg");

        // Upload the new avatar image
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        // Get the download URL of the uploaded image
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            // Update the user's avatar URL in Firestore
                            DocumentReference docRef = db.collection("user_profile").document(androidId);
                            docRef.update("avatarUrl", downloadUrl)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "Avatar URL successfully updated in Firestore!");
                                        callback.onAvatarUploaded(downloadUrl);  // Invoke callback with the new URL
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w(TAG, "Error updating avatar URL in Firestore", e);
                                        callback.onFailure(e);
                                    });
                        }))
                .addOnFailureListener(e -> {
                    // Error occurred while uploading the image
                    Log.e(TAG, "Error uploading image to Firebase Storage", e);
                    callback.onFailure(e);
                });
    }

    /**
     * Callback interface for when an avatar image is deleted from Firebase Storage.
     */
    public interface OnAvatarDeletedCallback {
        void onAvatarDeleted();        // Called when avatar deletion is successful
        void onFailure(Exception e);   // Called when there is an error deleting the avatar
    }

    /**
     * Deletes the user's avatar image from Firebase Storage and removes the avatar URL from Firestore.
     *
     * @param androidId    The unique identifier for the user's device.
     * @param oldAvatarUrl The URL of the avatar image to delete (if any).
     * @param callback     The callback to handle success or failure.
     */
    public void deleteAvatar(String androidId, String oldAvatarUrl, OnAvatarDeletedCallback callback) {
        if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
            // Delete the old avatar image from Firebase Storage
            StorageReference oldAvatarRef = storage.getReferenceFromUrl(oldAvatarUrl);
            oldAvatarRef.delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Old avatar successfully deleted");
                        // Remove the avatar URL from Firestore
                        DocumentReference docRef = db.collection("user_profile").document(androidId);
                        docRef.update("avatarUrl", null)
                                .addOnSuccessListener(aVoid1 -> {
                                    Log.d(TAG, "Avatar URL removed from Firestore");
                                    callback.onAvatarDeleted();  // Invoke callback upon success
                                })
                                .addOnFailureListener(e -> {
                                    Log.w(TAG, "Error removing avatar URL from Firestore", e);
                                    callback.onFailure(e);
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Error occurred while deleting the old avatar
                        Log.w(TAG, "Failed to delete old avatar", e);
                        callback.onFailure(e);
                    });
        } else {
            // No old avatar to delete; just remove the avatar URL from Firestore
            DocumentReference docRef = db.collection("user_profile").document(androidId);
            docRef.update("avatarUrl", null)
                    .addOnSuccessListener(aVoid1 -> {
                        Log.d(TAG, "Avatar URL removed from Firestore");
                        callback.onAvatarDeleted();  // Invoke callback upon success
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error removing avatar URL from Firestore", e);
                        callback.onFailure(e);
                    });
        }
    }

    /**
     * Initializes default user data in Firestore when a new user is detected.
     *
     * @param androidId The unique identifier for the user's device.
     * @param callback  The callback to handle success or failure.
     */
    public void initializeUserData(String androidId, OnUserSavedCallback callback) {
        DocumentReference docRef = db.collection("user_profile").document(androidId);

        // Use androidId as device info
        String deviceInfo = androidId;

        // Create a default User object with placeholder data
        User defaultUser = new User(
                "Default First Name",
                "Default Last Name",
                "default@example.com",
                "123-456-7890",
                deviceInfo
        );

        // Save the default user data to Firestore
        docRef.set(defaultUser)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Default user created successfully");
                    callback.onUserSaved();  // Invoke callback upon success
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating default user", e);
                    callback.onFailure(e);
                });
    }
}