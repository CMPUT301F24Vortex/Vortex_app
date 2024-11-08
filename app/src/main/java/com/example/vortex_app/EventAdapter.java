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
 * EventAdapter is a RecyclerView Adapter for displaying a list of events.
 * It binds Event objects to views and handles item click events to navigate to EventInfoActivity.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private Context context;

    /**
     * Constructs an EventAdapter with a list of events and a context.
     *
     * @param context   The context of the calling activity.
     * @param eventList The list of Event objects to display.
     */
    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
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
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        // Set event details in the ViewHolder
        holder.eventName.setText(event.getName());
        holder.eventImage.setImageResource(event.getImageResId());

        // Set click listener for each event item
        holder.itemView.setOnClickListener(v -> {
            // Create an Intent to navigate to the EventInfoActivity
            Intent intent = new Intent(context, EventInfoActivity.class);

            // Pass event details to the EventInfoActivity
            intent.putExtra("EVENT_NAME", event.getName());
            intent.putExtra("CLASS_DAY", event.getClassDay());
            intent.putExtra("TIME", event.getTime());
            intent.putExtra("PERIOD", event.getPeriod());
            intent.putExtra("REGISTRATION_DUE_DATE", event.getRegistrationDueDate());
            intent.putExtra("REGISTRATION_OPEN_DATE", event.getRegistrationOpenDate());
            intent.putExtra("PRICE", event.getPrice());
            intent.putExtra("LOCATION", event.getLocation());
            intent.putExtra("MAX_PEOPLE", event.getMaxPeople());
            intent.putExtra("DIFFICULTY", event.getDifficulty());
            intent.putExtra("REQUIRES_GEOLOCATION", event.isRequiresGeolocation());

            // Start the EventInfoActivity with the intent
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
     * ViewHolder class for representing a single Event item.
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
