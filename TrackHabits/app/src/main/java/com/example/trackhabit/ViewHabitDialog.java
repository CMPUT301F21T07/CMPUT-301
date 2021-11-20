package com.example.trackhabit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;



public class ViewHabitDialog extends AppCompatDialogFragment {
    private TextView habitNameView, habitTitleView, habitReasonView, habitStartDateView, habitPrivacyView, habitDaysView;
    private String userName, habitName, habitTitle, habitStart, habitReason, days;
    private String habitDays = "";
    private Boolean itemPrivacy; //if privacy setting will be included in the details
    private final String[] privacyOptions = new String[]{"Private","Public"};



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

        return builder.create();
    }

    private void setDays(){
        if (days.contains("M"))
            habitDays = habitDays + " Mon";
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

}