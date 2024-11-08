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
 * {@code EventAdapter} is a {@link RecyclerView.Adapter} that displays a list of events.
 * Each item in the RecyclerView shows an event’s name and image. Clicking on an item opens
 * the {@link EventInfoActivity} with detailed information about the selected event.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private Context context;

    /**
     * Constructs an {@code EventAdapter} with the specified context and list of events.
     *
     * @param context   The {@link Context} in which the adapter operates, used for starting activities.
     * @param eventList A {@link List} of {@link Event} objects to display in the RecyclerView.
     */
    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
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
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    /**
     * Binds data to each item in the RecyclerView by setting the event's name and image,
     * and sets a click listener to open {@link EventInfoActivity} with detailed event information.
     *
     * @param holder   The {@link EventViewHolder} that holds the view components for the item.
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
     * Specifically, it includes a {@link TextView} for the event's name and an {@link ImageView} for the event image.
     */
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventName;
        ImageView eventImage;

        /**
         * Constructs an {@code EventViewHolder} and initializes the views for the event's name and image.
         *
         * @param itemView The view of the item in the RecyclerView.
         */
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.text_event_name);
            eventImage = itemView.findViewById(R.id.image_event);
        }
    }
}
