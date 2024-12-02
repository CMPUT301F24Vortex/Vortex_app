package com.example.vortex_app.view.event;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.controller.adapter.EventListAdapter;
import com.example.vortex_app.model.Event;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


/**
 * Activity for managing events in the admin interface. It allows viewing events, deleting images,
 * removing QR codes, and deleting events from Firestore. It displays events for a specific facility.
 */
public class AdminEventScreen extends AppCompatActivity {

    private static final String TAG = "AdminEventScreen";

    private TextView facilityTitleTextView;
    private ListView eventListView;
    private ImageView backButton;  // Declare the back button

    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    private ArrayList<Event> eventDataList;
    private EventListAdapter eventListAdapter;


    /**
     * Initializes the activity, sets up the UI components, and loads events for the specified facility.
     * Sets up a click listener for each event and the back button.
     *
     * @param savedInstanceState The saved instance state if the activity is being re-initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_screen);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        // Initialize UI components
        facilityTitleTextView = findViewById(R.id.facilityTitleTextView);
        eventListView = findViewById(R.id.eventListView);
        backButton = findViewById(R.id.backButton);  // Initialize the back button

        // Get facility name from Intent
        String facilityName = getIntent().getStringExtra("FACILITY_NAME");
        if (facilityName == null || facilityName.isEmpty()) {
            Toast.makeText(this, "Facility name is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set facility title
        facilityTitleTextView.setText(facilityName);

        // Initialize Event List and Adapter
        eventDataList = new ArrayList<>();
        eventListAdapter = new EventListAdapter(this, eventDataList);
        eventListView.setAdapter(eventListAdapter);

        // Load events
        loadEventsForFacility(facilityName);

        // Set ListView click listener
        eventListView.setOnItemClickListener((parent, view, position, id) -> {
            Event selectedEvent = eventDataList.get(position);
            showOptionsDialog(selectedEvent);
        });

        // Set OnClickListener for the back button
        backButton.setOnClickListener(v -> {
            // Finish the current activity and go back to the previous screen
            finish();
        });
    }

    /**
     * Loads events from Firestore for the specified facility.
     */
    private void loadEventsForFacility(String facilityName) {
        Query query = eventsRef.whereEqualTo("facilityName", facilityName);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            eventDataList.clear();
            queryDocumentSnapshots.forEach(document -> {
                Event event = document.toObject(Event.class);
                event.setEventID(document.getId());
                eventDataList.add(event);
            });
            eventListAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to load events: ", e);
            Toast.makeText(this, "Failed to load events.", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Shows a dialog with options to delete image, remove QR code, or delete event.
     *
     * @param event The event to perform actions on.
     */
    private void showOptionsDialog(Event event) {
        String[] options = {"Delete Image", "Remove QR Code", "Delete Event"};
        new AlertDialog.Builder(this)
                .setTitle("Choose Action")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Option 1: Delete Image
                        deleteImage(event);
                    } else if (which == 1) {
                        // Option 2: Remove QR Code
                        removeQRCode(event);
                    } else if (which == 2) {
                        // Option 3: Delete Event
                        showDeleteConfirmationDialog(event);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Removes the QR code of the selected event from Firestore.
     *
     * @param event The event whose QR code needs to be removed.
     */
    private void removeQRCode(Event event) {
        db.collection("events").document(event.getEventID())
                .update("qrCodeHash", null) // Remove the qrCodeHash field
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "QR code removed successfully.", Toast.LENGTH_SHORT).show();
                    // Update the local event object
                    event.setQrCode(null);
                    // Optionally, refresh the UI by reloading the event list or notifying the adapter
                    eventListAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to remove QR code: ", e);
                    Toast.makeText(this, "Failed to remove QR code.", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Deletes the image URL of the selected event from Firestore.
     *
     * @param event The event whose image needs to be deleted.
     */
    private void deleteImage(Event event) {
        eventsRef.document(event.getEventID())
                .update("imageUrl", null) // Set the image URL to null
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Image deleted successfully.", Toast.LENGTH_SHORT).show();
                    // Update the local event list
                    event.setImageUrl(null);
                    eventListAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to delete image: ", e);
                    Toast.makeText(this, "Failed to delete image.", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Shows a confirmation dialog to delete the selected event.
     *
     * @param event The event to delete.
     */
    private void showDeleteConfirmationDialog(Event event) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteEvent(event);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Deletes the selected event from Firestore.
     *
     * @param event The event to delete.
     */
    private void deleteEvent(Event event) {
        eventsRef.document(event.getEventID())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event deleted successfully.", Toast.LENGTH_SHORT).show();
                    eventDataList.remove(event);
                    eventListAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to delete event: ", e);
                    Toast.makeText(this, "Failed to delete event.", Toast.LENGTH_SHORT).show();
                });
    }
}
