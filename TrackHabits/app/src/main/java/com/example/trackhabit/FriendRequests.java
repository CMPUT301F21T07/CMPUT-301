package com.example.trackhabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class FriendRequests extends AppCompatActivity {
    ListView friendsListView;
    ArrayList<String> wFriendsList;
    ArrayAdapter<String> friendsArrayAdapter;


    FloatingActionButton goBackButton;

    private String userName;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference friendRef = db.collection("Friends");

    Integer temp_index;
    final String TAG = "Sample";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        userName = getIntent().getExtras().getString("name_key");
        CollectionReference FIWRef = friendRef.document(userName).collection("Friends In Waiting");

        friendsListView = findViewById(R.id.friends_list_view_req);
        wFriendsList = new ArrayList<>();

        goBackButton        = findViewById(R.id.go_back_req);



        FIWRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                if (wFriendsList != null){
                    wFriendsList.clear();
                }
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String userNameU = doc.getId();
                    wFriendsList.add(userNameU); // Adding the userName and password from FireStore

                }
                friendsArrayAdapter.notifyDataSetChanged();
            }
        });


        goBackButton.setOnClickListener(v -> {
            finish();
        });

        friendsListView.setClickable(true);

        friendsListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            registerForContextMenu(friendsListView);
            temp_index = i;
            return false;
        });


        friendsArrayAdapter = new ArrayAdapter<>(this,R.layout.friends_content_list_view,wFriendsList);
        friendsListView.setAdapter(friendsArrayAdapter);
    }


    /**
     * Function that creates a context menu when there is a long press on a ListView item
     * @param menu This is the menu object
     * @param view This is the view object
     * @param menuInfo This is the info on the menu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        getMenuInflater().inflate(R.menu.friend_req_menu, menu);
    }

    /**
     * Function that determine what happens when a user clicks on an item in a context menu
     * @param item  Item that is selected from the context menu
     * @return  true -> item in context menu selected
     *          super.onContextItemSelected(item)
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.approve_friend:
                String a_friend  = wFriendsList.get(temp_index);
                DocumentReference FIWRef = friendRef.document(userName).collection("Friends In Waiting").document(a_friend);
                CollectionReference RFRef_user = friendRef.document(userName).collection("Real Friends");
                CollectionReference RFRefO_approved = friendRef.document(a_friend).collection("Real Friends");

                FIWRef.delete();
                HashMap<String, Object> data = new HashMap<>();
                data.put("UserName",a_friend);
                RFRef_user
                        .document(a_friend)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Friend Accepted!");
                                Toast.makeText(FriendRequests.this, "Friend Accepted!", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Friend Denied Successfully" + e.toString());
                                Toast.makeText(FriendRequests.this, "Friend Denied Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });

                HashMap<String, Object> data_a = new HashMap<>();
                data_a.put("UserName",userName);

                RFRefO_approved
                        .document(userName)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Friend Accepted!");
                                Toast.makeText(FriendRequests.this, "Friend Accepted!", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Friend Not Accepted Successfully" + e.toString());
                                Toast.makeText(FriendRequests.this, "Friend Denied Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                wFriendsList.remove(a_friend);
                friendsArrayAdapter.notifyDataSetChanged();
                return true;


            case R.id.deny_friend:
                String d_friend = wFriendsList.get(temp_index);
                DocumentReference FIWRef_deny = friendRef.document(userName).collection("Friends In Waiting").document(d_friend);

                FIWRef_deny.delete();
                wFriendsList.remove(d_friend);
                friendsArrayAdapter.notifyDataSetChanged();

                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }
}