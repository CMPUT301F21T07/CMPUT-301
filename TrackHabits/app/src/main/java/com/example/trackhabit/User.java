package com.example.trackhabit;

public class User {
    private String username;
    private String password;

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
