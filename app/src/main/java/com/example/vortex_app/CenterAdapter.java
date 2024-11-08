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
 * Adapter class for displaying a list of centers in a RecyclerView.
 * This adapter binds data from the Center objects to views in the RecyclerView.
 */
public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.CenterViewHolder> {

    private List<Center> centerList;
    private OnItemClickListener listener;

    /**
     * Interface for handling item click events.
     */
    public interface OnItemClickListener {
        /**
         * Called when an item is clicked.
         *
         * @param center The Center object that was clicked.
         */
        void onItemClick(Center center);
    }

    /**
     * Constructs a CenterAdapter with a list of centers and an item click listener.
     *
     * @param centerList The list of Center objects to be displayed.
     * @param listener   The listener to handle item click events.
     */
    public CenterAdapter(List<Center> centerList, OnItemClickListener listener) {
        this.centerList = centerList;
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new CenterViewHolder that holds a View for each item in the list.
     */
    @NonNull
    @Override
    public CenterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each center item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.center_item, parent, false);
        return new CenterViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the ViewHolder to reflect the item at the given position.
     *
     * @param holder   The CenterViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull CenterViewHolder holder, int position) {
        // Get the current center object
        Center center = centerList.get(position);

        // Bind center data to the view holder's views
        holder.centerName.setText(center.getName());
        holder.centerAddress.setText(center.getAddress());
        holder.locationIcon.setImageResource(R.drawable.ic_location_on);  // Set location icon

        // Set the click listener for each item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(center);  // Notify listener of the clicked center
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return centerList.size();
    }

    /**
     * ViewHolder class for handling each item in the RecyclerView.
     */
    public static class CenterViewHolder extends RecyclerView.ViewHolder {

        TextView centerName, centerAddress;
        ImageView locationIcon;  // ImageView to display the location icon

        /**
         * Constructs a CenterViewHolder, initializing the views to display a center's data.
         *
         * @param itemView The view of the individual item.
         */
        public CenterViewHolder(@NonNull View itemView) {
            super(itemView);
            centerName = itemView.findViewById(R.id.text_center_name);
            centerAddress = itemView.findViewById(R.id.text_center_address);
            locationIcon = itemView.findViewById(R.id.icon_location);  // Ensure this ID matches the one in the XML layout
        }
    }
}
