package com.atdroid.atyurin.futuremoney.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.serialization.Total;
import com.atdroid.atyurin.futuremoney.utils.DateFormater;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by atdroid on 14.09.2015.
 *
 * elements description
 * 0 - name (EditText)
 * 1 - amount (EditText)
 * 2 - type (LinearLayout{Text, Spinner})
 * 3 - single date (Date Picker)
 * 4 - begin date (Date Picker)
 * 5 - end date (Date Picker)
 * 6 - period (LinearLayout{Text, Spinner, EditText(optional - depende on spinner)})
 *
 */

public class TotalsFragment extends Fragment {
    final static String INCOME_KEY = "key_total";
    final static String LOG_TAG = "TotalItemFragment";
    Total total;
    Spinner spType;
    TextView tvCalculateDateValue;
    ArrayAdapter<String> adapterType;
    LinearLayout llCalculateDate;
    Activity activity;
    FragmentManager fragmentManager;

    public static TotalsFragment newInstance(Activity activity, FragmentManager fragmentManager) {
        TotalsFragment totalsFragment = new TotalsFragment();
        totalsFragment.activity = activity;
        totalsFragment.fragmentManager = fragmentManager;
        totalsFragment.total = new Total();
        return totalsFragment;
    }

    public TotalsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);//switch off menu for fragment

        View rootView =  inflater.inflate(R.layout.fragment_totals, container, false);
        //begin date type
        spType = (Spinner) rootView.findViewById(R.id.sp_begin_date_type);
        adapterType = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.total_begin_types));
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapterType);
        spType.setSelection(total.getBeginDateType());
        adapterType.notifyDataSetChanged();
        spType.setOnItemSelectedListener(typeSelectedListener);
        //calculate date
        llCalculateDate = (LinearLayout) rootView.findViewById(R.id.ll_calculate_date);
        llCalculateDate.setOnClickListener(endDateListener);
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
        tvCalculateDateValue = (TextView) rootView.findViewById(R.id.tv_calculate_date_value);
        tvCalculateDateValue.setText(sdf.format(total.getEnd_date().getTime()));

        Button btnCalculate = (Button) rootView.findViewById(R.id.btn_calc_totals);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcTotals();
            }
        });
        return rootView;

    }

    private AdapterView.OnItemSelectedListener typeSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            Log.d("typeSelectedListener", "" + position);
            if (position == Total.TYPE_ALL){
                total.setBeginDateType(Total.TYPE_ALL);
                llCalculateDate.setVisibility(View.GONE);
            }else if (position == Total.TYPE_SELECTED_DATE) {
                total.setBeginDateType(Total.TYPE_SELECTED_DATE);
                llCalculateDate.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private View.OnClickListener endDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerFragment datePicker = new DatePickerFragment(total.getEnd_date()) {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    tvCalculateDateValue.setText(sdf.format(calendar.getTime()));
                    total.setEnd_date(calendar);
                }
            };

            datePicker.show(getFragmentManager(), "datePicker");
        }
    };

    public void calcTotals(){

    }

    public class CalculateTotalsTask extends AsyncTask<Void,Void,Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity().getBaseContext());
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Calendar begin = total.getBegin_date();
            Calendar end= total.getEnd_date();
//            IncomesDAO
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pd.cancel();
        }
    }

}