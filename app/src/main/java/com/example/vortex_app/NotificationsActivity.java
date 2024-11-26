package com.example.vortex_app;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * {@code NotificationsActivity} displays a list of notifications for the user.
 * This activity retrieves notification data from Firestore, organizes it in a {@link RecyclerView},
 * and supports user interactions for marking notifications as read and navigating to detailed views.
 *
 * <p>This activity is accessible via the app's bottom navigation bar, enabling users to view
 * and manage notifications related to events or updates within the application.
 */
public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationModel> notificationList = new ArrayList<>();
    private FirebaseFirestore db;
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1001;
    private ListenerRegistration notificationListener;




    @Override
    protected void onStart() {
        super.onStart();
        listenForNotifications();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (notificationListener != null) {
            notificationListener.remove();
        }
    }






    /**
     * Called when the activity is first created.
     * Sets up the bottom navigation, initializes the RecyclerView for displaying notifications,
     * and loads notifications from Firestore.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        // Check if user is authenticated

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // User not authenticated; redirect to login
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_notifications);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, EntrantActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_notifications) {
                startActivity(new Intent(this, NotificationsActivity.class));
                return true;
            } else if (itemId == R.id.nav_events) {
                startActivity(new Intent(this, EventActivity.class));
                return true;
            }
            return false;
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(notificationList, this);
        recyclerView.setAdapter(adapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();


        // Check and request notification permission
        checkAndRequestNotificationPermission();
        fetchNotifications();

        // Do not schedule the worker here; it will be called after permission is granted
    }








    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 or higher
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                    // Show an explanation to the user
                    new AlertDialog.Builder(this)
                            .setTitle("Notification Permission Needed")
                            .setMessage("This app requires notification permission to inform you about important updates.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                ActivityCompat.requestPermissions(this,
                                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                                        REQUEST_CODE_POST_NOTIFICATIONS);
                            })
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show();
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            REQUEST_CODE_POST_NOTIFICATIONS);
                }
            } else {
                // Permission already granted
                // No action needed
            }
        } else {
            // Permission is automatically granted on SDK versions below 33
            // No action needed
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                // We can proceed to listen for notifications
            } else {
                // Permission denied
                Toast.makeText(this, "Notification permission denied. Notifications will not be displayed.", Toast.LENGTH_SHORT).show();
                // You may still proceed, but notifications won't be displayed
            }
        }
    }

    private void listenForNotifications() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.e("NotificationsActivity", "User not authenticated.");
            return;
        }
        String currentUserId = currentUser.getUid();

        notificationListener = db.collection("notifications")
                .whereEqualTo("userID", currentUserId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        // Handle error
                        return;
                    }

                    if (querySnapshot != null) {
                        for (DocumentChange dc : querySnapshot.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    NotificationModel notification = dc.getDocument().toObject(NotificationModel.class);
                                    notificationList.add(0, notification); // Add to the top
                                    adapter.notifyItemInserted(0);
                                    sendNotification(notification);
                                    break;
                                case MODIFIED:
                                    // Handle modified notifications if necessary
                                    break;
                                case REMOVED:
                                    // Handle removed notifications if necessary
                                    break;
                            }
                        }
                    }
                });
    }

    private void sendNotification(NotificationModel notification) {


        createNotificationChannel();

        Intent intent = new Intent(this, NotificationDetailActivity.class);
        intent.putExtra("eventID", NotificationModel.getEventID());
        intent.putExtra("title", NotificationModel.getTitle());
        intent.putExtra("message", NotificationModel.getMessage());
        intent.putExtra("status", NotificationModel.getStatus());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "status_channel_id")
                .setSmallIcon(R.drawable.notification) // Use your app's notification icon
                .setContentTitle(NotificationModel.getTitle())
                .setContentText(NotificationModel.getMessage())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        int notificationId = NotificationModel.getId().hashCode();

        // Check if permission is granted (for Android 13 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(notificationId, builder.build());
            } else {
                Log.w("NotificationsActivity", "POST_NOTIFICATIONS permission not granted, cannot display notification.");
            }
        } else {
            notificationManager.notify(notificationId, builder.build());
        }



    }

    /**
     * Fetches notifications from Firestore and updates the {@link RecyclerView} with the retrieved data.
     * This method orders notifications by timestamp in descending order and refreshes the UI upon data retrieval.
     */
    private void fetchNotifications() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // User is not logged in; handle appropriately
            Log.e("NotificationsActivity", "User not authenticated.");
            // Option 1: Redirect to login screen
            Intent intent = new Intent(this, MainActivity.class); // Replace with your login activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            // Option 2: Show a message and finish activity
            // Toast.makeText(this, "Please log in to view notifications.", Toast.LENGTH_SHORT).show();
            // finish();
            return;
        }

        String currentUserID = currentUser.getUid();

        db.collection("notifications")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereEqualTo("userID", currentUserID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notificationList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            NotificationModel notification = document.toObject(NotificationModel.class);
                            //notificationList.add(notification);
                            notificationList.add(notification);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("Firestore", "Error getting notifications.", task.getException());
                    }
                });
    }


    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Status Updates";
            String description = "Notifications for status changes in events";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("status_channel_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }




}