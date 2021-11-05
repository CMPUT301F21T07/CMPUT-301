package com.example.trackhabit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewEvents extends AppCompatActivity {
    private ListView EventList;
    private TextView dateText;

    ArrayAdapter<String> eventAdapter;
    ArrayList<HabitEvent> events;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference habitsRef = db.collection("Events");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);
        Intent intent=getIntent();
        dateText=findViewById(R.id.eventTitle);
        dateText.setText(intent.getStringExtra("date"));
        EventList=findViewById(R.id.habits_list_view);
        EventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent singleEvent=new Intent(ViewEvents.this,ViewSingleEvent.class);
                singleEvent.putExtra("index",i);
                singleEvent.putExtra("title",events.get(i).getHabitName());
                singleEvent.putExtra("list",events);
                singleEvent.putExtra("Adapter", (Parcelable) eventAdapter);
                startActivity(singleEvent);
            }
        });
    }
}