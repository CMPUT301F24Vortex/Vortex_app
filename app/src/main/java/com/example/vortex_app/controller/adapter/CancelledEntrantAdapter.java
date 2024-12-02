package com.example.vortex_app.controller.adapter;

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
 * Adapter for displaying a list of cancelled entrants in a RecyclerView.
 * It binds user data (first name and last name) to a view for each item.
 */
public class CancelledEntrantAdapter extends RecyclerView.Adapter<CancelledEntrantAdapter.ViewHolder> {

    private final List<User> entrantList;

    /**
     * Constructor for initializing the adapter with the list of cancelled entrants.
     *
     * @param entrantList List of User objects representing the cancelled entrants.
     */
    public CancelledEntrantAdapter(List<User> entrantList) {
        this.entrantList = entrantList;
    }

    /**
     * Creates a new ViewHolder for displaying the item at a given position.
     * Inflates the layout for an item in the RecyclerView.
     *
     * @param parent   The parent ViewGroup that this new item will be attached to.
     * @param viewType The view type of the new item.
     * @return A new ViewHolder for the item at the given position.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cancelled_entrant, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the data for a specific item at the given position to the corresponding views in the ViewHolder.
     *
     * @param holder   The ViewHolder that contains the views for the current item.
     * @param position The position of the item within the RecyclerView's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = entrantList.get(position);
        // Display firstName and lastName instead of userName
        holder.textViewName.setText(user.getFirstName() + " " + user.getLastName());
    }

    /**
     * Returns the total number of items in the data set.
     *
     * @return The number of items in the data set.
     */
    @Override
    public int getItemCount() {
        return entrantList.size();
    }

    /**
     * ViewHolder class for holding the views for each item in the RecyclerView.
     * It stores references to the TextViews for displaying user information.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;

        /**
         * Constructor for the ViewHolder.
         * It initializes the views for each item in the RecyclerView.
         *
         * @param itemView The root view of the item layout.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }
}
