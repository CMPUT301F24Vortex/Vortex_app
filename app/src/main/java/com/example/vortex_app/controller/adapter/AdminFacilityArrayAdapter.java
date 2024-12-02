package com.example.vortex_app.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vortex_app.R;
import com.example.vortex_app.model.Facility;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter for displaying facilities in a ListView with details such as name, ID, and address.
 * Used by the admin interface to manage and display facility information.
 */
public class AdminFacilityArrayAdapter extends ArrayAdapter<Facility> {

    private final ArrayList<Facility> facilities;
    private final Context context;

    /**
     * Constructor to initialize the adapter with context and list of facilities.
     *
     * @param context The context where the adapter will be used.
     * @param facilities The list of Facility objects to be displayed.
     */
    public AdminFacilityArrayAdapter(Context context, ArrayList<Facility> facilities) {
        super(context, 0, facilities);
        this.facilities = facilities;
        this.context = context;
    }

    /**
     * Returns a view for each item in the ListView.
     *
     * @param position The position of the item within the list.
     * @param convertView The recycled view, or null if no view is available.
     * @param parent The parent ViewGroup.
     * @return The view representing the current facility item.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        // Recycle the view if possible, or create a new one
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.admin_facility_listitem, parent, false);
        }

        // Get the facility object for the current position
        Facility facility = facilities.get(position);

        // Get references to the TextViews for displaying the facility details
        TextView facilityName = view.findViewById(R.id.textView_facilityname);
        TextView organizerID = view.findViewById(R.id.textView_facilityid);
        TextView facilityAddress = view.findViewById(R.id.textView_facilityaddress);

        // Set the facility details in the TextViews
        facilityName.setText(facility.getFacilityName());
        organizerID.setText(facility.getOrganizerID());
        facilityAddress.setText(facility.getAddress());

        return view;
    }
}
