package com.example.vortex_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * OrgWaitingListActivity displays a list of users in the waiting list for an event.
 * It sets up and manages a RecyclerView to display the data.
 */
public class OrgWaitingListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewWaitingList;
    private OrgWaitingListAdapter waitingListAdapter;
    private List<User> waitingListEntrants;

    /**
     * Called when the activity is created. Sets up the layout, initializes the RecyclerView,
     * and configures the adapter to display the list of users in the waiting list.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_waiting_list);

        // Initialize the RecyclerView
        recyclerViewWaitingList = findViewById(R.id.recyclerViewWaitingList);

        // Initialize entrants in the waiting list (can be replaced with database data)
        waitingListEntrants = new ArrayList<>();

        // Set up the RecyclerView with a LinearLayoutManager and the adapter
        recyclerViewWaitingList.setLayoutManager(new LinearLayoutManager(this));
        waitingListAdapter = new OrgWaitingListAdapter(waitingListEntrants);
        recyclerViewWaitingList.setAdapter(waitingListAdapter);
    }
}
