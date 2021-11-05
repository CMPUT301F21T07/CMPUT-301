package com.example.trackhabit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ManageHabitEventsFragment extends DialogFragment {

    private TextView dateText;
    private EditText commentEditText;
    private Button selectImageButton;
    private Button finishButton;
    private ImageView optionalPhoto;
    private ToggleButton locationPermissionButton;
    private OnFragmentInteractionListener listener;

    private int habitEventPosition = -1;
    private HabitEvent editableHabitEvent;
    private Habits habit;


    public interface OnFragmentInteractionListener {
        void onOkPressed(HabitEvent newHabitEvent, int pos);
    }

    public ManageHabitEventsFragment(Habits habit) {
        this.habit = habit;
    }

    public ManageHabitEventsFragment(int pos, HabitEvent habitEvent) {
        habitEventPosition = pos;
        editableHabitEvent = habitEvent;
        this.habit = habitEvent.getHabit();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_manage_habit_events,
                null);
        dateText = view.findViewById(R.id.date_text);
        commentEditText = view.findViewById(R.id.comment_editText);
        selectImageButton = view.findViewById(R.id.select_image_button);
        finishButton = view.findViewById(R.id.finish_button);
        optionalPhoto = view.findViewById(R.id.optionalPhoto);
        locationPermissionButton = view.findViewById(R.id.location_permission_button);


        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String currentDateStr = dateFormat.format(currentDate);
        dateText.setText(currentDateStr);

        String title = "Add HabitEvent Info";

        if (habitEventPosition > -1) {
            dateText.setText(editableHabitEvent.getDate());
            commentEditText.setText(editableHabitEvent.getComment());
            optionalPhoto.setImageBitmap(editableHabitEvent.getOptionalPhoto());
            title = "Edit HabitEvent Info";
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String comment = commentEditText.getText().toString();
                        String date = dateText.getText().toString();
                        optionalPhoto.setDrawingCacheEnabled(true);
                        Bitmap photo = optionalPhoto.getDrawingCache();
                        boolean isLocationPermitted = locationPermissionButton.isChecked();

                        if (comment.length() > 30) {
                            Toast.makeText(getContext(), "Comment length is exceeded!",
                                    Toast.LENGTH_SHORT).show();
                            commentEditText.setError("Comment length is exceeded!");
                        }
                        HabitEvent newHabitEvent = new HabitEvent(habit, date, comment, photo,
                                isLocationPermitted);
                        listener.onOkPressed(newHabitEvent, habitEventPosition);
                    }
                }).create();
    }
}