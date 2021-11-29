package com.example.trackhabit;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
    private ArrayList<Habit> habitsArrayList;
    private Context context;
    int indexToReturn;


    // RecyclerAdapter constructor
    public RecyclerAdapter(Context context, ArrayList<Habit> habitsArrayList){
        this.habitsArrayList = habitsArrayList;
        this.context = context;
    }


    // Declaring a public class to hold the layout of the recycler view
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView habitName, habitTitle, startDate;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            habitName  = itemView.findViewById(R.id.habit_name);
            habitTitle = itemView.findViewById(R.id.habit_title);
            startDate  = itemView.findViewById(R.id.date);
            cardView   = itemView.findViewById(R.id.card_view);
            cardView.setOnCreateContextMenuListener(this);
        }

        /**
         * Function that creates a context menu when there is a long press on a ListView item
         * @param contextMenu This is the menu object
         * @param view This is the view object
         * @param contextMenuInfo This is the info on the menu
        */
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(this.getAdapterPosition(), 121, 0, "View habit");
            contextMenu.add(this.getAdapterPosition(), 122, 1, "Edit habit");
            contextMenu.add(this.getAdapterPosition(), 123, 2, "Delete habit");
            indexToReturn = getAdapterPosition();

        }

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the layout view for each item in
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.habit_list_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        // Getting position of current habit
        Habit habit = habitsArrayList.get(position);

        // Setting values of the TextViews
        holder.habitName.setText(habit.getHabitName());
        holder.habitTitle.setText(habit.getHabitTitle());

        Timestamp timestamp = habit.getStartDate();
        Date date = timestamp.toDate();
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String strDate = dateFormat.format(date);
        holder.startDate.setText(strDate);
    }

    // Function to return which item was selected in the context menu
    public int getItem() {
        return indexToReturn;
    }

    // Function that returns the full length of the list
    @Override
    public int getItemCount() {
        return habitsArrayList.size();
    }
}
