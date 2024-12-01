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

public class EventAdapter extends BaseAdapter {
    private Context context;
    private List<String> eventNames;
    private List<String> eventIDs;
    private List<String> eventImageUrls;

    public EventAdapter(Context context, List<String> eventNames, List<String> eventIDs, List<String> eventImageUrls) {
        this.context = context;
        this.eventNames = eventNames;
        this.eventIDs = eventIDs;
        this.eventImageUrls = eventImageUrls;
    }

    @Override
    public int getCount() {
        return eventNames.size();
    }

    @Override
    public Object getItem(int position) {
        return eventNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_item, parent, false);
        }

        TextView textViewEventName = convertView.findViewById(R.id.text_event_name);
        ImageView imageViewEventIndicator = convertView.findViewById(R.id.image_event);

        // Set the event name
        textViewEventName.setText(eventNames.get(position));

        // Load image if URL is not null
        String imageUrl = eventImageUrls.get(position);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(imageViewEventIndicator);
        } else {
            imageViewEventIndicator.setVisibility(View.GONE);  // Hide the ImageView if no image URL
        }

        return convertView;
    }
}