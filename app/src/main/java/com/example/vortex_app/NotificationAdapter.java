package com.example.vortex_app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vortex_app.NotificationModel;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.Locale;

/**
 * {@code NotificationAdapter} is a custom {@link RecyclerView.Adapter} used to display a list of notifications in a RecyclerView.
 * Each item in the RecyclerView displays details of a notification, including title, message, status, and timestamp.
 *
 * <p>This adapter also handles user interactions, opening {@link NotificationDetailActivity} when a notification is clicked.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<NotificationModel> notificationList;
    private OnNotificationClickListener listener;
    private final Context context;

    /**
     * Constructs a {@code NotificationAdapter} with a list of notifications and a context.
     *
     * @param notificationList A list of {@link NotificationModel} objects representing the notifications to display.
     * @param context          The {@link Context} used for starting activities and accessing resources.
     */
    public NotificationAdapter(List<NotificationModel> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    /**
     * Interface definition for a callback to be invoked when a notification item is clicked.
     */
    public interface OnNotificationClickListener {
        /**
         * Called when a notification item is clicked.
         *
         * @param notification The {@link NotificationModel} associated with the clicked item.
         */
        void onNotificationClick(NotificationModel notification);
    }

    /**
     * Inflates the layout for each notification item in the RecyclerView.
     *
     * @param parent   The parent view that holds the item views.
     * @param viewType The type of view to create.
     * @return A new {@link ViewHolder} that holds the view for each notification item.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds data to each item in the RecyclerView.
     * Sets the notification details, formats the timestamp, and assigns a click listener to each item
     * that opens the {@link NotificationDetailActivity} when clicked.
     *
     * @param holder   The {@link ViewHolder} that holds the view components.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel notification = notificationList.get(position);
        holder.titleTextView.setText(NotificationModel.getTitle());
        holder.messageTextView.setText(NotificationModel.getMessage());
        holder.statusTextView.setText(NotificationModel.getStatus());

        // Format the timestamp before displaying it
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String formattedDate = formatter.format(notification.getTimeStamp());
        holder.dateTextView.setText(formattedDate);

        // Set click listener to open NotificationDetailActivity
        holder.itemView.setOnClickListener(v -> {
            if (context != null) {
                Intent intent = new Intent(context, NotificationDetailActivity.class);
                intent.putExtra("title", NotificationModel.getTitle());
                intent.putExtra("message", NotificationModel.getMessage());
                intent.putExtra("timestamp", notification.getTimeStamp());
                intent.putExtra("status", NotificationModel.getStatus());
                context.startActivity(intent);
            } else {
                Log.e("NotificationAdapter", "Context is null, cannot start NotificationDetailActivity");
            }
        });
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return The size of the notification list.
     */
    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    /**
     * ViewHolder class that represents each item view for a notification.
     * Holds references to the TextViews that display notification details.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, messageTextView, statusTextView, dateTextView;

        /**
         * Constructs a new {@code ViewHolder} for a notification item view.
         *
         * @param itemView The view of the item in the RecyclerView.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
