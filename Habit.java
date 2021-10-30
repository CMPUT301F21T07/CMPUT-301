package com.example.projectpart3;

import java.util.ArrayList;
import java.util.Date;

public class Habit {
    private String title;
    private String reason;
    private Date start;
    private boolean if_public;

    Habit(String title, String reason, Date start, boolean if_public){
        this.title=title;
        this.reason=reason;
        this.start=start;
        this.if_public=if_public;
    }
    String getTitle(){
        return this.title;
    }
    String getReason(){
        return this.reason;
    }
    Date start(){
        return this.start;
    }
    boolean isIf_public(){
        return this.if_public;
    }

}