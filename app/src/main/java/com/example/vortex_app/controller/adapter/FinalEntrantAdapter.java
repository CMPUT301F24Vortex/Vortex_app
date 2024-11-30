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

public class FinalEntrantAdapter extends RecyclerView.Adapter<FinalEntrantAdapter.ViewHolder> {

    private final List<User> entrantList;

    public FinalEntrantAdapter(List<User> entrantList) {
        this.entrantList = entrantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_final_entrant, parent, false); // Use your layout file
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = entrantList.get(position);
        holder.textViewName.setText(user.getUserName()); // Display the userName field
    }

    @Override
    public int getItemCount() {
        return entrantList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }
}
