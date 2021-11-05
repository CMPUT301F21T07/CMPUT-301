package com.example.trackhabit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventListAdapter extends ArrayAdapter<HabitEvent> {
    /**
     * The list of habits
     */
    private ArrayList<HabitEvent> eventList;
    private Context context;



    public EventListAdapter(Context context,ArrayList<HabitEvent> eventList) {
        super(context,R.layout.event_list_adapter,eventList);
        this.eventList = eventList;
        this.context=context;
    }

    /**
     * This function returns the view of the habit object containing the name, title, and date
     * @param position The position of the habit in the list
     * @param convertView The converted view of the habit object
     * @param parent The parent group
     * @return The view of the habit object in the list
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.event_list_adapter, parent, false);
        }

        HabitEvent event = eventList.get(position);

        TextView habitName  = view.findViewById(R.id.habit_name);
        TextView username = view.findViewById(R.id.username);
        TextView startDate  = view.findViewById(R.id.date);
        TextView comment=view.findViewById(R.id.comments);

        habitName.setText(event.getHabitName());
        username.setText(event.getUserName());
        comment.setText(event.getComment());


        startDate.setText(event.getDate());

        return view;
    }
}