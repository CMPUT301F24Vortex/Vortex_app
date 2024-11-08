package com.example.vortex_app;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * OrgQRCodeActivity generates and displays a QR code for a given event ID.
 * It uses the ZXing library to create the QR code and displays it in an ImageView.
 */
public class OrgQRCodeActivity extends AppCompatActivity {

    private String eventID;

    /**
     * Called when the activity is created. Sets up the layout, retrieves the event ID,
     * generates a QR code, and displays it on the screen.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.org_qrcode_activity);

        // Retrieve the event ID (hardcoded for now, can be modified to retrieve from intent)
        // Example: eventID = getIntent().getStringExtra("EVENTID");
        eventID = "121";

        // Get references to UI elements
        TextView qrHashValue = findViewById(R.id.textView_qrHashValue);
        ImageView qrImage = findViewById(R.id.imageView_qrCode);

        // Generate QR code
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(eventID, BarcodeFormat.QR_CODE, 400, 400);
        } catch (WriterException e) {
            e.printStackTrace();
            return;
        }

        // Convert the BitMatrix to a Bitmap
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
            }
        }

        // Set the ImageView to display the QR code
        qrImage.setImageBitmap(bitmap);

        // Display the event ID as a hash value
        qrHashValue.setText(eventID);
    }
}
