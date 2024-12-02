package com.example.vortex_app.view.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vortex_app.R;
import com.example.vortex_app.model.Event;
import com.example.vortex_app.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminImageScreen extends AppCompatActivity {


    String TAG = "AdminImageScreen";

    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private CollectionReference usersRef;
    private CollectionReference waitlistedRef;
    private CollectionReference selectedRef;
    private CollectionReference enrolledRef;
    private CollectionReference cancelledRef;

    ImageButton buttonBack;
    ListView imageList;
    ArrayList<String> imageDataList;
    ArrayList<Event> eventDataList;
    ArrayList<User> userDataList;
    AdminImageArrayAdapter imageArrayAdapter;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_imagescreen);


        //Set Firestore references
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");
        usersRef = db.collection("user_profile");

        //Create new dataLists
        imageDataList = new ArrayList<>();
        eventDataList = new ArrayList<>();
        userDataList = new ArrayList<>();

        //Set List and Button
        buttonBack = findViewById(R.id.button_back);
        imageList = findViewById(R.id.listview_images);

        //Set image list array adapter
        imageArrayAdapter = new AdminImageArrayAdapter(this, imageDataList);
        imageList.setAdapter(imageArrayAdapter);

        //Dynamically update event list
        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed", error);
                    return;
                }

                eventDataList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Event event = doc.toObject(Event.class);
                    eventDataList.add(event);
                }
                imageDataList.clear();
                imageDataList = combineLists(eventDataList, userDataList);
                imageArrayAdapter.notifyDataSetChanged();
            }
        });

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
                imageDataList.clear();
                imageDataList = combineLists(eventDataList, userDataList);
                imageArrayAdapter.notifyDataSetChanged();
            }
        });

        //Set list item long click listener
        imageList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //get url of image clicked
                String imageURL = imageDataList.get(position);
                new AdminConfirmImageDeleteFragment(imageURL).show(getSupportFragmentManager(), "Delete image");

                return true;
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

    private ArrayList<String> combineLists(ArrayList<Event> eventDataList, ArrayList<User> userDataList) {
        ArrayList<String> imageURLList = new ArrayList<>();

        for (Event event : eventDataList) {
            String eventImageURL = event.getImageUrl();
            if (eventImageURL != null) {
                imageURLList.add(eventImageURL);
            }
        }
        for (User user : userDataList) {
            String userImageURL = user.getAvatarUrl();
            if (userImageURL != null) {
                imageURLList.add(userImageURL);
            }
        }
        return imageURLList;
    }

    public void deleteImage(String imageURL) {
        //Set Firestore references
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");
        usersRef = db.collection("user_profile");

        eventsRef.whereEqualTo("imageUrl", imageURL)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().update("imageUrl", null);
                            }
                        }
                    }
                });

        usersRef.whereEqualTo("avatarUrl", imageURL)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().update("avatarUrl", null);
                            }
                        }
                    }
                });
    }
}
