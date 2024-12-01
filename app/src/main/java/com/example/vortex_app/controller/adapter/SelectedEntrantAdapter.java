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

public class SelectedEntrantAdapter extends RecyclerView.Adapter<SelectedEntrantAdapter.ViewHolder> {

    private final List<User> entrantList;

    public SelectedEntrantAdapter(List<User> entrantList) {
        this.entrantList = entrantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entrant_list_item_for_selected_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = entrantList.get(position);

        // Set the user's full name (firstName + lastName) for display
        holder.textViewName.setText(user.getFullName());
    }

    @Override
    public int getItemCount() {
        return entrantList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName); // Ensure this matches your layout's TextView ID
        }
    }
}
