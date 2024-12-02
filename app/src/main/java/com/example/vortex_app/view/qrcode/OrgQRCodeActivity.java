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

/**
 * OrgQRCodeActivity is responsible for generating and displaying a QR code based on the event ID passed from the previous activity.
 * It also allows the user to go back to the previous activity with the event ID.
 */
public class OrgQRCodeActivity extends AppCompatActivity {

    private static final String TAG = "OrgQRCodeActivity";  // Log tag for debugging

    /**
     * Initializes the activity by retrieving the event ID from the intent,
     * generating a QR code, and displaying it.
     * Also sets up a back button to return to the previous activity with the event ID.
     *
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_qrcode_activity);  // Set the layout for the activity

        // Retrieve the event ID passed from the Intent
        String eventID = getIntent().getStringExtra("EVENT_ID");

        // Check if the event ID is valid
        if (eventID == null || eventID.trim().isEmpty()) {
            Log.e(TAG, "No eventID passed. Cannot generate QR code.");
            Toast.makeText(this, "Invalid event ID. Cannot generate QR code.", Toast.LENGTH_SHORT).show();
            finish();  // Exit the activity if no event ID is provided
            return;
        }

        // Log the event ID for debugging purposes
        Log.d(TAG, "Received eventID: " + eventID);

        // Set the event ID in a TextView for display
        TextView qrTextView = findViewById(R.id.textView_qrHashValue);
        qrTextView.setText("Event ID: " + eventID);

        // Generate and display the QR code
        ImageView qrImageView = findViewById(R.id.imageView_qrCode);
        generateQRCode(eventID, qrImageView);

        // Back button functionality
        ImageView backButton = findViewById(R.id.imageViewBack);
        backButton.setOnClickListener(view -> {
            // Pass the event ID back to the previous activity
            Intent intent = new Intent();
            intent.putExtra("EVENT_ID", eventID);
            setResult(RESULT_OK, intent);  // Set the result with the event ID
            finish();  // Finish the current activity and go back
        });
    }

    /**
     * Generates a QR code for the given text and displays it in the provided ImageView.
     *
     * @param text        The text to encode in the QR code.
     * @param qrImageView The ImageView where the QR code will be displayed.
     */
    private void generateQRCode(String text, ImageView qrImageView) {
        try {
            // Create a BitMatrix for the QR code based on the input text
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 400, 400);

            // Convert the BitMatrix to a Bitmap to display as a QR code
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            // Set the generated Bitmap as the QR code in the ImageView
            qrImageView.setImageBitmap(bitmap);
            Log.d(TAG, "QR code generated successfully for: " + text);
        } catch (WriterException e) {
            // Log any error that occurs during QR code generation
            Log.e(TAG, "Error generating QR code: ", e);
            Toast.makeText(this, "Failed to generate QR code.", Toast.LENGTH_SHORT).show();
        }
    }
}
