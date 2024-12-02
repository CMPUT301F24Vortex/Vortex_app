package com.example.vortex_app.view.image;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.controller.adapter.ImageListAdapter;
import com.example.vortex_app.model.Event;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminImageScreen extends AppCompatActivity {

    private static final String TAG = "AdminImageScreen";

    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    private ListView imageListView;
    private ArrayList<Event> eventDataList;
    private ImageListAdapter imageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_image_screen);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        // Initialize UI components
        imageListView = findViewById(R.id.imageListView);

        // Initialize Event List and Adapter
        eventDataList = new ArrayList<>();
        imageListAdapter = new ImageListAdapter(this, eventDataList);
        imageListView.setAdapter(imageListAdapter);

        // Load all events
        loadEventImages();

        // Set ListView click listener
        imageListView.setOnItemClickListener((parent, view, position, id) -> {
            Event selectedEvent = eventDataList.get(position);
            showDeleteImageConfirmationDialog(selectedEvent);
        });
    }

    /**
     * Loads event images (URLs) from Firestore.
     */
    private void loadEventImages() {
        eventsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            eventDataList.clear();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Event event = doc.toObject(Event.class);
                event.setEventID(doc.getId());
                eventDataList.add(event);
            }
            imageListAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to load event images: ", e);
            Toast.makeText(this, "Failed to load event images.", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Shows a confirmation dialog to delete the image URL of the selected event.
     *
     * @param event The event whose image URL is to be deleted.
     */
    private void showDeleteImageConfirmationDialog(Event event) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage("Are you sure you want to delete the image for the event \"" + event.getName() + "\"?")
                .setPositiveButton("Yes", (dialog, which) -> deleteEventImage(event))
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Deletes the image URL of the selected event in Firestore.
     *
     * @param event The event whose image URL is to be deleted.
     */
    private void deleteEventImage(Event event) {
        eventsRef.document(event.getEventID())
                .update("imageUrl", null)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Image URL deleted successfully.", Toast.LENGTH_SHORT).show();
                    loadEventImages(); // Reload the list to reflect changes
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to delete image URL: ", e);
                    Toast.makeText(this, "Failed to delete image URL.", Toast.LENGTH_SHORT).show();
                });
    }
}
