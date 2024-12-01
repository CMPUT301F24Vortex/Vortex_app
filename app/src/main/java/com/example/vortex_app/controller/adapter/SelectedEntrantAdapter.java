package com.example.vortex_app.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vortex_app.R;
import com.example.vortex_app.model.User;
import java.util.List;

public class SelectedEntrantAdapter extends RecyclerView.Adapter<SelectedEntrantAdapter.SelectedEntrantViewHolder> {

    private List<User> selectedEntrantList;

    // Constructor
    public SelectedEntrantAdapter(List<User> selectedEntrantList) {
        this.selectedEntrantList = selectedEntrantList;
    }

    @Override
    public SelectedEntrantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new SelectedEntrantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectedEntrantViewHolder holder, int position) {
        User selectedEntrant = selectedEntrantList.get(position);
        holder.userNameTextView.setText(selectedEntrant.getUserName());
    }

    @Override
    public int getItemCount() {
        return selectedEntrantList.size();
    }

    // ViewHolder class
    public static class SelectedEntrantViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView;

        public SelectedEntrantViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.textViewEntrantName);
        }
    }
}
