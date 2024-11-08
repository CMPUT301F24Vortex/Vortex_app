package com.example.vortex_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * {@code FinalEntrantAdapter} is a {@link RecyclerView.Adapter} used to display a list of final entrants
 * in a RecyclerView. Each item in the RecyclerView shows the name of an entrant who has been selected as a final participant.
 *
 * <p>This adapter uses a {@link FinalEntrantViewHolder} to bind data from {@link User} objects to the view,
 * allowing efficient scrolling and display of entrant names.
 */
public class FinalEntrantAdapter extends RecyclerView.Adapter<FinalEntrantAdapter.FinalEntrantViewHolder> {

    private List<User> finalEntrantList;

    /**
     * Constructs a {@code FinalEntrantAdapter} with a list of final entrants.
     *
     * @param finalEntrantList A {@link List} of {@link User} objects representing the final entrants.
     */
    public FinalEntrantAdapter(List<User> finalEntrantList) {
        this.finalEntrantList = finalEntrantList;
    }

    /**
     * Called when RecyclerView needs a new {@link FinalEntrantViewHolder} to represent an item.
     * Inflates the layout for each entrant item and creates a new view holder instance.
     *
     * @param parent   The parent view that holds the item views.
     * @param viewType The type of the new view.
     * @return A new {@link FinalEntrantViewHolder} that holds the inflated view for each entrant item.
     */
    @NonNull
    @Override
    public FinalEntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new FinalEntrantViewHolder(view);
    }

    /**
     * Binds data to each item in the RecyclerView by setting the entrant's name in the TextView.
     *
     * @param holder   The {@link FinalEntrantViewHolder} that holds the view components for the item.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull FinalEntrantViewHolder holder, int position) {
        User entrant = finalEntrantList.get(position);
        holder.textViewEntrantName.setText(entrant.getName());
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return The size of the final entrant list.
     */
    @Override
    public int getItemCount() {
        return finalEntrantList.size();
    }

    /**
     * {@code FinalEntrantViewHolder} holds references to the views within each item of the RecyclerView.
     * It specifically includes a {@link TextView} for displaying the entrant's name.
     */
    static class FinalEntrantViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEntrantName;

        /**
         * Constructs a {@code FinalEntrantViewHolder} and initializes the TextView for the entrant's name.
         *
         * @param itemView The view of the item in the RecyclerView.
         */
        public FinalEntrantViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEntrantName = itemView.findViewById(R.id.textViewEntrantName);
        }
    }
}
