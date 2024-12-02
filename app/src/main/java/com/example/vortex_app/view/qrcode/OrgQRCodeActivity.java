
package com.example.vortex_app.view.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class OrgQRCodeActivity extends AppCompatActivity {

    private static final String TAG = "OrgQRCodeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_qrcode_activity);

        // Retrieve the eventID passed from the Intent
        String eventID = getIntent().getStringExtra("EVENT_ID");

        // Check if the eventID is null or empty
        if (eventID == null || eventID.trim().isEmpty()) {
            Log.e(TAG, "No eventID passed. Cannot generate QR code.");
            Toast.makeText(this, "Invalid event ID. Cannot generate QR code.", Toast.LENGTH_SHORT).show();
            finish(); // Exit the activity if no eventID is provided
            return;
        }

        // Debugging: Log the received eventID
        Log.d(TAG, "Received eventID: " + eventID);

        // Set the eventID in a TextView for display
        TextView qrTextView = findViewById(R.id.textView_qrHashValue);
        qrTextView.setText("Event ID: " + eventID);

        // Generate the QR code using the eventID
        ImageView qrImageView = findViewById(R.id.imageView_qrCode);
        generateQRCode(eventID, qrImageView);

        // Back Button Functionality
        ImageView backButton = findViewById(R.id.imageViewBack);
        backButton.setOnClickListener(view -> {
            // Create an intent and add the event ID
            Intent intent = new Intent();
            intent.putExtra("EVENT_ID", eventID);
            setResult(RESULT_OK, intent); // Pass result back to the calling activity
            finish(); // Finish this activity and go back
        });
    }

    /**
     * Generates a QR code for the given text and displays it in the provided ImageView.
     *
     * @param text        The text to encode in the QR code.
     * @param qrImageView The ImageView to display the QR code.
     */
    private void generateQRCode(String text, ImageView qrImageView) {
        try {
            // Generate a BitMatrix for the QR code
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 400, 400);

            // Convert the BitMatrix to a Bitmap
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            // Set the Bitmap in the ImageView
            qrImageView.setImageBitmap(bitmap);
            Log.d(TAG, "QR code generated successfully for: " + text);
        } catch (WriterException e) {
            Log.e(TAG, "Error generating QR code: ", e);
            Toast.makeText(this, "Failed to generate QR code.", Toast.LENGTH_SHORT).show();
        }
    }
}
