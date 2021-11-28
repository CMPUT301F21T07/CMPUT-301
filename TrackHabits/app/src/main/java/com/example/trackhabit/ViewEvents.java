package com.example.trackhabit;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.EventLog;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

public class ViewEvents extends AppCompatActivity {
    private ListView EventList;
    private TextView dateText;



    ArrayAdapter<HabitEvent> eventAdapter;
    ArrayList<HabitEvent> events;
    private String userId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference habitEventsRef = db.collection("Habit Events");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        EventList=findViewById(R.id.habits_list_view);
        Intent intent=getIntent();
        String selectedDate=getIntent().getExtras().getString("date");
        userId = getIntent().getExtras().getString("ID");
        events=new EventList(new ArrayList<>());
        eventAdapter= new EventListAdapter(ViewEvents.this, events);
        EventList.setAdapter(eventAdapter);
        habitEventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                events.clear();

                for(QueryDocumentSnapshot doc: value)
                {
                    Log.d("TAG", String.valueOf(doc.getData().get("HabitName")));
                    System.out.println("Checkpoint");
                    String habitName = (String) doc.getData().get("HabitName");
                    String userName = (String) doc.getData().get("UserName");
                    String date = (String) doc.getData().get("Date");
                    System.out.println(date);
                    System.out.println(selectedDate);
                    if (!selectedDate.equals(date) || !userName.equals(userId)){
                        continue;
                    }
                    String optionalComment = (String) doc.getData().get("OptionalComment");
                    String location=(String) doc.getData().get("Location");
                    boolean locationPermission = (boolean) doc.getData().get("LocationPermission");
                    boolean photoUploaded = (boolean) doc.getData().get("PhotoUploaded");

                    if (userId.equals(userName)){
                        HabitEvent newHabitEvent= new HabitEvent(habitName, userName, date,
                                optionalComment, locationPermission,location, photoUploaded);
                        events.add(newHabitEvent);

                    }
                }
                eventAdapter.notifyDataSetChanged();
            }
        });

        dateText=findViewById(R.id.eventTitle);
        dateText.setText(intent.getStringExtra("date"));
        EventList=findViewById(R.id.habits_list_view);
        EventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HabitEvent habitEvent = events.get(i);
                Intent singleEvent  = new Intent(ViewEvents.this,ViewSingleEvent.class);
                singleEvent.putExtra("habitName", habitEvent.getHabitName());
                singleEvent.putExtra("userName", habitEvent.getUserName());
                singleEvent.putExtra("date",habitEvent.getDate());
                singleEvent.putExtra("comment", habitEvent.getComment());
                singleEvent.putExtra("index",i);
                singleEvent.putExtra("Permission",habitEvent.getLocationPermission());
                singleEvent.putExtra("photoUploaded", habitEvent.getPhotoUploaded());
                singleEvent.putExtra("location",habitEvent.getLocation());
                startActivityForResult(singleEvent,0);
                Intent receive=getIntent();
                boolean toDelete=receive.getExtras().getBoolean("toDelete");
                if (toDelete) {
                    events.remove(i);
                    eventAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
