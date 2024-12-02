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

/**
 * Custom ArrayAdapter for displaying events in a ListView with images.
 * Each item in the list shows the event name and its associated image.
 */
public class ImageListAdapter extends android.widget.ArrayAdapter<Event> {

    private final Context context;
    private final List<Event> events;

    /**
     * Constructor to initialize the adapter with context and list of events.
     *
     * @param context The context where the adapter will be used.
     * @param events The list of Event objects to be displayed.
     */
    public ImageListAdapter(@NonNull Context context, @NonNull List<Event> events) {
        super(context, R.layout.list_item_image, events);
        this.context = context;
        this.events = events;
    }

    /**
     * Returns a view for a list item in the ListView.
     *
     * @param position The position of the item in the list.
     * @param convertView A recycled view, or null if no view is available.
     * @param parent The parent ViewGroup.
     * @return A view representing the event item.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item_image, parent, false);
        }

        // Get the current event at the specified position
        Event currentEvent = events.get(position);

        // Get references to the TextView and ImageView for the event item
        TextView eventName = listItem.findViewById(R.id.eventNameTextView);
        ImageView eventImage = listItem.findViewById(R.id.eventImageView);

        // Set the event name
        eventName.setText(currentEvent.getName());

        // Load the event image using Glide and set a placeholder while the image loads
        Glide.with(context)
                .load(currentEvent.getImageUrl())
                .placeholder(R.drawable.placeholder_image)  // Placeholder image while loading
                .into(eventImage);

        return listItem;
    }
}
