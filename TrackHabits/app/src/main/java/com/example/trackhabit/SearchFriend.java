package com.example.trackhabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchFriend extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference friendWRef = db.collection("Friends In Waiting");
    final CollectionReference userReference = db.collection("Users");
    private final CollectionReference friendRef = db.collection("Friends");

    EditText searchUsername;
    Button addButton;
    ArrayList<String> uDataList;
    ArrayList<String> fDataList;
    ArrayList<String> fwDataList;

    final String TAG = "Sample";
    TextView errView;
    private String userName;
    int success;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        userName = getIntent().getExtras().getString("name_key");

        searchUsername = findViewById(R.id.search_friend_edit);
        addButton = findViewById(R.id.add_friend_button);
        errView = findViewById(R.id.errSearch);


        userReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                if (uDataList != null){
                    uDataList.clear();
                }
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String userNameU = doc.getId();
                    uDataList.add(userNameU); // Adding the userName and password from FireStore
                }
            }
        });
        friendRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                if (fDataList != null){
                    fDataList.clear();
                }
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String userNameF = doc.getId();
                    fDataList.add(userNameF); // Adding the userName and password from FireStore
                }
            }
        });

        friendWRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                if (fwDataList != null){
                    fwDataList.clear();
                }
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String userNameF = doc.getId();
                    fwDataList.add(userNameF); // Adding the userName and password from FireStore
                }
            }
        });


        addButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userNameF = searchUsername.getText().toString();
                HashMap<String, Object> data = new HashMap<>();
                if (userNameF.length()>0) {
                    if (!uDataList.contains(userNameF) || userNameF.equals(userName)){
                        Log.d(TAG, "Friend is not Valid");
                        errView.setText("Friend is not Valid");
                        success = 1;
                    }

                    if (uDataList.contains(userNameF) && !fDataList.contains(userNameF) && !fDataList.contains(userNameF) && success ==0){
                        ArrayList<String> FIW = new ArrayList<>();
                        FIW.add(userName);
                        data.put("Friends Requests",FIW);
                        friendWRef
                                .document(userNameF)
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Friend Request Sent Successfully!");
                                        errView.setText("Friend Request Sent Successfully!");
                                        searchUsername.setText("");
                                        Intent HabitsIntent = new Intent(getApplicationContext(), HabitsActivity.class);
                                        HabitsIntent.putExtra("name_key", userName);
                                        startActivity(HabitsIntent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Friend Request Not Sent Successfully!" + e.toString());
                                        errView.setText("Friend Request Not Sent Successfully!");
                                    }
                                });

                    }

                    if (uDataList.contains(userNameF) && fDataList.contains(userNameF) && !fDataList.contains(userNameF) && success ==0){
                        DocumentReference friendReq = db.collection("Friends In Waiting").document(userNameF);
                        friendReq.update("Friends Requests", FieldValue.arrayUnion(userName));
                        errView.setText("Friend Request Sent Successfully!");
                    }

                    if (uDataList.contains(userNameF) && fDataList.contains(userNameF) && fDataList.contains(userNameF) && success ==0){
                        DocumentReference friendReq = db.collection("Friends In Waiting").document(userNameF);
                        DocumentReference friends = db.collection("Friends").document(userNameF);
                        friends.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        List<String> friendList = (List<String>) document.get("FriendList");
                                        if (friendList.contains(userName)){
                                            Log.d(TAG, "Already Friends");
                                            errView.setText("Already Friends");

                                        }
                                        else{
                                            friendReq.update("Friends Requests", FieldValue.arrayUnion(userName));
                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                            errView.setText("Friend Request Sent Successfully!");
                                        }

                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                }
            }
        });

        uDataList = new ArrayList<>();
        fDataList = new ArrayList<>();
        fwDataList = new ArrayList<>();
    }
}