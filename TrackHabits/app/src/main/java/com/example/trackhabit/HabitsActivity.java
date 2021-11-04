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
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
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
    private static final String KEY_DAYS    = "Days";

    ListView habitListView;

    ArrayList<Habits> todayHabitDataList;
    ArrayList<Habits> allHabitDataList;
    ArrayList<Habits> currentList;

    ArrayAdapter<Habits> habitsArrayAdapter;
    private String userName;
    private String strDate, strDay, days;
    private List<String> daysList;
    Integer temp_index;


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


        addHabitButton.setOnClickListener(view -> addNew());

        userName = getIntent().getExtras().getString("name_key");

        allHabitDataList = new ArrayList<>();
        habitListView = findViewById(R.id.habits_list_view);
        updateAllHabitList();

        yhSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    todayHabitDataList = new ArrayList<>();
                    yhSwitch.setText("Your Habits Today");
                    updateTodayHabitList();
                    currentList = todayHabitDataList;
                }
                else{
                    allHabitDataList = new ArrayList<>();
                    yhSwitch.setText("All Habits");
                    updateAllHabitList();
                    currentList = allHabitDataList;
                }
            }
        });

        habitListView.setClickable(true);
        habitListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            registerForContextMenu(habitListView);
            temp_index = i;
            return false;
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.edit_option:
                Habits tempEdit = currentList.get(temp_index);
                editDialog(tempEdit);
                return true;
            case R.id.open_option:
                Habits tempOpen = currentList.get(temp_index);
                viewDialog(tempOpen);
                return true;
            case R.id.delete_option:
                Toast.makeText(this, "Habit (and habit events) deleted", Toast.LENGTH_SHORT).show();
                Habits tempDelete = currentList.get(temp_index);
                removeHabit(tempDelete);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void viewDialog(Habits tempOpen) {
        // COMPLETE
    }

    private void editDialog(Habits tempEdit) {
        // COMPLETE
    }

    private void removeHabit(Habits tempDelete) {
        // COMPLETE
    }






    private void addNew() {
        NewHabitDialog newHabit = new NewHabitDialog();
        Bundle args = new Bundle();
        args.putString("user_name", userName);
        newHabit.setArguments(args);
        newHabit.show(getSupportFragmentManager(), "ADD NEW HABIT");
    }

    protected void updateAllHabitList(){
        allHabitDataList.clear();
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
                                (Boolean) doc.getData().get(KEY_PRIVATE),
                                (String) doc.getData().get(KEY_DAYS));
                        allHabitDataList.add(tempHabit);
                        habitsArrayAdapter = new habitListAdapter(HabitsActivity.this, allHabitDataList);
                        habitListView.setAdapter(habitsArrayAdapter);
                    }
                }

            }
        });
    }

    protected void updateTodayHabitList(){
        todayHabitDataList.clear();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        DateFormat day        = new SimpleDateFormat("EEEE");
        strDay  = day.format(date);
        strDate = dateFormat.format(date);
        habitsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: value)
                {
                    daysList.clear();
                    Log.d(TAG, String.valueOf(doc.getData().get(KEY_NAME)));

                    String userID = (String) doc.getData().get(KEY_USER);
                    days   = (String) doc.getData().get(KEY_DATE);
                    getDaysList();

                    if (userID.equals(userName) && daysList.indexOf(strDay) != -1)    {
                        Habits tempHabit = new Habits((String) doc.getData().get(KEY_NAME),
                                (String) doc.getData().get(KEY_USER),
                                (String) doc.getData().get(KEY_TITLE),
                                (String) doc.getData().get(KEY_REASON),
                                (Timestamp)doc.getData().get(KEY_DATE),
                                (Boolean) doc.getData().get(KEY_PRIVATE),
                                (String) doc.getData().get(KEY_DAYS));
                        todayHabitDataList.add(tempHabit);
                        habitsArrayAdapter = new habitListAdapter(HabitsActivity.this, todayHabitDataList);
                        habitListView.setAdapter(habitsArrayAdapter);
                    }
                }

            }
        });
    }

    private void getDaysList() {
        if (days.indexOf("M") != -1)
            daysList.add("Monday");
        if (days.indexOf("T") != -1)
            daysList.add("Tuesday");
        if (days.indexOf("W") != -1)
            daysList.add("Wednesday");
        if (days.indexOf("R") != -1)
            daysList.add("Thursday");
        if (days.indexOf("F") != -1)
            daysList.add("Friday");
        if (days.indexOf("S") != -1)
            daysList.add("Saturday");
        if (days.indexOf("U") != -1)
            daysList.add("Sunday");
        return ;
    }


    @Override
    public void addedHabit(String name, String title, String reason, Timestamp startTime, Boolean itemPrivacy, String days) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(KEY_NAME, name);
        data.put(KEY_TITLE, title);
        data.put(KEY_DATE, startTime);
        data.put(KEY_REASON, reason);
        data.put(KEY_PRIVATE, itemPrivacy);
        data.put(KEY_USER, userName);
        data.put(KEY_DAYS, days);
        habitsRef.document(name)
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

        updateAllHabitList();
    }
}