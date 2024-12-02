package com.example.vortex_app.view.profile;

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
import com.example.vortex_app.controller.adapter.AdminUserArrayAdapter;
import com.example.vortex_app.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * AdminProfileScreen allows administrators to browse user profiles and view details of each user.
 */
public class AdminProfileScreen extends AppCompatActivity {

    private static final String TAG = "AdminProfileScreen";

    private FirebaseFirestore db;
    private CollectionReference usersRef;

    private ImageButton buttonBack;
    private ListView userList;
    private ArrayList<User> userDataList;
    private AdminUserArrayAdapter userArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profilescreen);

        // Initialize Firestore references
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("user_profile");

        // Initialize user data list
        userDataList = new ArrayList<>();

        // Set up UI components
        buttonBack = findViewById(R.id.button_back);
        userList = findViewById(R.id.listview_users);

        // Set up the user list array adapter
        userArrayAdapter = new AdminUserArrayAdapter(this, userDataList);
        userList.setAdapter(userArrayAdapter);

        // Dynamically update user list from Firestore
        usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed", error);
                    return;
                }

                // Clear the current list and add updated data
                userDataList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    User user = doc.toObject(User.class);
                    user.setUserID(doc.getId()); // Set the document ID as the userID
                    userDataList.add(user);
                }
                userArrayAdapter.notifyDataSetChanged();
            }
        });

        // Set list item click listener to view user details
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected user
                User selectedUser = userDataList.get(position);

                // Start UserDetailActivity to view detailed information
                Intent intent = new Intent(AdminProfileScreen.this, UserDetailActivity.class);
                intent.putExtra("USER_ID", selectedUser.getUserID()); // Pass the document ID as userID
                intent.putExtra("FIRST_NAME", selectedUser.getFirstName());
                intent.putExtra("LAST_NAME", selectedUser.getLastName());
                intent.putExtra("EMAIL", selectedUser.getEmail());
                intent.putExtra("CONTACT_INFO", selectedUser.getContactInfo());
                intent.putExtra("DEVICE", selectedUser.getDevice());
                intent.putExtra("AVATAR_URL", selectedUser.getAvatarUrl());
                startActivity(intent);
            }
        });

        // Set back button click listener
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
