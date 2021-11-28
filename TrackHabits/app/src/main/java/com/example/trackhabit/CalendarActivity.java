package com.example.trackhabit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {
    private static final String TAG="Calendar";
    private CalendarView mCalendarView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mCalendarView=findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){

            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                String day;
                String month;
                i1=i1+1;
                if (i2<10){
                    day="0"+i2;
                }else{
                    day=""+i2;
                }
                if (i1<10){
                    month="0"+i1;
                }else
                {month=""+i1;}
                String date=day+" "+month+" "+i;
                Log.d(TAG,"onSelectedDayChange: date:"+date);
                Intent intent=new Intent(CalendarActivity.this,ViewEvent.class);
                Intent lastIntent=getIntent();
                String userID=lastIntent.getExtras().getString("ID");
                intent.putExtra("ID",userID);

                intent.putExtra("date",date);
                startActivity(intent);
            }
        });
    }
}