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

    // Declaring UI elements
    ListView friendsListView;
    FloatingActionButton friendsOptionButton, addFriendButton, viewRequestsButton, goBackButton;
    LinearLayout addFriendLayout, viewRequestsLayout, goBackLayout;

    // Declaring variables
    private String userName;
    private final String friend_list = "Real Friends";

    ArrayList<String> friendsList;
    ArrayAdapter<String> friendsArrayAdapter;

    Boolean flag_for_floating = true;

    Integer temp_index;

    // Getting access to a firebase document
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference friendRef = db.collection("Friends");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friends);

        // Getting username of current user
        userName = getIntent().getExtras().getString("name_key");
        DocumentReference friendsListRef = friendRef.document(userName);

        // Initializing UI elements
        friendsListView = findViewById(R.id.friends_list_view);

        friendsOptionButton = findViewById(R.id.open_friend_menu_button);
        addFriendButton     = findViewById(R.id.add_friend);
        viewRequestsButton  = findViewById(R.id.view_requests);
        goBackButton        = findViewById(R.id.go_back);

        addFriendLayout = findViewById(R.id.add_new_friend_layout);
        viewRequestsLayout = findViewById(R.id.view_requests_layout);
        goBackLayout       = findViewById(R.id.go_back_layout);

        friendsList = new ArrayList<>();

        // Getting access to collection inside of a document and iterating over it to get a list of friends
        CollectionReference friendsListCollection = friendsListRef.collection(friend_list);
        friendsListCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                friendsList.clear();
                assert value != null;
                for (QueryDocumentSnapshot doc : value) {
                    friendsList.add(doc.getId());
                }
                friendsArrayAdapter.notifyDataSetChanged();
            }
        });

        // Setting up listeners for button clicks
        friendsOptionButton.setOnClickListener(v -> {
            if (flag_for_floating) {
                openMenu();
            }
            else {
                closeMenu();
            }
        });

        goBackButton.setOnClickListener(v -> {
            finish();
        });
        viewRequestsButton.setOnClickListener(v -> friendRequest());
        addFriendButton.setOnClickListener(v -> addFriend());

        friendsListView.setClickable(true);

        // Generating a context menu when long pressing on a ListView item
        friendsListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            registerForContextMenu(friendsListView);
            temp_index = i;
            return false;
        });

        // Initializing an adapter for the ListView
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

    /**
     * Function that opens up a new activity that shows the habits of a friend
     * @param friendSelected username of the friend who's habits we want to view
     */
    private void openUserHabits(String friendSelected) {
        Intent friendHabitIntent = new Intent(ViewFriends.this, ViewFriendHabit.class);
        friendHabitIntent.putExtra("name_key", friendSelected);
        startActivity(friendHabitIntent);
    }

    /**
     * Function that removes a friend from the current user as well as the friend's friends list
     * @param deleteFriend friend that is to be removed from the user's list
     */
    private void removeFriend(String deleteFriend) {
        DocumentReference userList   = friendRef.document(userName);
        DocumentReference friendList = friendRef.document(deleteFriend);

        // Removing friend from current user's friends list
        CollectionReference subUserFriendsList = userList.collection(friend_list);
        subUserFriendsList.document(deleteFriend).delete();

        // Removing user from selected friend's friends list
        CollectionReference subFriendFriendsList = friendList.collection(friend_list);
        subFriendFriendsList.document(userName).delete();

        friendsList.remove(deleteFriend);
        friendsArrayAdapter.notifyDataSetChanged();

    }

    /**
     *  Function that accepts friend requests
     */
    private void friendRequest(){
        Intent newIntent= new Intent(ViewFriends.this, FriendRequests.class);
        closeMenu();;
        newIntent.putExtra("name_key", userName);
        startActivity(newIntent);
    }

    /**
     *  Function that searches for friends
     */
    private void addFriend(){
        Intent newIntent= new Intent(ViewFriends.this, SearchFriend.class);
        closeMenu();
        newIntent.putExtra("name_key", userName);
        startActivity(newIntent);
    }

    /**
     *  Function that displays more options for the user to click on
     */
    private void openMenu() {
        addFriendLayout.setVisibility(View.VISIBLE);
        viewRequestsLayout.setVisibility(View.VISIBLE);
        goBackLayout.setVisibility(View.VISIBLE);

        friendsOptionButton.setImageResource(R.drawable.ic_baseline_not_interested_24);
        flag_for_floating = false;
    }

    /**
     *  Function that removes more options for the user to click on
     */
    private void closeMenu() {
        addFriendLayout.setVisibility(View.GONE);
        viewRequestsLayout.setVisibility(View.GONE);
        goBackLayout.setVisibility(View.GONE);

        friendsOptionButton.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
        flag_for_floating = true;
    }


}