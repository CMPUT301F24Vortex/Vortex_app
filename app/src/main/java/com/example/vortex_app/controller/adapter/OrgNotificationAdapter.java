package com.example.vortex_app.controller.adapter;

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
import com.example.vortex_app.R;
import com.example.vortex_app.model.OrgNotification;
import com.example.vortex_app.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrgNotificationAdapter extends android.widget.ArrayAdapter<OrgNotification> {
    private Context context;
    private List<OrgNotification> notifications;

    public OrgNotificationAdapter(Context context, List<OrgNotification> notifications) {
        super(context, R.layout.list_item_org_notification, notifications);
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_org_notification, parent, false);
        }

        TextView titleView = convertView.findViewById(R.id.org_notification_title);
        TextView messageView= convertView.findViewById(R.id.org_notification_message);
        TextView dateView = convertView.findViewById(R.id.org_notification_date);
        OrgNotification notification = notifications.get(position);
        titleView.setText(notification.getTitle());
        messageView.setText(notification.getMessage());
        Date date = notification.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(date);
        dateView.setText(formattedDate);

        return convertView;
    }


}