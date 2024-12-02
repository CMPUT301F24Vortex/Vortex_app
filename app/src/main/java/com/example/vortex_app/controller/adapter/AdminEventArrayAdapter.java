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

import java.util.ArrayList;

/**
 * AdminEventArrayAdapter is responsible for displaying event details in the ListView.
 */
public class AdminEventArrayAdapter extends android.widget.ArrayAdapter<Event> {

    private ArrayList<Event> events;
    private Context context;

    public AdminEventArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.event_list_item, parent, false);
        }

        Event event = events.get(position);

        TextView eventName = view.findViewById(R.id.eventNameTextView);
        ImageView eventImage = view.findViewById(R.id.eventImageView);

        eventName.setText(event.getName());

        // Load event image with Glide
        Glide.with(context)
                .load(event.getImageUrl())
                .placeholder(R.drawable.event_placeholder) // Placeholder image
                .into(eventImage);

        return view;
    }
}
