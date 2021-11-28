package com.example.trackhabit;

import com.google.firebase.Timestamp;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;

public class HabitTest {

    Habit getHabitobject(String habitName, String habitUser, String habitTitle,
                        String habitReason, Timestamp timestamp, Boolean privacy, String days){
        Habit habit = new Habit(habitName, habitUser, habitTitle, habitReason, timestamp, privacy, days);
        return habit;
    }

    @Test
    public void testGetHabitName() {
        String habitname = "Test";
        Timestamp timestamp = new Timestamp(0, 0);
        Habit habit = getHabitobject(habitname, "","", "", timestamp, Boolean.TRUE, "");
        String habitname1 = habit.getHabitName();
        Assert.assertEquals(habitname, habitname1);
    }
    @Test
    public void testSetHabitName() {
        Timestamp timestamp = new Timestamp(0, 0);
        Habit habit = getHabitobject("", "", "", "", timestamp, Boolean.TRUE, "");
        String habitname = "Test";
        habit.setHabitName(habitname);
        Assert.assertTrue(habitname.equals(habit.getHabitName()));
    }
    @Test
    public void testGetHabitUser() {
        String habituser = "Test";
        Timestamp timestamp = new Timestamp(0, 0);
        Habit habit = getHabitobject("", habituser,"", "", timestamp, Boolean.TRUE, "");
        String habituser1 = habit.getHabitUser();
        Assert.assertEquals(habituser, habituser1);
    }
    @Test
    public void testSetHabitUser() {
        Timestamp timestamp = new Timestamp(0, 0);
        Habit habit = getHabitobject("", "", "", "", timestamp, Boolean.TRUE, "");
        String habituser = "Test";
        habit.setHabitUser(habituser);
        Assert.assertTrue(habituser.equals(habit.getHabitUser()));
    }
    @Test
    public void testGetHabitTitle() {
        String habittitle = "Test";
        Timestamp timestamp = new Timestamp(0, 0);
        Habit habit = getHabitobject("", "", habittitle, "", timestamp, Boolean.TRUE, "");
        String habittitle1 = habit.getHabitTitle();
        Assert.assertEquals(habittitle, habittitle1);
    }
    @Test
    public void testSetHabitTitle() {
        Timestamp timestamp = new Timestamp(0, 0);
        Habit habit = getHabitobject("", "", "", "", timestamp, Boolean.TRUE, "");
        String habittitle = "Test";
        habit.setHabitTitle(habittitle);
        Assert.assertTrue(habittitle.equals(habit.getHabitTitle()));
    }
    @Test
    public void testGetHabitReason() {
        String habitreason = "Test";
        Timestamp timestamp = new Timestamp(0, 0);
        Habit habit = getHabitobject("", "","", habitreason, timestamp, Boolean.TRUE, "");
        String habitreason1 = habit.getHabitReason();
        Assert.assertEquals(habitreason, habitreason1);
    }
    @Test
    public void testSetHabitReason() {
        Timestamp timestamp = new Timestamp(0, 0);
        Habit habit = getHabitobject("", "", "", "", timestamp, Boolean.TRUE, "");
        String habitreason = "Test";
        habit.setHabitReason(habitreason);
        Assert.assertTrue(habitreason.equals(habit.getHabitReason()));
    }
    @Test
    public void testGetStartDate() {
        Timestamp timestamp = new Timestamp(1, 1);
        Habit habit = getHabitobject("", "","", "", timestamp, Boolean.TRUE, "");
        Timestamp timestamp1 = habit.getStartDate();
        Assert.assertEquals(timestamp, timestamp1);
    }
    @Test
    public void testSetStartDate() {
        Timestamp timestamp = new Timestamp(0, 0);
        Habit habit = getHabitobject("", "", "", "", timestamp, Boolean.TRUE, "");
        Timestamp timestamp1 = new Timestamp(1, 1);
        habit.setStartDate(timestamp1);
        Assert.assertTrue(timestamp1.equals(habit.getStartDate()));
    }
    @Test
    public void testGetPrivacy() {
        Timestamp timestamp = new Timestamp(0, 0);
        Habit habit = getHabitobject("", "","", "", timestamp, Boolean.FALSE, "");
        Boolean privacy = habit.getPrivacy();
        Assert.assertEquals(privacy, Boolean.FALSE);
    }
    @Test
    public void testSetPrivacy() {
        Timestamp timestamp = new Timestamp(0, 0);
        Habit habit = getHabitobject("", "", "", "", timestamp, Boolean.TRUE, "");
        habit.setPrivacy(Boolean.FALSE);
        Assert.assertTrue(Boolean.FALSE.equals(habit.getPrivacy()));
    }
    @Test
    public void testGetDays() {
        String days = "Test";
        Timestamp timestamp = new Timestamp(0, 0);
        Habit habit = getHabitobject("", "","", "", timestamp, Boolean.TRUE, days);
        String days1 = habit.getDays();
        Assert.assertEquals(days, days1);
    }
    @Test
    public void testSetDays() {
        Timestamp timestamp = new Timestamp(0, 0);
        Habit habit = getHabitobject("", "", "", "", timestamp, Boolean.TRUE, "");
        String days = "Test";
        habit.setDays(days);
        Assert.assertTrue(days.equals(habit.getDays()));
    }
    @Test
    public void testGetDay() {
        Timestamp timestamp = new Timestamp(1, 1);
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        String day = dayFormat.format(timestamp.toDate());
        Habit habit = getHabitobject("", "","", "", timestamp, Boolean.TRUE, "");
        Timestamp timestamp1 = habit.getStartDate();
        String day1 = dayFormat.format(timestamp1.toDate());
        Assert.assertEquals(day, day1);
    }
    @Test
    public void testGetMonth() {
        Timestamp timestamp = new Timestamp(1, 1);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        String month = monthFormat.format(timestamp.toDate());
        Habit habit = getHabitobject("", "","", "", timestamp, Boolean.TRUE, "");
        Timestamp timestamp1 = habit.getStartDate();
        String month1 = monthFormat.format(timestamp1.toDate());
        Assert.assertEquals(month, month1);
    }
    @Test
    public void testGetYear() {
        Timestamp timestamp = new Timestamp(1, 1);
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String year = yearFormat.format(timestamp.toDate());
        Habit habit = getHabitobject("", "","", "", timestamp, Boolean.TRUE, "");
        Timestamp timestamp1 = habit.getStartDate();
        String year1 = yearFormat.format(timestamp1.toDate());
        Assert.assertEquals(year, year1);
    }
}