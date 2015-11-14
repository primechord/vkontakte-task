package com.atdroid.atyurin.futuremoney.fragments;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.dao.OutcomesDAO;
import com.atdroid.atyurin.futuremoney.serialization.Outcome;
import com.atdroid.atyurin.futuremoney.utils.DateFormater;
import com.atdroid.atyurin.futuremoney.utils.KeyboardManager;

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

public class OutcomeItemFragment extends Fragment {
    final static String INCOME_KEY = "key_outcome";
    final static String LOG_TAG = "OutcomeItemFragment";
    Outcome outcome;
    boolean isNewItem = true;
    EditText etName, etAmount, etPeriodValue;
    TextView tvNameTitle, tvAmountTitle, tvTypeTitle, tvSingleDateTitle, tvSingleDateValue, tvBeginDateTitle, tvBeginDateValue,tvEndDateTitle, tvEndDateValue, tvPeriodTitle;
    Spinner spType, spPeriodType;
    LinearLayout llName, llAmount, llPeriod;
    RelativeLayout llSingleDate, llBeginDate, llEndDate;
    ArrayAdapter<String> adapterType, adapterPeriodType;
    public static OutcomeItemFragment newInstance() {
        Log.d("Outcomer fragment", "newInstance");

        OutcomeItemFragment OutcomeItemFragment = new OutcomeItemFragment();
        OutcomeItemFragment.outcome = new Outcome();
        return OutcomeItemFragment;
    }

    public static OutcomeItemFragment newInstance(Outcome outcome) {
        Log.d("Outcomer fragment", "newInstance");
        OutcomeItemFragment OutcomeItemFragment = new OutcomeItemFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(INCOME_KEY, budget_item);
        OutcomeItemFragment.outcome = outcome;
        OutcomeItemFragment.isNewItem = false;
        return OutcomeItemFragment;
    }

    public OutcomeItemFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);//switch off menu for fragment

        View rootView =  inflater.inflate(R.layout.fragment_budget_item, container, false);
        //name
        tvNameTitle = (TextView) rootView.findViewById(R.id.tv_item_name_title);
        etName = (EditText) rootView.findViewById(R.id.et_item_name_value);
        if (outcome.getName().length() > 0){
            etName.setText(outcome.getName());
            tvNameTitle.setVisibility(View.VISIBLE);
        }
        etName.addTextChangedListener(nameTextWatcher);
        //amount
        tvAmountTitle = (TextView) rootView.findViewById(R.id.tv_item_amount_title);
        etAmount = (EditText) rootView.findViewById(R.id.et_item_amount_value);
        etAmount.setRawInputType(Configuration.KEYBOARD_QWERTY);
        if (outcome.getValue() > 0){
            etAmount.setText(Double.toString(outcome.getValue()));
            tvAmountTitle.setVisibility(View.VISIBLE);
        }
        etAmount.addTextChangedListener(amountTextWatcher);
        //type
//        tvTypeTitle = (TextView) rootView.findViewById(R.id.tv_item_);
        spType = (Spinner) rootView.findViewById(R.id.sp_item_type);
        adapterType = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.budget_item_types));
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapterType);
        spType.setSelection(outcome.getType());
        adapterType.notifyDataSetChanged();
        spType.setOnItemSelectedListener(typeSelectedListener);
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
        //single date
        llSingleDate = (RelativeLayout) rootView.findViewById(R.id.ll_item_single_date);
        llSingleDate.setOnClickListener(singleDateListener);
        tvSingleDateTitle = (TextView) llSingleDate.findViewById(R.id.tv_date_title);
        tvSingleDateTitle.setText(R.string.budget_item_single_date);
        tvSingleDateValue = (TextView) llSingleDate.findViewById(R.id.tv_date_value);
        tvSingleDateValue.setText(sdf.format(outcome.getSingle_date().getTime()));
        //begin date
        llBeginDate = (RelativeLayout) rootView.findViewById(R.id.ll_item_begin_date);
        llBeginDate.setOnClickListener(beginDateListener);
        tvBeginDateTitle = (TextView) llBeginDate.findViewById(R.id.tv_date_title);
        tvBeginDateTitle.setText(R.string.budget_item_begin_date);
        tvBeginDateValue = (TextView) llBeginDate.findViewById(R.id.tv_date_value);
        tvBeginDateValue.setText(sdf.format(outcome.getBegin_date().getTime()));
        //End date
        llEndDate = (RelativeLayout) rootView.findViewById(R.id.ll_item_end_date);
        llEndDate.setOnClickListener(endDateListener);
        tvEndDateTitle = (TextView) llEndDate.findViewById(R.id.tv_date_title);
        tvEndDateTitle.setText(R.string.budget_item_end_date);
        tvEndDateValue = (TextView) llEndDate.findViewById(R.id.tv_date_value);
        tvEndDateValue.setText(sdf.format(outcome.getEnd_date().getTime()));        //period
        llPeriod = (LinearLayout) rootView.findViewById(R.id.ll_item_period);
        spPeriodType = (Spinner) rootView.findViewById(R.id.sp_period_type_spinner);
        adapterPeriodType = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.budget_item_period_items));
        adapterPeriodType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPeriodType.setAdapter(adapterPeriodType);
        spPeriodType.setSelection(outcome.getPeriod_type());
        spPeriodType.setOnItemSelectedListener(periodTypeSelectedListener);

        etPeriodValue = (EditText) rootView.findViewById(R.id.et_period_value);
        etPeriodValue.setRawInputType(Configuration.KEYBOARD_QWERTY);
        etPeriodValue.setText(Integer.toString(outcome.getPeriod_value()));
        etPeriodValue.addTextChangedListener(periodValueWatcher);
        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
