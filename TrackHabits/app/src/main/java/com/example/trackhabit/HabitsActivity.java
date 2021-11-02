package com.example.trackhabit;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.Timestamp;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HabitsActivity extends AppCompatActivity implements NewHabitDialog.EditDialogListener {

    private static final String TAG = "TAG" ;

    private static final String KEY_NAME    = "Name";
    private static final String KEY_TITLE   = "Title";
    private static final String KEY_DATE    = "Start Date";
    private static final String KEY_REASON  = "Reason";
    private static final String KEY_PRIVATE = "Private";
    private static final String KEY_USER    = "User";

    CharSequence text;

    ListView habitListView;

    ArrayList<Habits> todayHabitDataList;
    ArrayList<Habits> allHabitDataList   = new ArrayList<>();

    ArrayAdapter<Habits> habitsArrayAdapter;
    private String userName;
    private String strDate;

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
        addHabitButton = findViewById(R.id.add_habit_button);
        habitListView = findViewById(R.id.habits_list_view);
        addHabitButton.setOnClickListener(view -> addNew());

        userName = getIntent().getExtras().getString("name_key");

        allHabitDataList = new ArrayList<>();
        updateAllHabitList();

        yhSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    todayHabitDataList = new ArrayList<>();
                    yhSwitch.setText("Your Habits Today");
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                    strDate = dateFormat.format(date);
                    updateTodayHabitList();
                }
                else{
                    allHabitDataList = new ArrayList<>();
                    yhSwitch.setText("All Habits");
                    updateAllHabitList();
                }
            }
        });

        habitListView.setClickable(true);
    }

    private void addNew() {
        NewHabitDialog newHabit = new NewHabitDialog();
        Bundle args = new Bundle();
        args.putString("user_name", userName);
        newHabit.setArguments(args);
        newHabit.show(getSupportFragmentManager(), "ADD NEW HABIT");
    }

    protected void updateAllHabitList(){
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
                                (Timestamp)doc.getData().get(KEY_DATE),
                                (Boolean) doc.getData().get(KEY_PRIVATE));
                        allHabitDataList.add(tempHabit);
                        habitsArrayAdapter = new habitListAdapter(HabitsActivity.this, allHabitDataList);
                        habitListView.setAdapter(habitsArrayAdapter);
                    }
                }
            }
        });
    }

    protected void updateTodayHabitList(){
        habitsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for(QueryDocumentSnapshot doc: value)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get(KEY_NAME)));

                    Timestamp docTimeStamp = (Timestamp) doc.getData().get(KEY_DATE);

                    Date currentDocDate = docTimeStamp.toDate();
                    DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

                    String currentDocDateStr = dateFormat.format(currentDocDate);
                    String userID = (String) doc.getData().get(KEY_USER);

                    if (userID.equals(userName) && strDate.equals(currentDocDateStr)){
                        Habits tempHabit = new Habits((String) doc.getData().get(KEY_NAME),
                                (String) doc.getData().get(KEY_USER),
                                (String) doc.getData().get(KEY_TITLE),
                                (String) doc.getData().get(KEY_REASON),
                                (Timestamp)doc.getData().get(KEY_DATE),
                                (Boolean) doc.getData().get(KEY_PRIVATE));
                        todayHabitDataList.add(tempHabit);
                        habitsArrayAdapter = new habitListAdapter(HabitsActivity.this, todayHabitDataList);
                        habitListView.setAdapter(habitsArrayAdapter);
                    }
                }
            }
        });
    }


    @Override
    public void addedHabit(Habits h) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(KEY_NAME, h.getHabitName());
        data.put(KEY_TITLE, h.getHabitTitle());
        data.put(KEY_DATE, h.getStartDate());
        data.put(KEY_REASON, h.getHabitReason());
        data.put(KEY_PRIVATE, h.getPrivacy());
        data.put(KEY_USER, h.getHabitUser());
        habitsRef.document(h.getHabitName())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Habit has been added successfully!");
                        Toast.makeText(HabitsActivity.this, "Habit has been added successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Habit could not be added!" + e.toString());

                    }
                });
        todayHabitDataList.add(h);
        allHabitDataList.add(h);
        habitsArrayAdapter = new habitListAdapter(HabitsActivity.this, todayHabitDataList);
        habitListView.setAdapter(habitsArrayAdapter);
    }
}