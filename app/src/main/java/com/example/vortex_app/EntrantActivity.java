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

public class EntrantActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CenterAdapter centerAdapter;
    private List<Center> centerList;
    private FirebaseFirestore db;

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
        centerAdapter = new CenterAdapter(centerList, new CenterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Center center) {
                // When an item is clicked, start EventActivity and pass the center's name
                Intent intent = new Intent(EntrantActivity.this, EventActivity.class);
                intent.putExtra("CENTER_NAME", center.getName());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(centerAdapter);

        // Bottom Navigation View Setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set a new navigation item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_events) {
                Intent intent = new Intent(this, EventsActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_notifications) {
                Intent intent = new Intent(this, NotificationsActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_home) {
                return true;
            }
            return false;
        });
    }

    // Method to add an invitation to Firestore
    public void addInvitation(String userId, String eventId, boolean accepted) {
        DocumentReference newInvitationRef = db.collection("invitations").document(); // Auto-generate an ID
        String invitationId = newInvitationRef.getId(); // Get the generated ID

        Map<String, Object> invitationData = new HashMap<>();
        invitationData.put("userId", userId);
        invitationData.put("eventId", eventId);
        invitationData.put("accepted", accepted);

        newInvitationRef.set(invitationData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Invitation added with ID: " + invitationId);
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding invitation", e);
                });
    }

    // Method to populate the list with data (this can be dynamic or static for now)
    private void loadCenterData() {
        centerList.add(new Center("Center 1333", "123 Main St"));
        centerList.add(new Center("Center 2", "456 Oak St"));
        centerList.add(new Center("Center 3", "789 Pine St"));
        centerList.add(new Center("Center 4", "101 Maple Ave"));
        centerList.add(new Center("Center 5", "202 Elm St"));
    }
}



