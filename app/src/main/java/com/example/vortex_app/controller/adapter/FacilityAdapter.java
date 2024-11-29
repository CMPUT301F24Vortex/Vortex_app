package com.example.vortex_app.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.Facility;

import java.util.List;

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Facility facility);
    }

    private final List<Facility> facilityList;
    private final OnItemClickListener listener;

    public FacilityAdapter(List<Facility> facilityList, OnItemClickListener listener) {
        this.facilityList = facilityList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_facility, parent, false);
        return new FacilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        Facility facility = facilityList.get(position);
        holder.textViewFacilityName.setText(facility.getFacilityName());
        holder.textViewFacilityAddress.setText(facility.getAddress());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(facility));
    }

    @Override
    public int getItemCount() {
        return facilityList.size();
    }

    public static class FacilityViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFacilityName;
        TextView textViewFacilityAddress;

        public FacilityViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFacilityName = itemView.findViewById(R.id.textViewFacilityName);
            textViewFacilityAddress = itemView.findViewById(R.id.textViewFacilityAddress);
        }
    }
}
