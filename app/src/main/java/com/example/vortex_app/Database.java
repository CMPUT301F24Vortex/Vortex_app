package com.example.vortex_app;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class Database {

    private static final String TAG = "Database";

    // Firestore database
    private FirebaseFirestore db;

    // Local lists




    // object collection references
    private CollectionReference usersRef;
    private CollectionReference eventsRef;
    private CollectionReference facilitiesRef;

    // relation collection references
    private CollectionReference waitlistedRef;
    private CollectionReference selectedRef;
    private CollectionReference enrolledRef;
    private CollectionReference canceledRef;
    
    // Database class constructor
    Database () {
        db = FirebaseFirestore.getInstance();
    }

    //helper variables
    boolean userExists;
    boolean eventExists;


    // User related functions
    public boolean userExists(User user) {
        // check if userID already exists
        DocumentReference userDocRef = db.document(user.getUserID());
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    userExists = document.exists();
                }
                else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    throw new RuntimeException("Failed to get existence of user");
                }
            }
        });
        return userExists;
    }


    public void addUser(User user) {
        // check if userID already exists
        if (userExists(user)) {
            throw new IllegalArgumentException();
        }

        // Create hashmap of attributes and values
        HashMap<String, Object> userData = new HashMap<>();
        // package User attributes
        userData.put("email", user.getEmail());
        userData.put("name", user.getName());
        userData.put("phoneNumber", user.getPhoneNumber());
        userData.put("roles", user.getRoles());
        // set database document
        usersRef.document(user.getUserID()).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
            }
        });


    }


    public void editUser(User user) {
        // check if user exists
        if (!userExists(user)) {
            throw new IllegalArgumentException();
        }
        // Create hashmap of attributes and values
        HashMap<String, Object> userData = new HashMap<>();
        // package User attributes
        userData.put("email", user.getEmail());
        userData.put("name", user.getName());
        userData.put("phoneNumber", user.getPhoneNumber());
        userData.put("roles", user.getRoles());
        // set database document
        usersRef.document(user.getUserID()).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
            }
        });
    }


    public void deleteUser (User user) {
        // check if user exists
        if (!userExists(user)) {
            throw new IllegalArgumentException();
        }
        usersRef.document(user.getUserID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "DocumentSnapshot successfully deleted!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error deleting document", e);
            }
        });
    }


    // Event related functions
    public boolean eventExists(Event event) {
        // check if eventID already exists
        DocumentReference eventDocRef = db.document(event.getEventID());
        eventDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    eventExists = document.exists();
                }
                else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    throw new RuntimeException("Failed to get existence of event");
                }
            }
        });
        return eventExists;
    }


    public void addEvent(Event event) {
        // check if eventID already exists
        if (eventExists(event)) {
            throw new IllegalArgumentException();
        }

        // Create hashmap of attributes and values
        HashMap<String, Object> eventData = new HashMap<>();
        // package Event attributes
        eventData.put("email", event.getEmail());
        eventData.put("name", event.getName());
        eventData.put("phoneNumber", event.getPhoneNumber());
        eventData.put("roles", event.getRoles());
        // set database document
        eventsRef.document(event.getEventID()).set(eventData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
            }
        });


    }


    public void editEvent(Event event) {
        // check if event exists
        if (!eventExists(event)) {
            throw new IllegalArgumentException();
        }
        // Create hashmap of attributes and values
        HashMap<String, Object> eventData = new HashMap<>();
        // package Event attributes
        eventData.put("email", event.getEmail());
        eventData.put("name", event.getName());
        eventData.put("phoneNumber", event.getPhoneNumber());
        eventData.put("roles", event.getRoles());
        // set database document
        eventsRef.document(event.getEventID()).set(eventData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
            }
        });
    }


    public void deleteEvent (Event event) {
        // check if event exists
        if (!eventExists(event)) {
            throw new IllegalArgumentException();
        }
        eventsRef.document(event.getEventID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "DocumentSnapshot successfully deleted!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error deleting document", e);
            }
        });
    }
}

