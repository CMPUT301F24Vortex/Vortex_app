package com.example.vortex_app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code WaitingListActivity} is an {@link AppCompatActivity} that displays a list of users currently on the waiting list for an event.
 * The activity retrieves a list of {@link User} objects from the {@link WaitingListManager} and displays their full names in a {@link ListView}.
 */
public class WaitingListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> waitingListNames;

    /**
     * Called when the activity is created. Sets up the layout, retrieves the list of users from the waiting list,
     * extracts their full names, and populates the {@link ListView} with these names.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the most recent data supplied by
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_list);

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
