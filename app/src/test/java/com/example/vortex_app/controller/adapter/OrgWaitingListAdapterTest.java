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
@Config(sdk = 33) // Use a supported API level
public class OrgWaitingListAdapterTest {

    private OrgWaitingListAdapter adapter;
    private List<User> users;

    @Before
    public void setUp() {
        // Mock user data
        users = new ArrayList<>();
        users.add(new User("John", "Doe", "user1"));
        users.add(new User("Jane", "Smith", "user2"));

        // Initialize the adapter
        adapter = new OrgWaitingListAdapter(users);
    }

    @Test
    public void testGetItemCount() {
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testOnCreateViewHolder() {
        // Mock parent ViewGroup
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());

        // Create ViewHolder
        RecyclerView.ViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Verify the ViewHolder is of the correct type
        assertEquals(OrgWaitingListAdapter.ViewHolder.class, holder.getClass());
    }

    @Test
    public void testOnBindViewHolder() {
        // Mock parent ViewGroup
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());

        // Create ViewHolder
        OrgWaitingListAdapter.ViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Bind the first item
        adapter.onBindViewHolder(holder, 0);

        // Verify data binding
        TextView userNameTextView = holder.itemView.findViewById(R.id.text_user_name);
        assertEquals("John Doe", userNameTextView.getText().toString());
    }

    @Test
    public void testUpdateData() {
        // Create a new list of users
        List<User> newUsers = new ArrayList<>();
        newUsers.add(new User("Alice", "Johnson", "user3"));
        newUsers.add(new User("Bob", "Brown", "user4"));

        // Update the adapter's dataset
        adapter.updateData(newUsers);

        // Verify the updated data and item count
        assertEquals(2, adapter.getItemCount());
        assertEquals("Alice Johnson", newUsers.get(0).getFirstName() + " " + newUsers.get(0).getLastName());
        assertEquals("Bob Brown", newUsers.get(1).getFirstName() + " " + newUsers.get(1).getLastName());
    }
}
