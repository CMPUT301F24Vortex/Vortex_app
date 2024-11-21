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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start the QR scanner
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // QR Code scanned successfully
                String scannedContent = result.getContents();

                // Navigate to EventInfoActivity
                Intent intent = new Intent(QrScannerActivity.this, EventInfoActivity.class);
                intent.putExtra("eventID", scannedContent); // Pass the scanned event ID
                startActivity(intent);
                finish();
            } else {
                // Scanning canceled
                Toast.makeText(this, "Scan canceled.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
