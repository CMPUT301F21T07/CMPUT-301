package com.example.trackhabit;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.Timestamp;

public class ViewHabitDetails extends AppCompatActivity {
    private Habits viewedHabit;
    private String habitUser;
    private EditText habitName;
    private EditText habitTitle;
    private EditText habitReason;
    private Timestamp timestamp;
    private Boolean privacy;
    private String[] days;

    protected void onCreate(Habits habit) {
        this.viewedHabit = habit;
        habitName = findViewById(R.id.edit_habitName);
        /*
        this.habitName = habitName;
        this.habitUser = habitUser;
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
        this.timestamp = timestamp;
        this.privacy = privacy;*/

    }

    @Override
    public void onBackPressed() { //same as if the user cancels the actions
        finish();
    } //cancel Activity
}
