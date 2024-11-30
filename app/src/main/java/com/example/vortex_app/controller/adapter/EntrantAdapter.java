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

public class EntrantAdapter extends RecyclerView.Adapter<EntrantAdapter.EntrantViewHolder> {

    private final List<Entrant> entrantList;
    private final OnEntrantClickListener listener;

    public interface OnEntrantClickListener {
        void onEntrantClick(Entrant entrant);
    }

    public EntrantAdapter(List<Entrant> entrantList, OnEntrantClickListener listener) {
        this.entrantList = entrantList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entrant, parent, false);
        return new EntrantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntrantViewHolder holder, int position) {
        Entrant entrant = entrantList.get(position);
        holder.nameTextView.setText(entrant.getFirstName() + " " + entrant.getLastName());
        holder.itemView.setOnClickListener(v -> listener.onEntrantClick(entrant));
    }

    @Override
    public int getItemCount() {
        return entrantList.size();
    }

    static class EntrantViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public EntrantViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewEntrantName);
        }
    }
}
