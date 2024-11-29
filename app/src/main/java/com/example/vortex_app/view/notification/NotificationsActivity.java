package com.example.vortex_app.view.notification;
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.vortex_app.R;
import com.example.vortex_app.controller.NotificationActionReceiver;
import com.example.vortex_app.model.NotificationModel;
import com.example.vortex_app.controller.adapter.NotificationAdapter;
import com.example.vortex_app.view.entrant.EntrantActivity;
import com.example.vortex_app.view.event.EventActivity;
import com.example.vortex_app.view.profile.ProfileActivity;
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


    private FirebaseAuth mAuth;


    // Define action strings
    public static final String ACTION_ACCEPT = "ACTION_ACCEPT";
    public static final String ACTION_DECLINE = "ACTION_DECLINE";
    public static final String ACTION_STAY = "ACTION_STAY";
    public static final String ACTION_LEAVE = "ACTION_LEAVE";


    private SwitchCompat switchNotifications;



    @Override protected void onStart() {
        super.onStart();

    }
    @Override protected void onStop(){
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

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(notificationList, this);
        recyclerView.setAdapter(adapter);

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_events);
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
                return true;
            } else if (itemId == R.id.nav_events) {
                startActivity(new Intent(this, EventActivity.class));
            }
            return false;
        });




        // Check and request notification permission
        checkAndRequestNotificationPermission();

        // Initialize the switch state based on Firestore data
        //initializeNotificationPreference();

        // Set a listener to handle toggle changes




        // Fetch existing notifications
        fetchNotifications();

        // Listen for new notifications in Firestore
        listenForNotifications();
    }


    /**
     * Updates the user's notification preference in Firestore.
     *
     * @param isEnabled Whether the user has enabled notifications.
     */
    private void updateNotificationPreference(boolean isEnabled) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Handle unauthenticated user
            Log.e("NotificationsActivity", "User not authenticated.");
            return;
        }

        String currentUserID = currentUser.getUid();

        db.collection("user_profile")
                .document(currentUserID)
                .update("notificationsEnabled", isEnabled)
                .addOnSuccessListener(aVoid -> {
                    if (isEnabled) {
                        Toast.makeText(this, "Notifications enabled.", Toast.LENGTH_SHORT).show();
                        // Re-initialize the listener
                        listenForNotifications();
                    } else {
                        Toast.makeText(this, "Notifications disabled.", Toast.LENGTH_SHORT).show();
                        // Remove existing notifications from device
                        clearLocalNotifications();
                        // Remove listener
                        if (notificationListener != null) {
                            notificationListener.remove();
                            notificationListener = null;
                        }
                        // Optionally, update Firestore to mark notifications as read or delete them
                        //clearFirestoreNotifications(currentUserID);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update notification preference.", Toast.LENGTH_SHORT).show();
                    Log.e("NotificationsActivity", "Error updating notification preference.", e);
                });
    }


    /**
     * Clears all local notifications from the device.
     */

    private void clearLocalNotifications() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }


    /**
     * Initializes the notification preference switch based on Firestore data.
     */
    private void initializeNotificationPreference() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Handle unauthenticated user
            Log.e("NotificationsActivity", "User not authenticated.");
            return;
        }

        String currentUserID = currentUser.getUid();

        db.collection("user_profile")
                .document(currentUserID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Boolean notificationsEnabled = documentSnapshot.getBoolean("notificationsEnabled");
                        if (notificationsEnabled != null) {
                            switchNotifications.setChecked(notificationsEnabled);
                        } else {
                            // Field doesn't exist, set default to true
                            switchNotifications.setChecked(true);
                            //Set default notification preference
                            Map<String, Object> defaultPref = new HashMap<>();
                            defaultPref.put("notificationsEnabled", true);
                            db.collection("user_profile")
                                    .document(currentUserID)
                                    .update(defaultPref)
                                    .addOnSuccessListener(aVoid -> Log.d("NotificationsActivity", "Default notification preference set."))
                                    .addOnFailureListener(e -> Log.e("NotificationsActivity", "Failed to set default notification preference.", e));
                        }
                    } else {
                        // User document doesn't exist, create it with default notification preference


                        Map<String, Object> userData = new HashMap<>();
                        userData.put("notificationsEnabled", true);
                        // Add other necessary fields as per your User class, e.g., firstName, lastName, etc.
                        // For example:
                        userData.put("firstName", currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "FirstName");
                        userData.put("lastName", "LastName"); // Replace with actual data if available
                        userData.put("email", currentUser.getEmail());

                        db.collection("user_profile")
                                .document(currentUserID)
                                .set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    switchNotifications.setChecked(true);
                                    Log.d("NotificationsActivity", "User profile created with default notification preference.");
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error creating user profile.", Toast.LENGTH_SHORT).show();
                                    Log.e("NotificationsActivity", "Error creating user profile.", e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching user data.", Toast.LENGTH_SHORT).show();
                    Log.e("NotificationsActivity", "Error fetching user data.", e);
                });

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Notification permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(this, "Notification permission denied. Notifications will not be displayed.", Toast.LENGTH_SHORT).show();
                // You may still proceed, but notifications won't be displayed
            }
        }
    }

    /**
     * Checks and requests notification permission if necessary (for Android 13+).
     */
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






    /**
     * Listens for new notifications in the 'notifications' collection and sends local notifications accordingly,
     * only if the user has enabled notifications.
     */
    private void listenForNotifications() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
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

                                    // Determine the notification type based on title or other fields
                                    String notificationType = "lost"; // Default
                                    if (notification.getTitle().contains("Congratulations")) {
                                        notificationType = "selected";
                                    }

                                    // Send a local notification using the title and message from the notification
                                    sendNotification(notification.getTitle(), notification.getMessage(),
                                            notification.getUserID(), notification.getEventID(), notificationType);
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



    /**
     * Sends a local notification with action buttons based on the notification type.
     *
     * @param title            The title of the notification.
     * @param message          The message/content of the notification.
     * @param userID           The ID of the user receiving the notification.
     * @param eventID          The ID of the associated event.
     * @param notificationType The type of notification ("selected" for winners, "lost" for non-winners).
     */
    private void sendNotification(String title, String message, String userID, String eventID, String notificationType) {
        createNotificationChannel();

        Intent intent = new Intent(this, NotificationDetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.putExtra("userID", userID); // Pass userID and eventID if needed in detail
        intent.putExtra("eventID", eventID);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Create action intents based on notification type
        PendingIntent actionIntent1 = null;
        PendingIntent actionIntent2 = null;
        String actionButton1 = "";
        String actionButton2 = "";

        if ("selected".equals(notificationType)) {
            // For winners: Accept and Decline
            Intent acceptIntent = new Intent(this, NotificationActionReceiver.class);
            acceptIntent.setAction(ACTION_ACCEPT);
            acceptIntent.putExtra("userID", userID);
            acceptIntent.putExtra("eventID", eventID);

            Intent declineIntent = new Intent(this, NotificationActionReceiver.class);
            declineIntent.setAction(ACTION_DECLINE);
            declineIntent.putExtra("userID", userID);
            declineIntent.putExtra("eventID", eventID);

            actionIntent1 = PendingIntent.getBroadcast(this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            actionIntent2 = PendingIntent.getBroadcast(this, 1, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            actionButton1 = "Accept";
            actionButton2 = "Decline";
        } else if ("lost".equals(notificationType)) {
            // For non-winners: Stay and Leave
            Intent stayIntent = new Intent(this, NotificationActionReceiver.class);
            stayIntent.setAction(ACTION_STAY);
            stayIntent.putExtra("userID", userID);
            stayIntent.putExtra("eventID", eventID);

            Intent leaveIntent = new Intent(this, NotificationActionReceiver.class);
            leaveIntent.setAction(ACTION_LEAVE);
            leaveIntent.putExtra("userID", userID);
            leaveIntent.putExtra("eventID", eventID);

            actionIntent1 = PendingIntent.getBroadcast(this, 2, stayIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            actionIntent2 = PendingIntent.getBroadcast(this, 3, leaveIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            actionButton1 = "Stay";
            actionButton2 = "Leave";
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "status_channel_id")
                .setSmallIcon(R.drawable.notification) // Use your app's notification icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Use high priority for action buttons
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Add action buttons if applicable
        if (actionIntent1 != null && actionIntent2 != null) {
            builder.addAction(R.drawable.ic_notifications, actionButton1, actionIntent1);
            builder.addAction(R.drawable.ic_notifications, actionButton2, actionIntent2);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        int notificationId = (int) System.currentTimeMillis();

        // Check notification permission for Android 13+
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
     * Creates a notification channel for Android Oreo and above.
     */
    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Status Updates";
            String description = "Notifications for status changes in events";
            int importance = NotificationManager.IMPORTANCE_HIGH; // Use high importance for action buttons
            NotificationChannel channel = new NotificationChannel("status_channel_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }}









    /**
     * Fetches notifications from Firestore and updates the {@link RecyclerView} with the retrieved data.
     * This method orders notifications by timestamp in descending order and refreshes the UI upon data retrieval.
     */
    private void fetchNotifications() {
        db.collection("notifications")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notificationList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String message = document.getString("message");
                            String status = document.getString("status");
                            Date Date = document.getDate("timestamp");
                            String id = document.getId();
                            String userID = document.getString("userID");
                            String eventID = document.getString("eventID");

                            NotificationModel notification = new NotificationModel(title, message, status, id, Date, eventID, userID);
                            notificationList.add(notification);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("Firestore", "Error getting notifications.", task.getException());
                    }
                });
    }

    /**
     * Handles marking a notification as read in Firestore.
     * Updates the status of a specific notification to "read" and refreshes the notification list.
     *
     * @param notification The {@link NotificationModel} object representing the notification to be marked as read.
     */
    private void handleNotificationClick(NotificationModel notification) {
        db.collection("notifications").document(notification.getId())
                .update("status", "read")
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Notification marked as read.");
                    fetchNotifications();
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error updating status", e));
    }

    /**
     * Opens the detailed view of a selected notification in {@link NotificationDetailActivity}.
     * Marks the notification as read in Firestore before transitioning to the detailed view.
     *
     * @param notification The {@link NotificationModel} object representing the notification to be opened in detail view.
     */
    private void openNotificationDetail(NotificationModel notification) {
        db.collection("notifications").document(notification.getId())
                .update("status", "read")
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Notification marked as read."))
                .addOnFailureListener(e -> Log.e("Firestore", "Error updating status", e));

        Intent intent = new Intent(this, NotificationDetailActivity.class);
        intent.putExtra("title", notification.getTitle());
        intent.putExtra("message", notification.getMessage());
        intent.putExtra("status", notification.getStatus());
        startActivity(intent);
    }

}
