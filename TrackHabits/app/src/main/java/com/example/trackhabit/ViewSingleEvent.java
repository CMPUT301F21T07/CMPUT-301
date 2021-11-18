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
    //    private TextView consistency;
    private Button Editing;
    private Button Deleting;


    private String habitTitle;
    private String habitReason;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference habitsRef = db.collection("Habits");



    String habitName;
    String userName;
    String date;
    String comment ;
    int index;
    boolean toDelete=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_event);

        Intent mIntent=getIntent();
        habitName = mIntent.getExtras().getString("habitName");
        userName = mIntent.getExtras().getString("userName");
        date = mIntent.getExtras().getString("date");
        comment = mIntent.getExtras().getString("comment");
        index=mIntent.getExtras().getInt("index");

        System.out.println("Date: "+date);
        Title=findViewById(R.id.showTitle);
        Title.setText("Habit: "+habitName);


        Reason=findViewById(R.id.showReason);
        Reason.setText("Comment: " + comment);

        StartDate=findViewById(R.id.Start_date);
        StartDate.setText("Date: "+date);

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



    }
    private void removeEvent(){
        habitsRef.addSnapshotListener((value, error) -> {
            assert value != null;
            for (QueryDocumentSnapshot doc : value) {
                Log.d(TAG, String.valueOf(doc.getData().get("HabitName")));
                String userID = (String) doc.getData().get("UserName");

                if (userID.equals(userName) && doc.getData().get("HabitName").equals(habitName) && doc.getData().get("Date").equals(date)) {
                    doc.getReference().delete().addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Data has been deleted successfully!");

                        Toast.makeText(ViewSingleEvent.this, "Habit (and habit events) deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ViewSingleEvent.this, ViewEvents.class);
                        toDelete = true;
                        intent.putExtra("toDelete", toDelete);
                        finish();
                    }).addOnFailureListener(e -> Log.d(TAG, "Data could not be deleted!" + e.toString()));
                }

            }

                        });
            }


}