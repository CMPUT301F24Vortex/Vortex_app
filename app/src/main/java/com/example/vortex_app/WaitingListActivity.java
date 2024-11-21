package com.example.vortex_app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class WaitingListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> waitingListNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_list);

        listView = findViewById(R.id.list_view_waiting_list);

        // Initialize list and adapter
        waitingListNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, waitingListNames);
        listView.setAdapter(adapter);


        // Add a new user to the waiting list
        User newUser = new User("John", "Doe", "john.doe@example.com", "1234567890", "user123");
        WaitingListManager.addUserToWaitingList(newUser);



        // Fetch data from Firebase
        WaitingListManager.fetchWaitingList(new WaitingListManager.WaitingListCallback() {
            @Override
            public void onWaitingListUpdated(List<User> updatedWaitingList) {
                waitingListNames.clear();
                for (User user : updatedWaitingList) {
                    waitingListNames.add(user.getFullName()); // Update UI with user names
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace(); // Log errors
            }
        });
    }
}