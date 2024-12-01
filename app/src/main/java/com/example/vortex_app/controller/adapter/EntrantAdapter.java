package com.example.vortex_app.controller.adapter;
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

    private List<Entrant> entrantList;
    private OnItemClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnItemClickListener {
        void onItemClick(Entrant entrant, int position);
    }

    public EntrantAdapter(List<Entrant> entrantList, OnItemClickListener listener) {
        this.entrantList = entrantList;
        this.listener = listener;
    }

    public void setSelectedPosition(int position) {
        int previousPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previousPosition); // Update the previously selected item
        notifyItemChanged(selectedPosition); // Update the newly selected item
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
        holder.itemView.setBackgroundColor(selectedPosition == position
                ? holder.itemView.getContext().getResources().getColor(R.color.selected_item) // Highlight color
                : holder.itemView.getContext().getResources().getColor(android.R.color.transparent)); // Default color

        holder.itemView.setOnClickListener(v -> listener.onItemClick(entrant, position));
    }

    @Override
    public int getItemCount() {
        return entrantList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}
