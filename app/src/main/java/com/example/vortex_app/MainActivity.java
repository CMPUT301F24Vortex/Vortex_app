package com.example.vortex_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;



import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vortex_app.NotificationHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.integration.android.IntentIntegrator;


public class MainActivity extends AppCompatActivity {


    private static final int NOTIFICATION_PERMISSION_CODE = 1;
    private static final String TAG = "MainActivity"; // Define TAG here
    //private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1 ;


    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_SIGNED_UP = "hasSignedUp";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize and store FCM token and userID locally
        UserTokenManager.initializeUserToken(this);



        // to get FCM token for client app
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get the new FCM registration token
                        String token = task.getResult();

                        // Log and show token
                        String msg = "FCM Token: " + token;
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                        // (Optional) Send this token to your server if needed
                        //sendTokenToServer(token);
                    }
                });

        // Create Notification Channel
        NotificationHelper.createNotificationChannel(this);

        // Request notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 or higher
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }

        // Get references to button objects on the screen
        Button buttonMainscreenEntrant = findViewById(R.id.button_mainscreen_entrant);
        Button buttonMainscreenOrganizer = findViewById(R.id.button_mainscreen_organizer);
        Button buttonMainscreenAdmin = findViewById(R.id.button_mainscreen_admin);

        // Initialize onClickListeners for the 3 buttons
        buttonMainscreenEntrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the user has already signed up
//                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//                boolean hasSignedUp = prefs.getBoolean(KEY_SIGNED_UP, false);
                Intent intent = new Intent(MainActivity.this, EntrantActivity.class);
                startActivity(intent);
            }
        });

        buttonMainscreenOrganizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to navigate to OrganizerActivity can be added here
                Intent intent = new Intent(MainActivity.this, OrganizerActivity.class);
                startActivity(intent);
            }
        });

        buttonMainscreenAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to navigate to AdminActivity can be added here

            }
        });



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
