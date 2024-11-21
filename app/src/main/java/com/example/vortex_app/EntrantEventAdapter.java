package com.example.vortex_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EntrantEventAdapter extends RecyclerView.Adapter<EntrantEventAdapter.EventViewHolder> {

    private final List<Event> eventList;
    private final Context context;

    public EntrantEventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        // Bind event data
        holder.eventName.setText(event.getName());

        // Click listener for Entrant role
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventInfoActivity.class);
            intent.putExtra("EVENT_ID", event.getEventID());
            intent.putExtra("EVENT_NAME", event.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.text_event_name);
        }
    }
}
