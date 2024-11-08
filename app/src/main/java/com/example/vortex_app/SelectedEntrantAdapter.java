package com.example.vortex_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * {@code SelectedEntrantAdapter} is a {@link RecyclerView.Adapter} that displays a list of selected entrants
 * in a RecyclerView. Each item in the RecyclerView shows the entrant's name, and optionally, their signup status.
 */
public class SelectedEntrantAdapter extends RecyclerView.Adapter<SelectedEntrantAdapter.SelectedEntrantViewHolder> {

    private List<User> selectedEntrantList;

    /**
     * Constructs a {@code SelectedEntrantAdapter} with a list of selected entrants.
     *
     * @param selectedEntrantList A {@link List} of {@link User} objects representing selected entrants.
     */
    public SelectedEntrantAdapter(List<User> selectedEntrantList) {
        this.selectedEntrantList = selectedEntrantList;
    }

    /**
     * Called when RecyclerView needs a new {@link SelectedEntrantViewHolder} to represent an item.
     * Inflates the layout for each entrant item and creates a new view holder instance.
     *
     * @param parent   The parent view that holds the item views.
     * @param viewType The type of the new view.
     * @return A new {@link SelectedEntrantViewHolder} that holds the inflated view for each entrant item.
     */
    @NonNull
    @Override
    public SelectedEntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new SelectedEntrantViewHolder(view);
    }

    /**
     * Binds data to each item in the RecyclerView by setting the entrant's name in the TextView.
     * An optional status TextView can be used to indicate signup confirmation.
     *
     * @param holder   The {@link SelectedEntrantViewHolder} that holds the view components for the item.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull SelectedEntrantViewHolder holder, int position) {
        User entrant = selectedEntrantList.get(position);
        holder.textViewEntrantName.setText(entrant.getName());
        // holder.textViewSignUpStatus.setText("Sign Up: " + (entrant.isConfirmed() ? "Confirmed" : "Not Confirmed"));
    }

    /**
     * Returns the total number of items in the list of selected entrants.
     *
     * @return The size of the selected entrant list.
     */
    @Override
    public int getItemCount() {
        return selectedEntrantList.size();
    }

    /**
     * {@code SelectedEntrantViewHolder} holds references to the views within each item of the RecyclerView.
     * Specifically, it includes a {@link TextView} for displaying the entrant's name, and optionally, their signup status.
     */
    static class SelectedEntrantViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEntrantName;
        TextView textViewSignUpStatus;

        /**
         * Constructs a {@code SelectedEntrantViewHolder} and initializes the TextViews for the entrant's name
         * and signup status.
         *
         * @param itemView The view of the item in the RecyclerView.
         */
        public SelectedEntrantViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEntrantName = itemView.findViewById(R.id.textViewEntrantName);
            // textViewSignUpStatus = itemView.findViewById(R.id.textViewSignUpStatus);
        }
    }
}
