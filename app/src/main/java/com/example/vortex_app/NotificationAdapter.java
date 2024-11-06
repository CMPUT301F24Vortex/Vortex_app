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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationModel> notificationList;
    private OnNotificationClickListener listener;
    private final Context context;

    public NotificationAdapter(List<NotificationModel> notificationList, NotificationsActivity notificationsActivity, Context context) {
        this.notificationList = notificationList;
        this.context = context; // Assign the passed context to the class variable
    }

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationModel notification);
    }

    public NotificationAdapter(List<NotificationModel> notificationList, Context context) {
        this.notificationList = notificationList;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel notification = notificationList.get(position);
        holder.titleTextView.setText(notification.getTitle());
        holder.messageTextView.setText(notification.getMessage());
        holder.statusTextView.setText(notification.getStatus());
        // Format the timestamp before displaying it
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String formattedDate = formatter.format(notification.getTimeStamp());

        holder.dateTextView.setText(formattedDate);

        // Set click listener to open NotificationDetailActivity
        holder.itemView.setOnClickListener(v -> {
            if (context != null) {  // Double-check context is not null before using
                Intent intent = new Intent(context, NotificationDetailActivity.class);
                intent.putExtra("title", notification.getTitle());
                intent.putExtra("message", notification.getMessage());
                intent.putExtra("status", notification.getStatus());
                context.startActivity(intent);
            } else {
                Log.e("NotificationAdapter", "Context is null, cannot start NotificationDetailActivity");
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //TextView dateTextView;
        TextView titleTextView, messageTextView, statusTextView, dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
