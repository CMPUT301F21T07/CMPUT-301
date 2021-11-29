
package com.example.trackhabit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents an Edit Habit Dialog
 */

public class EditHabitDialog extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {
    private TextView habitNameView, habitTitleView, habitReasonView, habitStartDay, habitStartYear;
    private String userName, oldName, habitName, habitTitle, habitStart, habitReason, habitDay, habitMonth, habitYear, privacy, habitDateString;
    private CheckBox monCheck, tueCheck, wedCheck, thuCheck, friCheck, satCheck, sunCheck;
    private final String[] privacyOptions = new String[]{"Private","Public"};
    private HabitDialogListener returnListener;
    private Timestamp startTime;
    private String days = "";
    private Spinner habitPrivacy;
    private Boolean itemPrivacy;
    private Date tempDate;
    private Button editDateButton;


    /**
     * Creates an instance that shows Edit Habit Dialog
     * will be check on creation of instance.
     * @param savedInstanceState This is the instance state from the previous creation of habits activity
     */

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_habit, null);

        //get strings from arguments
        userName = getArguments().getString("user_name");
        oldName = getArguments().getString("habit_name");
        habitTitle = getArguments().getString("habit_title");
        habitStart = getArguments().getString("habit_date");
        habitReason = getArguments().getString("habit_reason");
        days = getArguments().getString("habit_days");
        habitDay = getArguments().getString("habit_day");
        habitMonth = getArguments().getString("habit_month");
        habitYear = getArguments().getString("habit_year");

        habitDateString = habitDay + " " + habitMonth + " " + habitYear;

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
                    updateInput();
                    returnListener.updateHabit(oldName, habitName, habitTitle, habitReason, startTime, itemPrivacy, days);
                }));




        //set up views
        habitNameView   = view.findViewById(R.id.edit_habit_name);
        habitReasonView= view.findViewById(R.id.edit_habit_reason);
        habitTitleView = view.findViewById(R.id.edit_habit_title);
        habitStartYear = view.findViewById(R.id.edit_habit_year);
        habitPrivacy = view.findViewById(R.id.edit_privacy);
        monCheck    = view.findViewById(R.id.monday_edit);
        tueCheck    = view.findViewById(R.id.tuesday_edit);
        wedCheck    = view.findViewById(R.id.wednesday_edit);
        thuCheck    = view.findViewById(R.id.thursday_edit);
        friCheck    = view.findViewById(R.id.friday_edit);
        satCheck    = view.findViewById(R.id.saturday_edit);
        sunCheck    = view.findViewById(R.id.sunday_edit);
        editDateButton = view.findViewById(R.id.edit_date_button);

        editDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.setTargetFragment(EditHabitDialog.this, 0);
                datePicker.show(getFragmentManager(), "date picker");
            }
        });

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                requireActivity(), android.R.layout.simple_spinner_dropdown_item, privacyOptions);
        habitPrivacy.setAdapter(spinnerArrayAdapter);

        //populate views
        addInput();

        return builder.create();
    }
    /**
     *  Public function that attaches context
     * @param context Class Context
     */

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            returnListener = (EditHabitDialog.HabitDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement listener");
        }
    }

    /**
     *  Public function that sets the date
     * @param datePicker Class DatePicker
     * @param year Int
     * @param month Int
     * @param date int
     */

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
        // 1 is added to the month value as it'd show the array value instead of the month value,
        // thus it was off by 1
        month = month +1;
        String habitStart = date + " " + month + " " + year;
        habitStartYear.setText(habitStart,TextView.BufferType.EDITABLE);
    }

    /**
     *  Interface created to send data back to HabitsActivity after entering data into Text views
     */
    public interface HabitDialogListener{
        void updateHabit(String oldName, String name, String title, String reason, Timestamp startTime, Boolean itemPrivacy,String days);
    }

    /**
     *  Private function that adds data from habit in to text views
     */
    private void addInput(){
        //populate text views
        habitNameView.setText(oldName);
        habitReasonView.setText(habitReason);
        habitTitleView.setText(habitTitle);
        habitStartYear.setText(habitDateString);

        //set spinner to correct privacy setting
        if (!itemPrivacy){
            habitPrivacy.setSelection(1);}

        //mark checked days
        if (days.contains("M"))
            monCheck.setChecked(true);
        if (days.contains("T"))
            tueCheck.setChecked(true);
        if (days.contains("W"))
            wedCheck.setChecked(true);
        if (days.contains("R"))
            thuCheck.setChecked(true);
        if (days.contains("F"))
            friCheck.setChecked(true);
        if (days.contains("S"))
            satCheck.setChecked(true);
        if (days.contains("U"))
            sunCheck.setChecked(true);

    }


    /**
     *  Private function that gets the input from the text views and updates strings
     */
    private void updateInput() {
        //get text from text views
        habitName = habitNameView.getText().toString();
        habitTitle = habitTitleView.getText().toString();
        habitReason = habitReasonView.getText().toString();
        privacy = habitPrivacy.getSelectedItem().toString();
        habitYear = habitStartYear.getText().toString();

        //get chosen privacy
        if (privacy.equals("Private"))
            itemPrivacy = true;
        else if (privacy.equals("Public"))
            itemPrivacy = false;

        //get checked days
        days = "";
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



        try {
            tempDate=new SimpleDateFormat("dd MM yyyy").parse(habitYear);
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Incorrect format for date", Toast.LENGTH_SHORT).show();
            habitStartDay.setError("Incorrect format: DD MM YYYY");
            habitStartDay.requestFocus();
        }
        startTime = new Timestamp(tempDate);
    }


}