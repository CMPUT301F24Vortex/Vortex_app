package com.example.vortex_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * {@code MainActivity} is the entry point for the Vortex app. It initializes Firebase services,
 * requests notification permissions if needed, and provides navigation options to different user roles:
 * Entrant, Organizer, and Admin. It also handles setting up notification channels.
 */
public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_CODE = 1;
    private static final String TAG = "MainActivity";

    /**
     * Initializes Firebase, configures the layout, requests notification permissions, and sets
     * up click listeners for the Entrant, Organizer, and Admin buttons.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize and store FCM token and userID locally
        UserTokenManager.initializeUserToken(this);

        // Retrieve FCM token for client app and log it
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }
            String token = task.getResult();
            Log.d(TAG, "FCM Token: " + token);
            Toast.makeText(MainActivity.this, "FCM Token: " + token, Toast.LENGTH_SHORT).show();
        });

        // Create notification channel for FCM notifications
        NotificationHelper.createNotificationChannel(this);

        // Request notification permission if on Android 13 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
        }

        // Set up navigation buttons for Entrant, Organizer, and Admin
        Button buttonMainscreenEntrant = findViewById(R.id.button_mainscreen_entrant);
        Button buttonMainscreenOrganizer = findViewById(R.id.button_mainscreen_organizer);
        Button buttonMainscreenAdmin = findViewById(R.id.button_mainscreen_admin);

        // Navigate to EntrantActivity
        buttonMainscreenEntrant.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EntrantActivity.class);
            startActivity(intent);
        });

        // Navigate to OrganizerActivity
        buttonMainscreenOrganizer.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, OrganizerActivity.class);
            startActivity(intent);
        });

        // Navigate to AdminActivity (add intent when AdminActivity is implemented)
        buttonMainscreenAdmin.setOnClickListener(view -> {
            // TODO: Add code to navigate to AdminActivity
        });
    }

    /**
     * Handles the result of permission requests, showing a toast message based on whether
     * notification permission was granted or denied.
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions.
     * @param grantResults The results for the requested permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
