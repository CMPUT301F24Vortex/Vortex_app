package com.example.vortex_app.controller.adapter;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.controller.adapter.CancelledEntrantAdapter;
import com.example.vortex_app.model.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@Config(manifest = Config.NONE, sdk = {28})
@RunWith(RobolectricTestRunner.class)
public class CancelledEntrantAdapterTest {

    private CancelledEntrantAdapter adapter;
    private List<User> mockEntrantList;

    @Before
    public void setUp() {
        mockEntrantList = new ArrayList<>();
        mockEntrantList.add(new User("John", "Doe", "user1"));
        mockEntrantList.add(new User("Jane", "Smith", "user2"));
        adapter = new CancelledEntrantAdapter(mockEntrantList);
    }

    @Test
    public void testGetItemCount() {
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testOnBindViewHolder() {
        Context context = RuntimeEnvironment.getApplication();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup parent = (ViewGroup) inflater.inflate(R.layout.item_cancelled_entrant, null);

        CancelledEntrantAdapter.ViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder, 0);

        TextView textViewName = holder.itemView.findViewById(R.id.textViewName);
        assertEquals("John Doe", textViewName.getText().toString());
    }
}
