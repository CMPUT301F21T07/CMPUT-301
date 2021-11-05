package com.example.trackhabit;

import android.graphics.Bitmap;

/**
 * Represents the habit events
 */
public class HabitEvent {
    private String habitName;
    private String userName;
    private String date;
    private String comment;
    private Bitmap optionalPhoto;
    private boolean locationPermission;


    /**
     * Creates a habit event with the habit name, user name, date, comment, optional photo, and
     * optional geolocation
     *
     * @param habit Name of the habit
     * @param date The current date
     * @param comment Optional comment with max 30 characters
     * @param photo Optional bitmap photo
     * @param locationPermission Optional geolocation using Google data
     */
    public HabitEvent(Habits habit, String date, String comment, Bitmap photo, boolean locationPermission) {
        this.habitName = habit.getHabitName();
        this.userName = habit.getHabitUser();

    public HabitEvent(String habitName, String userName, String date, String comment, Bitmap photo,
                      boolean locationPermission) {
        this.habitName = habitName;
        this.userName = userName;

        this.date = date;
        this.comment = comment;
        this.optionalPhoto = photo;
        this.locationPermission = locationPermission;
    }

    /**
     * Get the habit name
     * @return Returns habit name
     */
    public String getHabitName() {
        return habitName;
    }

    /**
     * Set the habit name
     * @param habitName The habit name
     */
    public void setHabitName(String habitName) {
        this.habitName = habitName;

    }

    /**
     * Get the user name
     * @return Returns user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name
     * @param userName The user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the date
     * @return Returns the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date
     * @param date The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the comments
     * @return Returns the comments
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comments
     * @param comment The comments
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the optional photo
     * @return Returns the bitmap of an optional photo
     */
    public Bitmap getOptionalPhoto() {
        return optionalPhoto;
    }

    /**
     * Sets the optional photo
     * @param photo The bitmap photo
     */
    public void setOptionalPhoto(Bitmap photo) {
        this.optionalPhoto = photo;
    }

}


    public boolean getLocationPermission() {
        return locationPermission;
    }

    public void setLocationPermission(boolean locationPermission) {
        this.locationPermission = locationPermission;
    }
}

