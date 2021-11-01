package com.example.trackhabit;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HabitsActivity extends AppCompatActivity {

    private static final String TAG = "TAG" ;

    private static final String KEY_NAME    = "Name";
    private static final String KEY_TITLE   = "Title";
    private static final String KEY_DATE    = "Start Date";
    private static final String KEY_REASON  = "Reason";
    private static final String KEY_PRIVATE = "Private";
    private static final String KEY_USER    = "User";

    CharSequence text;

    ListView habitListView;
    ArrayList<Habits> todayHabitDataList = new ArrayList<>();
    ArrayList<Habits> allHabitDataList   = new ArrayList<>();

    ArrayAdapter<Habits> habitsArrayAdapter;

    Boolean switchState;
    Switch yhSwitch;

    FloatingActionButton addHabitButton;

    private FirebaseFirestore   db = FirebaseFirestore.getInstance();
    private CollectionReference habitsRef = db.collection("Habits");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        yhSwitch = findViewById(R.id.YHSwitch);
        switchState = yhSwitch.isChecked();
        addHabitButton = findViewById(R.id.add_button);
        habitListView = findViewById(R.id.habits_list_view);

        String userName = getIntent().getExtras().getString("name_key");


        habitsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: value)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get(KEY_NAME)));
                    String userID = (String) doc.getData().get(KEY_USER);
                    if (userID.equals(userName)){
                        Habits tempHabit = new Habits((String) doc.getData().get(KEY_NAME),
                                (String) doc.getData().get(KEY_USER),
                                (String) doc.getData().get(KEY_TITLE),
                                (String) doc.getData().get(KEY_REASON),
                                "2021-11-01",
                                (Boolean) doc.getData().get(KEY_PRIVATE));
                        todayHabitDataList.add(tempHabit);
                        habitsArrayAdapter = new habitListAdapter(HabitsActivity.this, todayHabitDataList);
                        habitListView.setAdapter(habitsArrayAdapter);
                    }
                }
            }
        });


        yhSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    yhSwitch.setText("Your Habits Today");
                }
                else{
                    yhSwitch.setText("All Habits");
                }
            }
        });



    }


}