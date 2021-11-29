package com.example.trackhabit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Represents a date picker fragment
 */
public class DatePickerFragment extends DialogFragment {

    /**
     * Creates an instance that shows an date picking instance.
     * will be check on creation of instance.
     * @param savedInstanceState This is the instance state from the previous creation of habits activity
     * @return DatePickerDialog
     */

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Initializing a date-picker dialog fragment
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getTargetFragment(), year, month, day);
    }
}

