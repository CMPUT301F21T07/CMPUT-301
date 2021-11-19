package com.example.trackhabit;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
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

/**
 * Represents the activity of creating, viewing, and deleting habits
 */
public class HabitsActivity extends AppCompatActivity implements NewHabitDialog.EditDialogListener, EditHabitDialog.HabitDialogListener {

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
    private String strDay, days;

    Integer temp_index;
    Boolean flag_for_floating = true;


    Boolean switchState;
    Switch yhSwitch;

    FloatingActionButton extraOptionsButton, addNewHabit, viewHabitEvents, viewFriendsButton, logOutButton;

    LinearLayout newHabitLayout, viewEventsLayout, viewFriendsLayout, logOutLayout;

    private final FirebaseFirestore   db = FirebaseFirestore.getInstance();
    private final CollectionReference habitsRef = db.collection("Habits");

    /**
     * Creates an instance that shows an extra options button and a toggle switch. The extra options
     * can be tapped to access the add new habits and view habit events buttons. The toggle switch
     * will be check on creation of instance.
     * @param savedInstanceState This is the instance state from the previous creation of habits activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        yhSwitch = findViewById(R.id.YHSwitch);
        switchState = yhSwitch.isChecked();

        extraOptionsButton = findViewById(R.id.open_menu_button);
        addNewHabit        = findViewById(R.id.add_habit);
        viewHabitEvents    = findViewById(R.id.view_habit_events);
        viewFriendsButton  = findViewById(R.id.view_friends);
        logOutButton       = findViewById(R.id.log_out_button);

        newHabitLayout    = findViewById(R.id.add_habit_layout);
        viewEventsLayout  = findViewById(R.id.view_events_layout);
        viewFriendsLayout = findViewById(R.id.view_friends_layout);
        logOutLayout      = findViewById(R.id.log_out_layout);

        extraOptionsButton.setOnClickListener(v -> {
            if (flag_for_floating) {
                newHabitLayout.setVisibility(View.VISIBLE);
                viewEventsLayout.setVisibility(View.VISIBLE);
                viewFriendsLayout.setVisibility(View.VISIBLE);
                logOutLayout.setVisibility(View.VISIBLE);

                extraOptionsButton.setImageResource(R.drawable.ic_baseline_not_interested_24);
                flag_for_floating = false;

            }
            else {
                closeMenu();
            }
        });

        viewHabitEvents.setOnClickListener(view -> viewAllHabitEvents());
        addNewHabit.setOnClickListener(view -> addNewHabit());
        viewFriendsButton.setOnClickListener(view -> viewFriends());
        logOutButton.setOnClickListener(view -> logOut());

        userName = getIntent().getExtras().getString("name_key");
        habitListView = findViewById(R.id.habits_list_view);

        allHabitDataList = new ArrayList<>();
        todayHabitDataList = new ArrayList<>();


        currentList = allHabitDataList;
        Date date = new Date();
        DateFormat day        = new SimpleDateFormat("EEEE");
        strDay  = day.format(date);
        habitsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * Function that updates the Firebase database on an event
             * @param value This is the message that holds any supported value type
             * @param error This is an error
             */
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                currentList.clear();

