package com.example.vortex_app.view.waitinglist;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.controller.adapter.OrgWaitingListAdapter;
import com.example.vortex_app.R;
import com.example.vortex_app.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrgWaitingListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewWaitingList;
    private OrgWaitingListAdapter waitingListAdapter;
    private List<User> waitingListEntrants;
    private FirebaseFirestore db;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_waiting_list);

        // Initialize the RecyclerView and the adapter
        recyclerViewWaitingList = findViewById(R.id.recyclerViewWaitingList);
        waitingListEntrants = new ArrayList<>();
        recyclerViewWaitingList.setLayoutManager(new LinearLayoutManager(this));
        waitingListAdapter = new OrgWaitingListAdapter(waitingListEntrants);
        recyclerViewWaitingList.setAdapter(waitingListAdapter);

        db = FirebaseFirestore.getInstance();
        eventID = getIntent().getStringExtra("EVENT_ID");
        fetchWaitingList(eventID);
    }

    private void fetchWaitingList(String eventID) {

        db.collection("waitlisted")
                .whereEqualTo("eventID", eventID)
                .get() // Execute the query
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            waitingListEntrants.clear();


                            for (DocumentSnapshot document : querySnapshot) {

                                String firstName = document.getString("firstName");
                                String lastName = document.getString("lastName");


                                User user = new User(firstName, lastName);
                                waitingListEntrants.add(user);
                            }

                            waitingListAdapter.notifyDataSetChanged();
                        }
                    } else {

                    }
                });
    }
}
