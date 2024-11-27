package com.example.vortex_app.view.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.vortex_app.R;

public class AdminConfirmEventDeleteFragment extends DialogFragment {

    interface AdminConfirmEventDeleteListener {
        void deleteEvent(String eventID);
    }
    private AdminConfirmEventDeleteListener listener;
    private String eventID;

    AdminConfirmEventDeleteFragment (String eventID) {this.eventID = eventID;}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AdminConfirmEventDeleteListener) {
            listener = (AdminConfirmEventDeleteListener) context;
        }
        else {
            throw new RuntimeException(context + " must implement AdminConfirmDeleteEventListener");
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
                .setTitle("Delete event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", (dialog, which) -> {
                    listener.deleteEvent(eventID);
                })
                .create();
    }
}
