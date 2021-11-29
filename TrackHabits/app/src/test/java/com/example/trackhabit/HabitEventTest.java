package com.example.trackhabit;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class HabitEventTest {

    HabitEvent getHabitEventObject(String habitName, String userName, String date, String comment,
                                   boolean locationPermission, String location, boolean photoUploaded){
        HabitEvent habitEvent = new HabitEvent(habitName, userName, date, comment, locationPermission, location, photoUploaded);
        return habitEvent;
    }

    @Test
    public void testGetHabitName() {
        String habitname = "Test";
        HabitEvent habitevent = getHabitEventObject(habitname, "","", "", Boolean.TRUE, "", Boolean.TRUE);
        String habitname1 = habitevent.getHabitName();
        Assert.assertEquals(habitname, habitname1);
    }

    @Test
    public void testSetHabitName() {
        HabitEvent habitevent = getHabitEventObject("", "","", "", Boolean.TRUE, "", Boolean.TRUE);
        String habitname = "Test";
        habitevent.setHabitName(habitname);
        Assert.assertTrue(habitname.equals(habitevent.getHabitName()));
    }

    @Test
    public void testGetUserName() {
        String username = "Test";
        HabitEvent habitevent = getHabitEventObject("", username,"", "", Boolean.TRUE, "", Boolean.TRUE);
        String username1 = habitevent.getUserName();
        Assert.assertEquals(username, username1);
    }
    @Test
    public void testSetUserName() {
        HabitEvent habitevent = getHabitEventObject("", "","", "", Boolean.TRUE, "", Boolean.TRUE);
        String username = "Test";
        habitevent.setUserName(username);
        Assert.assertTrue(username.equals(habitevent.getUserName()));
    }
    @Test
    public void testGetDate() {
        String date = "Test";
        HabitEvent habitevent = getHabitEventObject("", "",date, "", Boolean.TRUE, "", Boolean.TRUE);
        String date1 = habitevent.getDate();
        Assert.assertEquals(date, date1);
    }
    @Test
    public void testSetDate() {
        HabitEvent habitevent = getHabitEventObject("", "","", "", Boolean.TRUE, "", Boolean.TRUE);
        String date = "Test";
        habitevent.setDate(date);
        Assert.assertTrue(date.equals(habitevent.getDate()));
    }
    @Test
    public void testGetComment() {
        String comment = "Test";
        HabitEvent habitevent = getHabitEventObject("", "","", comment, Boolean.TRUE, "", Boolean.TRUE);
        String comment1 = habitevent.getComment();
        Assert.assertEquals(comment,comment1);
    }
    @Test
    public void testSetComment() {
        HabitEvent habitevent = getHabitEventObject("", "","", "", Boolean.TRUE, "", Boolean.TRUE);
        String comment = "Test";
        habitevent.setComment(comment);
        Assert.assertTrue(comment.equals(habitevent.getComment()));
    }
    @Test
    public void testGetLocationPermission() {
        HabitEvent habitevent = getHabitEventObject("", "","", "", Boolean.FALSE, "", Boolean.TRUE);
        Assert.assertEquals(habitevent.getLocationPermission(), Boolean.FALSE);
    }
    @Test
    public void testSetLocationPermission() {
        HabitEvent habitevent = getHabitEventObject("", "","", "", Boolean.TRUE, "", Boolean.TRUE);
        habitevent.setLocationPermission(Boolean.FALSE);
        Assert.assertTrue(Boolean.FALSE.equals(habitevent.getLocationPermission()));
    }
    @Test
    public void testGetLocation() {
        String location = "Test";
        HabitEvent habitevent = getHabitEventObject("", "","", "", Boolean.TRUE, location, Boolean.TRUE);
        String location1 = habitevent.getLocation();
        Assert.assertEquals(location, location1);
    }
    @Test
    public void testSetLocation() {
        HabitEvent habitevent = getHabitEventObject("", "","", "", Boolean.TRUE, "", Boolean.TRUE);
        String location = "Test";
        habitevent.setLocation(location);
        Assert.assertTrue(location.equals(habitevent.getLocation()));
    }
    @Test
    public void testGetPhotoUploaded() {
        HabitEvent habitevent = getHabitEventObject("", "","", "", Boolean.FALSE, "", Boolean.FALSE);
        Assert.assertEquals(habitevent.getPhotoUploaded(), Boolean.FALSE);
    }
    @Test
    public void testSetPhotoUploaded() {
        HabitEvent habitevent = getHabitEventObject("", "","", "", Boolean.TRUE, "", Boolean.TRUE);
        habitevent.setPhotoUploaded(Boolean.FALSE);
        Assert.assertTrue(Boolean.FALSE.equals(habitevent.getPhotoUploaded()));
    }
}