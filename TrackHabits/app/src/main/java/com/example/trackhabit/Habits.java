package com.example.trackhabit;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Habits {
    String habitName;
    String habitUser;
    String habitTitle;
    String habitReason;
    Timestamp timestamp;
    Boolean privacy;    // true for private, false for public
    String[] days;

    Habits(String habitName,String habitUser,String habitTitle,
           String habitReason,Timestamp timestamp, Boolean privacy) {
        this.habitName = habitName;
        this.habitUser = habitUser;
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
        this.timestamp = timestamp;
        this.privacy = privacy;
    }

    Habits(String habitName,String habitUser,String habitTitle,
           String habitReason, Boolean privacy) {
        this.habitName = habitName;
        this.habitUser = habitUser;
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date);
        this.timestamp = timestamp;
        this.privacy = privacy;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public String getHabitUser() {
        return habitUser;
    }

    public void setHabitUser(String habitUser) {
        this.habitUser = habitUser;
    }

    public String getHabitTitle() {
        return habitTitle;
    }

    public void setHabitTitle(String habitTitle) {
        this.habitTitle = habitTitle;
    }

    public String getHabitReason() {
        return habitReason;
    }

    public void setHabitReason(String habitReason) {
        this.habitReason = habitReason;
    }

    public Timestamp getStartDate() {
        return timestamp;
    }

    public void setStartDate(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Boolean privacy) {
        this.privacy = privacy;
    }
}
