package com.example.trackhabit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewHabitDialog extends AppCompatDialogFragment implements TextWatcher {
    private EditText habitNameEditText, habitTitleEditText, habitReasonEditText, habitStartDate, habitStartMonth, habitStartYear;
    private Spinner  habitPrivate;
    private final String[] privacy = new String[]{"Private","Public"};
    private String userName, item, habitName, habitTitle, habitStart, habitReason, day, month, year;
    private String days = "";
    private Boolean itemPrivacy;
    private Boolean flag = true;
    private EditDialogListener  returnListener;
    private Date tempDate;
    private Timestamp startTime;
    private CheckBox monCheck, tueCheck, wedCheck, thuCheck, friCheck, satCheck, sunCheck;
    Boolean dayInput = false, monthInput = false;

    public Button positiveButton;

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setEnabled(false);
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_habit, null);

        userName = getArguments().getString("user_name");


        builder.setView(view)
                .setTitle("Add new habit")
                .setNegativeButton("Cancel", (dialogInterface, i) -> {})
                .setPositiveButton("Ok", ((dialogInterface, i) -> {
                    checkInput();
                    returnListener.addedHabit(habitName, habitTitle, habitReason, startTime, itemPrivacy, days);
                }));

        habitNameEditText   = view.findViewById(R.id.add_habit_name);
        habitReasonEditText = view.findViewById(R.id.add_habit_reason);
        habitTitleEditText  = view.findViewById(R.id.add_habit_title);
        habitStartDate      = view.findViewById(R.id.add_habit_date);
        habitStartMonth     = view.findViewById(R.id.add_habit_month);
        habitStartYear      = view.findViewById(R.id.add_habit_year);
        habitPrivate        = view.findViewById(R.id.select_privacy);
        monCheck    = view.findViewById(R.id.monday_check);
        tueCheck    = view.findViewById(R.id.tuesday_check);
        wedCheck    = view.findViewById(R.id.wednesday_check);
        thuCheck    = view.findViewById(R.id.thursday_check);
        friCheck    = view.findViewById(R.id.friday_check);
        satCheck    = view.findViewById(R.id.saturday_check);
        sunCheck    = view.findViewById(R.id.sunday_check);

        habitStartYear.addTextChangedListener(this);
        habitStartMonth.addTextChangedListener(this);
        habitStartDate.addTextChangedListener(this);




        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                requireActivity(), android.R.layout.simple_spinner_dropdown_item, privacy);
        habitPrivate.setAdapter(spinnerArrayAdapter);

        return builder.create();
    }

    // Function that is called when the fragment is associated with its activity
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            returnListener = (EditDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement listener");
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String tempHabitName   = habitNameEditText.getText().toString();
        String tempHabitTitle  = habitTitleEditText.getText().toString();
        String tempHabitReason = habitReasonEditText.getText().toString();
        String tempDay         = habitStartDate.getText().toString();
        String tempMonth       = habitStartMonth.getText().toString();
        String tempYear        = habitStartYear.getText().toString();

        if (habitStartYear.getText().hashCode() == charSequence.hashCode()){
            Boolean value = (tempHabitName.length() != 0 && tempHabitTitle.length() !=0 &&
                    tempHabitReason.length()!=0 && tempDay.length()!=0 && tempMonth.length()!=0 &&
                    tempYear.length()!=0 && dayInput && monthInput);
            if (value) {
                positiveButton.setEnabled(true);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String tempHabitName   = habitNameEditText.getText().toString();
        String tempHabitTitle  = habitTitleEditText.getText().toString();
        String tempHabitReason = habitReasonEditText.getText().toString();
        String tempDay         = habitStartDate.getText().toString();
        String tempMonth       = habitStartMonth.getText().toString();
        String tempYear        = habitStartYear.getText().toString();

        Integer month;
        if (tempMonth.equals("")) {
            month = 0;
        } else
            month = Integer.parseInt(tempMonth);
        Integer day   = Integer.parseInt(tempDay);
        List<Integer> monthsWithThirtyOne = new ArrayList<>();
        monthsWithThirtyOne.add(1);
        monthsWithThirtyOne.add(3);
        monthsWithThirtyOne.add(5);
        monthsWithThirtyOne.add(7);
        monthsWithThirtyOne.add(8);
        monthsWithThirtyOne.add(10);
        monthsWithThirtyOne.add(12);

        List<Integer> monthsWithThirty = new ArrayList<>();
        monthsWithThirty.add(4);
        monthsWithThirty.add(6);
        monthsWithThirty.add(9);
        monthsWithThirty.add(11);


        if (habitStartDate.getText().hashCode() == editable.hashCode() && month != 0){
            month = Integer.parseInt(tempMonth);
            if (monthsWithThirtyOne.contains(month)) {
                if (day <= 31)
                {
                    dayInput = true;
                    monthInput = true;
                }
                else{
                    dayInput = false;
                    Toast.makeText(getContext(), "Re-enter day", Toast.LENGTH_SHORT).show();
                }
            } else if (month == 2) {
                if (day <= 28)
                {
                    dayInput = true;
                    monthInput = true;
                }
                else{
                    dayInput = false;
                    Toast.makeText(getContext(), "Re-enter day", Toast.LENGTH_SHORT).show();
                }
            } else if (monthsWithThirty.contains(month)) {
                if (day <= 30) {
                    dayInput = true;
                    monthInput = true;
                }
                else{
                    dayInput = false;
                    Toast.makeText(getContext(), "Re-enter day", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Re-enter month", Toast.LENGTH_SHORT).show();
                monthInput = false;
            }
        }
        if (habitStartMonth.getText().hashCode() == editable.hashCode() && month != 0) {
            month = Integer.parseInt(tempMonth);
            if (monthsWithThirtyOne.contains(month)) {
                if (day <= 31)
                {
                    dayInput = true;
                    monthInput = true;
                }
                else{
                    dayInput = false;
                    Toast.makeText(getContext(), "Re-enter day", Toast.LENGTH_SHORT).show();
                }
            } else if (month == 2) {
                if (day <= 28)
                {
                    dayInput = true;
                    monthInput = true;
                }
                else{
                    dayInput = false;
                    Toast.makeText(getContext(), "Re-enter day", Toast.LENGTH_SHORT).show();
                }
            } else if (monthsWithThirty.contains(month)) {
                if (day <= 30) {
                    dayInput = true;
                    monthInput = true;
                }
                else{
                    dayInput = false;
                    Toast.makeText(getContext(), "Re-enter day", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Re-enter month", Toast.LENGTH_SHORT).show();
                monthInput = false;
            }
        }
        if (habitStartYear.getText().hashCode() == editable.hashCode()) {
            Boolean value = (tempHabitName.length() != 0 && tempHabitTitle.length() !=0 &&
                    tempHabitReason.length()!=0 && tempDay.length()!=0 && tempMonth.length()!=0 &&
                    tempYear.length()!=0 && dayInput && monthInput);
            if (value) {
                positiveButton.setEnabled(true);
            }

        }


    }

    /**
     *  Interface created to send data back to HabitsActivity after entering data into EditTexts
     */
    public interface EditDialogListener{
        void addedHabit(String name, String title, String reason, Timestamp startTime, Boolean itemPrivacy, String days);
    }

    /**
     *  Function that checks for correct user input in EditText fields and refocuses on text fields
     *  if input is wrong
     */
    private void checkInput(){
        flag        = true;
        item        = habitPrivate.getSelectedItem().toString();
        habitName   = habitNameEditText.getText().toString();
        habitTitle  = habitTitleEditText.getText().toString();
        habitReason = habitReasonEditText.getText().toString();
        day         = habitStartDate.getText().toString();
        month       = habitStartMonth.getText().toString();
        year        = habitStartYear.getText().toString();

        habitStart = day + " " + month + " " + year;

        try {
            tempDate=new SimpleDateFormat("dd MM yyyy").parse(habitStart);
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Incorrect format for date", Toast.LENGTH_SHORT).show();
            habitStartDate.setError("Incorrect format: DD MM YYYY");
            habitStartDate.requestFocus();
            flag = false;
        }
        startTime = new Timestamp(tempDate);


        if (item.equals("Private"))
            itemPrivacy = true;
        else if (item.equals("Public"))
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

        if (!flag) {
            flag = true;
            checkInput();
        }

    }
}