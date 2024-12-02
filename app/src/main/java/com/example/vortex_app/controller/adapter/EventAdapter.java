package com.example.vortex_app.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vortex_app.R;

import java.util.List;

/**
 * Custom adapter for displaying event details in a ListView.
 * This adapter binds event names and image URLs to the corresponding views in the list item layout.
 */
public class EventAdapter extends BaseAdapter {
    private Context context;
    private List<String> eventNames;
    private List<String> eventIDs;
    private List<String> eventImageUrls;

    /**
     * Constructor for initializing the adapter with event data.
     *
     * @param context The context where the adapter is used.
     * @param eventNames The list of event names to be displayed.
     * @param eventIDs The list of event IDs (currently unused but can be extended).
     * @param eventImageUrls The list of event image URLs.
     */
    public EventAdapter(Context context, List<String> eventNames, List<String> eventIDs, List<String> eventImageUrls) {
        this.context = context;
        this.eventNames = eventNames;
        this.eventIDs = eventIDs;
        this.eventImageUrls = eventImageUrls;
    }

    /**
     * Returns the number of items in the adapter's data set.
     *
     * @return The number of events.
     */
    @Override
    public int getCount() {
        return eventNames.size();
    }

    /**
     * Returns the data item at the specified position.
     *
     * @param position The position of the item to return.
     * @return The event name at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return eventNames.get(position);
    }

    /**
     * Returns the item ID at the specified position. Here, it returns the position as the item ID.
     *
     * @param position The position of the item.
     * @return The item ID (position).
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Returns a view for a list item at the specified position. This method binds data to the list item view.
     * The event name and image URL are set to the corresponding views in the list item layout.
     *
     * @param position The position of the item within the data set.
     * @param convertView The old view to reuse (if available).
     * @param parent The parent ViewGroup.
     * @return The updated view for the list item.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // Inflate a new view for the item if not available
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_item, parent, false);
        }

        // Find the views within the list item layout
        TextView textViewEventName = convertView.findViewById(R.id.text_event_name);
        ImageView imageViewEventIndicator = convertView.findViewById(R.id.image_event);

        // Set the event name to the TextView
        textViewEventName.setText(eventNames.get(position));

        // Load the event image using Glide if the URL is not empty
        String imageUrl = eventImageUrls.get(position);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(imageViewEventIndicator);
        } else {
            // Hide the ImageView if no image URL is provided
            imageViewEventIndicator.setVisibility(View.GONE);
        }

        return convertView;
    }
}
