package com.example.trackhabit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
/**
 * @param calendarView a view of calendar
 * @param  i year
 * @param i1 month
 * @param i2 day
 * Get the date according to user selection and view a list of events on that day
 * */
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
                //start the ViewEvent activity
                Intent intent=new Intent(CalendarActivity.this,ViewEvent.class);
                Intent lastIntent=getIntent();
                String userID=lastIntent.getExtras().getString("ID");
                intent.putExtra("ID",userID);

                intent.putExtra("date",date);
                startActivity(intent);
            }
        });
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}