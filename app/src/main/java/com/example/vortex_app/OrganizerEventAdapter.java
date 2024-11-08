package com.example.vortex_app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * OrganizerEventAdapter is a RecyclerView Adapter for displaying and managing a list of events
 * for an organizer. It binds Event objects to views and handles user interactions, such as clicking
 * on an event to navigate to additional details.
 */
public class OrganizerEventAdapter extends RecyclerView.Adapter<OrganizerEventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private OnEventClickListener listener;

    /**
     * Interface for item click events. Used to handle clicks on individual events.
     */
    public interface OnEventClickListener {
        void onEventClick(Event event);
    }

    public OrganizerEventAdapter(List<Event> eventList, OnEventClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new EventViewHolder that holds a View for each item in the list.
     */

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method binds the data from an Event object to the corresponding views in the ViewHolder.
     *
     * @param holder   The EventViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        Log.d("OrganizerEventAdapter", "Event name: " + event.getName());
        holder.eventName.setText(event.getName());
        holder.itemView.setOnClickListener(v -> listener.onEventClick(event));
    }


    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return eventList.size();
    }

    /**
     * ViewHolder class for representing a single event item.
     */
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName ;
        public EventViewHolder(View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.text_event_name);
        }
    }
}






