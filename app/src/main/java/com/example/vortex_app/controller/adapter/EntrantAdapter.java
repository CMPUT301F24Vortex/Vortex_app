package com.example.vortex_app.controller.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.Entrant;

import java.util.List;

/**
 * Adapter for displaying a list of entrants in a RecyclerView.
 * Handles the selection of items and communicates item clicks to the parent activity or fragment.
 */
public class EntrantAdapter extends RecyclerView.Adapter<EntrantAdapter.ViewHolder> {

    private final List<Entrant> entrantList;
    private final OnItemClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION; // Keeps track of selected position

    /**
     * Interface for handling item click events in the RecyclerView.
     */
    public interface OnItemClickListener {
        /**
         * Called when an item is clicked in the RecyclerView.
         *
         * @param entrant  The Entrant object at the clicked position.
         * @param position The position of the clicked item.
         */
        void onItemClick(Entrant entrant, int position);
    }

    /**
     * Constructor for initializing the adapter with a list of entrants and a click listener.
     *
     * @param entrantList The list of entrants to display.
     * @param listener    The listener that will handle item clicks.
     */
    public EntrantAdapter(List<Entrant> entrantList, OnItemClickListener listener) {
        this.entrantList = entrantList;
        this.listener = listener;
    }

    /**
     * Returns the currently selected position in the RecyclerView.
     *
     * @return The position of the selected item.
     */
    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * Updates the selected position and refreshes the RecyclerView to highlight the newly selected item.
     *
     * @param position The new selected position.
     */
    public void setSelectedPosition(int position) {
        int previousPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previousPosition); // Refresh previously selected item
        notifyItemChanged(selectedPosition); // Refresh newly selected item
    }

    /**
     * Creates a new ViewHolder for an item view.
     * This is where you inflate the item layout for each individual item in the RecyclerView.
     *
     * @param parent   The parent view group for the item view.
     * @param viewType The type of the view to create.
     * @return A new ViewHolder containing the item view.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entrant, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds data to a specific item in the RecyclerView.
     * This method is called for each item in the list.
     *
     * @param holder   The ViewHolder for the item.
     * @param position The position of the item within the RecyclerView's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Entrant entrant = entrantList.get(position);
        holder.nameTextView.setText(entrant.getFirstName() + " " + entrant.getLastName());

        // Highlight the selected item
        holder.itemView.setBackgroundColor(
                selectedPosition == position
                        ? holder.itemView.getContext().getResources().getColor(R.color.selected_item, null) // Highlight color
                        : Color.TRANSPARENT // Default color
        );

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(entrant, position);
            }
            setSelectedPosition(position); // Update selection state
        });
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
     * ViewHolder for displaying Entrant details in a RecyclerView.
     * Holds references to the views that display Entrant data.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        /**
         * Constructor for initializing the ViewHolder.
         * Finds and binds the views for displaying entrant details.
         *
         * @param itemView The root view of the item layout.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}
