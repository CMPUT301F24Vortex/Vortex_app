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
 * {@code LocationAdapter} is a {@link RecyclerView.Adapter} that displays a list of entrants along with
 * their location details. Each item in the RecyclerView includes the entrant's name, address, and a timestamp.
 * The adapter also allows navigation to a detailed view when an entrant item is clicked.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<User> locationEntrants;
    private Context context;

    /**
     * Constructs a {@code LocationAdapter} with the specified context and list of entrants.
     *
     * @param context          The {@link Context} in which the adapter is being used.
     * @param locationEntrants A {@link List} of {@link User} objects representing entrants with location details.
     */
    public LocationAdapter(Context context, List<User> locationEntrants) {
        this.context = context;
        this.locationEntrants = locationEntrants;
    }

    /**
     * Called when RecyclerView needs a new {@link LocationViewHolder} to represent an item.
     * Inflates the layout for each entrant item and creates a new view holder instance.
     *
     * @param parent   The parent view that holds the item views.
     * @param viewType The type of the new view.
     * @return A new {@link LocationViewHolder} that holds the inflated view for each entrant item.
     */
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location_entrant, parent, false);
        return new LocationViewHolder(view);
    }

    /**
     * Binds data to each item in the RecyclerView by setting the entrant's name, address, and timestamp
     * in the respective TextViews. An item click listener is set up for navigating to a detail view of the entrant.
     *
     * @param holder   The {@link LocationViewHolder} that holds the view components for the item.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        User entrant = locationEntrants.get(position);
        holder.textViewName.setText(entrant.getName());

        // Placeholder for address and date-time fields (to be updated with database data)
        // holder.textViewAddress.setText(entrant.getAddress());
        // holder.textViewDateTime.setText(entrant.getDateTime());

        holder.itemView.setOnClickListener(v -> {
            // Intent to navigate to EntrantLocationDetailActivity (to be enabled with database integration)
            // Intent intent = new Intent(context, EntrantLocationDetailActivity.class);
            // intent.putExtra("ENTRANT_NAME", entrant.getName());
            // intent.putExtra("ADDRESS", entrant.getAddress());
            // intent.putExtra("DATE_TIME", entrant.getDateTime());
            // context.startActivity(intent);
        });
    }

    /**
     * Returns the total number of items in the list of location entrants.
     *
     * @return The size of the location entrant list.
     */
    @Override
    public int getItemCount() {
        return locationEntrants.size();
    }

    /**
     * {@code LocationViewHolder} holds references to the views within each item of the RecyclerView.
     * Specifically, it includes TextViews for displaying the entrant's name, address, and date-time information.
     */
    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewAddress;
        TextView textViewDateTime;

        /**
         * Constructs a {@code LocationViewHolder} and initializes the TextViews for entrant's name,
         * address, and date-time.
         *
         * @param itemView The view of the item in the RecyclerView.
         */
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewEntrantName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
        }
    }
}
