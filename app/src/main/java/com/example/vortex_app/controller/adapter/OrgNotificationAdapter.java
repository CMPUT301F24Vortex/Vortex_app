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
import com.example.vortex_app.model.OrgNotification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * {@code OrgNotificationAdapter} is a custom adapter for displaying a list of organization notifications.
 * It extends {@link ArrayAdapter} and binds {@link OrgNotification} objects to the list items in the view.
 * Each item shows the notification's title, message, and date.
 */
public class OrgNotificationAdapter extends ArrayAdapter<OrgNotification> {
    private Context context;
    private List<OrgNotification> notifications;

    /**
     * Constructs a new {@code OrgNotificationAdapter} with the provided context and list of notifications.
     *
     * @param context The context in which the adapter is used, typically the activity or fragment.
     * @param notifications A list of {@link OrgNotification} objects representing the notifications to display.
     */
    public OrgNotificationAdapter(Context context, List<OrgNotification> notifications) {
        super(context, R.layout.list_item_org_notification, notifications);
        this.context = context;
        this.notifications = notifications;
    }

    /**
     * Creates and returns a view for each item in the list. Binds the notification's data (title, message, and date)
     * to the corresponding views in the layout.
     *
     * @param position The position of the item within the data set.
     * @param convertView A recycled view to reuse, or null if no view is available for recycling.
     * @param parent The parent view group that this view will be attached to.
     * @return The view for the list item.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_org_notification, parent, false);
        }

        TextView titleView = convertView.findViewById(R.id.org_notification_title);
        TextView messageView = convertView.findViewById(R.id.org_notification_message);
        TextView dateView = convertView.findViewById(R.id.org_notification_date);

        // Retrieve the notification at the specified position
        OrgNotification notification = notifications.get(position);

        // Set the title, message, and formatted date for the notification
        titleView.setText(notification.getTitle());
        messageView.setText(notification.getMessage());
        Date date = notification.getDate();

        // Format the date to "yyyy-MM-dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(date);
        dateView.setText(formattedDate);

        return convertView;
    }
}
