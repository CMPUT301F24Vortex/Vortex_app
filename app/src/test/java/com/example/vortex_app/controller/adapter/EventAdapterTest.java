package com.example.vortex_app.controller.adapter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vortex_app.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33) // Use SDK 33, as Robolectric supports it
public class EventAdapterTest {

    private EventAdapter adapter;
    private List<String> eventNames;
    private List<String> eventIDs;
    private List<String> eventImageUrls;
    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.getApplication();

        // Initialize test data
        eventNames = Arrays.asList("Event 1", "Event 2");
        eventIDs = Arrays.asList("id1", "id2");
        eventImageUrls = Arrays.asList("https://example.com/image1.jpg", "");

        // Initialize the adapter
        adapter = new EventAdapter(context, eventNames, eventIDs, eventImageUrls);
    }

    @Test
    public void testGetCount() {
        // Verify the adapter returns the correct count
        assertEquals(2, adapter.getCount());
    }

    @Test
    public void testGetItem() {
        // Verify the adapter returns the correct item
        assertEquals("Event 1", adapter.getItem(0));
        assertEquals("Event 2", adapter.getItem(1));
    }

    @Test
    public void testGetItemId() {
        // Verify the adapter returns the correct item ID
        assertEquals(0, adapter.getItemId(0));
        assertEquals(1, adapter.getItemId(1));
    }

    @Test
    public void testGetView() {
        // Mock the parent ViewGroup
        ViewGroup parent = new FrameLayout(context); // Use a concrete implementation

        // Inflate a view for the first item
        View view = adapter.getView(0, null, parent);

        // Verify the event name is set correctly
        TextView textViewEventName = view.findViewById(R.id.text_event_name);
        assertEquals("Event 1", textViewEventName.getText().toString());

        // Verify the ImageView is visible and has content for the first item
        ImageView imageViewEventIndicator = view.findViewById(R.id.image_event);
        assertEquals(View.VISIBLE, imageViewEventIndicator.getVisibility());

        // Inflate a view for the second item
        View view2 = adapter.getView(1, null, parent);

        // Verify the event name is set correctly for the second item
        TextView textViewEventName2 = view2.findViewById(R.id.text_event_name);
        assertEquals("Event 2", textViewEventName2.getText().toString());

        // Verify the ImageView is hidden for the second item (no URL)
        ImageView imageViewEventIndicator2 = view2.findViewById(R.id.image_event);
        assertEquals(View.GONE, imageViewEventIndicator2.getVisibility());
    }

}
