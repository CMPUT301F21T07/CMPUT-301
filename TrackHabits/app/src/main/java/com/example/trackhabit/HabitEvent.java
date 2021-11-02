package com.example.trackhabit;

import android.graphics.Bitmap;

public class HabitEvent {
    private Habit habit;
    private String date;
    private String comment;
    private Bitmap optionalPhoto;

    public HabitEvent(Habit habit, String date) {
        this.habit = habit;
        this.date = date;
    }

    public HabitEvent(Habit habit, String date, String comment, Bitmap photo) {
        this.habit = habit;
        this.date = date;
        this.comment = comment;
        this.optionalPhoto = photo;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
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
