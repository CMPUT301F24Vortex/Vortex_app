package com.example.vortex_app.controller.adapter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.Facility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30) // Use a supported SDK version
public class FacilityAdapterTest {

    private FacilityAdapter adapter;
    private List<Facility> facilityList;
    private FacilityAdapter.OnItemClickListener mockListener;

    @Before
    public void setUp() {
        // Mock data
        facilityList = new ArrayList<>();
        facilityList.add(new Facility("Facility A", "Address A"));
        facilityList.add(new Facility("Facility B", "Address B"));

        // Mock OnItemClickListener
        mockListener = mock(FacilityAdapter.OnItemClickListener.class);

        // Initialize the adapter
        adapter = new FacilityAdapter(facilityList, mockListener);
    }

    @Test
    public void testGetItemCount() {
        // Verify the item count matches the list size
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testOnBindViewHolder() {
        // Simulate parent ViewGroup
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());

        // Create a ViewHolder
        FacilityAdapter.FacilityViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Bind the first item
        adapter.onBindViewHolder(holder, 0);

        // Verify the facility name and address are set correctly
        TextView textViewFacilityName = holder.itemView.findViewById(R.id.textViewFacilityName);
        TextView textViewFacilityAddress = holder.itemView.findViewById(R.id.textViewFacilityAddress);

        assertEquals("Facility A", textViewFacilityName.getText().toString());
        assertEquals("Address A", textViewFacilityAddress.getText().toString());
    }

    @Test
    public void testOnItemClick() {
        // Create a FrameLayout as the parent ViewGroup
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());

        // Create a ViewHolder
        FacilityAdapter.FacilityViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Bind the first item to the ViewHolder
        adapter.onBindViewHolder(holder, 0);

        // Simulate a click on the itemView
        holder.itemView.performClick();

        // Verify the click listener was triggered with the correct item
        verify(mockListener).onItemClick(facilityList.get(0));
    }

}
