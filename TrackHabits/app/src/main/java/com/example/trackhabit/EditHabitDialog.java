
package com.example.trackhabit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EditHabitDialog extends AppCompatDialogFragment {
    private TextView habitNameView, habitTitleView, habitReasonView, habitStartDay, habitStartMonth, habitStartYear;
    private String userName, habitName, habitTitle, habitStart, habitReason, habitDay, habitMonth, habitYear, privacy;
    private CheckBox monCheck, tueCheck, wedCheck, thuCheck, friCheck, satCheck, sunCheck;
    private final String[] privacyOptions = new String[]{"Private","Public"};
    private HabitDialogListener returnListener;
    private Timestamp startTime;
    private String days = "";
    private Spinner habitPrivacy;
    private Boolean itemPrivacy;
    private Date tempDate;



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_habit, null);

        //get strings from arguments
        userName = getArguments().getString("user_name");
        habitName = getArguments().getString("habit_name");
        habitTitle = getArguments().getString("habit_title");
        habitStart = getArguments().getString("habit_date");
        habitReason = getArguments().getString("habit_reason");
        days = getArguments().getString("habit_days");

        itemPrivacy = getArguments().getBoolean("habit_privacy");

        //itemPrivacy = true;
        try {
            tempDate=new SimpleDateFormat("MM/dd/yyyy").parse(habitStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startTime = new Timestamp(tempDate);


        builder.setView(view)
                .setTitle("Edit Habit")
                .setNegativeButton("Back", (dialogInterface, i) -> {})
                .setPositiveButton("Confirm", ((dialogInterface, i) -> {
                    checkInput();
                    returnListener.updateHabit(habitName, habitTitle, habitReason, startTime, itemPrivacy, days);
                }));

        //get text views
        habitNameView   = view.findViewById(R.id.edit_habit_name);
        habitReasonView= view.findViewById(R.id.edit_habit_reason);
        habitTitleView = view.findViewById(R.id.edit_habit_title);
        habitStartDay = view.findViewById(R.id.edit_habit_date);
        habitStartMonth = view.findViewById(R.id.edit_habit_month);
        habitStartYear = view.findViewById(R.id.edit_habit_year);
        habitPrivacy = view.findViewById(R.id.edit_privacy);
        monCheck    = view.findViewById(R.id.monday_edit);
        tueCheck    = view.findViewById(R.id.tuesday_edit);
        wedCheck    = view.findViewById(R.id.wednesday_edit);
        thuCheck    = view.findViewById(R.id.thursday_edit);
        friCheck    = view.findViewById(R.id.friday_edit);
        satCheck    = view.findViewById(R.id.saturday_edit);
        sunCheck    = view.findViewById(R.id.sunday_edit);




        //set text views
        habitNameView.setText(habitName);
        habitReasonView.setText(habitReason);
        habitTitleView.setText(habitTitle);
        habitStartDay.setText(getDay(tempDate));
        habitStartMonth.setText(getMonth(tempDate));
        habitStartYear.setText(getYear(tempDate));
        setDays(days);


        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                requireActivity(), android.R.layout.simple_spinner_dropdown_item, privacyOptions);
        habitPrivacy.setAdapter(spinnerArrayAdapter);
        if (!itemPrivacy){
            habitPrivacy.setSelection(1);}



        return builder.create();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            returnListener = (EditHabitDialog.HabitDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement listener");
        }
    }

    private void checkInput() {
        habitTitle = habitTitleView.getText().toString();
        habitReason = habitReasonView.getText().toString();
        privacy = habitPrivacy.getSelectedItem().toString();
        habitDay = habitStartDay.getText().toString();
        habitMonth = habitStartMonth.getText().toString();
        habitYear = habitStartYear.getText().toString();
        days = "";

        if (privacy.equals("Private"))
            itemPrivacy = true;
        else if (privacy.equals("Public"))
            itemPrivacy = false;

        if (monCheck.isChecked())
            days = days + "M";
        if (tueCheck.isChecked())
            days = days + "T";
        if (wedCheck.isChecked())
            days = days + "W";
        if (thuCheck.isChecked())
            days = days + "R";
        if (friCheck.isChecked())
            days = days + "F";
        if (satCheck.isChecked())
            days = days + "S";
        if (sunCheck.isChecked())
            days = days + "U";

        habitStart = habitDay + " " + habitMonth + " " + habitYear;

        try {
            tempDate=new SimpleDateFormat("dd MM yyyy").parse(habitStart);
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Incorrect format for date", Toast.LENGTH_SHORT).show();
            habitStartDay.setError("Incorrect format: DD MM YYYY");
            habitStartDay.requestFocus();
        }
        startTime = new Timestamp(tempDate);
    }

    public interface HabitDialogListener{
        void updateHabit(String name, String title, String reason, Timestamp startTime, Boolean itemPrivacy,String days);
    }

    private String getDay(Date date){
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        String day = String.valueOf(dayFormat.format(date));
        return day;
    }

    private String getMonth(Date date){
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        String month = String.valueOf(monthFormat.format(date));
        return month;
    }

    private String getYear(Date date){
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String year = String.valueOf(yearFormat.format(date));
        return year;
    }

    private void setDays(String setDays){
        if (setDays.contains("M"))
            monCheck.setChecked(true);
        if (setDays.contains("T"))
            tueCheck.setChecked(true);
        if (setDays.contains("W"))
            wedCheck.setChecked(true);
        if (setDays.contains("R"))
            thuCheck.setChecked(true);
        if (setDays.contains("F"))
            friCheck.setChecked(true);
        if (setDays.contains("S"))
            satCheck.setChecked(true);
        if (setDays.contains("U"))
            sunCheck.setChecked(true);
    }

}