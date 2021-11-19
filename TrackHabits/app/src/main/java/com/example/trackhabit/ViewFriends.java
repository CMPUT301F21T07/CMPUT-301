package com.example.trackhabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
        friendsArrayAdapter = new ArrayAdapter<>(this,R.layout.friends_content_list_view,friendsList);
        friendsListView.setAdapter(friendsArrayAdapter);
    }
}