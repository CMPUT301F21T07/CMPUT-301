package com.example.trackhabit;

import android.graphics.Bitmap;

public class HabitEvent {
    private String habitName;
    private String userName;
    private String date;
    private String comment;
    private Bitmap optionalPhoto;
    private boolean locationPermission;

    public HabitEvent(String habitName, String userName, String date, String comment, Bitmap photo,
                      boolean locationPermission) {
        this.habitName = habitName;
        this.userName = userName;
        this.date = date;
        this.comment = comment;
        this.optionalPhoto = photo;
        this.locationPermission = locationPermission;
    }


    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public boolean getLocationPermission() {
        return locationPermission;
    }

    public void setLocationPermission(boolean locationPermission) {
        this.locationPermission = locationPermission;
    }
}