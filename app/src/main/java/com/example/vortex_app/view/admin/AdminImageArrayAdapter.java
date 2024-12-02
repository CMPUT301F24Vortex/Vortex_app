package com.example.vortex_app.view.admin;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.vortex_app.R;
import com.example.vortex_app.model.Event;

import java.util.ArrayList;

public class AdminImageArrayAdapter extends ArrayAdapter<String> {
    private ArrayList<String> imageURLs;
    private Context context;

    public AdminImageArrayAdapter (Context context, ArrayList<String> imageURLs) {
        super(context, 0, imageURLs);
        this.imageURLs = imageURLs;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.admin_image_listitem, parent, false);
        }

        String imageURL = imageURLs.get(position);


        ImageView imageView = view.findViewById(R.id.imageView);

        // load image
        if (imageURL != null && !imageURL.isEmpty()) {
            Glide.with(this.getContext())
                    .load(imageURL)
                    .placeholder(R.drawable.upload_placeholder) // Placeholder image
                    .into(imageView);
        }

        return view;
    }
}
