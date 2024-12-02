package com.example.vortex_app.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.vortex_app.R;
import com.example.vortex_app.model.Event;

import java.util.List;

public class ImageListAdapter extends android.widget.ArrayAdapter<Event> {

    private final Context context;
    private final List<Event> events;

    public ImageListAdapter(@NonNull Context context, @NonNull List<Event> events) {
        super(context, R.layout.list_item_image, events);
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item_image, parent, false);
        }

        Event currentEvent = events.get(position);

        TextView eventName = listItem.findViewById(R.id.eventNameTextView);
        ImageView eventImage = listItem.findViewById(R.id.eventImageView);

        // Set the event name
        eventName.setText(currentEvent.getName());

        // Load the event image using Glide
        Glide.with(context)
                .load(currentEvent.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .into(eventImage);

        return listItem;
    }
}
