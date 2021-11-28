package com.example.trackhabit;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class UserTest {

    User getUserObject(String username, String password){
        User user = new User(username, password);
        return user;
    }

    @Test
    public void getUserName() {
        String username = "Test";
        User user = getUserObject(username, "");
        String username1 = user.getUserName();
        Assert.assertEquals(username, username1);
    }

    @Test
    public void getPassword() {
        String password = "Test";
        User user = getUserObject("", password);
        String password1 = user.getPassword();
        Assert.assertEquals(password, password1);
    }
}