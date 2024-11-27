package com.example.vortex_app.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vortex_app.R;

import java.util.ArrayList;
import java.util.Map;

public class EventListAdapter extends ArrayAdapter<Map<String, String>> {

    private Context context;
    private ArrayList<Map<String, String>> eventList;

    public EventListAdapter(Context context, ArrayList<Map<String, String>> eventList) {
        super(context, R.layout.list_item_event, eventList);
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_event, parent, false);
        }

        Map<String, String> eventData = eventList.get(position);

        TextView eventNameTextView = convertView.findViewById(R.id.text_event_name);
        eventNameTextView.setText(eventData.get("eventName"));

        return convertView;
    }
}
