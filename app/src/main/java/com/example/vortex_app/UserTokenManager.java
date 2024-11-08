package com.example.vortex_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * {@code UserTokenManager} handles the management of the Firebase Cloud Messaging (FCM) token and user ID.
 * This utility class retrieves the current user's ID and FCM token, storing them locally in
 * {@link android.content.SharedPreferences} for easy access.
 *
 * <p>This class is used to manage user-related tokens required for push notifications and user identification.
 */
public class UserTokenManager {

    private static final String PREFS_NAME = "UserPrefs";
    private static final String TOKEN_KEY = "fcmToken";
    private static final String USER_ID_KEY = "userID";
    private static final String TAG = "UserTokenManager";

    /**
     * Initializes and saves the user ID and FCM token locally in {@link SharedPreferences}.
     * Retrieves the current user's Firebase user ID, obtains the FCM token from {@link FirebaseMessaging},
     * and saves both locally for future use.
     *
     * @param context The {@link Context} used to access shared preferences.
     */
    public static void initializeUserToken(Context context) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid(); // Get user ID
            saveUserIDLocally(context, userID); // Save user ID locally

            // Retrieve FCM token and save it
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String token = task.getResult();
                    if (token != null) {
                        saveTokenLocally(context, token); // Save FCM token locally
                        Log.d(TAG, "FCM Token and User ID stored locally.");
                    }
                } else {
                    Log.e(TAG, "Failed to retrieve FCM Token", task.getException());
                }
            });
        } else {
            Log.e(TAG, "User not authenticated. Cannot retrieve userID.");
        }
    }

    /**
     * Saves the user ID locally in {@link SharedPreferences}.
     *
     * @param context The {@link Context} used to access shared preferences.
     * @param userID  The user ID to be saved.
     */
    private static void saveUserIDLocally(Context context, String userID) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(USER_ID_KEY, userID)
                .apply();
    }

    /**
     * Saves the FCM token locally in {@link SharedPreferences}.
     *
     * @param context The {@link Context} used to access shared preferences.
     * @param token   The FCM token to be saved.
     */
    private static void saveTokenLocally(Context context, String token) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(TOKEN_KEY, token)
                .apply();
    }

    /**
     * Retrieves the user ID stored in {@link SharedPreferences}.
     *
     * @param context The {@link Context} used to access shared preferences.
     * @return The stored user ID, or {@code null} if no user ID is found.
     */
    public static String getUserID(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(USER_ID_KEY, null);
    }

    /**
     * Retrieves the FCM token stored in {@link SharedPreferences}.
     *
     * @param context The {@link Context} used to access shared preferences.
     * @return The stored FCM token, or {@code null} if no token is found.
     */
    public static String getToken(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(TOKEN_KEY, null);
    }
}
