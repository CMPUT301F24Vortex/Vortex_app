package com.example.vortex_app.controller.adapter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.Entrant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = {28})
public class EntrantAdapterTest {

    private EntrantAdapter adapter;
    private List<Entrant> entrantList;
    private EntrantAdapter.OnItemClickListener mockListener;

    @Before
    public void setUp() {
        // Initialize test data
        entrantList = new ArrayList<>();
        entrantList.add(new Entrant("John", "Doe", 0.0, 0.0));
        entrantList.add(new Entrant("Jane", "Smith", 0.0, 0.0));

        // Mock listener
        mockListener = mock(EntrantAdapter.OnItemClickListener.class);

        // Initialize adapter
        adapter = new EntrantAdapter(entrantList, mockListener);
    }

    @Test
    public void testGetItemCount() {
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testOnBindViewHolder() {
        // Inflate the parent view
        ViewGroup parent = (ViewGroup) LayoutInflater.from(Robolectric.buildActivity(android.app.Activity.class).get())
                .inflate(R.layout.item_entrant, null, false);

        // Create ViewHolder
        EntrantAdapter.ViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Bind ViewHolder
        adapter.onBindViewHolder(holder, 0);

        // Verify data binding
        TextView nameTextView = holder.itemView.findViewById(R.id.nameTextView);
        assertEquals("John Doe", nameTextView.getText().toString());
    }

    @Test
    public void testOnItemClick() {
        // Simulate parent ViewGroup
        ViewGroup parent = (ViewGroup) LayoutInflater.from(Robolectric.buildActivity(android.app.Activity.class).get())
                .inflate(R.layout.item_entrant, null, false);

        // Create and bind ViewHolder
        EntrantAdapter.ViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder, 0);

        // Simulate click
        holder.itemView.performClick();

        // Verify click was handled
        verify(mockListener).onItemClick(entrantList.get(0), 0);
    }
}
