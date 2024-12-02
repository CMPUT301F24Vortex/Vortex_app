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
 * Adapter for displaying a list of events in a ListView.
 * This adapter binds event name and image URL to the views of each list item.
 */
public class EventListAdapter extends android.widget.ArrayAdapter<Event> {

    private final Context context;
    private final ArrayList<Event> events;

    /**
     * Constructor for initializing the adapter with event data.
     *
     * @param context The context where the adapter is used.
     * @param events The list of events to be displayed.
     */
    public EventListAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.context = context;
        this.events = events;
    }

    /**
     * Returns a view for a list item at the specified position.
     * This method binds the event name and image URL to the views in the item layout.
     *
     * @param position The position of the item within the data set.
     * @param convertView The old view to reuse (if available).
     * @param parent The parent ViewGroup.
     * @return The updated view for the list item.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            // Inflate a new view for the item if not available
            convertView = LayoutInflater.from(context).inflate(R.layout.event_list_item, parent, false);
        }

        // Get the event at the current position
        Event event = events.get(position);

        // Find the views within the list item layout
        ImageView eventImageView = convertView.findViewById(R.id.eventImageView);
        TextView eventNameTextView = convertView.findViewById(R.id.eventNameTextView);

        // Set the event name
        eventNameTextView.setText(event.getName());

        // Load the event image using Glide if the URL is not empty
        if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
            Glide.with(context).load(event.getImageUrl()).into(eventImageView);
        } else {
            // Set a placeholder image if no URL is available
            eventImageView.setImageResource(R.drawable.placeholder_image);
        }

        return convertView;
    }
}
