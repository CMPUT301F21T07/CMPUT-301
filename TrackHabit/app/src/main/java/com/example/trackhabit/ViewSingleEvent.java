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
    int index=mIntent.getIntExtra("index",0);
    String getTitle=mIntent.getStringExtra("title");
    ArrayList<HabitEvent> events= (ArrayList<HabitEvent>) mIntent.getSerializableExtra("list");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_event);
        Title=findViewById(R.id.showTitle);
        Title.setText(getTitle);

        Reason=findViewById(R.id.showReason);
        Reason.setText(events.get(index).getComment());

        StartDate=findViewById(R.id.Start_date);
        StartDate.setText(events.get(index).getDate());
        consistency=findViewById(R.id.showConsistency);
        Editing=findViewById(R.id.Edit);
        Deleting=findViewById(R.id.Delete);
        Title.setText(getTitle);

//        Editing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
          Deleting.setOnClickListener(view -> removeEvent(events.get(index)));

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
    private void removeEvent(HabitEvent event){
        habitsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: value)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get(KEY_TITLE)));
                    String userID = (String) doc.getData().get("User");

                    if (userID.equals(events.get(index).getUserName()) && doc.getData().get(KEY_TITLE).equals(event.getHabitName())){
                        doc.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Data has been deleted successfully!");
                                events.remove(event);
                                Toast.makeText(ViewSingleEvent.this, "Habit (and habit events) deleted", Toast.LENGTH_SHORT).show();
//                                eventArrayAdapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Data could not be deleted!" + e.toString());
                            }
                        });
                    }
                }

            }
        });

    }
}