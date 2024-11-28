package com.example.vortex_app.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vortex_app.R;
import com.example.vortex_app.model.User;
import java.util.List;

public class OrgWaitingListAdapter extends RecyclerView.Adapter<OrgWaitingListAdapter.ViewHolder> {

    private List<User> waitingListEntrants;


    public OrgWaitingListAdapter(List<User> waitingListEntrants) {
        this.waitingListEntrants = waitingListEntrants;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            userNameTextView = itemView.findViewById(R.id.text_user_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.waiting_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        User user = waitingListEntrants.get(position);
        holder.userNameTextView.setText(user.getFirstName() + " " + user.getLastName());
    }

    @Override
    public int getItemCount() {
        // Return the size of the waiting list
        return waitingListEntrants.size();
    }
}
