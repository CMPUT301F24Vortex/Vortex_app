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
 * Adapter for displaying a list of final entrants in a RecyclerView.
 * Each item shows the full name (first name and last name) of an entrant.
 */
public class FinalEntrantAdapter extends RecyclerView.Adapter<FinalEntrantAdapter.ViewHolder> {

    private final List<User> entrantList;

    /**
     * Constructor for initializing the adapter with a list of users (entrants).
     *
     * @param entrantList List of entrants to be displayed in the RecyclerView.
     */
    public FinalEntrantAdapter(List<User> entrantList) {
        this.entrantList = entrantList;
    }

    /**
     * Creates a new ViewHolder for the final entrant item in the RecyclerView.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The type of view to be created.
     * @return A new ViewHolder instance.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_final_entrant, parent, false); // Use your layout file
        return new ViewHolder(view);
    }

    /**
     * Binds data (entrant's full name) to a ViewHolder.
     *
     * @param holder The ViewHolder for the item.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = entrantList.get(position);
        // Display the full name using firstName and lastName
        holder.textViewName.setText(user.getFirstName() + " " + user.getLastName());
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return The number of items in the entrant list.
     */
    @Override
    public int getItemCount() {
        return entrantList.size();
    }

    /**
     * ViewHolder class for binding final entrant data to the item view.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;

        /**
         * Constructor for initializing the views of the final entrant item.
         *
         * @param itemView The view for the item.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }
}
