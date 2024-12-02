package com.example.vortex_app.controller.adapter;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.OrgNotification;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33) // Use a supported API level
public class OrgNotificationAdapterTest {

    private OrgNotificationAdapter adapter;
    private List<OrgNotification> notifications;
    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.getApplication();

        // Mock notification data
        notifications = new ArrayList<>();
        notifications.add(new OrgNotification("Title1", "Message1", new Date(), "1"));
        notifications.add(new OrgNotification("Title2", "Message2", new Date(), "2"));

        // Initialize the adapter
        adapter = new OrgNotificationAdapter(context, notifications);
    }

    @Test
    public void testGetCount() {
        assertEquals(2, adapter.getCount());
    }

    @Test
    public void testGetItem() {
        assertEquals("Title1", adapter.getItem(0).getTitle());
        assertEquals("Title2", adapter.getItem(1).getTitle());
    }

    @Test
    public void testGetView() {
        ViewGroup parent = new FrameLayout(context);
        View view = adapter.getView(0, null, parent);

        // Verify data binding in the view
        TextView titleView = view.findViewById(R.id.org_notification_title);
        TextView messageView = view.findViewById(R.id.org_notification_message);
        TextView dateView = view.findViewById(R.id.org_notification_date);

        OrgNotification notification = notifications.get(0);

        assertEquals(notification.getTitle(), titleView.getText().toString());
        assertEquals(notification.getMessage(), messageView.getText().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(dateFormat.format(notification.getDate()), dateView.getText().toString());
    }
}

