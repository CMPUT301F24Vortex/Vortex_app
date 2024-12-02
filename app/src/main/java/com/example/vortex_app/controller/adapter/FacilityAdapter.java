package com.example.vortex_app.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.Facility;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying a list of facilities in a RecyclerView.
 * Each item shows the facility's name and address.
 */
public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder> {

    /**
     * Interface for handling item click events.
     */
    public interface OnItemClickListener {
        void onItemClick(Facility facility);
    }

    private final List<Facility> facilityList;
    private final OnItemClickListener listener;

    /**
     * Constructor for initializing the adapter with a list of facilities.
     *
     * @param facilityList List of facilities to display in the RecyclerView.
     * @param listener The listener for handling item click events.
     */
    public FacilityAdapter(List<Facility> facilityList, OnItemClickListener listener) {
        this.facilityList = facilityList != null ? facilityList : new ArrayList<>();
        this.listener = listener;
    }

    /**
     * Updates the list of facilities and notifies the adapter that the data has changed.
     *
     * @param newFacilities The new list of facilities to replace the current one.
     */
    public void updateFacilities(List<Facility> newFacilities) {
        facilityList.clear();
        if (newFacilities != null) {
            facilityList.addAll(newFacilities);
        }
        notifyDataSetChanged();
    }

    /**
     * Creates a new ViewHolder for a facility item in the RecyclerView.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The type of the view.
     * @return A new FacilityViewHolder.
     */
    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_facility, parent, false);
        return new FacilityViewHolder(view);
    }

    /**
     * Binds a facility item to a ViewHolder.
     *
     * @param holder The ViewHolder for the item.
     * @param position The position of the item within the list.
     */
    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        Facility facility = facilityList.get(position);
        holder.textViewFacilityName.setText(facility.getFacilityName());
        holder.textViewFacilityAddress.setText(facility.getAddress());

        // Set the click listener for the item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(facility);
            }
        });
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return The number of items.
     */
    @Override
    public int getItemCount() {
        return facilityList.size();
    }

    /**
     * ViewHolder class for binding facility details to the item view.
     */
    public static class FacilityViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFacilityName;
        TextView textViewFacilityAddress;

        /**
         * Constructor for initializing the views of the facility item.
         *
         * @param itemView The view for the item.
         */
        public FacilityViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFacilityName = itemView.findViewById(R.id.textViewFacilityName);
            textViewFacilityAddress = itemView.findViewById(R.id.textViewFacilityAddress);
        }
    }
}
