package com.example.vortex_app;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ListenerRegistration waitlistListener;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;





    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (waitlistListener != null) {
            waitlistListener.remove();
        }
        if (notificationListener != null) {
            notificationListener.remove();
        }
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mAuthListener = firebaseAuth -> {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser == null) {
                // User not authenticated; redirect to login
                Intent intent = new Intent(NotificationsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // User is authenticated
                setupNotifications();
            }
        };}


    private void setupNotifications(){

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(notificationList, this);
        recyclerView.setAdapter(adapter);


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


        // Initialize Firestore



        // Check and request notification permission
        checkAndRequestNotificationPermission();
        fetchNotifications();
        listenForWaitlistChanges();
        listenForNotifications();

        // Do not schedule the worker here; it will be called after permission is granted
    }

    private void listenForWaitlistChanges() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e("NotificationsActivity", "User not authenticated.");
            return;
        }
        String currentUserId = currentUser.getUid();

        DocumentReference waitlistedRef = db.collection("waitlisted").document(currentUserId);

        waitlistListener = waitlistedRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.w("NotificationsActivity", "Listen failed.", e);
                return;
            }

            if (snapshot != null && !snapshot.exists()) {
                // User is no longer waitlisted
                checkUserStatus(currentUserId);
            }
        });}


    private void checkUserStatus(String userId) {
        db.collection("selected").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        handleUserWin(userId, documentSnapshot);
                    } else {
                        db.collection("cancelled").document(userId).get()
                                .addOnSuccessListener(cancelledSnapshot -> {
                                    if (cancelledSnapshot.exists()) {
                                        handleUserLoss(userId, cancelledSnapshot);
                                    } else {
                                        Log.w("NotificationsActivity", "User not found in selected or cancelled collections.");
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> Log.e("NotificationsActivity", "Error checking user status", e));
    }

    private void handleUserWin(String userId, DocumentSnapshot documentSnapshot) {
        String eventId = documentSnapshot.getString("eventID");
        checkNotificationPreference(userId, () -> {
            String title = "Congratulations!";
            String message = "You've been selected for the event!";
            sendNotification(title, message);
            updateNotificationsCollection(userId, eventId, title, message);
        });
    }

    private void handleUserLoss(String userId, DocumentSnapshot documentSnapshot) {
        String eventId = documentSnapshot.getString("eventID");
        checkNotificationPreference(userId, () -> {
            String title = "Better Luck Next Time";
            String message = "You have not been selected for the event.";
            sendNotification(title, message);
            updateNotificationsCollection(userId, eventId, title, message);
        });
    }

    private void checkNotificationPreference(String userId, Runnable onPreferenceAllowed) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Boolean notificationsEnabled = documentSnapshot.getBoolean("notificationsEnabled");
                    if (notificationsEnabled == null || notificationsEnabled) {
                        onPreferenceAllowed.run();
                    } else {
                        Log.d("NotificationsActivity", "User has opted out of notifications.");
                    }
                })
                .addOnFailureListener(e -> Log.w("NotificationsActivity", "Failed to get user preferences.", e));
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
        // Get the current authenticated user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e("NotificationsActivity", "User not authenticated.");
            return;
        }
        String currentUserId = currentUser.getUid();

        // Set up a listener on the 'notifications' collection for the current user
        notificationListener = db.collection("notifications")
                .whereEqualTo("userID", currentUserId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        // Handle any errors
                        Log.w("NotificationsActivity", "Listen failed.", e);
                        return;
                    }

                    if (querySnapshot != null) {
                        // Loop through the document changes
                        for (DocumentChange dc : querySnapshot.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    // Deserialize the document into a NotificationModel object
                                    NotificationModel notification = dc.getDocument().toObject(NotificationModel.class);

                                    // Add the notification to the list and update the adapter
                                    notificationList.add(0, notification); // Add to the top of the list
                                    adapter.notifyItemInserted(0);

                                    // Send a local notification using the title and message from the notification
                                    sendNotification(notification.getTitle(), notification.getMessage());
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





    private void sendNotification(String title, String message) {
        createNotificationChannel();

        Intent intent = new Intent(this, NotificationDetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "status_channel_id")
                .setSmallIcon(R.drawable.notification) // Use your app's notification icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        int notificationId = (int) System.currentTimeMillis();

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


    private void updateNotificationsCollection(String userId, String eventId, String title, String message) {
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("eventID", eventId);
        notificationData.put("timestamp", new Date());
        notificationData.put("title", title);
        notificationData.put("message", message);
        notificationData.put("userID", userId);
        notificationData.put("status", "unread");

        db.collection("notifications").add(notificationData)
                .addOnSuccessListener(documentReference -> Log.d("NotificationsActivity", "Notification logged"))
                .addOnFailureListener(e -> Log.w("NotificationsActivity", "Error logging notification", e));
    }

    /**
     * Fetches notifications from Firestore and updates the {@link RecyclerView} with the retrieved data.
     * This method orders notifications by timestamp in descending order and refreshes the UI upon data retrieval.
     */
    private void fetchNotifications() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not logged in; handle appropriately
            Log.e("NotificationsActivity", "User not authenticated.");
            // Redirect to login screen
            Intent intent = new Intent(this, MainActivity.class); // Replace with your login activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
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