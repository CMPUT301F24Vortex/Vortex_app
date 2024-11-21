package com.example.vortex_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CancelledEntrantAdapter extends RecyclerView.Adapter<CancelledEntrantAdapter.EntrantViewHolder> {

    private List<User> entrantList;

    public CancelledEntrantAdapter(List<User> entrantList) {
        this.entrantList = entrantList;
    }

    @NonNull
    @Override
    public EntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new EntrantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntrantViewHolder holder, int position) {
        User entrant = entrantList.get(position);
        holder.textViewEntrantName.setText(entrant.getFullName());
    }

    @Override
    public int getItemCount() {
        return entrantList.size();
    }

    static class EntrantViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEntrantName;

        public EntrantViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEntrantName = itemView.findViewById(R.id.textViewEntrantName);
        }
    }
}