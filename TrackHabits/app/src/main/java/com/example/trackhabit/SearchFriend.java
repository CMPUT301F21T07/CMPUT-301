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
import android.widget.Toast;

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
/**
 * Represents the activity to search and add a friend
 */

public class SearchFriend extends AppCompatActivity {

    //access Firestore database
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    //create reference to user and friend collections
    final CollectionReference userReference = db.collection("Users");
    private final CollectionReference friendRef = db.collection("Friends");

    //setting variables
    EditText searchUsername;
    Button addButton, cancelButton;
    ArrayList<String> uDataList;
    ArrayList<String> fDataList;
    ArrayList<String> fwDataList;

    final String TAG = "Sample";
    TextView errView;
    private String userName;

    /**
     * Creates an instance that shows search friend activity
     * will be check on creation of instance.
     * @param savedInstanceState This is the instance state from the previous creation of habits activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        userName = getIntent().getExtras().getString("name_key");

        //setting variables to the ui ids

        searchUsername = findViewById(R.id.search_friend_edit);

        addButton = findViewById(R.id.add_friend_button);
        cancelButton = findViewById(R.id.cancel_button);

        userReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * function that checks the document queries for user reference collection and places them in a list
             * @param queryDocumentSnapshots document queries
             * @param error exception error
             */
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
        //create reference to the user's existing friends
        CollectionReference realFriendsRef = friendRef.document(userName).collection("Real Friends");


        realFriendsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * function that checks the document queries for friends reference collection and places them in a list
             * @param queryDocumentSnapshots document queries
             * @param error exception error
             */
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

        cancelButton.setOnClickListener(v -> finish());

        //when the add button is clicked
        addButton.setOnClickListener( new View.OnClickListener() {
            /**
             * function when add button is clicked
             * @param view View
             */
            @Override
            public void onClick(View view) {
                final String userNameF = searchUsername.getText().toString();
                CollectionReference FIWRef = friendRef.document(userNameF).collection("Friends In Waiting");
                FIWRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    /**
                     * function that checks the document queries for friends reference collection and places them in a list
                     * @param queryDocumentSnapshots document queries
                     * @param error exception error
                     */
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                            FirebaseFirestoreException error) {
                        // Clear the old list
                        if (fwDataList != null){
                            fwDataList.clear();
                        }
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                        {
                            String userNameFIW = doc.getId();
                            fwDataList.add(userNameFIW); // Adding the userName and password from FireStore
                        }
                    }
                });
                HashMap<String, Object> data = new HashMap<>();
                //if the text box is not empty
                if (userNameF.length()>0) {
                    // if the username is not valid
                    if (!uDataList.contains(userNameF) || userNameF.equals(userName)){
                        Log.d(TAG, "Friend is not Valid");
                        Toast.makeText(SearchFriend.this, "Friend is not Valid", Toast.LENGTH_SHORT ).show();
                    }
                    //if the user name is valid and not already friends
                    if (uDataList.contains(userNameF) && !fwDataList.contains(userName) && !fDataList.contains(userNameF) && !userNameF.equals(userName)){
                        data.put("UserName",userName);
                        FIWRef
                                .document(userName)
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    /**
                                     * function if friend request is sent successfully
                                     */
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Friend Request Sent Successfully!");
                                        Toast.makeText(SearchFriend.this, "Friend Request Sent Successfully!", Toast.LENGTH_SHORT ).show();
                                        searchUsername.setText("");
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    /**
                                     * function if friend request is sent not successfully
                                     */
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Friend Request Not Sent Successfully!" + e.toString());
                                        Toast.makeText(SearchFriend.this, "Friend Request Not Sent Successfully!", Toast.LENGTH_SHORT ).show();
                                    }
                                });

                    }
                    //friend request is already sent
                    if (uDataList.contains(userNameF) && fwDataList.contains(userName) && !fDataList.contains(userNameF) && !userNameF.equals(userName)){
                        errView.setText("Friend Request Already Sent Successfully!");
                        Toast.makeText(SearchFriend.this, "Friend Request Already Sent Successfully!", Toast.LENGTH_SHORT ).show();
                    }
                    //already friends with the other user
                    if (uDataList.contains(userNameF) && fDataList.contains(userNameF) &&  !userNameF.equals(userName)){
                        Log.d(TAG, "Already Friends");
                        Toast.makeText(SearchFriend.this, "Already Friends", Toast.LENGTH_SHORT ).show();
                    }
                }
            }
        });

        uDataList = new ArrayList<>();
        fDataList = new ArrayList<>();
        fwDataList = new ArrayList<>();
    }
}