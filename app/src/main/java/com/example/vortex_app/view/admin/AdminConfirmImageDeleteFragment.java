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

public class AdminConfirmImageDeleteFragment extends DialogFragment {

    interface AdminConfirmImageDeleteListener {
        void deleteImage(String imageURL);
    }
    private AdminConfirmImageDeleteListener listener;
    private String imageURL;

    AdminConfirmImageDeleteFragment (String imageURL) {this.imageURL = imageURL;}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AdminConfirmImageDeleteListener) {
            listener = (AdminConfirmImageDeleteListener) context;
        }
        else {
            throw new RuntimeException(context + " must implement AdminConfirmDeleteImageListener");
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
                .setTitle("Delete image")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", (dialog, which) -> {
                    listener.deleteImage(imageURL);
                })
                .create();
    }
}
