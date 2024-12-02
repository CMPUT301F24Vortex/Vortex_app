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

public class EventListAdapter extends android.widget.ArrayAdapter<Event> {

    private final Context context;
    private final ArrayList<Event> events;

    public EventListAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.event_list_item, parent, false);
        }

        Event event = events.get(position);

        ImageView eventImageView = convertView.findViewById(R.id.eventImageView);
        TextView eventNameTextView = convertView.findViewById(R.id.eventNameTextView);

        // Set event name
        eventNameTextView.setText(event.getName());

        // Load event image
        if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
            Glide.with(context).load(event.getImageUrl()).into(eventImageView);
        } else {
            eventImageView.setImageResource(R.drawable.placeholder_image); // Placeholder image
        }

        return convertView;
    }
}
