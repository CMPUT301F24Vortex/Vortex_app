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

public class OrgQRCodeActivity extends AppCompatActivity {

    private String eventID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.org_qrcode_activity);

        // get eventID passed through intent
        // Bundle extras = getIntent().getExtras();
        // eventID = extras.getString("eventID");
        eventID = "121";

        // Get resource references
        TextView qrHashValue = findViewById(R.id.textView_qrHashValue);
        ImageView qrImage = findViewById(R.id.imageView_qrCode);


        // create QR code
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(eventID, BarcodeFormat.QR_CODE, 400, 400);
        } catch (WriterException e) {
            e.printStackTrace();
            return;
        }
        // convert to bitmap
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK: Color.WHITE);
            }
        }

        // set imageview to qr code
        qrImage.setImageBitmap(bitmap);


        //dsafgdfg







    }
}