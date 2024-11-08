package com.example.vortex_app;

import android.content.Context;
import android.content.Intent;
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
    private Context context;
    private OnItemClickListener listener;

    /**
     * Interface for item click events. Used to handle clicks on individual events.
     */
    public interface OnItemClickListener {
        /**
         * Called when an item is clicked.
         *
         * @param event The Event object that was clicked.
         */
        void onItemClick(Event event);
    }

    /**
     * Constructs an OrganizerEventAdapter with the provided context and event list.
     *
     * @param context   The context of the calling activity.
     * @param eventList The list of Event objects to be displayed.
     */
    public OrganizerEventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    /**
     * Sets the click listener for the adapter's items.
     *
     * @param listener The listener to handle item clicks.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new EventViewHolder that holds a View for each item in the list.
     */
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method binds the data from an Event object to the corresponding views in the ViewHolder.
     *
     * @param holder   The EventViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        // Get the current event
        Event event = eventList.get(position);

        // Bind the event data to the views
        holder.eventName.setText(event.getName());
        holder.eventImage.setImageResource(event.getImageResId());

        // Set click listener for each event item
        holder.itemView.setOnClickListener(v -> {
            // Create an intent to navigate to OrganizerMenu
            Intent intent = new Intent(context, OrganizerMenu.class);

            // Pass event details via intent extras
            intent.putExtra("EVENT_NAME", event.getName());
            intent.putExtra("CLASS_DAY", event.getClassDay());
            intent.putExtra("TIME", event.getTime());
            intent.putExtra("PERIOD", event.getPeriod());
            intent.putExtra("REG_DUE_DATE", event.getRegistrationDueDate());
            intent.putExtra("REG_OPEN_DATE", event.getRegistrationOpenDate());
            intent.putExtra("PRICE", event.getPrice());
            intent.putExtra("LOCATION", event.getLocation());
            intent.putExtra("MAX_PEOPLE", event.getMaxPeople());
            intent.putExtra("DIFFICULTY", event.getDifficulty());
            intent.putExtra("EVENTID", event.getEventID());
            context.startActivity(intent);
        });
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

        TextView eventName;
        ImageView eventImage;

        /**
         * Constructs an EventViewHolder, initializing views for displaying event details.
         *
         * @param itemView The view of the individual item.
         */
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.text_event_name);
            eventImage = itemView.findViewById(R.id.image_event);
        }
    }
}
