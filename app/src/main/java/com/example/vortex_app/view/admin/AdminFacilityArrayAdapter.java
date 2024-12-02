package com.example.vortex_app.view.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vortex_app.R;
import com.example.vortex_app.model.Center;
import com.example.vortex_app.model.Facility;

import java.util.ArrayList;

public class AdminFacilityArrayAdapter extends ArrayAdapter<Facility> {
    private ArrayList<Facility> facilities;
    private Context context;

    public AdminFacilityArrayAdapter (Context context, ArrayList<Facility> facilities) {
        super(context, 0, facilities);
        this.facilities = facilities;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.admin_facility_listitem, parent, false);
        }

        Facility facility = facilities.get(position);

        TextView facilityName = view.findViewById(R.id.textView_facilityname);
        TextView organizerID = view.findViewById(R.id.textView_facilityid);
        TextView facilityAddress = view.findViewById(R.id.textView_facilityaddress);

        facilityName.setText(facility.getFacilityName());
        organizerID.setText(facility.getOrganizerID());
        facilityAddress.setText(facility.getAddress());

        return view;
    }
}
