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
import com.example.vortex_app.model.Center;
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

public class AdminFacilityScreen extends AppCompatActivity implements AdminConfirmFacilityDeleteFragment.AdminConfirmFacilityDeleteListener{

    String TAG = "AdminFacilityScreen";

    private FirebaseFirestore db;
    private CollectionReference facilitiesRef;
    private CollectionReference eventsRef;
    private CollectionReference waitlistedRef;
    private CollectionReference selectedRef;
    private CollectionReference enrolledRef;
    private CollectionReference cancelledRef;

    ImageButton buttonBack;
    ListView facilityList;
    ArrayList<Center> facilityDataList;
    AdminFacilityArrayAdapter facilityArrayAdapter;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_facilityscreen);

        //Set Firestore references
        db = FirebaseFirestore.getInstance();
        facilitiesRef = db.collection("centers");

        //Create new facility dataList
        facilityDataList = new ArrayList<>();

        //Set List and Button
        buttonBack = findViewById(R.id.button_back);
        facilityList = findViewById(R.id.listview_facilities);

        //Set facility list array adapter
        facilityArrayAdapter = new AdminFacilityArrayAdapter(this, facilityDataList);
        facilityList.setAdapter(facilityArrayAdapter);

        //Dynamically update facility list
        facilitiesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed", error);
                    return;
                }

                facilityDataList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Center center = doc.toObject(Center.class);
                    facilityDataList.add(center);
                }
                facilityArrayAdapter.notifyDataSetChanged();
            }
        });


        //Set list item click listener
        facilityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get id of facility clicked
                String facilityID = facilityDataList.get(position).getFacilityID();

                //start new activity to view facility events
                Intent intent = new Intent(AdminFacilityScreen.this, AdminEventScreen.class);
                intent.putExtra("FACILITYID", facilityID);
                startActivity(intent);
            }
        });

        //Set list item long click listener
        facilityList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //get id of facility clicked
                String facilityID = facilityDataList.get(position).getFacilityID();
                new AdminConfirmFacilityDeleteFragment(facilityID).show(getSupportFragmentManager(), "Delete facility");

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

    public void deleteFacility(String facilityID) {

        //Set Firestore references for all relevant collections
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");
        facilitiesRef = db.collection("centers");

        //Delete all events for the facility
        Query queryEvents = eventsRef.whereEqualTo("facilityID", facilityID);
        queryEvents.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        deleteEvent(document.getId());
                    }
                }
            }
        });

        Query queryFacilities = facilitiesRef.whereEqualTo("facilityID", facilityID);
        queryFacilities.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        facilitiesRef.document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
