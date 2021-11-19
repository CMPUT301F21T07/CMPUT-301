
package com.example.trackhabit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
    private String userName, habitName, habitTitle, habitStart, habitReason, habitDay, habitMonth, habitYear;
    private HabitDialogListener returnListener;
    private Timestamp startTime;
    private String days = "";
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
        itemPrivacy = true;
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
                    returnListener.updateHabit(habitName, habitTitle, habitReason, startTime);
                }));

        //get text views
        habitNameView   = view.findViewById(R.id.edit_habit_name);
        habitReasonView= view.findViewById(R.id.edit_habit_reason);
        habitTitleView  = view.findViewById(R.id.edit_habit_title);
        habitStartDay      = view.findViewById(R.id.edit_habit_date);
        habitStartMonth     = view.findViewById(R.id.edit_habit_month);
        habitStartYear      = view.findViewById(R.id.edit_habit_year);



        //set text views
        habitNameView.setText(habitName);
        habitReasonView.setText(habitReason);
        habitTitleView.setText(habitTitle);
        habitStartDay.setText(getDay(tempDate));
        habitStartMonth.setText(getMonth(tempDate));
        habitStartYear.setText(getYear(tempDate));

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
        habitDay = habitStartDay.getText().toString();
        habitMonth = habitStartMonth.getText().toString();
        habitYear = habitStartYear.getText().toString();

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
        void updateHabit(String name, String title, String reason, Timestamp startTime);
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

}