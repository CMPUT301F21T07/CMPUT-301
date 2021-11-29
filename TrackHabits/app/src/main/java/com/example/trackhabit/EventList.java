package com.example.trackhabit;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a Event List that extends Habit Event arraylist
 */

public class EventList extends ArrayList<HabitEvent> implements Serializable {
    ArrayList<HabitEvent> eventList;
    public  EventList(ArrayList<HabitEvent> eventList){
        super(eventList);
        this.eventList=eventList;
    }

}
