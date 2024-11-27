package com.example.vortex_app.view.admin;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.bumptech.glide.Glide;
import com.example.vortex_app.R;

import java.util.ArrayList;

public class AdminUserArrayAdapter extends ArrayAdapter<User> {
    private ArrayList<User> users;
    private Context context;

    public AdminUserArrayAdapter (Context context, ArrayList<User> users) {
        super(context, 0, users);
        this.users = users;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.admin_profile_listitem, parent, false);
        }

        User user = users.get(position);

        TextView userName = view.findViewById(R.id.textView_username);
        TextView userID = view.findViewById(R.id.textView_userid);
        ImageView userProfilePic = view.findViewById(R.id.imageView_profilepic);

        userName.setText(user.getFullName());
        userID.setText(user.getUserID());

        // load user pfp
        String profilePicUrl = user.getProfilePicUrl();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            Glide.with(this.getContext())
                    .load(profilePicUrl)
                    .placeholder(R.drawable.profile) // Placeholder image
                    .into(userProfilePic);
        }

        return view;
    }
}
