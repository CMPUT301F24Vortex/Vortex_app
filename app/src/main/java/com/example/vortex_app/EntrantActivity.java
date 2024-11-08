package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code EntrantActivity} is an {@link AppCompatActivity} that displays a list of community centers
 * and enables users to view events at each center by clicking on a center item. It also includes
 * functionality for adding invitations to Firestore and setting up a bottom navigation bar for navigation.
 */
public class EntrantActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CenterAdapter centerAdapter;
    private List<Center> centerList;
    private FirebaseFirestore db;

    /**
     * Called when the activity is first created. Initializes Firestore, sets up the layout, and configures
     * the RecyclerView and bottom navigation. Loads a list of centers to display in the RecyclerView.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_entrant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_centers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the data list and populate the data
        centerList = new ArrayList<>();
        loadCenterData();

        // Set up the RecyclerView adapter and handle click events
        centerAdapter = new CenterAdapter(centerList, center -> {
            // Start EventActivity and pass the center's name when an item is clicked
            Intent intent = new Intent(EntrantActivity.this, EventActivity.class);
            intent.putExtra("CENTER_NAME", center.getName());
            startActivity(intent);
        });
        recyclerView.setAdapter(centerAdapter);

        // Bottom Navigation View Setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set a new navigation item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (itemId == R.id.nav_events) {
                startActivity(new Intent(this, EventsActivity.class));
                return true;
            } else if (itemId == R.id.nav_notifications) {
                startActivity(new Intent(this, NotificationsActivity.class));
                return true;
            } else if (itemId == R.id.nav_home) {
                return true;
            }
            return false;
        });
    }

    /**
     * Adds an invitation document to Firestore with details about the user and event.
     *
     * @param userId   The ID of the user being invited.
     * @param eventId  The ID of the event to which the user is invited.
     * @param accepted Indicates whether the invitation is accepted or not.
     */
    public void addInvitation(String userId, String eventId, boolean accepted) {
        DocumentReference newInvitationRef = db.collection("invitations").document(); // Auto-generate an ID
        String invitationId = newInvitationRef.getId(); // Get the generated ID

        Map<String, Object> invitationData = new HashMap<>();
        invitationData.put("userId", userId);
        invitationData.put("eventId", eventId);
        invitationData.put("accepted", accepted);

        newInvitationRef.set(invitationData)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Invitation added with ID: " + invitationId))
                .addOnFailureListener(e -> Log.w("Firestore", "Error adding invitation", e));
    }

    /**
     * Populates the {@code centerList} with static data representing community centers.
     * This can be replaced with dynamic data from Firestore or another source in the future.
     */
    private void loadCenterData() {
        centerList.add(new Center("Center 1", "123 Main St"));
        centerList.add(new Center("Center 2", "456 Oak St"));
        centerList.add(new Center("Center 3", "789 Pine St"));
        centerList.add(new Center("Center 4", "101 Maple Ave"));
        centerList.add(new Center("Center 5", "202 Elm St"));
    }
}
