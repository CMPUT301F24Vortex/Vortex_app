package com.example.vortex_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * SelectedEntrantAdapter is a RecyclerView Adapter for displaying a list of selected entrants.
 * It binds User objects to views for display and handles data presentation for each entrant in the list.
 */
public class SelectedEntrantAdapter extends RecyclerView.Adapter<SelectedEntrantAdapter.SelectedEntrantViewHolder> {

    private List<User> selectedEntrantList;

    /**
     * Constructs a SelectedEntrantAdapter with a list of selected entrants.
     *
     * @param selectedEntrantList The list of User objects representing the selected entrants.
     */
    public SelectedEntrantAdapter(List<User> selectedEntrantList) {
        this.selectedEntrantList = selectedEntrantList;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new SelectedEntrantViewHolder that holds a View for each item in the list.
     */
    @NonNull
    @Override
    public SelectedEntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new SelectedEntrantViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method binds the data from a User object to the corresponding views in the ViewHolder.
     *
     * @param holder   The SelectedEntrantViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull SelectedEntrantViewHolder holder, int position) {
        User entrant = selectedEntrantList.get(position);
        holder.textViewEntrantName.setText(entrant.getName());
        // Optionally display the sign-up status (uncomment if needed)
        // holder.textViewSignUpStatus.setText("Sign Up: " + (entrant.isConfirmed() ? "Confirmed" : "Not Confirmed"));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return selectedEntrantList.size();
    }

    /**
     * ViewHolder class for representing a single selected entrant.
     */
    static class SelectedEntrantViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEntrantName;
        // Optional TextView to display sign-up status (uncomment if needed)
        // TextView textViewSignUpStatus;

        /**
         * Constructs a SelectedEntrantViewHolder, initializing views for displaying an entrant's name.
         *
         * @param itemView The view of the individual item.
         */
        public SelectedEntrantViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEntrantName = itemView.findViewById(R.id.textViewEntrantName);
            // Initialize the sign-up status TextView (uncomment if needed)
            // textViewSignUpStatus = itemView.findViewById(R.id.textViewSignUpStatus);
        }
    }
}
