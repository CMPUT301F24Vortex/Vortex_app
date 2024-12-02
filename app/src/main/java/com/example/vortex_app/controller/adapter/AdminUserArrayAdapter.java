package com.example.vortex_app.controller.adapter;

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
import com.example.vortex_app.model.User;

import java.util.ArrayList;

/**
 * AdminUserArrayAdapter is an adapter for displaying user profiles in a ListView.
 * It binds user data, such as the full name, user ID, and profile picture, to a custom layout.
 */
public class AdminUserArrayAdapter extends ArrayAdapter<User> {
    private ArrayList<User> users;
    private Context context;

    /**
     * Constructor to initialize the adapter with the provided context and list of users.
     *
     * @param context The context where the adapter is being used (usually an Activity or Fragment).
     * @param users   The list of User objects to be displayed.
     */
    public AdminUserArrayAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
        this.users = users;
        this.context = context;
    }

    /**
     * This method is responsible for creating and returning a view for each item in the list.
     * It binds the user data (full name, user ID, and profile picture) to the corresponding views.
     *
     * @param position    The position of the item in the data set.
     * @param convertView The recycled view to reuse, if possible.
     * @param parent      The parent view that this view will eventually be attached to.
     * @return The view for the item at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Reuse or inflate a new view
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.admin_profile_listitem, parent, false);
        }

        // Get the current user at the specified position
        User user = users.get(position);

        // Find the views to update
        TextView userName = view.findViewById(R.id.textView_username);
        TextView userID = view.findViewById(R.id.textView_userid);
        ImageView userProfilePic = view.findViewById(R.id.imageView_profilepic);

        // Set the user name and ID
        userName.setText(user.getFullName());
        userID.setText(user.getUserID());

        // Load the user's profile picture using Glide (with a placeholder)
        String profilePicUrl = user.getAvatarUrl();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            Glide.with(this.getContext())
                    .load(profilePicUrl)
                    .placeholder(R.drawable.profile) // Placeholder image in case the image is loading
                    .into(userProfilePic);
        }

        return view;
    }
}
