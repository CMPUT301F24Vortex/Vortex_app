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
 * {@code SelectedEntrantAdapter} is a custom adapter used to display a list of selected entrants
 * in a RecyclerView. Each item in the RecyclerView displays the full name of an entrant (first and last name).
 */
public class SelectedEntrantAdapter extends RecyclerView.Adapter<SelectedEntrantAdapter.ViewHolder> {

    private final List<User> entrantList;

    /**
     * Constructs a new {@code SelectedEntrantAdapter} with the provided list of entrants.
     *
     * @param entrantList A list of {@link User} objects representing the entrants to display in the RecyclerView.
     */
    public SelectedEntrantAdapter(List<User> entrantList) {
        this.entrantList = entrantList;
    }

    /**
     * Inflates the layout for each entrant item in the RecyclerView and returns a new {@link ViewHolder}.
     *
     * @param parent   The parent view that holds the item views.
     * @param viewType The type of view to create.
     * @return A new {@link ViewHolder} that holds the view for each entrant item.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entrant_list_item_for_selected_activity, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds data to each item in the RecyclerView. Sets the entrant's full name (first name + last name) to be displayed.
     *
     * @param holder   The {@link ViewHolder} that holds the view components.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = entrantList.get(position);

        // Set the user's full name (firstName + lastName) for display
        holder.textViewName.setText(user.getFullName());
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return The size of the entrant list.
     */
    @Override
    public int getItemCount() {
        return entrantList.size();
    }

    /**
     * ViewHolder class that represents each item view for an entrant.
     * Holds references to the TextViews that display entrant details.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;

        /**
         * Constructs a new {@code ViewHolder} for an entrant item view.
         *
         * @param itemView The view of the item in the RecyclerView.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName); // Ensure this matches your layout's TextView ID
        }
    }
}
