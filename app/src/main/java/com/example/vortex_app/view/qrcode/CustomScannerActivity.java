package com.example.vortex_app.view.qrcode;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.CaptureManager;

public class CustomScannerActivity extends AppCompatActivity {

    private CaptureManager captureManager; // Handles the scanning logic
    private DecoratedBarcodeView barcodeView; // Custom scanner view

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

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }
}
