package com.example.vortex_app;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WaitingListManager {
    private static DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("waiting_list");
    private static List<User> waitingList = new ArrayList<>();

    // Add user to Firebase waiting list
    public static void addUserToWaitingList(User user) {
        if (!waitingList.contains(user)) {
            databaseRef.child(user.getUserID()).setValue(user); // Corrected syntax for setValue
        }
    }

    // Retrieve the waiting list from Firebase
    public static void fetchWaitingList(final WaitingListCallback callback) {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                waitingList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        waitingList.add(user);
                    }
                }
                callback.onWaitingListUpdated(waitingList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError(error.toException());
            }
        });
    }

    // Remove user from Firebase waiting list
    public static void removeUserFromWaitingList(User user) {
        if (user.getUserID() != null) {
            databaseRef.child(user.getUserID()).removeValue(); // Corrected method to remove a user
        }
    }

    // Provide current waiting list (local cache)
    public static List<User> getWaitingList() {
        return waitingList;
    }

    // Callback interface for Firebase updates
    public interface WaitingListCallback {
        void onWaitingListUpdated(List<User> updatedWaitingList);

        void onError(Exception e);
    }
}
