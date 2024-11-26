package com.example.vortex_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FinalEntrantAdapter extends RecyclerView.Adapter<FinalEntrantAdapter.FinalEntrantViewHolder> {

    private List<User> finalEntrantList;

    public FinalEntrantAdapter(List<User> finalEntrantList) {
        this.finalEntrantList = finalEntrantList;
    }

    @NonNull
    @Override
    public FinalEntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new FinalEntrantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinalEntrantViewHolder holder, int position) {
        User entrant = finalEntrantList.get(position);
        holder.textViewEntrantName.setText(entrant.getFullName());
    }

    @Override
    public int getItemCount() {
        return finalEntrantList.size();
    }

    static class FinalEntrantViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEntrantName;

        public FinalEntrantViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEntrantName = itemView.findViewById(R.id.textViewEntrantName);
        }
    }
}