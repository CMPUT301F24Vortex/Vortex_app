package com.example.vortex_app.view.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminProfileScreen extends AppCompatActivity {

    String TAG = "AdminProfileScreen";

    private FirebaseFirestore db;
    private CollectionReference usersRef;

    ImageButton buttonBack;
    ListView userList;
    ArrayList<User> userDataList;
    AdminUserArrayAdapter userArrayAdapter;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profilescreen);

        //Set Firestore references
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("user_profile");

        //Create new user dataList
        userDataList = new ArrayList<>();

        //Set List and Button
        buttonBack = findViewById(R.id.button_back);
        userList = findViewById(R.id.listview_users);

        //Set user list array adapter
        userArrayAdapter = new AdminUserArrayAdapter(this, userDataList);
        userList.setAdapter(userArrayAdapter);

        //Dynamically update user list
        usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed", error);
                    return;
                }

                userDataList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    User user = doc.toObject(User.class);
                    userDataList.add(user);
                }
                userArrayAdapter.notifyDataSetChanged();
            }
        });


        //Set list item click listener
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get id of user clicked
                String userID = userDataList.get(position).getUserID();

                //start new activity to view user info
                Intent intent = new Intent(AdminProfileScreen.this, AdminProfileViewer.class);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        //Set back button click listener
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