//        Log.d("OutcomeItemFragment", "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_budget_item_new_item, menu);
    }

    public void onPrepareOptionsMenu(Menu menu) {
//        Log.d("OutcomeItemFragment", "onPrepareOptionsMenu");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("OutcomeItemFragment", "onOptionsItemSelected");
        if (item.getItemId() == R.id.action_btn_add_budget_item_confirm) {
            if (outcome.getName().length() < 1){
                Toast.makeText(getActivity().getBaseContext(), R.string.msg_budget_item_empty_name, Toast.LENGTH_LONG).show();
                return false;
            }
            if (outcome.getValue() < 1){
                Toast.makeText(getActivity().getBaseContext(), R.string.msg_budget_item_empty_amount, Toast.LENGTH_LONG).show();
                return false;
            }
            if (outcome.getBegin_date().after(outcome.getEnd_date())){
                Toast.makeText(getActivity().getBaseContext(), R.string.msg_budget_item_wrong_dates, Toast.LENGTH_LONG).show();
                return false;
            }
            OutcomesDAO dao = new OutcomesDAO(getActivity().getBaseContext());
            dao.openWritable();
            if (isNewItem){
                if (dao.addOutcome(outcome)){
                    Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.msg_budget_item_success), Toast.LENGTH_LONG).show();
                };
            }else{
                if (dao.updateOutcome(outcome)){
                    Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.msg_budget_item_success), Toast.LENGTH_LONG).show();
                }
            }
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, OutcomesFragmentContainer.newInstance())
                    .commit();
            dao.close();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            outcome.setName(charSequence.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0){
                tvNameTitle.setVisibility(View.VISIBLE);
            }else{
                tvNameTitle.setVisibility(View.GONE);
            }
        }
    };

    private TextWatcher amountTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            Log.d(LOG_TAG, editable.toString());
            if (editable.length() > 0){
                outcome.setValue(Double.valueOf(editable.toString()));
            }
            if (editable.length() > 0){
                tvAmountTitle.setVisibility(View.VISIBLE);
            }else{
                tvAmountTitle.setVisibility(View.GONE);
            }
        }
    };

    private AdapterView.OnItemSelectedListener typeSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            Log.d("typeSelectedListener", "" + position);
            if (position == Outcome.TYPE_PERIODICAL){
                outcome.setType(Outcome.TYPE_PERIODICAL);//1
                llSingleDate.setVisibility(View.GONE);
                llBeginDate.setVisibility(View.VISIBLE);
                llEndDate.setVisibility(View.VISIBLE);
                llPeriod.setVisibility(View.VISIBLE);
            }else {
                outcome.setType(Outcome.TYPE_SINGLE);//0
                llSingleDate.setVisibility(View.VISIBLE);
                llBeginDate.setVisibility(View.GONE);
                llEndDate.setVisibility(View.GONE);
                llPeriod.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private View.OnClickListener singleDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerFragment datePicker = new DatePickerFragment() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    tvSingleDateValue.setText(sdf.format(calendar.getTime()));
                    outcome.setSingle_date(calendar);
                }
            };
            datePicker.setCalendar(outcome.getSingle_date());
            datePicker.show(getFragmentManager(), "datePicker");
        }
    };

    private View.OnClickListener beginDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerFragment datePicker = new DatePickerFragment() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    tvBeginDateValue.setText(sdf.format(calendar.getTime()));
                    outcome.setBegin_date(calendar);
                }
            };
            datePicker.setCalendar(outcome.getBegin_date());
            datePicker.show(getFragmentManager(), "datePicker");
        }
    };

    private View.OnClickListener endDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerFragment datePicker = new DatePickerFragment() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    tvEndDateValue.setText(sdf.format(calendar.getTime()));
                    outcome.setEnd_date(calendar);
                }
            };
            datePicker.setCalendar(outcome.getEnd_date());
            datePicker.show(getFragmentManager(), "datePicker");
        }
    };

    private AdapterView.OnItemSelectedListener periodTypeSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            outcome.setPeriod_type(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private TextWatcher periodValueWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() > 0){
                outcome.setPeriod_value(Integer.parseInt(charSequence.toString()));
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {}
    };
}
