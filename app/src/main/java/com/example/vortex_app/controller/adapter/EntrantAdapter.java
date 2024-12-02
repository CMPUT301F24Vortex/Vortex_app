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

public class EntrantAdapter extends RecyclerView.Adapter<EntrantAdapter.ViewHolder> {

    private final List<Entrant> entrantList;
    private final OnItemClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION; // Keeps track of selected position

    /**
     * Interface for handling item click events.
     */
    public interface OnItemClickListener {
        void onItemClick(Entrant entrant, int position);
    }

    public EntrantAdapter(List<Entrant> entrantList, OnItemClickListener listener) {
        this.entrantList = entrantList;
        this.listener = listener;
    }

    /**
     * Returns the currently selected position.
     */
    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * Updates the selected position and refreshes the RecyclerView.
     *
     * @param position The new selected position.
     */
    public void setSelectedPosition(int position) {
        int previousPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previousPosition); // Refresh previously selected item
        notifyItemChanged(selectedPosition); // Refresh newly selected item
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entrant, parent, false);
        return new ViewHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return entrantList.size();
    }

    /**
     * ViewHolder for displaying Entrant details.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}
