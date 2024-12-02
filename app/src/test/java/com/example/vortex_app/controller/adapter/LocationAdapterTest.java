package com.example.vortex_app.controller.adapter;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33) // Use a supported SDK
public class LocationAdapterTest {

    private LocationAdapter adapter;
    private List<User> locationEntrants;
    private Context context;

    @Before
    public void setUp() {
        // Initialize context and mock entrant data
        context = RuntimeEnvironment.getApplication();
        locationEntrants = new ArrayList<>();
        locationEntrants.add(new User("John", "Doe", "user1"));
        locationEntrants.add(new User("Jane", "Smith", "user2"));

        // Initialize adapter
        adapter = new LocationAdapter(context, locationEntrants);
    }

    @Test
    public void testGetItemCount() {
        // Verify the item count matches the size of the locationEntrants list
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testOnCreateViewHolder() {
        // Create a parent ViewGroup
        ViewGroup parent = new FrameLayout(context);

        // Create ViewHolder
        RecyclerView.ViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Verify the ViewHolder is not null and is of correct type
        assertEquals(LocationAdapter.LocationViewHolder.class, holder.getClass());
    }

    @Test
    public void testOnBindViewHolder() {
        // Create a parent ViewGroup
        ViewGroup parent = new FrameLayout(context);

        // Create ViewHolder
        LocationAdapter.LocationViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Bind the first item
        adapter.onBindViewHolder(holder, 0);

        // Verify data binding to TextViews
        TextView textViewName = holder.itemView.findViewById(R.id.textViewEntrantName);
        assertEquals("John Doe", textViewName.getText().toString());

        // Additional checks can be added if other fields are updated in the future.
    }
}
