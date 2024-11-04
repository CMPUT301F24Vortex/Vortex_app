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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private Context context;

    // Constructor to receive eventList and context
    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        // Set event details in the ViewHolder
        holder.eventName.setText(event.getName());
        holder.eventImage.setImageResource(event.getImageResId());

        // Set click listener for each event item
        holder.itemView.setOnClickListener(v -> {
            // Create an Intent to navigate to the Event Info activity
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

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    // ViewHolder class for Event items
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventName;
        ImageView eventImage;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.text_event_name);
            eventImage = itemView.findViewById(R.id.image_event);
        }
    }
}
