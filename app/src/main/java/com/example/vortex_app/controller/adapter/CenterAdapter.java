package com.example.vortex_app.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.Center;

import java.util.List;

public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.CenterViewHolder> {

    private List<Center> centerList;
    private OnItemClickListener listener;

    // Define the interface for item click events
    public interface OnItemClickListener {
        void onItemClick(Center center);
    }

    // Adapter constructor
    public CenterAdapter(List<Center> centerList, OnItemClickListener listener) {
        this.centerList = centerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CenterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each center item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.center_item, parent, false);
        return new CenterViewHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return centerList.size();
    }

    // ViewHolder class for handling each item in the RecyclerView
    public static class CenterViewHolder extends RecyclerView.ViewHolder {

        TextView centerName, centerAddress;
        ImageView locationIcon;  // ImageView to display the location icon

        public CenterViewHolder(@NonNull View itemView) {
            super(itemView);
            centerName = itemView.findViewById(R.id.text_center_name);
            centerAddress = itemView.findViewById(R.id.text_center_address);
            locationIcon = itemView.findViewById(R.id.icon_location);  // Ensure this ID matches the one in the XML layout
        }
    }
}
