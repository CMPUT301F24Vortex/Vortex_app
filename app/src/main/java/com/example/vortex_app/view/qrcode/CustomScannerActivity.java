package com.example.vortex_app.view.qrcode;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.CaptureManager;

/**
 * CustomScannerActivity provides a custom QR code scanner interface.
 * It uses the ZXing barcode scanner library to decode QR codes.
 * The activity allows users to scan a QR code and navigate back via a back button.
 */
public class CustomScannerActivity extends AppCompatActivity {

    private CaptureManager captureManager; // Handles the scanning logic
    private DecoratedBarcodeView barcodeView; // Custom scanner view

    /**
     * Initializes the activity and sets up the custom QR code scanner.
     * Sets up the CaptureManager and barcode view, and also initializes the back button functionality.
     *
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_scanner_layout); // Use the custom layout

        // Initialize the barcode view
        barcodeView = findViewById(R.id.zxing_barcode_scanner);

        // Set up the CaptureManager
        captureManager = new CaptureManager(this, barcodeView);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();

        // Back button functionality
        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> finish()); // Close the scanner and go back
    }

    /**
     * Resumes the CaptureManager when the activity is resumed.
     * Ensures the barcode scanning functionality continues when the activity comes into view.
     */
    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    /**
     * Pauses the CaptureManager when the activity is paused.
     * Ensures that barcode scanning functionality is paused when the activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    /**
     * Destroys the CaptureManager when the activity is destroyed.
     * Ensures the cleanup of resources used by the CaptureManager to prevent memory leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }
}
