package com.example.vortex_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code OrgWaitingListActivity} is an {@link AppCompatActivity} that displays a list of entrants
 * currently on the waiting list for an event. The entrants are displayed in a {@link RecyclerView}
 * using the {@link OrgWaitingListAdapter}.
 */
public class OrgWaitingListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewWaitingList;
    private OrgWaitingListAdapter waitingListAdapter;
    private List<User> waitingListEntrants;

    /**
     * Called when the activity is created. Sets up the layout, initializes the waiting list,
     * and configures the RecyclerView with a {@link OrgWaitingListAdapter} to display the waiting list entrants.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_waiting_list);

        recyclerViewWaitingList = findViewById(R.id.recyclerViewWaitingList);

        // Initialize entrants in waiting list
        waitingListEntrants = new ArrayList<>();

        // Placeholder data (replace with data from a database)
        /*
        waitingListEntrants.add(new User("Gyurim Do"));
        waitingListEntrants.add(new User("Peter Kim"));
        waitingListEntrants.add(new User("Peyton"));
        waitingListEntrants.add(new User("Alex S"));
        */

        // Set up RecyclerView with a linear layout manager and adapter
        recyclerViewWaitingList.setLayoutManager(new LinearLayoutManager(this));
        waitingListAdapter = new OrgWaitingListAdapter(waitingListEntrants);
        recyclerViewWaitingList.setAdapter(waitingListAdapter);
    }
}
