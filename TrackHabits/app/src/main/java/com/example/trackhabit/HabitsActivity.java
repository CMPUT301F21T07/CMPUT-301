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

    private ArrayList<Habits> todayHabitDataList;
    private ArrayList<Habits> allHabitDataList;
    private ArrayList<Habits> currentList;
    private ArrayList<String> daysList;

    ArrayAdapter<Habits> habitsArrayAdapter;
    private String userName;
    private String strDate, strDay, days;

    Integer temp_index;
    Boolean flag_for_floating = true;


    Boolean switchState;
    Switch yhSwitch;

    FloatingActionButton extraOptionsButton, addNewHabit, viewHabitEvents;

    private FirebaseFirestore   db = FirebaseFirestore.getInstance();
    private CollectionReference habitsRef = db.collection("Habits");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        yhSwitch = findViewById(R.id.YHSwitch);
        switchState = yhSwitch.isChecked();

        extraOptionsButton = findViewById(R.id.open_menu_button);
        addNewHabit        = findViewById(R.id.add_habit);
        viewHabitEvents    = findViewById(R.id.view_habit_events);

        extraOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_for_floating) {
                    addNewHabit.show();
                    viewHabitEvents.show();

                    extraOptionsButton.setImageResource(R.drawable.ic_baseline_not_interested_24);
                    flag_for_floating = false;

                }else {
                    addNewHabit.hide();
                    viewHabitEvents.hide();

                    extraOptionsButton.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
                    flag_for_floating = true;
                }
            }
        });

        viewHabitEvents.setOnClickListener(view -> viewAllHabitEvents());
        addNewHabit.setOnClickListener(view -> addNewHabit());

        userName = getIntent().getExtras().getString("name_key");
        habitListView = findViewById(R.id.habits_list_view);

        allHabitDataList = new ArrayList<Habits>();
        todayHabitDataList = new ArrayList<Habits>();


        currentList = allHabitDataList;
        Date date = new Date();
        DateFormat day        = new SimpleDateFormat("EEEE");
        strDay  = day.format(date);
        habitsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
              currentList.clear();

                for(QueryDocumentSnapshot doc: value)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get(KEY_NAME)));
                    String userID = (String) doc.getData().get(KEY_USER);

                    daysList = new ArrayList<String>();
                    days     = (String) doc.getData().get(KEY_DAYS);

                    getDaysList();

                    if (userID.equals(userName)){
                        Habits tempHabit = new Habits((String) doc.getData().get(KEY_NAME),
                                (String) doc.getData().get(KEY_USER),
                                (String) doc.getData().get(KEY_TITLE),
                                (String) doc.getData().get(KEY_REASON),
                                (Timestamp)doc.getData().get(KEY_DATE),
                                (Boolean) doc.getData().get(KEY_PRIVATE),
                                (String) doc.getData().get(KEY_DAYS));
                        allHabitDataList.add(tempHabit);
                        if (daysList.contains(strDay)){
                            todayHabitDataList.add(tempHabit);
                        }

                    }
                }
                habitsArrayAdapter.notifyDataSetChanged();
            }
        });


        habitListView.setClickable(true);
        habitListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            registerForContextMenu(habitListView);
            temp_index = i;
            return false;
        });

        yhSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    yhSwitch.setText("Your Habits Today");

                    currentList = todayHabitDataList;
                    habitsArrayAdapter = new habitListAdapter(HabitsActivity.this, currentList);
                    habitListView.setAdapter(habitsArrayAdapter);

                }

                else{
                    yhSwitch.setText("All Habits");

                    currentList = allHabitDataList;
                    habitsArrayAdapter = new habitListAdapter(HabitsActivity.this, currentList);
                    habitListView.setAdapter(habitsArrayAdapter);

                }
            }
        });


        habitsArrayAdapter = new habitListAdapter(HabitsActivity.this, currentList);
        habitListView.setAdapter(habitsArrayAdapter);

    }

    /**
     *  Function that views all the habit events for the user so far
     */
    private void viewAllHabitEvents() {
        Intent newIntent= new Intent(HabitsActivity.this, CalendarActivity.class);
        startActivity(newIntent);
        // View All Habit Events
    }


    /**
     * Function that creates a context menu when there is a long press on a ListView item
     * @param menu
     * @param view
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
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
            case R.id.edit_option:
                Habits tempEdit = currentList.get(temp_index);
                editDialog(tempEdit);
                return true;
            case R.id.open_option:
                Habits tempOpen = currentList.get(temp_index);
                viewDialog(tempOpen);
                return true;
            case R.id.delete_option:
                Habits tempDelete = currentList.get(temp_index);
                removeHabit(tempDelete);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Function that opens a fragment that allows a user to view a given habit
     */
    private void viewDialog(Habits tempOpen) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        ViewHabitDialog viewHabit = new ViewHabitDialog();
        Bundle args = new Bundle();
        args.putString("user_name", userName);
        args.putString("habit_name", tempOpen.getHabitName());
        args.putString("habit_title", tempOpen.getHabitTitle());
        args.putString("habit_date", dateFormat.format(tempOpen.getStartDate().toDate()));
        args.putString("habit_reason", tempOpen.getHabitReason());
        viewHabit.setArguments(args);
        viewHabit.show(getSupportFragmentManager(), "VIEW HABIT DETAILS");
    }


    private void editDialog(Habits tempEdit) {
        // COMPLETE
    }

    private void removeHabit(Habits tempDelete) {
        habitsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: value)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get(KEY_NAME)));
                    String userID = (String) doc.getData().get(KEY_USER);

                    if (userID.equals(userName) && doc.getData().get(KEY_NAME).equals(tempDelete.getHabitName())){
                        doc.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Data has been deleted successfully!");
                                currentList.remove(tempDelete);
                                Toast.makeText(HabitsActivity.this, "Habit (and habit events) deleted", Toast.LENGTH_SHORT).show();
                                habitsArrayAdapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Data could not be deleted!" + e.toString());
                            }
                        });
                    }
                }

            }
        });

    }


    /**
     * Function that opens a fragment that allows a user to input a new habit
     */
    private void addNewHabit() {
        NewHabitDialog newHabit = new NewHabitDialog();
        Bundle args = new Bundle();
        args.putString("user_name", userName);
        newHabit.setArguments(args);
        newHabit.show(getSupportFragmentManager(), "ADD NEW HABIT");
    }


    /**
     *  Converts letter denoting the days into full day names and adds it to a list
     */
    private void getDaysList() {
        if (daysList.size() != 0) {
            daysList.clear();
        }
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


    /**
     * Function that gets data back from the addHabit dialog box using a listener
     * @param name Habit name
     * @param title Habit title
     * @param reason Habit reason
     * @param startTime Habit start date
     * @param itemPrivacy Habit privacy
     * @param days Habit days
     */
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
    }
}
