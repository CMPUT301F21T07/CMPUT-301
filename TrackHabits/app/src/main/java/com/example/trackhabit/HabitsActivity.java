package com.example.trackhabit;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 * Represents the activity of creating, viewing, and deleting habits
 */
public class HabitsActivity extends AppCompatActivity implements NewHabitDialog.EditDialogListener, EditHabitDialog.HabitDialogListener {

    // Frequently used strings initialized
    private static final String TAG = "TAG" ;

    private static final String KEY_NAME    = "Name";
    private static final String KEY_TITLE   = "Title";
    private static final String KEY_DATE    = "Start Date";
    private static final String KEY_REASON  = "Reason";
    private static final String KEY_PRIVATE = "Private";
    private static final String KEY_USER    = "User";
    private static final String KEY_DAYS    = "Days";

    // UI elements declared
    ListView habitListView;
    RecyclerView dynamicHabitListView;
    RecyclerAdapter listViewAdapter;

    Boolean switchState;
    Switch yhSwitch;

    FloatingActionButton extraOptionsButton, addNewHabit, viewHabitEvents, viewFriendsButton, logOutButton;

    LinearLayout newHabitLayout, viewEventsLayout, viewFriendsLayout, logOutLayout;


    ItemTouchHelper itemTouchHelper;

    TextView tV1, tV2;

    // Variables declared
    private ArrayList<Habit> todayHabitDataList;
    private ArrayList<Habit> allHabitDataList;
    private ArrayList<Habit> currentList;
    private ArrayList<String> daysList;

    private String userName;
    private String strDay, days;

    Integer temp_index;
    Boolean flag_for_floating = true;

    // Firebase reference initialized
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

        // Initializing UI elements
        yhSwitch = findViewById(R.id.YHSwitch);
        switchState = yhSwitch.isChecked();

