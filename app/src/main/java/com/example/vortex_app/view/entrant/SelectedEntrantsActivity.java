package com.example.vortex_app.view.entrant;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vortex_app.R;
import com.example.vortex_app.model.User;
import com.example.vortex_app.controller.adapter.SelectedEntrantAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectedEntrantsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSelectedEntrants;
    private SelectedEntrantAdapter selectedEntrantAdapter;
    private List<User> selectedEntrantList;
    private Button buttonDraw;
    private String eventID;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_entrants);

        recyclerViewSelectedEntrants = findViewById(R.id.recyclerViewSelectedEntrants);
        buttonDraw = findViewById(R.id.buttonDraw);


        db = FirebaseFirestore.getInstance();
        selectedEntrantList = new ArrayList<>();

        // Set up the RecyclerView
        recyclerViewSelectedEntrants.setLayoutManager(new LinearLayoutManager(this));
        selectedEntrantAdapter = new SelectedEntrantAdapter(selectedEntrantList);
        recyclerViewSelectedEntrants.setAdapter(selectedEntrantAdapter);
        eventID = getIntent().getStringExtra("EVENT_ID");

        db.collection("selected")
                .whereEqualTo("eventID", eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot documents = task.getResult();
                        if (documents != null) {
                            for (QueryDocumentSnapshot document : documents) {
                                String userName = document.getString("userName");
                                User user = new User(userName, document.getId());
                                selectedEntrantList.add(user);
                                selectedEntrantAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });


        buttonDraw.setOnClickListener(v -> {
            Toast.makeText(this, "Draw initiated", Toast.LENGTH_SHORT).show();
        });
    }
}
