package com.example.vortex_app.model;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserRepository {
    private static final String TAG = "UserRepository";

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public interface OnUserLoadedCallback {
        void onUserLoaded(User user);
        void onFailure(Exception e);
    }

    public void getUser(String androidId, OnUserLoadedCallback callback) {
        DocumentReference docRef = db.collection("user_profile").document(androidId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    User user = document.toObject(User.class);
                    callback.onUserLoaded(user);
                } else {
                    callback.onUserLoaded(null);
                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    public interface OnUserSavedCallback {
        void onUserSaved();
        void onFailure(Exception e);
    }

    public void saveUser(String androidId, User user, OnUserSavedCallback callback) {
        DocumentReference docRef = db.collection("user_profile").document(androidId);
        docRef.set(user)
                .addOnSuccessListener(aVoid -> callback.onUserSaved())
                .addOnFailureListener(e -> callback.onFailure(e));
    }

    public interface OnAvatarUploadedCallback {
        void onAvatarUploaded(String downloadUrl);
        void onFailure(Exception e);
    }

    public void uploadAvatar(String androidId, Uri imageUri, String oldAvatarUrl, OnAvatarUploadedCallback callback) {
        if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
            StorageReference oldAvatarRef = storage.getReferenceFromUrl(oldAvatarUrl);
            oldAvatarRef.delete()
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Old avatar successfully deleted"))
                    .addOnFailureListener(e -> Log.w(TAG, "Failed to delete old avatar", e));
        }

        StorageReference storageRef = storage.getReference()
                .child("user_avatars/" + androidId + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            DocumentReference docRef = db.collection("user_profile").document(androidId);
                            docRef.update("avatarUrl", downloadUrl)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "Avatar URL successfully updated in Firestore!");
                                        callback.onAvatarUploaded(downloadUrl);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w(TAG, "Error updating avatar URL in Firestore", e);
                                        callback.onFailure(e);
                                    });
                        }))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error uploading image to Firebase Storage", e);
                    callback.onFailure(e);
                });
    }

    public interface OnAvatarDeletedCallback {
        void onAvatarDeleted();
        void onFailure(Exception e);
    }

    public void deleteAvatar(String androidId, String oldAvatarUrl, OnAvatarDeletedCallback callback) {
        if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
            StorageReference oldAvatarRef = storage.getReferenceFromUrl(oldAvatarUrl);
            oldAvatarRef.delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Old avatar successfully deleted");
                        DocumentReference docRef = db.collection("user_profile").document(androidId);
                        docRef.update("avatarUrl", null)
                                .addOnSuccessListener(aVoid1 -> {
                                    Log.d(TAG, "Avatar URL removed from Firestore");
                                    callback.onAvatarDeleted();
                                })
                                .addOnFailureListener(e -> {
                                    Log.w(TAG, "Error removing avatar URL from Firestore", e);
                                    callback.onFailure(e);
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Failed to delete old avatar", e);
                        callback.onFailure(e);
                    });
        } else {
            DocumentReference docRef = db.collection("user_profile").document(androidId);
            docRef.update("avatarUrl", null)
                    .addOnSuccessListener(aVoid1 -> {
                        Log.d(TAG, "Avatar URL removed from Firestore");
                        callback.onAvatarDeleted();
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error removing avatar URL from Firestore", e);
                        callback.onFailure(e);
                    });
        }
    }

    public void initializeUserData(String androidId, OnUserSavedCallback callback) {
        DocumentReference docRef = db.collection("user_profile").document(androidId);

        // Use androidId as device info
        String deviceInfo = androidId;

        User defaultUser = new User(
                "Default First Name",
                "Default Last Name",
                "default@example.com",
                "123-456-7890",
                deviceInfo
        );

        docRef.set(defaultUser)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Default user created successfully");
                    callback.onUserSaved();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating default user", e);
                    callback.onFailure(e);
                });
    }
}