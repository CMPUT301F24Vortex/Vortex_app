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

public class SelectedEntrantAdapter extends RecyclerView.Adapter<SelectedEntrantAdapter.SelectedEntrantViewHolder> {

    private List<User> selectedEntrantList;

    public SelectedEntrantAdapter(List<User> selectedEntrantList) {
        this.selectedEntrantList = selectedEntrantList;
    }

    @NonNull
    @Override
    public SelectedEntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new SelectedEntrantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedEntrantViewHolder holder, int position) {
        User entrant = selectedEntrantList.get(position);
        holder.textViewEntrantName.setText(entrant.getFullName());
        //holder.textViewSignUpStatus.setText("Sign Up: " + (entrant.isConfirmed() ? "Confirmed" : "Not Confirmed"));
    }

    @Override
    public int getItemCount() {
        return selectedEntrantList.size();
    }

    static class SelectedEntrantViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEntrantName;
        TextView textViewSignUpStatus;

        public SelectedEntrantViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEntrantName = itemView.findViewById(R.id.textViewEntrantName);
            //textViewSignUpStatus = itemView.findViewById(R.id.textViewSignUpStatus);
        }
    }
}