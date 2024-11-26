package com.example.vortex_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AdminConfirmFacilityDeleteFragment extends DialogFragment {

    interface AdminConfirmFacilityDeleteListener {
        void deleteFacility(String facilityID);
    }
    private AdminConfirmFacilityDeleteListener listener;
    private String facilityID;

    AdminConfirmFacilityDeleteFragment (String facilityID) {this.facilityID = facilityID;}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AdminConfirmFacilityDeleteListener) {
            listener = (AdminConfirmFacilityDeleteListener) context;
        }
        else {
            throw new RuntimeException(context + " must implement AdminConfirmDeleteFacilityListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_admin_confirmdelete, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Delete facility")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", (dialog, which) -> {
                    listener.deleteFacility(facilityID);
                })
                .create();
    }
}
