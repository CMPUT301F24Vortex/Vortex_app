package com.example.vortex_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import com.example.vortex_app.User;
import com.example.vortex_app.User;

public class WaitingListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> waitingListNames;

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
