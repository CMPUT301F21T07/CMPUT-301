package com.example.trackhabit;

import static java.lang.String.valueOf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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


public class ViewHabitDialog extends AppCompatDialogFragment {
    private TextView habitNameView, habitTitleView, habitReasonView, habitStartDateView, habitPrivacyView, habitDaysView, habitConsistView;
    private String userName, habitName, habitTitle, habitStart, habitReason, days;
    private String habitDays = "";
    private Integer habitConsist = 0, amountEvents = 0;
    private Boolean itemPrivacy;
    private final String[] privacyOptions = new String[]{"Private","Public"};
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference habitEventsRef = db.collection("Habit Events");



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


        builder.setView(view)
                .setTitle("Habit Details")
                .setNegativeButton("Back", (dialogInterface, i) -> {})
                .setPositiveButton("Add Habit Event", ((dialogInterface, i) -> {
                    ManageHabitEventsFragment addHabitDialog = new ManageHabitEventsFragment(
                            userName, habitName);
                    addHabitDialog.show(getFragmentManager(), "ADD NEW HABIT EVENT");
                    dismiss();
                }));

        //get text views
        habitNameView   = view.findViewById(R.id.view_habit_name);
        habitReasonView= view.findViewById(R.id.view_habit_reason);
        habitTitleView  = view.findViewById(R.id.view_habit_title);
        habitStartDateView  = view.findViewById(R.id.view_start_date);
        habitPrivacyView  = view.findViewById(R.id.view_privacy);
        habitDaysView  = view.findViewById(R.id.view_days);
        habitConsistView = view.findViewById(R.id.view_consist);

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
                    System.out.println("Checkpoint get for habit Name: "+habitName+", and user Name: "+userName);
                    String habitNames = (String) doc.getData().get("HabitName");
                    String userNames = (String) doc.getData().get("UserName");
                    String date = (String) doc.getData().get("Date");

                    if (userNames.equals(habitName) && habitNames.equals(userName)){ /**This will need to be updated once events is fixed*/

                        amountEvents++;
                        System.out.println("**!! SUCCESSFUL MATCH !! - habit Name: "+habitNames+",  username: "+userNames+", date: "+date+", # Events: "+amountEvents);
                    }
                }
                Integer nDays = countDays();
                System.out.println("**!! Calculating consistency with - # days: "+nDays+", # Events: "+amountEvents);
                if(nDays > 0 && amountEvents > 0){
                    habitConsist = 100/nDays*amountEvents;}
                else {habitConsist = 0+100*amountEvents;}
                habitConsistView.setText(valueOf(habitConsist)+" %");

            }
        });

    }

    private Integer countDays(){
        Integer amountDays = days.length();
        //multiply the days by amount of weeks since start date+days for partial weeks
        return amountDays;
    }


}