                assert value != null;
                for(QueryDocumentSnapshot doc: value)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get(KEY_NAME)));
                    String userID = (String)doc.getData().get(KEY_USER);

                    daysList = new ArrayList<>();
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
            /**
             * Function that switches the habit list between today's habit list and all habit list
             * @param buttonView Toggle switch
             * @param isChecked Toggled on
             */
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    yhSwitch.setText("Your Habits Today");
                    currentList = todayHabitDataList;
                }

                else{
                    yhSwitch.setText("All Habits");
                    currentList = allHabitDataList;
                }
                habitsArrayAdapter = new habitListAdapter(HabitsActivity.this, currentList);
                habitListView.setAdapter(habitsArrayAdapter);
            }
        });
        habitsArrayAdapter = new habitListAdapter(HabitsActivity.this, currentList);
        habitListView.setAdapter(habitsArrayAdapter);
    }

    /**
     *  Function that views all the habit events for the user so far
     */
    private void viewFriends() {
        Intent newIntent= new Intent(HabitsActivity.this, ViewFriends.class);
        closeMenu();
        newIntent.putExtra("name_key", userName);
        startActivity(newIntent);
    }

    private void closeMenu() {
        newHabitLayout.setVisibility(View.GONE);
        viewEventsLayout.setVisibility(View.GONE);
        viewFriendsLayout.setVisibility(View.GONE);
        logOutLayout.setVisibility(View.GONE);

        extraOptionsButton.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
        flag_for_floating = true;
    }

    /**
     *  Function that logs out the user from the application
     */
    private void logOut() {
        Intent newIntent = new Intent(HabitsActivity.this, LogInActivity.class);
        Toast.makeText(HabitsActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
        startActivity(newIntent);
        finish();

    }

    /**
     *  Function that views all the habit events for the user so far
     */
    private void viewAllHabitEvents() {
        Intent newIntent= new Intent(HabitsActivity.this, CalendarActivity.class);
        closeMenu();
        newIntent.putExtra("ID", userName);
        startActivity(newIntent);
        // View All Habit
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
     * @param tempOpen This is the habit object that is being viewed
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        EditHabitDialog editHabit = new EditHabitDialog();
        Bundle args = new Bundle();
        args.putString("user_name", userName);
        args.putString("habit_name", tempEdit.getHabitName());
        args.putString("habit_title", tempEdit.getHabitTitle());
        args.putString("habit_date", dateFormat.format(tempEdit.getStartDate().toDate()));
        args.putString("habit_reason", tempEdit.getHabitReason());
        editHabit.setArguments(args);
        editHabit.show(getSupportFragmentManager(), "EDIT HABIT");
        // COMPLETE
    }

    /**
     * Function that deletes a habit object for a specific user and returns a message
     * @param tempDelete This is the habit object that needs to be deleted
     */
    private void removeHabit(Habits tempDelete) {
        habitsRef.addSnapshotListener((value, error) -> {
            assert value != null;
            for(QueryDocumentSnapshot doc: value)
            {
                Log.d(TAG, String.valueOf(doc.getData().get(KEY_NAME)));
                String userID = (String) doc.getData().get(KEY_USER);

                if (userID.equals(userName) && doc.getData().get(KEY_NAME).equals(tempDelete.getHabitName())){
                    doc.getReference().delete().addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Data has been deleted successfully!");
                        currentList.remove(tempDelete);
                        Toast.makeText(HabitsActivity.this, "Habit (and habit events) deleted", Toast.LENGTH_SHORT).show();
                        habitsArrayAdapter.notifyDataSetChanged();
                    }).addOnFailureListener(e -> Log.d(TAG, "Data could not be deleted!" + e.toString()));
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
        if (days.contains("M"))
            daysList.add("Monday");
        if (days.contains("T"))
            daysList.add("Tuesday");
        if (days.contains("W"))
            daysList.add("Wednesday");
        if (days.contains("R"))
            daysList.add("Thursday");
        if (days.contains("F"))
            daysList.add("Friday");
        if (days.contains("S"))
            daysList.add("Saturday");
        if (days.contains("U"))
            daysList.add("Sunday");
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
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Habit has been added successfully!");
                    Toast.makeText(HabitsActivity.this, "Habit has been added successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.d(TAG, "Habit could not be added!" + e.toString()));
    }
    /**
     * Function that gets data from the editHabit dialog box using a listener
     * @param name Habit name
     * @param title Habit title
     * @param reason Habit reason
     */
    @Override
    public void updateHabit(String name, String title, String reason) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(KEY_NAME, name);
        data.put(KEY_TITLE, title);
        //data.put(KEY_DATE, startTime);
        data.put(KEY_REASON, reason);
        //data.put(KEY_PRIVATE, itemPrivacy);
        data.put(KEY_USER, userName);
        //data.put(KEY_DAYS, days);
        habitsRef.document(name)
                .update(data)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Habit has been updated successfully!");
                    Toast.makeText(HabitsActivity.this, "Habit has been updated successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.d(TAG, "Habit could not be updated!" + e.toString()));
    }
}