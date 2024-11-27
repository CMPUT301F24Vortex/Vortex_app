package com.example.vortex_app.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.User;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<User> locationEntrants;
    private Context context;

    public LocationAdapter(Context context, List<User> locationEntrants) {
        this.context = context;
        this.locationEntrants = locationEntrants;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location_entrant, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        User entrant = locationEntrants.get(position);
        holder.textViewName.setText(entrant.getFullName());

        // fix with database functionality
        //holder.textViewAddress.setText(entrant.getAddress());
        //holder.textViewDateTime.setText(entrant.getDateTime());

        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, EntrantLocationDetailActivity.class);
//            intent.putExtra("ENTRANT_NAME", entrant.getName());
//            intent.putExtra("ADDRESS", entrant.getAddress());
//            intent.putExtra("DATE_TIME", entrant.getDateTime());
//            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return locationEntrants.size();
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewAddress;
        TextView textViewDateTime;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewEntrantName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
        }
    }
}