package com.example.vortex_app.view.entrant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.controller.adapter.EntrantEventAdapter;
import com.example.vortex_app.model.Event;
import com.example.vortex_app.R;
import com.example.vortex_app.view.event.EventInfoActivity;
import com.example.vortex_app.view.event.ManageEventsActivity;
import com.example.vortex_app.view.notification.NotificationsActivity;
import com.example.vortex_app.view.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class EntrantActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private RecyclerView recyclerView;
    private EntrantEventAdapter eventAdapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrant_new);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load events
        eventList = new ArrayList<>();
        loadEventData();
        eventAdapter = new EntrantEventAdapter(this, eventList);
        recyclerView.setAdapter(eventAdapter);

        // Scan QR Code Button
        Button scanQrButton = findViewById(R.id.button_scan_qr);
        scanQrButton.setOnClickListener(v -> checkCameraPermission());

        // Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (itemId == R.id.nav_events) {
                startActivity(new Intent(this, ManageEventsActivity.class));
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

    // Check and request camera permissions
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startQRCodeScanner();
        }
    }

    // Handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startQRCodeScanner();
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Start QR code scanner
    private void startQRCodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(true);
        integrator.setPrompt("Scan a QR code");
        integrator.initiateScan(); // Default ZXing scanner
    }

    // Handle scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedContent = result.getContents();
                Toast.makeText(this, "Scanned: " + scannedContent, Toast.LENGTH_LONG).show();

                // Navigate to EventInfoActivity with scanned data
                Intent intent = new Intent(this, EventInfoActivity.class);
                intent.putExtra("EVENT_ID", scannedContent);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No scan result!", Toast.LENGTH_SHORT).show();
        }
    }

    // Load sample event data
    private void loadEventData() {
        eventList.add(new Event("Class Name 1", "Difficulty: Beginner"));
        eventList.add(new Event("Class Name 2", "Difficulty: Intermediate"));
    }
}