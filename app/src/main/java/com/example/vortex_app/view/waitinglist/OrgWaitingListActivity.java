package com.example.vortex_app.view.waitinglist;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.controller.adapter.OrgWaitingListAdapter;
import com.example.vortex_app.R;
import com.example.vortex_app.model.User;

import java.util.ArrayList;
import java.util.List;

public class OrgWaitingListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewWaitingList;
    private OrgWaitingListAdapter waitingListAdapter;
    private List<User> waitingListEntrants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_waiting_list);

        recyclerViewWaitingList = findViewById(R.id.recyclerViewWaitingList);

        // initialize entrants in waiting list
        waitingListEntrants = new ArrayList<>();

        // replace w database
        /*waitingListEntrants.add(new WaitingListEntrant("Gyurim Do"));
        waitingListEntrants.add(new WaitingListEntrant("Peter Kim"));
        waitingListEntrants.add(new WaitingListEntrant("Peyton"));
        waitingListEntrants.add(new WaitingListEntrant("Alex S"));*/

        // set RecyclerView
        recyclerViewWaitingList.setLayoutManager(new LinearLayoutManager(this));
        waitingListAdapter = new OrgWaitingListAdapter(waitingListEntrants);
        recyclerViewWaitingList.setAdapter(waitingListAdapter);
    }
}
