package com.example.trackhabit;

/**
 * This Object Class represents the user information
 */
public class User {
    private final String username;
    private final String password;

    User(String username, String password){
        this.username = username;
        this.password = password;
    }

    /**
     * This function gets the username
     */
    String getUserName() {
        return this.username;
    }

    /**
     * This function gets the password
     */
    String getPassword(){
        return this.password;
    }
}
