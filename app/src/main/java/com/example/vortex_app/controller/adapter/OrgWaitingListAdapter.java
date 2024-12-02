package com.example.vortex_app.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.User;

import java.util.List;

/**
 * {@code OrgWaitingListAdapter} is a custom adapter used to display a list of users waiting for a particular event or organization.
 * It binds the user data to a RecyclerView to display their full names in a list.
 */
public class OrgWaitingListAdapter extends RecyclerView.Adapter<OrgWaitingListAdapter.ViewHolder> {

    private List<User> users;

    /**
     * Constructs a new {@code OrgWaitingListAdapter} with the given list of users.
     *
     * @param users A list of {@link User} objects representing the users waiting for an event or organization.
     * @throws IllegalArgumentException if the user list is null.
     */
    public OrgWaitingListAdapter(List<User> users) {
        if (users == null) {
            throw new IllegalArgumentException("User list cannot be null");
        }
        this.users = users;
    }

    /**
     * ViewHolder class that represents each item view for a user in the waiting list.
     * It holds references to the TextViews that display user details.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;

        /**
         * Constructs a new {@code ViewHolder} for a user item view.
         *
         * @param itemView The view of the item in the RecyclerView.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.text_user_name);
        }
    }

    /**
     * Inflates the layout for each user item in the RecyclerView.
     *
     * @param parent The parent view that holds the item views.
     * @param viewType The type of view to create.
     * @return A new {@link ViewHolder} that holds the view for each user item.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.waiting_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Binds data to each item in the RecyclerView. Sets the full name of the user to the TextView.
     *
     * @param holder The {@link ViewHolder} that holds the view components.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);
        if (user != null) {
            // Using StringBuilder for better performance in case of many concatenations
            StringBuilder fullName = new StringBuilder();
            fullName.append(user.getFirstName()).append(" ").append(user.getLastName());
            holder.userNameTextView.setText(fullName.toString());
        }
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return The size of the user list, or 0 if the list is null.
     */
    @Override
    public int getItemCount() {
        return (users == null) ? 0 : users.size();  // Handle the case of null list
    }

    /**
     * Updates the data in the adapter with a new list of users.
     * Clears the existing list and adds all new users, then notifies the adapter that the data has changed.
     *
     * @param newUsers The new list of {@link User} objects to update the dataset.
     * @throws IllegalArgumentException if the new user list is null.
     */
    public void updateData(List<User> newUsers) {
        if (newUsers == null) {
            throw new IllegalArgumentException("New user list cannot be null");
        }
        this.users.clear();
        this.users.addAll(newUsers);
        notifyDataSetChanged();
    }
}
