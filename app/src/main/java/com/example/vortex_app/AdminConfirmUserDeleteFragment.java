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

public class AdminConfirmUserDeleteFragment extends DialogFragment {

    interface AdminConfirmUserDeleteListener {
        void deleteUser(String userID);
    }
    private AdminConfirmUserDeleteListener listener;
    private String userID;

    AdminConfirmUserDeleteFragment (String userID) {this.userID = userID;}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AdminConfirmUserDeleteListener) {
            listener = (AdminConfirmUserDeleteListener) context;
        }
        else {
            throw new RuntimeException(context + " must implement AdminConfirmDeleteUserListener");
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
                .setTitle("Delete user")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", (dialog, which) -> {
                    listener.deleteUser(userID);
                })
                .create();
    }
}
