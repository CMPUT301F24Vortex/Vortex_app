package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QrScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start the QR code scanning process
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false); // Allow orientation changes
        integrator.setBeepEnabled(true);       // Enable beep sound
        integrator.setPrompt("Scan a QR code"); // Add a prompt message
        integrator.setCaptureActivity(CustomScannerActivity.class); // Use CustomScannerActivity
        integrator.initiateScan();             // Start scanning
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the QR code scan result
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // QR code scanned successfully
                String scannedContent = result.getContents();
                Toast.makeText(this, "Scanned: " + scannedContent, Toast.LENGTH_LONG).show();

                // Navigate to another activity with the scanned content
                Intent intent = new Intent(this, EventInfoActivity.class);
                intent.putExtra("EVENT_ID", scannedContent);
                startActivity(intent);
                finish();
            } else {
                // Scan canceled
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
