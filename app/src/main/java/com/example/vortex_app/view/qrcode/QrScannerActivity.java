package com.example.vortex_app.view.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.view.event.EventInfoActivity;
import com.example.vortex_app.view.qrcode.CustomScannerActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * QrScannerActivity is responsible for scanning QR codes using the device's camera.
 * It initializes the QR scanner, handles the scan result, and navigates to the EventInfoActivity
 * with the scanned event ID.
 */
public class QrScannerActivity extends AppCompatActivity {

    /**
     * Initializes the activity by starting the QR code scanning process.
     * Configures the scanner to allow orientation changes, enable the beep sound,
     * and use a custom scanning activity.
     *
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start the QR code scanning process
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false); // Allow orientation changes
        integrator.setBeepEnabled(true);         // Enable beep sound
        integrator.setPrompt("Scan a QR code");  // Add a prompt message
        integrator.setCaptureActivity(CustomScannerActivity.class); // Use CustomScannerActivity
        integrator.initiateScan();               // Start scanning
    }

    /**
     * Handles the result of the QR code scan.
     * If the scan is successful, the scanned content is extracted, displayed in a Toast,
     * and passed to the EventInfoActivity. If the scan is canceled, a cancellation message is shown.
     *
     * @param requestCode The request code that identifies the activity result.
     * @param resultCode  The result code returned by the scanning activity.
     * @param data        The intent data containing the result of the scan.
     */
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

                // Navigate to the EventInfoActivity with the scanned event ID
                Intent intent = new Intent(this, EventInfoActivity.class);
                intent.putExtra("EVENT_ID", scannedContent);
                startActivity(intent);
                finish(); // Close the QrScannerActivity
            } else {
                // Scan canceled
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
