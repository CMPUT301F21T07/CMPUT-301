package com.example.trackhabit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Represents the Dialog of a new habit
 */

public class NewHabitDialog extends AppCompatDialogFragment implements TextWatcher, DatePickerDialog.OnDateSetListener {

    // Initializing UI elements
    private EditText habitNameEditText, habitTitleEditText, habitReasonEditText, habitStartDate;
    private Spinner  habitPrivate;
    private CheckBox monCheck, tueCheck, wedCheck, thuCheck, friCheck, satCheck, sunCheck;
    public Button positiveButton, addDateButton;

    // Declaring/Initializing variables
    private final String[] privacy = new String[]{"Private","Public"};
    private String userName, item, habitName, habitTitle, habitStart, habitReason;
    private String days = "";
    private Boolean itemPrivacy;
    private Boolean flag = true;
    private EditDialogListener  returnListener;
    private Date tempDate;
    private Timestamp startTime;



    /**
     * Function when the dialog starts
     */
    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setEnabled(false);
        }

    }

    /**
     * Creates an instance that shows a dialog of a new habit
     * will be check on creation of instance.
     * @param savedInstanceState This is the instance state from the previous creation of habits activity
     */

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Initialzing a builder for the dialog fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Initialzing a layout inflater and then inflating a view
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_habit, null);

        // Getting username of the user
        userName = getArguments().getString("user_name");

        // Initializing UI elements of the dialog fragment
        habitNameEditText   = view.findViewById(R.id.add_habit_name);
        habitReasonEditText = view.findViewById(R.id.add_habit_reason);
        habitTitleEditText  = view.findViewById(R.id.add_habit_title);
        habitStartDate      = view.findViewById(R.id.add_habit_date);
        habitPrivate        = view.findViewById(R.id.select_privacy);

        monCheck    = view.findViewById(R.id.monday_check);
        tueCheck    = view.findViewById(R.id.tuesday_check);
        wedCheck    = view.findViewById(R.id.wednesday_check);
        thuCheck    = view.findViewById(R.id.thursday_check);
        friCheck    = view.findViewById(R.id.friday_check);
        satCheck    = view.findViewById(R.id.saturday_check);
        sunCheck    = view.findViewById(R.id.sunday_check);

        addDateButton = view.findViewById(R.id.add_date_button);

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                requireActivity(), android.R.layout.simple_spinner_dropdown_item, privacy);
        habitPrivate.setAdapter(spinnerArrayAdapter);


        // Building the dialog fragment
        builder.setView(view)
                .setTitle("Add new habit")
                .setNegativeButton("Cancel", (dialogInterface, i) -> {})
                .setPositiveButton("Ok", ((dialogInterface, i) -> {
                    checkInput();
                    returnListener.addedHabit(habitName, habitTitle, habitReason, startTime, itemPrivacy, days);
                }));


        // Setting up a listener for a button click
        addDateButton.setOnClickListener(new View.OnClickListener(){
                                              @Override
                                              public void onClick(View v) {
                                                  DialogFragment datePicker = new DatePickerFragment();
                                                  datePicker.setTargetFragment(NewHabitDialog.this, 0);
                                                  datePicker.show(getFragmentManager(), "date picker");
                                              }
                                              });

        // Initializing a textWatcher
        habitStartDate.addTextChangedListener(this);

        return builder.create();
    }

    // Function that is called when the fragment is associated with its activity

    /**
     * function when fragment is attached to activity
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            returnListener = (EditDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement listener");
        }
    }

    // Functions of the TextWatecher
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {    }

    /**
     * function on text is changed
     * @param charSequence CharSequence
     * @param i int
     * @param i1 int
     * @param i2 int
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String tempHabitName   = habitNameEditText.getText().toString();
        String tempHabitTitle  = habitTitleEditText.getText().toString();
        String tempHabitReason = habitReasonEditText.getText().toString();
        String tempDate         = habitStartDate.getText().toString();

        // Check if text fields are empty or not
        if (habitStartDate.getText().hashCode() == charSequence.hashCode()){
            Boolean value = (tempHabitName.length() != 0 && tempHabitTitle.length() !=0 &&
                    tempHabitReason.length()!=0 && tempDate.length()!=0);
            if (value) {
                positiveButton.setEnabled(true);
            }
        }
    }

    /**
     * function when after text is changed
     * @param editable Editable
     */
    @Override
    public void afterTextChanged(Editable editable) {
        String tempHabitName   = habitNameEditText.getText().toString();
        String tempHabitTitle  = habitTitleEditText.getText().toString();
        String tempHabitReason = habitReasonEditText.getText().toString();
        String tempDate         = habitStartDate.getText().toString();

        Boolean value = (tempHabitName.length() != 0 && tempHabitTitle.length() !=0 &&
                tempHabitReason.length()!=0 && tempDate.length()!=0);
        if (value)
            positiveButton.setEnabled(true);
    }

    /**
     *  Interface created to send data back to HabitsActivity after entering data into EditTexts
     */
    public interface EditDialogListener{
        void addedHabit(String name, String title, String reason, Timestamp startTime, Boolean itemPrivacy, String days);
    }

    /**
     * function to set date
     * @param datePicker DatePicker
     * @param year int
     * @param month int
     * @param date int
     */

    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
        // 1 is added to the month value as it'd show the array value instead of the month value,
        // thus it was off by 1
        month = month +1;
        String habitStart = date + " " + month + " " + year;
        habitStartDate.setText(habitStart,TextView.BufferType.EDITABLE);
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
        habitStart  = habitStartDate.getText().toString();

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