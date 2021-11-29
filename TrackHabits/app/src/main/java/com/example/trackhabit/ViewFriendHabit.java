package com.example.trackhabit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;

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
/**
 * Represents an activity for viewing friend's habits
 */

public class ViewFriendHabit extends AppCompatActivity {
    // Declaring commonly used strings
    private static final String TAG = "TAG" ;

    private static final String KEY_NAME    = "Name";
    private static final String KEY_TITLE   = "Title";
    private static final String KEY_DATE    = "Start Date";
    private static final String KEY_REASON  = "Reason";
    private static final String KEY_PRIVATE = "Private";
    private static final String KEY_USER    = "User";
    private static final String KEY_DAYS    = "Days";

    // Declaring UI elements
    ListView habitListView;
    FloatingActionButton extraOptionsButton, goBackButton;
    LinearLayout goBackLayout;

    // Declaring variables
    private ArrayList<Habit> allHabitDataList;
    private ArrayList<String> daysList;

    HabitListAdapter habitsArrayAdapter;

    private String userName;
    private String strDay, days;

    Boolean flag_for_floating = true;

    // Initializing access to Firestore database
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference habitsRef = db.collection("Habits");

    /**
     * Creates an instance that creates the activity for viewing friends's habits
     * will be check on creation of instance.
     * @param savedInstanceState This is the instance state from the previous creation of habits activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_habit);


        // Initializing buttons and layouts
        extraOptionsButton = findViewById(R.id.friend_open_menu_button);
        goBackButton       = findViewById(R.id.friend_go_back);
        goBackLayout       = findViewById(R.id.friend_go_back_layout);
        habitListView = findViewById(R.id.friend_habits_list_view);


        // Setting up button click listeners
        extraOptionsButton.setOnClickListener(v -> {
            if (flag_for_floating) {
                openMenu();
            }
            else {
                closeMenu();
            }
        });

        goBackButton.setOnClickListener(view -> finish());

        // Getting username of a friend from the previous activity
        userName = getIntent().getExtras().getString("name_key");

        this.setTitle(userName + "'s Habits");

        // Initializing array lists
        allHabitDataList = new ArrayList<>();

        Date date = new Date();
        DateFormat day        = new SimpleDateFormat("EEEE");
        strDay  = day.format(date);

        // Iterating over the habits list to get list of habits of a friend
        habitsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * Function that updates the Firebase database on an event
             * @param value This is the message that holds any supported value type
             * @param error This is an error
             */
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                allHabitDataList.clear();
                assert value != null;
                for(QueryDocumentSnapshot doc: value)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get(KEY_NAME)));
                    String userID = (String)doc.getData().get(KEY_USER);

                    daysList = new ArrayList<>();
                    days     = (String) doc.getData().get(KEY_DAYS);
                    Boolean publicHabit = (Boolean) doc.getData().get(KEY_PRIVATE);
                    getDaysList();
                    if (userID.equals(userName) && !publicHabit){
                        Habit tempHabit = new Habit((String) doc.getData().get(KEY_NAME),
                                (String) doc.getData().get(KEY_USER),
                                (String) doc.getData().get(KEY_TITLE),
                                (String) doc.getData().get(KEY_REASON),
                                (Timestamp)doc.getData().get(KEY_DATE),
                                (Boolean) doc.getData().get(KEY_PRIVATE),
                                (String) doc.getData().get(KEY_DAYS));
                        allHabitDataList.add(tempHabit);
                    }
                }
                habitsArrayAdapter.notifyDataSetChanged();
            }
        });

        // Initializing the list view to display habits in the allHabitDataList
        habitsArrayAdapter = new HabitListAdapter(ViewFriendHabit.this, allHabitDataList);
        habitListView.setAdapter(habitsArrayAdapter);
        habitListView.setClickable(true);

        // Setting up long press on a list view item
        habitListView.setOnItemLongClickListener((adapterView, view, i, l) -> viewHabit(allHabitDataList.get(i)));
    }

    /**
     * function that returns a boolean after viewing habit
     * @param tempOpen Habit
     * @return boolean
     */
    private boolean viewHabit(Habit tempOpen) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        ViewFriendHabitDialog viewHabit = new ViewFriendHabitDialog();
        Bundle args = new Bundle();
        args.putString("user_name", userName);
        args.putString("habit_name", tempOpen.getHabitName());
        args.putString("habit_title", tempOpen.getHabitTitle());
        args.putString("habit_date", dateFormat.format(tempOpen.getStartDate().toDate()));
        args.putString("habit_reason", tempOpen.getHabitReason());
        args.putString("habit_days", tempOpen.getDays());
        args.putBoolean("habit_privacy", tempOpen.getPrivacy());
        viewHabit.setArguments(args);
        viewHabit.show(getSupportFragmentManager(), "VIEW HABIT DETAILS");
        return true;
    }

    /**
     * function that opens the menu
     */
    private void openMenu() {
        goBackLayout.setVisibility(View.VISIBLE);

        extraOptionsButton.setImageResource(R.drawable.ic_baseline_not_interested_24);
        flag_for_floating = false;
    }

    /**
     * function that closes the menu
     */
    private void closeMenu() {
        goBackLayout.setVisibility(View.GONE);

        extraOptionsButton.setImageResource(R.drawable.ic_baseline_not_interested_24);
        flag_for_floating = true;
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
}