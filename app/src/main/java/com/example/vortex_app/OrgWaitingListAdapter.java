package com.example.vortex_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrgWaitingListAdapter extends RecyclerView.Adapter<OrgWaitingListAdapter.WaitingListViewHolder> {

    private List<User> waitingListEntrants;

    public OrgWaitingListAdapter(List<User> waitingListEntrants) {
        this.waitingListEntrants = waitingListEntrants;
    }

    @NonNull
    @Override
    public WaitingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.waiting_list_item, parent, false);
        return new WaitingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaitingListViewHolder holder, int position) {
        User entrant = waitingListEntrants.get(position);
        holder.textViewEntrantName.setText(entrant.getFullName());
    }

    @Override
    public int getItemCount() {
        return waitingListEntrants.size();
    }

    static class WaitingListViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEntrantName;

        public WaitingListViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEntrantName = itemView.findViewById(R.id.textViewEntrantName);
        }
    }
}