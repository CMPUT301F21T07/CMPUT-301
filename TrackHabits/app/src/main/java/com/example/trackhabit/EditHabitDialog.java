
package com.example.trackhabit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;



public class EditHabitDialog extends AppCompatDialogFragment {
    private TextView habitNameView, habitTitleView, habitReasonView, habitStartDateView;
    private String userName, habitName, habitTitle, habitStart, habitReason;
    private Boolean itemPrivacy;



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


        builder.setView(view)
                .setTitle("Edit Habit")
                .setNegativeButton("Back", (dialogInterface, i) -> {})
                .setPositiveButton("Confirm", ((dialogInterface, i) -> {

                }));

        //get text views
        habitNameView   = view.findViewById(R.id.edit_habit_name);
        habitReasonView= view.findViewById(R.id.edit_habit_reason);
        habitTitleView  = view.findViewById(R.id.edit_habit_title);
        habitStartDateView  = view.findViewById(R.id.edit_start_date);

        //set text views
        habitNameView.setText(habitName);
        habitReasonView.setText(habitReason);
        habitTitleView.setText(habitTitle);
        habitStartDateView.setText(habitStart);

        return builder.create();
    }

}