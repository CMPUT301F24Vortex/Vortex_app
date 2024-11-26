package com.example.vortex_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdminEventArrayAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;

    public AdminEventArrayAdapter (Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.admin_profile_listitem, parent, false);
        }

        Event event = events.get(position);

        TextView eventName = view.findViewById(R.id.textView_eventname);
        TextView eventID = view.findViewById(R.id.textView_eventid);
        ImageView eventPoster = view.findViewById(R.id.imageView_profilepic);

        eventName.setText(event.getName());
        eventID.setText(event.getEventID());

        // load event poster
        String profilePicUrl = event.getPosterURL();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            Glide.with(this.getContext())
                    .load(profilePicUrl)
                    .placeholder(R.drawable.sample_event_image) // Placeholder image
                    .into(eventPoster);
        }

        return view;
    }
}
