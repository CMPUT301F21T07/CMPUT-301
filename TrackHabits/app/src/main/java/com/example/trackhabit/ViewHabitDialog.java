package com.example.trackhabit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ViewHabitDialog extends AppCompatDialogFragment {
    private TextView habitNameView, habitTitleView, habitReasonView, habitStartDateView;
    private String userName, habitName, habitTitle, habitStart, habitReason;
    private String days = "";
    private Boolean itemPrivacy;
    private Boolean flag = true;
    private OnFragmentInteractionListener listener;
    private Date tempDate;
    private Timestamp startTime;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_habit, null);


        userName = getArguments().getString("user_name");
        /*
        habitName = getArguments().getString("habit_name");
        habitTitle = getArguments().getString("habit_title");
        habitStart = getArguments().getString("habit_date");
        habitReason = getArguments().getString("habit_reason"); */ //ready for when set up

        /**temporary autoset*/
        habitName = "Temporary Name";
        habitTitle = "Temporary Title";
        habitStart = "Temporary Start";
        habitReason = "Temporary Reason";



        builder.setView(view)
                .setTitle("Add new habit")
                .setNegativeButton("Cancel", (dialogInterface, i) -> {})
                .setPositiveButton("Ok", ((dialogInterface, i) -> {
                    listener.onOkPressed();

                }));

        habitNameView   = view.findViewById(R.id.view_habit_name);
        habitReasonView= view.findViewById(R.id.view_habit_reason);
        habitTitleView  = view.findViewById(R.id.view_habit_title);
        habitStartDateView  = view.findViewById(R.id.view_start_date);

        habitNameView.setText(habitName);
        habitReasonView.setText(habitReason);
        habitTitleView.setText(habitTitle);
        habitStartDateView.setText(habitStart);


        return builder.create();
    }
    public interface OnFragmentInteractionListener {
        void onOkPressed();
    }

    // Function that is called when the fragment is associated with its activity
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement listener");
        }
    }


}





