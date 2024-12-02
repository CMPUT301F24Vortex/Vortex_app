//package com.example.vortex_app.view.profile;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.example.vortex_app.R;
//import com.example.vortex_app.model.User;
//import com.example.vortex_app.view.admin.AdminConfirmUserDeleteFragment;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.QuerySnapshot;
//
//public class AdminProfileViewer extends AppCompatActivity implements AdminConfirmUserDeleteFragment.AdminConfirmUserDeleteListener {
//
//    String TAG = "AdminProfileViewer";
//
//    private FirebaseFirestore db;
//    private CollectionReference usersRef;
//    private CollectionReference waitlistedRef;
//    private CollectionReference selectedRef;
//    private CollectionReference enrolledRef;
//    private CollectionReference cancelledRef;
//
//    TextView textviewUserID;
//    TextView textviewFirstname;
//    TextView textviewLastname;
//    TextView textviewEmail;
//    TextView textviewPhonenumber;
//    ImageButton buttonBack;
//    Button buttonDeleteUser;
//    ImageView imageviewProfilePic;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_profile_viewer);
//        Context context = this;
//
//        //Get data from intent
//        Intent intent = getIntent();
//        String userID = intent.getStringExtra("USERID");
//
//        //Set Firestore references for all relevant collections
//        db = FirebaseFirestore.getInstance();
//        usersRef = db.collection("user_profile");
//        waitlistedRef = db.collection("waitlisted");
//        selectedRef = db.collection("selected_but_not_confirmed");
//        enrolledRef = db.collection("final");
//        cancelledRef = db.collection("cancelled");
//
//
//        //Set TextViews and Buttons
//        textviewUserID = findViewById(R.id.textView_userid);
//        textviewFirstname = findViewById(R.id.textView_firstname);
//        textviewLastname = findViewById(R.id.textView_lastname);
//        textviewEmail = findViewById(R.id.textView_email);
//        textviewPhonenumber = findViewById(R.id.textView_phonenumber);
//
//        buttonDeleteUser = findViewById(R.id.button_deleteuser);
//        buttonBack = findViewById(R.id.button_back);
//
//
//        //Load user info from firebase
//        usersRef.document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.w(TAG, "Listen failed", error);
//                    return;
//                }
//
//                if (value != null && value.exists()) {
//                    User user = value.toObject(User.class);
//                    //Set all textViews to reflect user info
//                    textviewUserID.setText(user.getUserID());
//                    textviewFirstname.setText(user.getFirstName());
//                    textviewLastname.setText(user.getLastName());
//                    textviewEmail.setText(user.getEmail());
//                    textviewPhonenumber.setText(user.getContactInfo());
//                    // load user pfp
//                    String profilePicUrl = user.getAvatarUrl();
//                    if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
//                        Glide.with(context)
//                                .load(profilePicUrl)
//                                .placeholder(R.drawable.profile) // Placeholder image
//                                .into(imageviewProfilePic);
//                    }
//                }
//            }
//        });
//
//
//        //Set click listeners for buttons
//        buttonBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AdminConfirmUserDeleteFragment(userID).show(getSupportFragmentManager(), "Delete user");
//            }
//        });
//    }
//
//    public void deleteUser(String userID) {
//
//        //Set Firestore references for all relevant collections
//        db = FirebaseFirestore.getInstance();
//        usersRef = db.collection("user_profile");
//        waitlistedRef = db.collection("waitlisted");
//        selectedRef = db.collection("selected_but_not_confirmed");
//        enrolledRef = db.collection("final");
//        cancelledRef = db.collection("cancelled");
//
//        //For each collection, query by userID to get relevant docs, and delete each
//        //Users collection
//        Query queryUsers = usersRef.whereEqualTo("userID", userID);
//        queryUsers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot document : task.getResult()) {
//                        usersRef.document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Log.d(TAG, "Document successfully deleted");
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "Error deleting document", e);
//                            }
//                        });
//                    }
//                }
//            }
//        });
//
//        //Waitlisted collection
//        Query queryWaitlisted = waitlistedRef.whereEqualTo("userID", userID);
//        queryWaitlisted.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot document : task.getResult()) {
//                        waitlistedRef.document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Log.d(TAG, "Document successfully deleted");
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "Error deleting document", e);
//                            }
//                        });
//                    }
//                }
//            }
//        });
//
//        //Selected collection
//        Query querySelected = selectedRef.whereEqualTo("userID", userID);
//        querySelected.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot document : task.getResult()) {
//                        selectedRef.document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Log.d(TAG, "Document successfully deleted");
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "Error deleting document", e);
//                            }
//                        });
//                    }
//                }
//            }
//        });
//
//        //Enrolled collection
//        Query queryEnrolled = enrolledRef.whereEqualTo("userID", userID);
//        queryEnrolled.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot document : task.getResult()) {
//                        enrolledRef.document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Log.d(TAG, "Document successfully deleted");
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "Error deleting document", e);
//                            }
//                        });
//                    }
//                }
//            }
//        });
//
//        //Cancelled collection
//        Query queryCancelled = cancelledRef.whereEqualTo("userID", userID);
//        queryCancelled.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot document : task.getResult()) {
//                        cancelledRef.document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Log.d(TAG, "Document successfully deleted");
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "Error deleting document", e);
//                            }
//                        });
//                    }
//                }
//            }
//        });
//
//        finish();
//
//    }
//}
