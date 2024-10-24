package com.example.vortex_app;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Database {


    private FirebaseFirestore db;

    private CollectionReference usersRef;
    private CollectionReference eventsRef;
    

    Database () {
        db = FirebaseFirestore.getInstance();
    }
}
