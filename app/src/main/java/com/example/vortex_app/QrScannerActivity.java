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

        // Start the QR scanner
        new IntentIntegrator(this)
                .setOrientationLocked(false) // Allows orientation changes
                .setBeepEnabled(true)       // Enable beep sound on scan success
                .setPrompt("Scan a QR Code") // Display a prompt text
                .initiateScan();            // Start the scanning process
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Process the scan result
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // QR code scanned successfully
                String scannedContent = result.getContents();
                Toast.makeText(this, "Scanned: " + scannedContent, Toast.LENGTH_LONG).show();

                // Optionally, navigate to another activity with the scanned content
                Intent intent = new Intent(QrScannerActivity.this, EventInfoActivity.class);
                intent.putExtra("EVENT_ID", scannedContent);
                startActivity(intent);
                finish();
            } else {
                // User cancelled the scan
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
