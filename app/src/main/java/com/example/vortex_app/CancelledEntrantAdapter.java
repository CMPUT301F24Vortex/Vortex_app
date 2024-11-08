package com.example.vortex_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Adapter class for displaying a list of cancelled entrants in a RecyclerView.
 * This adapter binds data for each User object to the corresponding view in the RecyclerView.
 */
public class CancelledEntrantAdapter extends RecyclerView.Adapter<CancelledEntrantAdapter.EntrantViewHolder> {

    private List<User> entrantList;
    /**
     * Constructs a CancelledEntrantAdapter with a list of entrants.
     *
     * @param entrantList The list of User objects representing cancelled entrants.
     */
    public CancelledEntrantAdapter(List<User> entrantList) {
        this.entrantList = entrantList;
    }


    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new EntrantViewHolder that holds a View for each item in the list.
     */
    @NonNull
    @Override
    public EntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new EntrantViewHolder(view);
    }


    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the ViewHolder to reflect the item at the given position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull EntrantViewHolder holder, int position) {
        User entrant = entrantList.get(position);
        holder.textViewEntrantName.setText(entrant.getName());
    }


    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return entrantList.size();
    }

    /**
     * ViewHolder class for representing a single entrant item view.
     */
    static class EntrantViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEntrantName;

        /**
         * Constructs an EntrantViewHolder, initializing the views that will display each User's name.
         *
         * @param itemView The view of the individual item.
         */
        public EntrantViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEntrantName = itemView.findViewById(R.id.textViewEntrantName);
        }
    }
}