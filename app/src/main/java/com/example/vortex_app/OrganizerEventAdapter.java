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

public class OrganizerEventAdapter extends RecyclerView.Adapter<OrganizerEventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private Context context;
    private OnItemClickListener listener; // Add listener variable

    // Interface for click listener
    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    // Constructor to pass context and event list
    public OrganizerEventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener; // Set the listener
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        // Get the current event
        Event event = eventList.get(position);

        // Bind the event data to the views
        holder.eventName.setText(event.getName());
        holder.eventImage.setImageResource(event.getImageResId());

        holder.itemView.setOnClickListener(v -> {
            // Create an intent to navigate to EventInfoActivity
            Intent intent = new Intent(context, OrganizerMenu.class);

            // Pass the necessary event data using intent extras
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
            context.startActivity(intent);  // Start the EventInfoActivity

        });
    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }

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