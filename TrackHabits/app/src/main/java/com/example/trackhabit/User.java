package com.example.trackhabit;

/**
 * This represents the user information
 */
public class User {
    private final String username;
    private final String password;

    User(String username, String password){
        this.username = username;
        this.password = password;
    }

    String getUserName(){
        return this.username;
    }

    String getPassword(){
        return this.password;
    }
}
