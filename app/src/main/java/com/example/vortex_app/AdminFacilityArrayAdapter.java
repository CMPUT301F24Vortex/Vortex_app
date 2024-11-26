package com.example.vortex_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdminFacilityArrayAdapter extends ArrayAdapter<Center> {
    private ArrayList<Center> facilities;
    private Context context;

    public AdminFacilityArrayAdapter (Context context, ArrayList<Center> facilities) {
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

        Center facility = facilities.get(position);

        TextView facilityName = view.findViewById(R.id.textView_facilityname);
        TextView facilityID = view.findViewById(R.id.textView_facilityid);
        TextView facilityAddress = view.findViewById(R.id.textView_facilityaddress);

        facilityName.setText(facility.getName());
        facilityID.setText(facility.getFacilityID());
        facilityAddress.setText(facility.getAddress());

        return view;
    }
}
