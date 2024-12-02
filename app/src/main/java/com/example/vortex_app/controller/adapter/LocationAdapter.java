package com.example.vortex_app.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.User;

import java.util.List;

/**
 * Adapter for displaying a list of users (entrants) at a specific location.
 * Binds data such as the full name, address, and date/time to a RecyclerView item view.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private final List<User> locationEntrants;
    private final Context context;

    /**
     * Constructor for initializing the LocationAdapter with a list of entrants and context.
     *
     * @param context The context in which the adapter is used (e.g., an Activity or Fragment).
     * @param locationEntrants The list of users (entrants) to display in the RecyclerView.
     */
    public LocationAdapter(Context context, List<User> locationEntrants) {
        this.context = context;
        this.locationEntrants = locationEntrants;
    }

    /**
     * Creates and returns a new ViewHolder for each item in the RecyclerView.
     *
     * @param parent The parent view group to which the new view will be attached.
     * @param viewType The view type for the new view.
     * @return A new LocationViewHolder.
     */
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location_entrant, parent, false);
        return new LocationViewHolder(view);
    }

    /**
     * Binds the data of a specific entrant to the corresponding views in the RecyclerView item.
     *
     * @param holder The ViewHolder that holds the views for the current item.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        User entrant = locationEntrants.get(position);
        holder.textViewName.setText(entrant.getFullName());

        // Uncomment and fix once database functionality is implemented
        // holder.textViewAddress.setText(entrant.getAddress());
        // holder.textViewDateTime.setText(entrant.getDateTime());

        holder.itemView.setOnClickListener(v -> {
            // Uncomment and implement the following code to start a new activity with details
            // Intent intent = new Intent(context, EntrantLocationDetailActivity.class);
            // intent.putExtra("ENTRANT_NAME", entrant.getFullName());
            // intent.putExtra("ADDRESS", entrant.getAddress());
            // intent.putExtra("DATE_TIME", entrant.getDateTime());
            // context.startActivity(intent);
        });
    }

    /**
     * Returns the total number of items in the data set.
     *
     * @return The number of items in the list.
     */
    @Override
    public int getItemCount() {
        return locationEntrants.size();
    }

    /**
     * ViewHolder for holding the views in each item of the RecyclerView.
     * Holds references to the name, address, and date/time of the entrant.
     */
    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewAddress;
        TextView textViewDateTime;

        /**
         * Constructor for initializing the ViewHolder with the item view.
         *
         * @param itemView The item view containing the views to be bound.
         */
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewEntrantName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
        }
    }
}
