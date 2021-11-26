package com.example.trackhabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewSingleEvent extends AppCompatActivity implements ManageHabitEventsFragment.EditEventListener{
    private static final String TAG = "TAG" ;

    private static final String KEY_TITLE   = "Title";
    private static final String KEY_DATE    = "Start Date";
    private static final String KEY_REASON  = "Reason";
    private static final String KEY_DAYS    = "Days";

    private Habits habit;
    private TextView Title;
    private TextView habitNameText;
    private TextView Reason;
    private TextView StartDate;
    private TextView locationPermissionText;
    private TextView userNameText;
    private ImageView imageView;
    private Bitmap photo;
    private TextView showLocation;
    //    private TextView consistency;
    private Button Editing;
    private Button Deleting;

    private String location;
    private String habitTitle;
    private String habitReason;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference habitsRef = db.collection("Habit Events");



    private String habitName;
    private String userName;
    private String date;
    private String comment;
    private Boolean locationPermission;
    int index;
    boolean toDelete=false;

    @Override
    public void onOkPressed() {
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_event);

        Intent mIntent=getIntent();
        habitName = mIntent.getExtras().getString("habitName");
        System.out.println(habitName);
        userName = mIntent.getExtras().getString("userName");
        date = mIntent.getExtras().getString("date");
        comment = mIntent.getExtras().getString("comment");
        locationPermission = mIntent.getExtras().getBoolean("Permission");
        photo = (Bitmap) mIntent.getParcelableExtra("photo");
        index = mIntent.getExtras().getInt("index");
        location=mIntent.getExtras().getString("location");
        System.out.println("Date: "+date);
        Title=findViewById(R.id.showTitle);
        Title.setText("Habit: " + habitName);

        Reason=findViewById(R.id.showReason);
        Reason.setText("Comment: " + comment);

        locationPermissionText = findViewById(R.id.location_permission);
        locationPermissionText.setText("Location Permission: " + locationPermission.toString());

        userNameText = findViewById(R.id.username);
        userNameText.setText("User Name: " + userName);

        imageView = findViewById(R.id.imageView);

        StartDate=findViewById(R.id.Start_date);
        StartDate.setText("Date: " + date);

        showLocation=findViewById(R.id.show_location);
        showLocation.setText(location);



        Editing=findViewById(R.id.Edit);
        Deleting=findViewById(R.id.Delete);

        Editing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManageHabitEventsFragment editHabitDialog = new ManageHabitEventsFragment(
                        habitName, userName, "Edit", comment, photo,
                        locationPermission, date,location);
                editHabitDialog.show(getSupportFragmentManager(), "EDIT NEW HABIT EVENT");

            }
        });

        Deleting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> data=new HashMap<>();
                habitsRef
                        .document(habitName + " " + userName + " " + date)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG,"Data has been deleted successfully!");
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Cannot delete data"+e.toString());
                        finish();
                    }

                });
            }
        });



    }



}