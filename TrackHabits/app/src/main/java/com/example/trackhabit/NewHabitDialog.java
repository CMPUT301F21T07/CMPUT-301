package com.example.trackhabit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.api.Context;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewHabitDialog extends AppCompatDialogFragment {
    private EditText habitNameEditText, habitTitleEditText, habitReasonEditText, habitStartDate;
    private Spinner  habitPrivate;
    private final String[] privacy = new String[]{"SELECT PRIVACY","Private","Public"};
    private String userName;
    private Boolean itemPrivacy;
    private Boolean flag = true;
    private EditDialogListener listener;
    private Date tempDate;
    private Habits returnHabit;

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
                    String item        = habitPrivate.getSelectedItem().toString();
                    String habitName   = habitNameEditText.getText().toString();
                    String habitTitle  = habitTitleEditText.getText().toString();
                    String habitReason = habitReasonEditText.getText().toString();
                    String habitStart  = habitStartDate.getText().toString();
                    Timestamp startTime;

                    try {
                        tempDate=new SimpleDateFormat("dd MM yyyy").parse(habitStart);
                    } catch (ParseException e) {
                        Toast.makeText(getContext(), "Incorrect format for date", Toast.LENGTH_SHORT).show();
                    }
                    startTime = new Timestamp(tempDate);
                    if (habitName.length() == 0) {
                        Toast.makeText(getContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }
                    if (habitTitle.length() > 20) {
                        Toast.makeText(getContext(), "Title is too long!", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }
                    else if (habitTitle.length() == 0) {
                        Toast.makeText(getContext(), "Enter title!", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }
                    if (habitReason.length() > 30) {
                        Toast.makeText(getContext(), "Reason is too long", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }
                    else if (habitReason.length() == 0) {
                        Toast.makeText(getContext(), "Enter reason", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }

                    if (item.equals("Private"))
                        itemPrivacy = true;
                    else if (item.equals("Public"))
                        itemPrivacy = false;
                    else {
                        Toast.makeText(getContext(), "Select privacy", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }

                    returnHabit = new Habits(habitName, userName, habitTitle, habitReason, startTime, itemPrivacy);
                    listener.addedHabit(returnHabit);
                }));

        habitNameEditText   = view.findViewById(R.id.add_habit_name);
        habitReasonEditText = view.findViewById(R.id.add_habit_reason);
        habitTitleEditText  = view.findViewById(R.id.add_habit_title);
        habitStartDate      = view.findViewById(R.id.add_start_date);
        habitPrivate        = view.findViewById(R.id.select_privacy);

        @SuppressLint({"NewApi", "LocalSuppress"})
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                requireActivity(), android.R.layout.simple_spinner_dropdown_item, privacy);
        habitPrivate.setAdapter(spinnerArrayAdapter);

        return builder.create();
    }

    public interface EditDialogListener{
        void addedHabit(Habits h);
    }

}
