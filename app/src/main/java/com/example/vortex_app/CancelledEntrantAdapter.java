package com.example.vortex_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * {@code CancelledEntrantAdapter} is a {@link RecyclerView.Adapter} designed to display a list of
 * cancelled entrants in a RecyclerView. Each item in the list shows the name of an entrant who
 * was previously registered but is now cancelled.
 *
 * <p>This adapter uses a {@link RecyclerView.ViewHolder} to bind data from a {@link User} object to the view,
 * allowing efficient scrolling and display of entrant names.
 */
public class CancelledEntrantAdapter extends RecyclerView.Adapter<CancelledEntrantAdapter.EntrantViewHolder> {

    private List<User> entrantList;

    /**
     * Constructs a {@code CancelledEntrantAdapter} with a list of cancelled entrants.
     *
     * @param entrantList A {@link List} of {@link User} objects representing the cancelled entrants.
     */
    public CancelledEntrantAdapter(List<User> entrantList) {
        this.entrantList = entrantList;
    }

    /**
     * Called when RecyclerView needs a new {@link EntrantViewHolder} to represent an item.
     * Inflates the item layout and creates a new view holder instance.
     *
     * @param parent   The parent view that holds the item views.
     * @param viewType The type of the new view.
     * @return A new {@link EntrantViewHolder} that holds the inflated view for each cancelled entrant item.
     */
    @NonNull
    @Override
    public EntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new EntrantViewHolder(view);
    }

    /**
     * Binds data to each item in the RecyclerView by setting the entrant's name in the TextView.
     *
     * @param holder   The {@link EntrantViewHolder} that holds the view components for the item.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull EntrantViewHolder holder, int position) {
        User entrant = entrantList.get(position);
        holder.textViewEntrantName.setText(entrant.getName());
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return The size of the entrant list.
     */
    @Override
    public int getItemCount() {
        return entrantList.size();
    }

    /**
     * {@code EntrantViewHolder} holds references to the views within each item of the RecyclerView.
     * Specifically, it contains a {@link TextView} that displays the entrant's name.
     */
    static class EntrantViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEntrantName;

        /**
         * Constructs an {@code EntrantViewHolder} and initializes the TextView for the entrant's name.
         *
         * @param itemView The view of the item in the RecyclerView.
         */
        public EntrantViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEntrantName = itemView.findViewById(R.id.textViewEntrantName);
        }
    }
}
