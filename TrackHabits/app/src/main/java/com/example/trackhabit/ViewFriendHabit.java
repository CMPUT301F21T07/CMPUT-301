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

public class ViewFriendHabit extends AppCompatActivity {
    private static final String TAG = "TAG" ;

    private static final String KEY_NAME    = "Name";
    private static final String KEY_TITLE   = "Title";
    private static final String KEY_DATE    = "Start Date";
    private static final String KEY_REASON  = "Reason";
    private static final String KEY_PRIVATE = "Private";
    private static final String KEY_USER    = "User";
    private static final String KEY_DAYS    = "Days";

    ListView habitListView;

    private ArrayList<Habit> todayHabitDataList;
    private ArrayList<Habit> allHabitDataList;
    private ArrayList<Habit> currentList;
    private ArrayList<String> daysList;

    ArrayAdapter<Habit> habitsArrayAdapter;

    private String userName;
    private String strDay, days;

    Integer temp_index;
    Boolean flag_for_floating = true;

    FloatingActionButton extraOptionsButton, goBackButton;

    LinearLayout goBackLayout;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference habitsRef = db.collection("Habits");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_habit);

        extraOptionsButton = findViewById(R.id.friend_open_menu_button);
        goBackButton       = findViewById(R.id.friend_go_back);
        goBackLayout       = findViewById(R.id.friend_go_back_layout);


        extraOptionsButton.setOnClickListener(v -> {
            if (flag_for_floating) {
                openMenu();
            }
            else {
                closeMenu();
            }
        });

        goBackButton.setOnClickListener(view -> finish());

        userName = getIntent().getExtras().getString("name_key");
        habitListView = findViewById(R.id.friend_habits_list_view);

        this.setTitle(userName + "'s Habits");

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

                        if (daysList.contains(strDay)){
                            todayHabitDataList.add(tempHabit);
                        }

                    }
                }
                habitsArrayAdapter.notifyDataSetChanged();
            }
        });

        habitsArrayAdapter = new habitListAdapter(ViewFriendHabit.this, currentList);
        habitListView.setAdapter(habitsArrayAdapter);
        habitListView.setClickable(true);
        habitListView.setOnItemLongClickListener((adapterView, view, i, l) -> viewHabit(currentList.get(i)));
    }

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


    private void openMenu() {
        goBackLayout.setVisibility(View.VISIBLE);

        extraOptionsButton.setImageResource(R.drawable.ic_baseline_not_interested_24);
        flag_for_floating = false;
    }

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