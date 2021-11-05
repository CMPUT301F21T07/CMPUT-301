package com.example.trackhabit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ManageHabitEventsFragment extends DialogFragment {

    private TextView dateText;
    private EditText commentEditText;
    private Button selectImageButton;
    private ImageView optionalPhoto;
    private ToggleButton locationPermissionButton;

    private int habitEventPosition = -1;
    private HabitEvent editableHabitEvent;
    private String habitName;
    private String userName;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference habitEventsRef = db.collection("Habit Events");


    public ManageHabitEventsFragment(String habitName, String userName) {
        this.habitName = habitName;
        this.userName = userName;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_manage_habit_events,
                null);
        dateText = view.findViewById(R.id.date_text);
        commentEditText = view.findViewById(R.id.comment_editText);
        selectImageButton = view.findViewById(R.id.select_image_button);
        optionalPhoto = view.findViewById(R.id.optionalPhoto);
        locationPermissionButton = view.findViewById(R.id.location_permission_button);


        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
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

                        checkInputCorrectness();

                        HabitEvent newHabitEvent = new HabitEvent(habitName, userName, date, comment, photo,
                                isLocationPermitted);
                        HashMap<String, Object> habitEventData = new HashMap<>();
                        habitEventData.put("HabitName", habitName);
                        habitEventData.put("UserName", userName);
                        habitEventData.put("Date", date);
                        habitEventData.put("OptionalComment", comment);
                        habitEventData.put("OptionalPhoto", photo);
                        habitEventData.put("LocationPermission", isLocationPermitted);
                        String dataName = habitName + " " + userName + " " + date;
                        habitEventsRef.document(dataName)
                                .set(habitEventData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TAG", "Habit Event Successfully Added!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "Habit Successfully Added!");
                                    }
                                });
                    }
                }).create();
    }

    public void checkInputCorrectness() {
        String comment = commentEditText.getText().toString();
        boolean isInputCorrect = (comment.length() <= 20);

        if (!isInputCorrect) {
            commentEditText.setError("Comment Length Is Exceeded!");
            checkInputCorrectness();
        }
    }
}