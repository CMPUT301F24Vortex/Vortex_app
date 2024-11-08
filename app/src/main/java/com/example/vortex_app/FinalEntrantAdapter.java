package com.example.vortex_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * FinalEntrantAdapter is a RecyclerView Adapter for displaying a list of final entrants.
 * It binds User objects to views in a RecyclerView.
 */
public class FinalEntrantAdapter extends RecyclerView.Adapter<FinalEntrantAdapter.FinalEntrantViewHolder> {

    private List<User> finalEntrantList;

    /**
     * Constructs a FinalEntrantAdapter with a list of final entrants.
     *
     * @param finalEntrantList The list of User objects representing final entrants.
     */
    public FinalEntrantAdapter(List<User> finalEntrantList) {
        this.finalEntrantList = finalEntrantList;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new FinalEntrantViewHolder that holds a View for each item in the list.
     */
    @NonNull
    @Override
    public FinalEntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new FinalEntrantViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method binds the data from a User object to the corresponding views in the ViewHolder.
     *
     * @param holder   The FinalEntrantViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull FinalEntrantViewHolder holder, int position) {
        User entrant = finalEntrantList.get(position);
        holder.textViewEntrantName.setText(entrant.getName());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return finalEntrantList.size();
    }

    /**
     * ViewHolder class for representing a single entrant item.
     */
    static class FinalEntrantViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEntrantName;

        /**
         * Constructs a FinalEntrantViewHolder, initializing the views for displaying an entrant's name.
         *
         * @param itemView The view of the individual item.
         */
        public FinalEntrantViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEntrantName = itemView.findViewById(R.id.textViewEntrantName);
        }
    }
}
