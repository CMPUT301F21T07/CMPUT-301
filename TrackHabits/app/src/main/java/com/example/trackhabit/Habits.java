package com.example.trackhabit;

import com.google.type.DateTime;

public class Habits {
    String habitName;
    String habitUser;
    String habitTitle;
    String habitReason;
    String startDate;
    Boolean privacy;    // true for private, false for public

    Habits(String habitName,String habitUser,String habitTitle,
           String habitReason,String startDate, Boolean privacy) {
        this.habitName = habitName;
        this.habitUser = habitUser;
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
        this.startDate = startDate;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Boolean privacy) {
        this.privacy = privacy;
    }
}
