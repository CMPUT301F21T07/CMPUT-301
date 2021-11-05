package com.example.trackhabit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

//this fragment adds a new medicine
public class AddHabitFragment extends DialogFragment {
    private EditText editTitle;//declaring the fragments
    private EditText editReason;
    private DatePicker editDate;
    private Boolean editPrivacy;
    private EditText editName;
    private AddHabitFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onAdd(Habits newHabit);//listen to the ok button when adding
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener=(OnFragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString()+"must implement OnFragmentInteractionListener");
        }
    }

    static AddHabitFragment newInstance(Habits habit){
        Bundle args=new Bundle();
        args.putSerializable("habit", (Serializable) habit);
        AddHabitFragment fragment=new AddHabitFragment();
        fragment.setArguments(args);
        return fragment;//retrieving the medicine class from the main activity through bundle
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {//
        //Inflate the layout for this fragment
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.add_habit_fragment,null);





        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Habit")
                .setNegativeButton("Cancel",null)//cancel operation
                .setPositiveButton("OK",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface,int i){


                    }
                }).create();

    }
}
