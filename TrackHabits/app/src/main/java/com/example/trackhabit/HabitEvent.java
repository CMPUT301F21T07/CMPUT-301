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
    private boolean locationPermission;
    private boolean photoUploaded;
    private String location;

    /**
     * Creates a habit event with the habit name, user name, date, comment, optional photo, and
     * optional geolocation
     *
     * @param habitName Name of the habit
     * @param date The current date
     * @param comment Optional comment with max 20 characters
     * @param photoUploaded True if photo was uploaded to FirebaseStorage
     * @param locationPermission Optional geolocation using Google data
     * @param location String of location that is stored as longitude and latitude
     */
    public HabitEvent(String habitName, String userName, String date, String comment,
                      boolean locationPermission,String location, boolean photoUploaded) {
        this.habitName = habitName;
        this.userName = userName;
        this.date = date;
        this.comment = comment;
        this.locationPermission = locationPermission;
        this.location = location;
        this.photoUploaded = photoUploaded;
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
     *Gets the location permission
     *@return Returns the boolean of location permission
     */
    public boolean getLocationPermission() {
        return locationPermission;
    }

    /**
     * Sets the location permission
     * @param locationPermission true if location is permitted to track
     */
    public void setLocationPermission(boolean locationPermission) {
        this.locationPermission = locationPermission;
    }

    /**
     *Gets the location
     *@return Returns the string of location
     */
    public String getLocation(){return location; }

    /**
     * Sets the location permission
     * @param location String of location in terms longitude and latitude
     */
    public void setLocation(String location){this.location=location;}

    /**
     * Gets the boolean of photo upload
     * @return Returns the boolean of photo upload
     */
    public boolean getPhotoUploaded() {
        return photoUploaded;
    }

    /**
     * Sets the new boolean of photo uploaded
     * @param photoUploaded the new boolean of photoUploaded
     */
    public void setPhotoUploaded(boolean photoUploaded) {
        this.photoUploaded = photoUploaded;
    }
}

