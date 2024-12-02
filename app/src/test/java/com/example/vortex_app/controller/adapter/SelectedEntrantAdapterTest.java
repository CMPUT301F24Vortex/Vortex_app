package com.example.vortex_app.controller.adapter;

import static org.junit.Assert.assertEquals;

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

/**
 * Unit test for SelectedEntrantAdapter to verify item count, view creation, and data binding.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 28) // Use SDK 28 to avoid legacy resources mode issues
public class SelectedEntrantAdapterTest {

    private SelectedEntrantAdapter adapter;
    private List<User> entrantList;

    @Before
    public void setUp() {
        // Mock user data
        entrantList = new ArrayList<>();
        entrantList.add(new User("John", "Doe", "user1"));
        entrantList.add(new User("Jane", "Smith", "user2"));

        // Initialize the adapter
        adapter = new SelectedEntrantAdapter(entrantList);
    }

    @Test
    public void testGetItemCount() {
        // Verify the adapter returns the correct item count
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testOnCreateViewHolder() {
        // Mock a parent ViewGroup using a FrameLayout
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());

        // Inflate the layout for the item (simulating the real parent layout)
        LayoutInflater.from(parent.getContext()).inflate(R.layout.entrant_list_item_for_selected_activity, parent, false);

        // Create ViewHolder
        RecyclerView.ViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Verify the ViewHolder is not null and is of the correct type
        assertEquals(SelectedEntrantAdapter.ViewHolder.class, holder.getClass());
    }

    @Test
    public void testOnBindViewHolder() {
        // Mock a parent ViewGroup using a FrameLayout
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());

        // Inflate the layout for the item (simulating the real parent layout)
        LayoutInflater.from(parent.getContext()).inflate(R.layout.entrant_list_item_for_selected_activity, parent, false);

        // Create ViewHolder
        SelectedEntrantAdapter.ViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Bind the first item
        adapter.onBindViewHolder(holder, 0);

        // Verify data binding
        TextView textViewName = holder.itemView.findViewById(R.id.textViewName);
        assertEquals("John Doe", textViewName.getText().toString());
    }
}
