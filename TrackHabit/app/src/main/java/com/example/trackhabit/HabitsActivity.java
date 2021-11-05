package com.example.trackhabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HabitsActivity extends AppCompatActivity {

    CharSequence text;

    List<String> todayHabitsDataList;
    List<String> allHabitsDataList;
    Boolean switchState;
    Switch yhSwitch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        yhSwitch = findViewById(R.id.YHSwitch);
        switchState = yhSwitch.isChecked();

        yhSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    yhSwitch.setText("Your Habits Today");

                }
                else{
                    yhSwitch.setText("All Habits");

                }
            }
        });
//        final FloatingActionButton addCityButton=findViewById(R.id.add_habit_button);
//        addCityButton.setOnClickListener((v)->{
//            new AddHabitFragment().show(getSupportFragmentManager(), "ADD_MEDICINE");
//        });



    }


}