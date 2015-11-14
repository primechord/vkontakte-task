package com.atdroid.atyurin.futuremoney.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.utils.KeyboardManager;

import java.util.Calendar;

/**
 * Created by atdroid on 25.09.2015.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    final static String LOG_TAG = "DatePickerFragment";
    final static String KEY_CALENDAR = "data_picker_fragment_key_calendar";
    Calendar calendar;
//    public static DatePickerFragment newInstance(Calendar calendar){
//        DatePickerFragment datePickerFragment = new DatePickerFragment();
//        Bundle arguments = new Bundle();
//        arguments.putSerializable(KEY_CALENDAR, calendar);
//        datePickerFragment.setArguments(arguments);
//        return datePickerFragment;
//    }

    public DatePickerFragment() {
        super();
//        this.calendar = (Calendar) getArguments().getSerializable(KEY_CALENDAR);
    }

    public void setCalendar(Calendar calendar){
        this.calendar = calendar;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(LOG_TAG, "onViewCreated");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        View focusView = getActivity()
                .getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        // Override this method in calling class
    }

    @Override
    public void show(FragmentManager fragmentManager, String str){
        super.show(fragmentManager, str);
        Log.d(LOG_TAG, "show");

//        new KeyboardManager(this).closeKeyboard();
    }
}
