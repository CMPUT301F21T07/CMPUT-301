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

public class habitListAdapter  extends ArrayAdapter<Habits> {
    private ArrayList<Habits> habits;
    private Context context;

    public habitListAdapter(Context context, ArrayList<Habits> habitArrayList){
        super(context, R.layout.habit_list_adapter, habitArrayList);
        this.habits = habitArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.habit_list_adapter, parent, false);
        }

        Habits habit = habits.get(position);

        TextView habitName  = view.findViewById(R.id.habit_name);
        TextView habitTitle = view.findViewById(R.id.habit_title);
        TextView startDate  = view.findViewById(R.id.date);

        habitName.setText(habit.getHabitName());
        habitTitle.setText(habit.getHabitTitle());

        Timestamp timestamp = habit.getStartDate();
        Date date = timestamp.toDate();
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String strDate = dateFormat.format(date);
        startDate.setText(strDate);

        return view;
    }
}