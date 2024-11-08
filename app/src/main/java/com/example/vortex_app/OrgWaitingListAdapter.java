package com.example.vortex_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * OrgWaitingListAdapter is a RecyclerView Adapter for displaying a list of users in the waiting list.
 * It binds User objects to views and handles data display for each item in the list.
 */
public class OrgWaitingListAdapter extends RecyclerView.Adapter<OrgWaitingListAdapter.WaitingListViewHolder> {

    private List<User> waitingListEntrants;

    /**
     * Constructs an OrgWaitingListAdapter with a list of waiting list entrants.
     *
     * @param waitingListEntrants The list of User objects representing the entrants in the waiting list.
     */
    public OrgWaitingListAdapter(List<User> waitingListEntrants) {
        this.waitingListEntrants = waitingListEntrants;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new WaitingListViewHolder that holds a View for each item in the list.
     */
    @NonNull
    @Override
    public WaitingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.waiting_list_item, parent, false);
        return new WaitingListViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method binds the data from a User object to the corresponding views in the ViewHolder.
     *
     * @param holder   The WaitingListViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull WaitingListViewHolder holder, int position) {
        User entrant = waitingListEntrants.get(position);
        holder.textViewEntrantName.setText(entrant.getName());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return waitingListEntrants.size();
    }

    /**
     * ViewHolder class for representing a single waiting list entrant.
     */
    static class WaitingListViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEntrantName;

        /**
         * Constructs a WaitingListViewHolder, initializing views for displaying an entrant's name.
         *
         * @param itemView The view of the individual item.
         */
        public WaitingListViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEntrantName = itemView.findViewById(R.id.textViewEntrantName);
        }
    }
}