        tV1 = findViewById(R.id.no_habit_id);
        tV2 = findViewById(R.id.no_habit_id2);

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
                openMenu();
            }
            else {
                closeMenu();
            }
        });

        // Setting up button clicks
        viewHabitEvents.setOnClickListener(view -> viewAllHabitEvents());
        addNewHabit.setOnClickListener(view -> addNewHabit());
        viewFriendsButton.setOnClickListener(view -> viewFriends());
        logOutButton.setOnClickListener(view -> logOut());

        // Getting userName from login screen
        userName = getIntent().getExtras().getString("name_key");
        dynamicHabitListView = findViewById(R.id.dynamic_list_view);

        // Initializing array lists
        allHabitDataList   = new ArrayList<>();
        todayHabitDataList = new ArrayList<>();
        currentList = allHabitDataList;

        // Getting the current date
        Date date = new Date();
        DateFormat day        = new SimpleDateFormat("EEEE");
        strDay  = day.format(date);

        // Iterating through database to get a user's habits
        habitsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * Function that updates the Firebase database on an event
             * @param value This is the message that holds any supported value type
             * @param error This is an error
             */
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                currentList.clear();
                allHabitDataList.clear();
                todayHabitDataList.clear();
                assert value != null;
                for(QueryDocumentSnapshot doc: value)
                {

                    Log.d(TAG, String.valueOf(doc.getData().get(KEY_NAME)));
                    String userID = (String)doc.getData().get(KEY_USER);
                    days     = (String) doc.getData().get(KEY_DAYS);
                    daysList = getDaysList(days);
                    if (userID.equals(userName)){
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
                if (listViewAdapter.getItemCount() == 0){
                    tV1.setVisibility(View.VISIBLE);
                    tV2.setVisibility(View.VISIBLE);
                    newHabitLayout.setVisibility(View.VISIBLE);
                }
                else {
                    tV1.setVisibility(View.GONE);
                    tV2.setVisibility(View.GONE);
                }
                listViewAdapter.notifyDataSetChanged();
            }
        });

        // Checking if switch is toggled on or off
        yhSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * Function that switches the habit list between today's habit list and all habit list
             * @param buttonView Toggle switch
             * @param isChecked Toggled on
             */
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    yhSwitch.setText("Your Habits Today");
                    currentList = todayHabitDataList;
                }else{
                    yhSwitch.setText("All Habits");
                    currentList = allHabitDataList;
                }
                listViewAdapter = new RecyclerAdapter(HabitsActivity.this, currentList);
                dynamicHabitListView.setAdapter(listViewAdapter);
                dynamicHabitListView.setLayoutManager(new LinearLayoutManager(HabitsActivity.this));
            }
        });

        // Initializing RecyclerAdapter and setting it as the adapter for our dynamicListView
        listViewAdapter = new RecyclerAdapter(HabitsActivity.this, currentList);
        dynamicHabitListView.setAdapter(listViewAdapter);
        dynamicHabitListView.setLayoutManager(new LinearLayoutManager(HabitsActivity.this));

        // Initializing a ItemTouchHelper to make items in our recycler view moveable
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(dynamicHabitListView);
    }


    /**
     *  Function that displays more options for the user to click on
     */
    private void openMenu() {
        newHabitLayout.setVisibility(View.VISIBLE);
        viewEventsLayout.setVisibility(View.VISIBLE);
        viewFriendsLayout.setVisibility(View.VISIBLE);
        logOutLayout.setVisibility(View.VISIBLE);

        extraOptionsButton.setImageResource(R.drawable.ic_baseline_not_interested_24);
        flag_for_floating = false;
    }

    /**
     *  Function that removes more options for users
     */
    private void closeMenu() {
        newHabitLayout.setVisibility(View.GONE);
        viewEventsLayout.setVisibility(View.GONE);
        viewFriendsLayout.setVisibility(View.GONE);
        logOutLayout.setVisibility(View.GONE);

        extraOptionsButton.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
        flag_for_floating = true;
    }

    /**
     *  Function that views all the habit events for the user so far
     */
    private void viewFriends() {
        Intent newIntent= new Intent(HabitsActivity.this, ViewFriend.class);
        closeMenu();
        newIntent.putExtra("name_key", userName);
        startActivity(newIntent);
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
        temp_index = listViewAdapter.getItem();
        switch(item.getItemId()) {
            case 121:
                Habit tempOpen = currentList.get(temp_index);
                viewDialog(tempOpen);
                return true;
            case 122:
                Habit tempEdit = currentList.get(temp_index);
                editDialog(tempEdit);
                return true;
            case 123:
                Habit tempDelete = currentList.get(temp_index);
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
    private void viewDialog(Habit tempOpen) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        ViewHabitDialog viewHabit = new ViewHabitDialog();
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
    }

    /**
     * Function that opens a fragment that allows a user to edit a given habit
     * @param tempEdit This is the habit object that is being viewed
     */
    private void editDialog(Habit tempEdit) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        EditHabitDialog editHabit = new EditHabitDialog();
        Bundle args = new Bundle();
        args.putString("user_name", userName);
        args.putString("habit_name", tempEdit.getHabitName());
        args.putString("habit_title", tempEdit.getHabitTitle());
        args.putString("habit_date", dateFormat.format(tempEdit.getStartDate().toDate()));
        args.putString("habit_reason", tempEdit.getHabitReason());
        args.putString("habit_days", tempEdit.getDays());
        args.putString("habit_day", tempEdit.getDay());
        args.putString("habit_month", tempEdit.getMonth());
        args.putString("habit_year", tempEdit.getYear());
        args.putBoolean("habit_privacy", tempEdit.getPrivacy());
        editHabit.setArguments(args);
        editHabit.show(getSupportFragmentManager(), "EDIT HABIT");
    }

    /**
     * Function that deletes a habit object for a specific user and returns a message
     * @param tempDelete This is the habit object that needs to be deleted
     */
    private void removeHabit(Habit tempDelete) {

        // Deleting habits from the habits collection
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
                        listViewAdapter.notifyDataSetChanged();
                    }).addOnFailureListener(e -> Log.d(TAG, "Data could not be deleted!" + e.toString()));
                }
            }

        });

        // Deleting habit events from the Habit Events collections
        CollectionReference habitEventsRef = db.collection("Habit Events");
        habitEventsRef.addSnapshotListener((value, error) -> {
           assert value != null;
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReference();
            StorageReference photoRef;
           for(QueryDocumentSnapshot doc: value) {
              String userID = (String) doc.getData().get("UserName");
              String habitName = (String) doc.getData().get("HabitName");
              String currentHabitName = tempDelete.getHabitName();
              String date = (String)doc.getData().get("Date");
              boolean photoUploaded = (Boolean)doc.getData().get("PhotoUploaded");

              if (userID.equals(userName) && currentHabitName.equals(habitName)) {
                  doc.getReference().delete().addOnSuccessListener(success -> {
                      Log.d(TAG, "Event " + habitName + " " + date + " deleted");
                  }).addOnFailureListener(failure -> {
                      Log.d(TAG, "Event " + habitName + " " + date + " was not deleted!");
                  });
                  if (photoUploaded) {
                      String dataName = habitName + " " + userName + " " + date + ".jpg";
                      photoRef = storageRef.child(dataName);
                      photoRef.delete().addOnSuccessListener(success -> {
                          Log.d(TAG, "Image deleted");
                      }).addOnFailureListener(failure -> {
                          Log.d(TAG, "Image was not deleted!");
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


    /*
     *  Converts letter denoting the days into full day names and adds it to a list
     */
    private ArrayList<String> getDaysList(String daysOfWeek) {
        ArrayList<String> dList = new ArrayList<>();
        if (daysOfWeek.contains("M"))
            dList.add("Monday");
        if (daysOfWeek.contains("T"))
            dList.add("Tuesday");
        if (daysOfWeek.contains("W"))
            dList.add("Wednesday");
        if (daysOfWeek.contains("R"))
            dList.add("Thursday");
        if (daysOfWeek.contains("F"))
            dList.add("Friday");
        if (daysOfWeek.contains("S"))
            dList.add("Saturday");
        if (daysOfWeek.contains("U"))
            dList.add("Sunday");
        return dList;
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
        Habit addedTempHabit = new Habit(name, userName, title, reason,startTime, itemPrivacy, days);
        data.put(KEY_NAME, name);
        data.put(KEY_TITLE, title);
        data.put(KEY_DATE, startTime);
        data.put(KEY_REASON, reason);
        data.put(KEY_PRIVATE, itemPrivacy);
        data.put(KEY_USER, userName);
        data.put(KEY_DAYS, days);

        // Adding data of new habit to firebase
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
     * @param startTime Habit start date
     * @param itemPrivacy Habit privacy
     */
    @Override
    public void updateHabit(String oldName, String name, String title, String reason, Timestamp startTime, Boolean itemPrivacy, String days) {
        HashMap<String, Object> data = new HashMap<>();
        //data.put(KEY_NAME, name);
        data.put(KEY_TITLE, title);
        data.put(KEY_DATE, startTime);
        data.put(KEY_REASON, reason);
        data.put(KEY_PRIVATE, itemPrivacy);
        data.put(KEY_USER, userName);
        data.put(KEY_DAYS, days);
        habitsRef.document(oldName)
                .update(data)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Habit has been updated successfully!");
                    Toast.makeText(HabitsActivity.this, "Habit has been updated successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.d(TAG, "Habit could not be updated!" + e.toString()));
    }

    // Initializing a SimpleCallback to allowing reordering the habits list
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(currentList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


}