package com.example.trackhabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class ViewSingleEvent extends AppCompatActivity {
    private static final String TAG = "TAG" ;

    private static final String KEY_TITLE   = "Title";
    private static final String KEY_DATE    = "Start Date";
    private static final String KEY_REASON  = "Reason";
    private static final String KEY_DAYS    = "Days";

    private Habits habit;
    private TextView Title;
    private TextView Reason;
    private TextView StartDate;
    private TextView consistency;
    private Button Editing;
    private Button Deleting;


    private String habitTitle;
    private String habitReason;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference habitsRef = db.collection("Habits");

    Intent mIntent=getIntent();
    String habitName = mIntent.getExtras().getString("habitName");
    String userName = mIntent.getExtras().getString("userName");
    String date = mIntent.getExtras().getString("date");
    String comment = mIntent.getExtras().getString("comment");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_event);
        Title=findViewById(R.id.showTitle);
        Title.setText(habitName);

        Reason=findViewById(R.id.showReason);
        Reason.setText(comment);

        StartDate=findViewById(R.id.Start_date);
        StartDate.setText(date);
        Editing=findViewById(R.id.Edit);
        Deleting=findViewById(R.id.Delete);

//        Editing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        Deleting.setOnClickListener(view -> removeEvent());

//        Deleting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                HashMap<String, String> data=new HashMap<>();
//                habitsRef
//                        .document(events.get(index).getHabit().getHabitTitle())
//                        .delete()
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Log.d(TAG,"Data has been deleted successfully!");
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG,"Cannot delete data"+e.toString());
//                    }
//                });
//            }
//        }
//        );

        habitTitle=habit.getHabitTitle();
        habitReason=habit.getHabitReason();
    }
    private void removeEvent(){
        String documentName = habitName + " " + userName + " " + date;
        habitsRef.document(documentName).delete()
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TAG", "Habit Event Successfully Deleted!");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Habit Event Successfully Deleted!");
            }
        });

    }
}