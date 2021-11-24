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


public class ViewHabitDialog extends AppCompatDialogFragment {
    private TextView habitNameView, habitTitleView, habitReasonView, habitStartDateView, habitPrivacyView, habitDaysView, habitConsistView;
    private ProgressBar consistency;
    private String userName, habitName, habitTitle, habitStart, habitReason, days, habitDays = "";
    private String[] weekdays = new String[]{"U", "M", "T", "W", "R", "F", "S"};
    private final String[] privacyOptions = new String[]{"Private","Public"};
    private Integer habitConsist = 0, amountEvents = 0, todayDay, startDay;
    private Boolean itemPrivacy;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference habitEventsRef = db.collection("Habit Events");
    private Date startDate, today;
    private Calendar startCal, todayCal;



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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

        todayCal = Calendar.getInstance();
        today = todayCal.getTime();
        todayDay = todayCal.get(Calendar.DAY_OF_WEEK);
        startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        startDay = startCal.get(Calendar.DAY_OF_WEEK);


        if(days.contains(weekdays[todayDay-1]) && today.getTime()>startDate.getTime()){
            builder.setView(view)
                    .setTitle("Habit Details")
                    .setNegativeButton("Back", (dialogInterface, i) -> {})
                    .setPositiveButton("Add Habit Event", ((dialogInterface, i) -> {
                        ManageHabitEventsFragment addHabitDialog = new ManageHabitEventsFragment(
                                userName, habitName);
                        addHabitDialog.show(getFragmentManager(), "ADD NEW HABIT EVENT");
                        dismiss();
                    }));
        } else {
            builder.setView(view)
                    .setTitle("Habit Details")
                    .setNegativeButton("Back", (dialogInterface, i) -> {
                    });
        }

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

    private void getConsistency(){

        habitEventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * Function that updates the Firebase database on an event
             * @param value This is the message that holds any supported value type
             * @param error This is an error
             */
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                for(QueryDocumentSnapshot doc: value)
                {
                    Log.d("TAG", String.valueOf(doc.getData().get(habitName)));
                    String habitNames = (String) doc.getData().get("HabitName");
                    String userNames = (String) doc.getData().get("UserName");
                    String date = (String) doc.getData().get("Date");

                    if (userNames.equals(habitName) && habitNames.equals(userName)) {amountEvents++;} /**This will need to be updated once events is fixed*/
                }
                Integer amountDays = countDays();

                if(amountDays > 0 && amountEvents > 0){
                    habitConsist = 100/amountDays*amountEvents;}
                else {habitConsist = 0+100*amountEvents;}
                habitConsistView.setText(valueOf(habitConsist)+" %");
                consistency.setProgress(habitConsist);
                System.out.println("**!! Calculating consistency with - # days: "+amountDays+", # Events: "+amountEvents+", consistency: "+habitConsist);

            }
        });

    }


    private Integer countDays(){

        long milliseconds = today.getTime()-startDate.getTime(); //difference between dated in milliseconds
        float hours = milliseconds / 3600000;
        float day = (hours / 24)+1;
        float amountDay = (day / 7) * days.length();
        int amountDays = (int) Math.ceil(amountDay); //round up days to include days that aren't finished

        for (int i = 0; i < startDay-1; i++){ //if the event occurred in the same week as, but before the start date
            if (days.contains(weekdays[i])) {--amountDays;}
        }
        for (int i = 0; i <= todayDay-1; i++){ //events in an incomplete current week
            if (days.contains(weekdays[i])) {++amountDays;}
        }
        return amountDays;
    }


}