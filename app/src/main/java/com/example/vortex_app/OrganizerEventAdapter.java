package com.example.vortex_app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * {@code OrganizerEventAdapter} is a {@link RecyclerView.Adapter} that displays a list of events for organizers.
 * Each item in the RecyclerView shows the event's name and includes a click listener to handle event selection.
 */
public class OrganizerEventAdapter extends RecyclerView.Adapter<OrganizerEventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private OnEventClickListener listener;

    /**
     * Interface for handling click events on event items.
     */
    public interface OnEventClickListener {
        /**
         * Called when an event item is clicked.
         *
         * @param event The {@link Event} associated with the clicked item.
         */
        void onEventClick(Event event);
    }

    /**
     * Constructs an {@code OrganizerEventAdapter} with a list of events and a click listener.
     *
     * @param eventList A {@link List} of {@link Event} objects to be displayed.
     * @param listener  An {@link OnEventClickListener} to handle click events on event items.
     */
    public OrganizerEventAdapter(List<Event> eventList, OnEventClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new {@link EventViewHolder} to represent an item.
     * Inflates the layout for each event item and creates a new view holder instance.
     *
     * @param parent   The parent view that holds the item views.
     * @param viewType The type of the new view.
     * @return A new {@link EventViewHolder} that holds the inflated view for each event item.
     */
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(itemView);
    }

    /**
     * Binds data to each item in the RecyclerView by setting the event name in the TextView
     * and sets a click listener to handle user interactions.
     *
     * @param holder   The {@link EventViewHolder} that holds the view components for the item.
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
     * Returns the total number of items in the list.
     *
     * @return The size of the event list.
     */
    @Override
    public int getItemCount() {
        return eventList.size();
    }

    /**
     * {@code EventViewHolder} holds references to the views within each item of the RecyclerView.
     * Specifically, it includes a {@link TextView} for displaying the event's name.
     */
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;

        /**
         * Constructs an {@code EventViewHolder} and initializes the TextView for the event's name.
         *
         * @param itemView The view of the item in the RecyclerView.
         */
        public EventViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.text_event_name);
        }
    }
}
