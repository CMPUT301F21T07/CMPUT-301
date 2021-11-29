package com.example.trackhabit;

import static java.lang.String.valueOf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Represents an dialog for viewing friend's habits
 */
public class ViewFriendHabitDialog extends AppCompatDialogFragment {
    // Declaring UI elements
    private TextView habitNameView, habitTitleView, habitReasonView, habitStartDateView, habitPrivacyView, habitDaysView, habitConsistView;

    private ProgressBar consistency;

    // Declaring variables
    private String userName, habitName, habitTitle, habitStart, habitReason, days, habitDays = "";
    private final String[] privacyOptions = new String[]{"Private","Public"};
    private Integer habitConsist = 0, amountEvents = 0;
    private Boolean itemPrivacy;

    // Getting access to a firebase collection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference habitEventsRef = db.collection("Habit Events");
    private Date startDate;


    /**
     * Creates an instance that creates the dialog for viewing friends's habits
     * will be check on creation of instance.
     * @param savedInstanceState This is the instance state from the previous creation of habits activity
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflating dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_habit, null);

        //get strings from arguments
        userName = getArguments().getString("user_name");
        habitName = getArguments().getString("habit_name");
        habitTitle = getArguments().getString("habit_title");
        habitStart = getArguments().getString("habit_date");
        habitReason = getArguments().getString("habit_reason");
        days = getArguments().getString("habit_days");
        itemPrivacy = getArguments().getBoolean("habit_privacy");

        try {
            startDate=new SimpleDateFormat("MM/dd/yyyy").parse(habitStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Building view
        builder.setView(view)
                .setTitle("Habit Details")
                .setPositiveButton("Back", (dialogInterface, i) -> {});


        //get text views
        habitNameView   = view.findViewById(R.id.view_habit_name);
        habitReasonView= view.findViewById(R.id.view_habit_reason);
        habitTitleView  = view.findViewById(R.id.view_habit_title);
        habitStartDateView  = view.findViewById(R.id.view_start_date);
        habitPrivacyView  = view.findViewById(R.id.view_privacy);
        habitDaysView  = view.findViewById(R.id.view_days);
        habitConsistView = view.findViewById(R.id.view_consist);
        consistency = view.findViewById(R.id.progressBar);

        consistency.setMax(100);

        //set text views
        habitNameView.setText(habitName);
        habitReasonView.setText(habitReason);
        habitTitleView.setText(habitTitle);
        habitStartDateView.setText(habitStart);
        setDays();
        habitDaysView.setText(habitDays);
        if (itemPrivacy){
            habitPrivacyView.setText(privacyOptions[0]);
        } else {
            habitPrivacyView.setText(privacyOptions[1]);
        }
        getConsistency();

        return builder.create();
    }

    /**
     * Function that sets days
     */
    private void setDays(){
        if (days.contains("M"))
            habitDays = habitDays + "Mon";
        if (days.contains("T"))
            habitDays = habitDays + " Tue";
        if (days.contains("W"))
            habitDays = habitDays + " Wed";
        if (days.contains("R"))
            habitDays = habitDays + " Thu";
        if (days.contains("F"))
            habitDays = habitDays + " Fri";
        if (days.contains("S"))
            habitDays = habitDays + " Sat";
        if (days.contains("U"))
            habitDays = habitDays + " Sun";
        if (habitDays == ""){
            habitDays = "None";
        }
    }
    /**
     * Function that gets the consistency of a habit
     */


    // Calculate the consistency of the selected habit
    private void getConsistency(){

        habitEventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * Function that gets Events from the Firebase database on an event
             * and keeps track of the amount of events for a given habit and user to calculate consistency
             * @param value This is the message that holds any supported value type
             * @param error This is an error
             */
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                for(QueryDocumentSnapshot doc: value) { //search through habit events for a given habit and user
                    Log.d("TAG", String.valueOf(doc.getData().get(habitName)));
                    String habitNames = (String) doc.getData().get("HabitName");
                    String userNames = (String) doc.getData().get("UserName");

                    if (userNames.equals(userName) && habitNames.equals(habitName)) {
                        amountEvents++;
                    }
                }

                Integer eventDays = countDays(startDate); //get the number of event days

                if(eventDays > 0 && amountEvents > 0){habitConsist = 100/eventDays*amountEvents;} //if there have been event Days and events then calculate consistency
                else if(eventDays == 0){habitConsist = 100;} //if there aren't any event days, then any number of events is 100% consistency
                else {habitConsist = 0;} //if there have been event days, but not events then consistency = 0%

                habitConsistView.setText(" "+habitConsist+" %");
                consistency.setProgress(habitConsist);
                System.out.println("**!! Calculating consistency with - # days: "+eventDays+", # Events: "+amountEvents+", consistency: "+habitConsist);

            }
        });

    }

    /**
     * Function that calculates the number of event days,
     * where an event day is the number of days a Habit should have occurred on between the start date and today inclusively
     * @param startDate Date object of the day the habit started on
     * @return eventDays As an Integer of the amount of days a habit should have events for
     */
    private Integer countDays(Date startDate){
        String[] weekdays = new String[]{"U", "M", "T", "W", "R", "F", "S"};

        Calendar todayCal = Calendar.getInstance();
        Date today = todayCal.getTime(); //get today's date

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        int startDay = startCal.get(Calendar.DAY_OF_WEEK); //get day of the week for start date

        int eventDays = 0;
        long milliseconds = today.getTime()-startDate.getTime(); //difference between start date and today in milliseconds
        float hours = milliseconds / 3600000;
        float day = (hours / 24);
        int dayNum = (int) Math.ceil(day); //round up to include today as a day

        for (int x = startDay, y = 0; y < dayNum;x++,y++){ //starting at the weekday of the start date, cycle through weekdays for the amount of days between the start date and today
            if(x==8){x = 1;} //if weekday is passed saturday, set it to sunday
            if (days.contains(weekdays[x-1])) {eventDays++;} //add an event day if the habit is scheduled to occur on this weekday
        }
        return eventDays;
    }


}
