package com.example.trackhabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Map;

public class ViewFriends extends AppCompatActivity {

    ListView friendsListView;
    ArrayList<String> friendsList;
    ArrayAdapter<String> friendsArrayAdapter;

    FloatingActionButton friendsOptionButton, addFriendButton, viewRequestsButton, goBackButton;
    LinearLayout addFriendLayout, viewRequestsLayout, goBackLayout;

    private String userName;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference friendRef = db.collection("Friends");
    Boolean flag_for_floating = true;

    Integer temp_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friends);

        userName = getIntent().getExtras().getString("name_key");
        DocumentReference friendsListRef = friendRef.document(userName);

        friendsListView = findViewById(R.id.friends_list_view);
        friendsList = new ArrayList<>();

        friendsOptionButton = findViewById(R.id.open_friend_menu_button);
        addFriendButton     = findViewById(R.id.add_friend);
        viewRequestsButton  = findViewById(R.id.view_requests);
        goBackButton        = findViewById(R.id.go_back);

        addFriendLayout = findViewById(R.id.add_new_friend_layout);
        viewRequestsLayout = findViewById(R.id.view_requests_layout);
        goBackLayout       = findViewById(R.id.go_back_layout);

        friendsListRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> list = new ArrayList<>();
                        Map<String, Object> map = document.getData();
                        if (map != null) {
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                list.add(entry.getValue().toString());
                                friendsList.add(entry.getValue().toString());
                                Log.d("Massive tag", "ADDING Friend TO LIST");
                            }
                        }
                        friendsArrayAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        friendsOptionButton.setOnClickListener(v -> {
            if (flag_for_floating) {
                addFriendLayout.setVisibility(View.VISIBLE);
                viewRequestsLayout.setVisibility(View.VISIBLE);
                goBackLayout.setVisibility(View.VISIBLE);

                friendsOptionButton.setImageResource(R.drawable.ic_baseline_not_interested_24);
                flag_for_floating = false;

            }
            else {
                addFriendLayout.setVisibility(View.GONE);
                viewRequestsLayout.setVisibility(View.GONE);
                goBackLayout.setVisibility(View.GONE);

                friendsOptionButton.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
                flag_for_floating = true;
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



        friendsArrayAdapter = new ArrayAdapter<>(this,R.layout.friends_content_list_view,friendsList);
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
        getMenuInflater().inflate(R.menu.friends_context_menu, menu);
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
            case R.id.show_habits:
                String friendSelected = friendsList.get(temp_index);
                openUserHabits(friendSelected);
                return true;
            case R.id.remove_friend:
                String deleteFriend = friendsList.get(temp_index);
                removeFriend(deleteFriend);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void openUserHabits(String friendSelected) {
    }

    private void removeFriend(String deleteFriend) {
        DocumentReference userList = friendRef.document(userName);
        DocumentReference friendList = friendRef.document(deleteFriend);

        friendsList.remove(deleteFriend);
        friendsArrayAdapter.notifyDataSetChanged();

        Map<String,Object> updateUser = new HashMap<>();
        updateUser.put(deleteFriend, FieldValue.delete());

        userList.update(updateUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ViewFriends.this, "Friend removed", Toast.LENGTH_SHORT).show();
            }
        });

        Map<String,Object> updateFriend = new HashMap<>();
        updateUser.put(userName, FieldValue.delete());

        friendList.update(updateFriend).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }


}