package com.example.vortex_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * {@code CenterAdapter} is a {@link RecyclerView.Adapter} that displays a list of community centers.
 * Each item in the RecyclerView shows the name and address of a center, along with an icon indicating location.
 * The adapter also supports click events on each center item.
 *
 * <p>This adapter uses a {@link CenterViewHolder} to bind data from {@link Center} objects to the views,
 * providing an efficient way to display the list and handle user interactions.
 */
public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.CenterViewHolder> {

    private List<Center> centerList;
    private OnItemClickListener listener;

    /**
     * Interface definition for a callback to be invoked when a center item is clicked.
     */
    public interface OnItemClickListener {
        /**
         * Called when a center item is clicked.
         *
         * @param center The {@link Center} associated with the clicked item.
         */
        void onItemClick(Center center);
    }

    /**
     * Constructs a {@code CenterAdapter} with a list of centers and an item click listener.
     *
     * @param centerList A {@link List} of {@link Center} objects to display.
     * @param listener   An {@link OnItemClickListener} for handling item click events.
     */
    public CenterAdapter(List<Center> centerList, OnItemClickListener listener) {
        this.centerList = centerList;
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new {@link CenterViewHolder} to represent an item.
     * Inflates the layout for each center item and creates a new view holder instance.
     *
     * @param parent   The parent view that holds the item views.
     * @param viewType The type of the new view.
     * @return A new {@link CenterViewHolder} that holds the inflated view for each center item.
     */
    @NonNull
    @Override
    public CenterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.center_item, parent, false);
        return new CenterViewHolder(view);
    }

    /**
     * Binds data to each item in the RecyclerView by setting the center's name, address,
     * and location icon, and by setting up a click listener for each item.
     *
     * @param holder   The {@link CenterViewHolder} that holds the view components for the item.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull CenterViewHolder holder, int position) {
        Center center = centerList.get(position);
        holder.centerName.setText(center.getName());
        holder.centerAddress.setText(center.getAddress());
        holder.locationIcon.setImageResource(R.drawable.ic_location_on);  // Set location icon

        // Set the click listener for each item
        holder.itemView.setOnClickListener(v -> listener.onItemClick(center));
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return The size of the center list.
     */
    @Override
    public int getItemCount() {
        return centerList.size();
    }

    /**
     * {@code CenterViewHolder} holds references to the views within each item of the RecyclerView.
     * It specifically includes a {@link TextView} for the center's name, a {@link TextView} for the address,
     * and an {@link ImageView} for the location icon.
     */
    public static class CenterViewHolder extends RecyclerView.ViewHolder {

        TextView centerName, centerAddress;
        ImageView locationIcon;

        /**
         * Constructs a {@code CenterViewHolder} and initializes the views for the center's name, address,
         * and location icon.
         *
         * @param itemView The view of the item in the RecyclerView.
         */
        public CenterViewHolder(@NonNull View itemView) {
            super(itemView);
            centerName = itemView.findViewById(R.id.text_center_name);
            centerAddress = itemView.findViewById(R.id.text_center_address);
            locationIcon = itemView.findViewById(R.id.icon_location);
        }
    }
}
