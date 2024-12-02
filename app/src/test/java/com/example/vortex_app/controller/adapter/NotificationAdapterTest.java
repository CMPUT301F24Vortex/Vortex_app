package com.example.vortex_app.controller.adapter;

import static org.junit.Assert.assertEquals;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.NotificationModel;
import com.example.vortex_app.view.notification.NotificationDetailActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33) // Use a supported SDK
public class NotificationAdapterTest {

    private NotificationAdapter adapter;
    private List<NotificationModel> notificationList;

    @Before
    public void setUp() {
        // Initialize mock notification data
        notificationList = new ArrayList<>();
        notificationList.add(new NotificationModel("Title1", "Message1", "Read", "1", new Date()));
        notificationList.add(new NotificationModel("Title2", "Message2", "Unread", "2", new Date()));

        // Initialize adapter with real context
        adapter = new NotificationAdapter(notificationList, RuntimeEnvironment.getApplication());
    }

    @Test
    public void testGetItemCount() {
        // Verify the adapter returns the correct item count
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testOnCreateViewHolder() {
        // Create a parent ViewGroup
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());

        // Create ViewHolder
        RecyclerView.ViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Verify the ViewHolder is not null and is of the correct type
        assertEquals(NotificationAdapter.ViewHolder.class, holder.getClass());
    }

    @Test
    public void testOnBindViewHolder() {
        // Create a parent ViewGroup
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());

        // Create ViewHolder
        NotificationAdapter.ViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Bind the first item
        adapter.onBindViewHolder(holder, 0);

        // Verify data binding
        TextView titleTextView = holder.itemView.findViewById(R.id.titleTextView);
        TextView messageTextView = holder.itemView.findViewById(R.id.messageTextView);
        TextView statusTextView = holder.itemView.findViewById(R.id.statusTextView);

        assertEquals("Title1", titleTextView.getText().toString());
        assertEquals("Message1", messageTextView.getText().toString());
        assertEquals("Read", statusTextView.getText().toString());
    }

    @Test
    public void testOnItemClick() {
        // Create a parent ViewGroup
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());

        // Create ViewHolder
        NotificationAdapter.ViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Bind the first item
        adapter.onBindViewHolder(holder, 0);

        // Simulate item click
        holder.itemView.performClick();

        // Verify the intent sent
        ShadowApplication shadowApplication = ShadowApplication.getInstance();
        Intent nextIntent = shadowApplication.getNextStartedActivity();

        assertEquals(NotificationDetailActivity.class.getName(), nextIntent.getComponent().getClassName());
        assertEquals("Title1", nextIntent.getStringExtra("title"));
        assertEquals("Message1", nextIntent.getStringExtra("message"));
        assertEquals("Read", nextIntent.getStringExtra("status"));
    }

}
