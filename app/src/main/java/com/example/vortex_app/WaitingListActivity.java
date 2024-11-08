package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * WaitingListActivity displays a list of users in the waiting list using a ListView.
 * The activity retrieves User objects, converts them to a list of names, and uses an ArrayAdapter
 * to display the names in a simple list format.
 */
public class WaitingListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> waitingListNames;

    /**
     * Called when the activity is created. Sets up the layout, retrieves the waiting list of users,
     * and populates a ListView with their names.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_list);

        // Initialize the ListView
        listView = findViewById(R.id.list_view_waiting_list);

        // Convert User objects to a list of names for display
        waitingListNames = new ArrayList<>();
        for (User user : WaitingListManager.getWaitingList()) {
            waitingListNames.add(user.getFullName());
        }

        // Set up the adapter to display user names
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, waitingListNames);
        listView.setAdapter(adapter);
    }
}
