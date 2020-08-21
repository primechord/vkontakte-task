package com.atdroid.atyurin.futuremoney.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;
import java.util.Objects;
import java.util.function.Function;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    final static String LOG_TAG = "DatePickerFragment";
    final static String KEY_CALENDAR = "data_picker_fragment_key_calendar";
    Calendar calendar;
    Function<Integer[], Void> dataSetChangedFun;

    public static DatePickerFragment newInstance(Calendar calendar, Function<Integer[], Void> dataSetChanged) {
        DatePickerFragment datePickerFragment = new DatePickerFragment(dataSetChanged);
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_CALENDAR, calendar);
        datePickerFragment.setArguments(arguments);
        return datePickerFragment;
    }

    private DatePickerFragment(Function<Integer[], Void> dataSetChanged) {
        super();
        this.dataSetChangedFun = dataSetChanged;
//        this.calendar = (Calendar) getArguments().getSerializable(KEY_CALENDAR);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
            Integer[] date = new Integer[]{year, month, day};
        dataSetChangedFun.apply(date);
        // Do something with the date chosen by the user
        // Override this method in calling class
    }

    @Override
    public void show(FragmentManager fragmentManager, String str) {
        super.show(fragmentManager, str);
        Log.d(LOG_TAG, "show");

//        new KeyboardManager(this).closeKeyboard();
    }
}
