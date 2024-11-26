package com.example.vortex_app;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminEventScreen extends AppCompatActivity implements AdminConfirmEventDeleteFragment.AdminConfirmEventDeleteListener{

    String TAG = "AdminEventScreen";

    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private CollectionReference usersRef;
    private CollectionReference waitlistedRef;
    private CollectionReference selectedRef;
    private CollectionReference enrolledRef;
    private CollectionReference cancelledRef;

    ImageButton buttonBack;
    ListView eventList;
    ArrayList<Event> eventDataList;
    AdminEventArrayAdapter eventArrayAdapter;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_eventscreen);

        //Get info from intent
        String facilityID = getIntent().getStringExtra("FACILITYID");

        //Set Firestore references
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        //Create new event dataList
        eventDataList = new ArrayList<>();

        //Set List and Button
        buttonBack = findViewById(R.id.button_back);
        eventList = findViewById(R.id.listview_events);

        //Set event list array adapter
        eventArrayAdapter = new AdminEventArrayAdapter(this, eventDataList);
        eventList.setAdapter(eventArrayAdapter);

        //Dynamically update event list
        eventsRef.whereEqualTo("facilityID", facilityID).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                eventArrayAdapter.notifyDataSetChanged();
            }
        });


        //Set list item click listener
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get id of event clicked
                String eventID = eventDataList.get(position).getEventID();

                //start new activity to view events
                Intent intent = new Intent(AdminEventScreen.this, AdminEventInfoScreen.class);
                intent.putExtra("EVENTID", eventID);
                startActivity(intent);
            }
        });

        //Set list item long click listener
        eventList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //get id of event clicked
                String eventID = eventDataList.get(position).getEventID();
                new AdminConfirmEventDeleteFragment(eventID).show(getSupportFragmentManager(), "Delete event");

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

    public void deleteEvent(String eventID) {
        //Set Firestore references for all relevant collections
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");
        waitlistedRef = db.collection("waitlisted");
        selectedRef = db.collection("selected");
        enrolledRef = db.collection("enrolled");
        cancelledRef = db.collection("cancelled");

        //For each collection, query by eventID to get relevant docs, and delete each
        //Events collection
        Query queryEvents = eventsRef.whereEqualTo("eventID", eventID);
        queryEvents.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        eventsRef.document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Document successfully deleted");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error deleting document", e);
                            }
                        });
                    }
                }
            }
        });

        //Waitlisted collection
        Query queryWaitlisted = waitlistedRef.whereEqualTo("eventID", eventID);
        queryWaitlisted.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        waitlistedRef.document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Document successfully deleted");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error deleting document", e);
                            }
                        });
                    }
                }
            }
        });

        //Selected collection
        Query querySelected = selectedRef.whereEqualTo("eventID", eventID);
        querySelected.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        selectedRef.document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Document successfully deleted");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error deleting document", e);
                            }
                        });
                    }
                }
            }
        });

        //Enrolled collection
        Query queryEnrolled = enrolledRef.whereEqualTo("eventID", eventID);
        queryEnrolled.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        enrolledRef.document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Document successfully deleted");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error deleting document", e);
                            }
                        });
                    }
                }
            }
        });

        //Cancelled collection
        Query queryCancelled = cancelledRef.whereEqualTo("eventID", eventID);
        queryCancelled.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        cancelledRef.document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Document successfully deleted");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error deleting document", e);
                            }
                        });
                    }
                }
            }
        });

    }


}
