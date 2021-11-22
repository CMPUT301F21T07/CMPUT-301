package com.example.trackhabit;

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Represents a habit entered by a user
 */
public class Habits {
    String habitName;
    String habitUser;
    String habitTitle;
    String habitReason;
    Timestamp timestamp;
    Boolean privacy;    // true for private, false for public
    String days;


    /**
     * Creates a habit with Name, Title, Reason, Start Date, Days of the week to repeat habit,
     * privacy boolean, and saves the username of the user as a foreign id
     *
     * @param habitName Name of the Habit
     * @param habitUser Username of the user entering the habit
     * @param habitTitle Title of the habit
     * @param habitReason Reason for the habit
     * @param timestamp  Start Date for the habit
     * @param privacy  Privacy setting -> TRUE for Private, FALSE for Public
     * @param days  Days of the week on which to repeat -> M - Monday, T - Tuesday, W - Wednesday,
     *              R - Thursday, F - Friday, S - Saturday, U - Sunday
     */
    Habits(String habitName,String habitUser,String habitTitle,
           String habitReason,Timestamp timestamp, Boolean privacy, String days) {
        this.habitName = habitName;
        this.habitUser = habitUser;
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
        this.timestamp = timestamp;
        this.privacy = privacy;
        this.days    = days;
    }

    /**
     * @return Name of the Habit
     */
    public String getHabitName() {
        return habitName;
    }

    /**
     * @param habitName Name of the Habit
     */
    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    /**
     * @return Username of the Habit
     */
    public String getHabitUser() {
        return habitUser;
    }

    /**
     * @param habitUser Username of the Habit
     */
    public void setHabitUser(String habitUser) {
        this.habitUser = habitUser;
    }

    /**
     * @return Title of the Habit
     */
    public String getHabitTitle() {
        return habitTitle;
    }

    /**
     * @param habitTitle Title of the Habit
     */
    public void setHabitTitle(String habitTitle) {
        this.habitTitle = habitTitle;
    }

    /**
     * @return Reason of the Habit
     */
    public String getHabitReason() {
        return habitReason;
    }

    /**
     * @param habitReason Reason of the Habit
     */
    public void setHabitReason(String habitReason) {
        this.habitReason = habitReason;
    }

    /**
     * @return Start date of the Habit
     */
    public Timestamp getStartDate() {
        return timestamp;
    }

    /**
     * @param timestamp Start date of the Habit
     */
    public void setStartDate(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return Privacy status of the Habit
     */
    public Boolean getPrivacy() {
        return privacy;
    }

    /**
     * @param privacy Privacy status of the Habit
     */
    public void setPrivacy(Boolean privacy) {
        this.privacy = privacy;
    }

    /**
     * @return Days of the Habit
     */
    public String getDays() {
        return days;
    }

    /**
     * @param days Days of the Habit
     */
    public void setDays(String days) {
        this.days = days;
    }

    public String getDay(){
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        String day = dayFormat.format(timestamp.toDate());
        return day;
    }

    public String getMonth(){
        SimpleDateFormat dayFormat = new SimpleDateFormat("MM");
        String month = dayFormat.format(timestamp.toDate());
        return month;
    }

    public String getYear(){
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy");
        String year = dayFormat.format(timestamp.toDate());
        return year;
    }


}
