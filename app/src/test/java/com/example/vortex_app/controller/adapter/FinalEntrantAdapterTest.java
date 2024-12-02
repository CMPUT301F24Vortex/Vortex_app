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

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33) // Use a compatible SDK version
public class FinalEntrantAdapterTest {

    private FinalEntrantAdapter adapter;
    private List<User> entrantList;

    @Before
    public void setUp() {
        // Mock data for testing
        entrantList = new ArrayList<>();
        entrantList.add(new User("John", "Doe", "user1"));
        entrantList.add(new User("Jane", "Smith", "user2"));

        // Initialize the adapter with the mock list
        adapter = new FinalEntrantAdapter(entrantList);
    }

    @Test
    public void testGetItemCount() {
        // Verify the adapter returns the correct count
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testOnCreateViewHolder() {
        // Create a parent ViewGroup
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());

        // Create a ViewHolder
        RecyclerView.ViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Verify the ViewHolder is not null
        assertEquals(FinalEntrantAdapter.ViewHolder.class, holder.getClass());
    }

    @Test
    public void testOnBindViewHolder() {
        // Create a parent ViewGroup
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());

        // Create a ViewHolder
        FinalEntrantAdapter.ViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Bind the first item to the ViewHolder
        adapter.onBindViewHolder(holder, 0);

        // Verify the name is displayed correctly
        TextView textViewName = holder.itemView.findViewById(R.id.textViewName);
        assertEquals("John Doe", textViewName.getText().toString());
    }
}
