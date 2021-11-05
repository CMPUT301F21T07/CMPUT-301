package com.example.trackhabit;

import android.graphics.Bitmap;
import android.widget.Switch;

public class HabitEvent {
    private Habits habit;
    private String date;
    private String comment;
    private Bitmap optionalPhoto;
    private boolean locationPermission;

    public HabitEvent(Habits habit, String date) {
        this.habit = habit;
        this.date = date;
    }

    public HabitEvent(Habits habit, String date, String comment, Bitmap photo, boolean locationPermission) {
        this.habit = habit;
        this.date = date;
        this.comment = comment;
        this.optionalPhoto = photo;
        this.locationPermission = locationPermission;
    }

    public Habits getHabit() {
        return habit;
    }

    public void setHabit(Habits habit) {
        this.habit = habit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Bitmap getOptionalPhoto() {
        return optionalPhoto;
    }

    public void setOptionalPhoto(Bitmap photo) {
        this.optionalPhoto = photo;
    }
}
