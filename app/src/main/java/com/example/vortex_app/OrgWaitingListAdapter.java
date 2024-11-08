package com.example.vortex_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * {@code OrgWaitingListAdapter} is a {@link RecyclerView.Adapter} that displays a list of entrants
 * on an event's waiting list. Each item in the RecyclerView shows the entrant's name.
 */
public class OrgWaitingListAdapter extends RecyclerView.Adapter<OrgWaitingListAdapter.WaitingListViewHolder> {

    private List<User> waitingListEntrants;

    /**
     * Constructs an {@code OrgWaitingListAdapter} with a list of entrants on the waiting list.
     *
     * @param waitingListEntrants A {@link List} of {@link User} objects representing entrants on the waiting list.
     */
    public OrgWaitingListAdapter(List<User> waitingListEntrants) {
        this.waitingListEntrants = waitingListEntrants;
    }

    /**
     * Called when RecyclerView needs a new {@link WaitingListViewHolder} to represent an item.
     * Inflates the layout for each waiting list item and creates a new view holder instance.
     *
     * @param parent   The parent view that holds the item views.
     * @param viewType The type of the new view.
     * @return A new {@link WaitingListViewHolder} that holds the inflated view for each waiting list item.
     */
    @NonNull
    @Override
    public WaitingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.waiting_list_item, parent, false);
        return new WaitingListViewHolder(view);
    }

    /**
     * Binds data to each item in the RecyclerView by setting the entrant's name in the TextView.
     *
     * @param holder   The {@link WaitingListViewHolder} that holds the view components for the item.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull WaitingListViewHolder holder, int position) {
        User entrant = waitingListEntrants.get(position);
        holder.textViewEntrantName.setText(entrant.getName());
    }

    /**
     * Returns the total number of items in the waiting list.
     *
     * @return The size of the waiting list.
     */
    @Override
    public int getItemCount() {
        return waitingListEntrants.size();
    }

    /**
     * {@code WaitingListViewHolder} holds references to the views within each item of the RecyclerView.
     * Specifically, it includes a {@link TextView} for displaying the entrant's name.
     */
    static class WaitingListViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEntrantName;

        /**
         * Constructs a {@code WaitingListViewHolder} and initializes the TextView for the entrant's name.
         *
         * @param itemView The view of the item in the RecyclerView.
         */
        public WaitingListViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEntrantName = itemView.findViewById(R.id.textViewEntrantName);
        }
    }
}
