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
 * {@code OrgQRCodeActivity} is an {@link AppCompatActivity} that generates and displays a QR code for a given event ID.
 * The QR code encodes the event ID, which can be scanned for quick access to event information.
 */
public class OrgQRCodeActivity extends AppCompatActivity {

    private String eventID;

    /**
     * Called when the activity is created. Sets up the layout, retrieves the event ID, generates a QR code bitmap,
     * and displays the QR code in an {@link ImageView}.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.org_qrcode_activity);

        // Retrieve event ID from intent
        eventID = "121"; // Temporary placeholder, should retrieve from intent in production

        // Initialize UI components
        TextView qrHashValue = findViewById(R.id.textView_qrHashValue);
        ImageView qrImage = findViewById(R.id.imageView_qrCode);

        // Generate and display QR code
        generateQRCode(qrImage, qrHashValue, eventID);
    }

    /**
     * Generates a QR code for the specified event ID, converts it into a bitmap,
     * and sets it on the provided {@link ImageView}. The event ID is also displayed as a string.
     *
     * @param qrImage     The {@link ImageView} where the QR code bitmap will be displayed.
     * @param qrHashValue The {@link TextView} where the event ID is displayed.
     * @param eventID     The event ID to encode in the QR code.
     */
    private void generateQRCode(ImageView qrImage, TextView qrHashValue, String eventID) {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(eventID, BarcodeFormat.QR_CODE, 400, 400);
        } catch (WriterException e) {
            e.printStackTrace();
            return;
        }

        // Convert the bit matrix to a bitmap
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
            }
        }

        // Set the bitmap on the ImageView and display the event ID
        qrImage.setImageBitmap(bitmap);
        qrHashValue.setText(eventID);
    }
}
