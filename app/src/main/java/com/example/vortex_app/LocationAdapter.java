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

/**
 * LocationAdapter is a RecyclerView Adapter for displaying a list of users with location data.
 * It binds User objects to views and handles user interactions such as item clicks.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<User> locationEntrants;
    private Context context;

    /**
     * Constructs a LocationAdapter with a list of users and a context.
     *
     * @param context          The context of the calling activity.
     * @param locationEntrants The list of User objects representing users with location data.
     */
    public LocationAdapter(Context context, List<User> locationEntrants) {
        this.context = context;
        this.locationEntrants = locationEntrants;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new LocationViewHolder that holds a View for each item in the list.
     */
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location_entrant, parent, false);
        return new LocationViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method binds the data from a User object to the corresponding views in the ViewHolder.
     *
     * @param holder   The LocationViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        User entrant = locationEntrants.get(position);
        holder.textViewName.setText(entrant.getName());

        // Placeholder for potential database functionality
        // holder.textViewAddress.setText(entrant.getAddress());
        // holder.textViewDateTime.setText(entrant.getDateTime());

        holder.itemView.setOnClickListener(v -> {
            // Uncomment and modify as needed for navigating to detailed view
            // Intent intent = new Intent(context, EntrantLocationDetailActivity.class);
            // intent.putExtra("ENTRANT_NAME", entrant.getName());
            // intent.putExtra("ADDRESS", entrant.getAddress());
            // intent.putExtra("DATE_TIME", entrant.getDateTime());
            // context.startActivity(intent);
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return locationEntrants.size();
    }

    /**
     * ViewHolder class for representing a single user's location data.
     */
    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewAddress;
        TextView textViewDateTime;

        /**
         * Constructs a LocationViewHolder, initializing views for displaying a user's location data.
         *
         * @param itemView The view of the individual item.
         */
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewEntrantName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
        }
    }
}
