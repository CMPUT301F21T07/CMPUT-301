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
import java.util.HashMap;
import java.util.List;

/**
 * Represents an individual item in the habits listview
 */
public class habitListAdapter  extends ArrayAdapter<Habit> {
    /**
     * The list of habits
     */
    private final ArrayList<Habit> habits;
    private Context context;

    final int INVALID_ID = -1;
    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public habitListAdapter(Context context, ArrayList<Habit> habitArrayList){
        super(context, R.layout.habit_list_adapter, habitArrayList);
        this.habits = habitArrayList;
        this.context = context;
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
            view = LayoutInflater.from(getContext()).inflate(R.layout.habit_list_adapter, parent, false);
        }

        Habit habit = habits.get(position);

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
