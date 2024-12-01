package com.example.vortex_app.view.event;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.controller.adapter.EventAdapter;
import com.example.vortex_app.model.Event;
import com.example.vortex_app.view.entrant.EntrantActivity;
import com.example.vortex_app.view.notification.NotificationsActivity;
import com.example.vortex_app.view.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManageEventsActivity extends AppCompatActivity {

    private ListView selectedListView, waitlistedListView;
    private EventAdapter selectedEventAdapter, waitlistedEventAdapter;
    private List<String> selectedEventNames, selectedEventIDs, selectedEventImageUrls;
    private List<String> waitlistedEventNames, waitlistedEventIDs, waitlistedEventImageUrls;
    private FirebaseFirestore db;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_events);

        db = FirebaseFirestore.getInstance();
        currentUserID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        selectedListView = findViewById(R.id.list_view_selected_events);
        waitlistedListView = findViewById(R.id.list_view_waitlisted_events);

        selectedEventNames = new ArrayList<>();
        selectedEventIDs = new ArrayList<>();
        selectedEventImageUrls = new ArrayList<>();

        waitlistedEventNames = new ArrayList<>();
        waitlistedEventIDs = new ArrayList<>();
        waitlistedEventImageUrls = new ArrayList<>();

        selectedEventAdapter = new EventAdapter(this, selectedEventNames, selectedEventIDs, selectedEventImageUrls);
        waitlistedEventAdapter = new EventAdapter(this, waitlistedEventNames, waitlistedEventIDs, waitlistedEventImageUrls);

        selectedListView.setAdapter(selectedEventAdapter);
        waitlistedListView.setAdapter(waitlistedEventAdapter);

        fetchUserEventIDs(currentUserID);

        // Set up bottom navigation and handle item selection
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
                Intent intent = new Intent(this, NotificationsActivity.class);
                startActivity(intent);
                finish();
                return true;


            } else if (itemId == R.id.nav_events) {
                // Current activity; do nothing
                return true;
            }
            return false;
        });

        selectedListView.setOnItemClickListener((parent, view, position, id) -> {
            String eventID = selectedEventIDs.get(position);
            String eventName = selectedEventNames.get(position);

            Log.d("EventID_Log", "Selected Event ID: " + eventID);
            Log.d("EventID_Log", "Selected User ID: " + currentUserID);

            new AlertDialog.Builder(this)
                    .setTitle("Confirm Joining")
                    .setMessage("Do you want to join the event: " + eventName + "?")
                    .setPositiveButton("Confirm", (dialog, which) -> moveToFinalCollection(eventID, currentUserID))
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        waitlistedListView.setOnItemClickListener((parent, view, position, id) -> {
            String eventID = waitlistedEventIDs.get(position);
            String eventName = waitlistedEventNames.get(position);

            new AlertDialog.Builder(this)
                    .setTitle("Remove from Waitlist")
                    .setMessage("Do you want to remove yourself from the waitlist for: " + eventName + "?")
                    .setPositiveButton("Remove", (dialog, which) -> removeFromList(eventID, currentUserID, "waitlisted"))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void fetchUserEventIDs(String currentUserID) {
        fetchEventIDsFromCollection("selected_but_not_confirmed", currentUserID);
        fetchEventIDsFromCollection("waitlisted", currentUserID);
    }

    private void fetchEventIDsFromCollection(String collectionName, String currentUserID) {
        db.collection(collectionName)
                .whereEqualTo("userID", currentUserID)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    Set<String> eventIDs = new HashSet<>();
                    if (snapshots != null) {
                        for (DocumentSnapshot document : snapshots) {
                            String eventID = document.getString("eventID");
                            if (eventID != null) {
                                eventIDs.add(eventID);
                            }
                        }
                        loadEventDetails(eventIDs, collectionName);
                    }
                });
    }

    private void loadEventDetails(Set<String> allEventIDs, String collectionName) {
        List<String> eventNames = collectionName.equals("selected_but_not_confirmed") ? selectedEventNames : waitlistedEventNames;
        List<String> eventIDs = collectionName.equals("selected_but_not_confirmed") ? selectedEventIDs : waitlistedEventIDs;
        List<String> eventImageUrls = collectionName.equals("selected_but_not_confirmed") ? selectedEventImageUrls : waitlistedEventImageUrls;

        eventNames.clear();
        eventIDs.clear();
        eventImageUrls.clear();

        for (String eventID : allEventIDs) {
            fetchEventDetails(eventID, collectionName);
        }
    }

    private void fetchEventDetails(String eventID, String collectionName) {
        db.collection("events").document(eventID)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String eventName = documentSnapshot.getString("eventName");
                        String eventImageUrl = documentSnapshot.getString("imageUrl");
                        if (collectionName.equals("selected_but_not_confirmed")) {
                            selectedEventNames.add(eventName);
                            selectedEventIDs.add(eventID);
                            selectedEventImageUrls.add(eventImageUrl);
                            selectedEventAdapter.notifyDataSetChanged();
                        } else if (collectionName.equals("waitlisted")) {
                            waitlistedEventNames.add(eventName);
                            waitlistedEventIDs.add(eventID);
                            waitlistedEventImageUrls.add(eventImageUrl);
                            waitlistedEventAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void moveToFinalCollection(String eventID, String userID) {
        db.collection("selected_but_not_confirmed")
                .whereEqualTo("userID", userID)
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        db.collection("final")
                                .add(document.getData())
                                .addOnSuccessListener(documentReference -> {
                                    removeFromList(eventID, userID, "selected_but_not_confirmed");
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to add to final collection: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                });
    }

    private void removeFromList(String eventID, String currentUserID, String collectionName) {
        db.collection(collectionName)
                .whereEqualTo("userID", currentUserID)
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String documentID = task.getResult().getDocuments().get(0).getId();
                        db.collection(collectionName).document(documentID)
                                .delete()
                                .addOnCompleteListener(deleteTask -> {
                                    if (deleteTask.isSuccessful()) {
                                        if (collectionName.equals("selected_but_not_confirmed")) {
                                            selectedEventNames.remove(eventID);
                                            selectedEventIDs.remove(eventID);
                                            selectedEventImageUrls.remove(eventID);
                                            selectedEventAdapter.notifyDataSetChanged();
                                        } else if (collectionName.equals("waitlisted")) {
                                            waitlistedEventNames.remove(eventID);
                                            waitlistedEventIDs.remove(eventID);
                                            waitlistedEventImageUrls.remove(eventID);
                                            waitlistedEventAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                    }
                });
    }
}
