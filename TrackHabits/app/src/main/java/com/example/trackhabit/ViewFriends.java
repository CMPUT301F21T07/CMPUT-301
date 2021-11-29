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

/**
 * Represents an activity for viewing friends
 */

public class ViewFriend extends AppCompatActivity {

    ListView friendsListView;
    ArrayList<String> friendsList;
    ArrayAdapter<String> friendsArrayAdapter;

    FloatingActionButton friendsOptionButton, addFriendButton, viewRequestsButton, goBackButton;
    LinearLayout addFriendLayout, viewRequestsLayout, goBackLayout;

    private String userName;
    private final String friend_list = "Real Friends";
    private final String friend_requests = "Friends in Waiting";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference friendRef = db.collection("Friends");
    Boolean flag_for_floating = true;

    Integer temp_index;

    /**
     * Creates an instance that creates the activity for viewing friends
     * will be check on creation of instance.
     * @param savedInstanceState This is the instance state from the previous creation of habits activity
     */

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

        CollectionReference friendsListCollection = friendsListRef.collection(friend_list);
        friendsListCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * function that checks the document queries for friendsListCollection collection and places them in a list
             * @param value document queries
             * @param error exception error
             */
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

    /**
     * Function that opens user habits activity
     * @param friendSelected String
     */
    private void openUserHabits(String friendSelected) {
        Intent friendHabitIntent = new Intent(ViewFriend.this, ViewFriendHabit.class);
        friendHabitIntent.putExtra("name_key", friendSelected);
        startActivity(friendHabitIntent);
    }

    /**
     * Function that removes friend
     * @param deleteFriend String
     */

    private void removeFriend(String deleteFriend) {
        DocumentReference userList   = friendRef.document(userName);
        DocumentReference friendList = friendRef.document(deleteFriend);

        CollectionReference subUserFriendsList = userList.collection(friend_list);
        subUserFriendsList.document(deleteFriend).delete();

        CollectionReference subFriendFriendsList = friendList.collection(friend_list);
        subFriendFriendsList.document(userName).delete();

        friendsList.remove(deleteFriend);
        friendsArrayAdapter.notifyDataSetChanged();

    }

    /**
     *  Function that accepts friend requests
     */
    private void friendRequest(){
        Intent newIntent= new Intent(ViewFriend.this, FriendRequest.class);
        closeMenu();;
        newIntent.putExtra("name_key", userName);
        startActivity(newIntent);
    }

    /**
     *  Function that searches for friends
     */
    private void addFriend(){
        Intent newIntent= new Intent(ViewFriend.this, SearchFriend.class);
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
     *  Function that closes the menu
     */
    private void closeMenu() {
        addFriendLayout.setVisibility(View.GONE);
        viewRequestsLayout.setVisibility(View.GONE);
        goBackLayout.setVisibility(View.GONE);

        friendsOptionButton.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
        flag_for_floating = true;
    }


}
