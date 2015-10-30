package com.atdroid.atyurin.futuremoney.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.dao.IncomesDAO;
import com.atdroid.atyurin.futuremoney.serialization.Income;
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

public class IncomeItemFragment extends Fragment {
    final static String INCOME_KEY = "key_income";
    final static String LOG_TAG = "IncomeItemFragment";
    Income income;
    boolean isNewItem = true;
    EditText etName, etAmount, etPeriodValue;
    TextView tvNameTitle, tvAmountTitle, tvTypeTitle, tvSingleDateTitle, tvSingleDateValue, tvBeginDateTitle, tvBeginDateValue,tvEndDateTitle, tvEndDateValue, tvPeriodTitle;
    Spinner spType, spPeriodType;
    FragmentManager fragmentManager;
    LinearLayout llName, llAmount, llSingleDate, llBeginDate, llEndDate, llPeriod;
    ArrayAdapter<String> adapterType, adapterPeriodType;
    public static IncomeItemFragment newInstance() {
        Log.d("Incomer fragment", "newInstance");

        IncomeItemFragment IncomeItemFragment = new IncomeItemFragment();
        IncomeItemFragment.income = new Income();
        return IncomeItemFragment;
    }

    public static IncomeItemFragment newInstance(Income income) {
        Log.d("Incomer fragment", "newInstance");
        IncomeItemFragment IncomeItemFragment = new IncomeItemFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(INCOME_KEY, budget_item);
        IncomeItemFragment.income = income;
        IncomeItemFragment.isNewItem = false;
        return IncomeItemFragment;
    }

    public IncomeItemFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);//switch off menu for fragment

        View rootView =  inflater.inflate(R.layout.fragment_budget_item, container, false);
        //name
        tvNameTitle = (TextView) rootView.findViewById(R.id.tv_item_name_title);
        etName = (EditText) rootView.findViewById(R.id.et_item_name_value);
        if (income.getName().length() > 0){
            etName.setText(income.getName());
            tvNameTitle.setVisibility(View.VISIBLE);
        }
        etName.addTextChangedListener(nameTextWatcher);
        //amount
        tvAmountTitle = (TextView) rootView.findViewById(R.id.tv_item_amount_title);
        etAmount = (EditText) rootView.findViewById(R.id.et_item_amount_value);
        etAmount.setRawInputType(Configuration.KEYBOARD_QWERTY);
        if (income.getValue() > 0){
            etAmount.setText(Double.toString(income.getValue()));
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
        spType.setSelection(income.getType());
        adapterType.notifyDataSetChanged();
        spType.setOnItemSelectedListener(typeSelectedListener);
        //single date
        llSingleDate = (LinearLayout) rootView.findViewById(R.id.ll_item_single_date);
        llSingleDate.setOnClickListener(singleDateListener);
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
        tvSingleDateValue = (TextView) rootView.findViewById(R.id.tv_item_single_date_value);
        tvSingleDateValue.setText(sdf.format(income.getSingle_date().getTime()));
        //begin date
        llBeginDate = (LinearLayout) rootView.findViewById(R.id.ll_item_begin_date);
        llBeginDate.setOnClickListener(beginDateListener);
        tvBeginDateValue = (TextView) rootView.findViewById(R.id.tv_item_begin_date_value);
        tvBeginDateValue.setText(sdf.format(income.getBegin_date().getTime()));
        //End date
        llEndDate = (LinearLayout) rootView.findViewById(R.id.ll_item_end_date);
        llEndDate.setOnClickListener(endDateListener);
        tvEndDateValue = (TextView) rootView.findViewById(R.id.tv_item_end_date_value);
        tvEndDateValue.setText(sdf.format(income.getEnd_date().getTime()));
        //period
        llPeriod = (LinearLayout) rootView.findViewById(R.id.ll_item_period);
        spPeriodType = (Spinner) rootView.findViewById(R.id.sp_period_type_spinner);
        adapterPeriodType = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.budget_item_period_items));
        adapterPeriodType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPeriodType.setAdapter(adapterPeriodType);
        spPeriodType.setSelection(income.getPeriod_type());
        spPeriodType.setOnItemSelectedListener(periodTypeSelectedListener);

        etPeriodValue = (EditText) rootView.findViewById(R.id.et_period_value);
        etPeriodValue.setRawInputType(Configuration.KEYBOARD_QWERTY);
        etPeriodValue.setText(Integer.toString(income.getPeriod_value()));
        etPeriodValue.addTextChangedListener(periodValueWatcher);
        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
//        Log.d("IncomeItemFragment", "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_budget_item_new_item, menu);
    }

    public void onPrepareOptionsMenu(Menu menu) {
//        Log.d("IncomeItemFragment", "onPrepareOptionsMenu");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("IncomeItemFragment", "onOptionsItemSelected");
        if (item.getItemId() == R.id.action_btn_add_budget_item_confirm) {
            if (income.getName().length() < 1){
                Toast.makeText(getActivity().getBaseContext(), R.string.msg_budget_item_empty_name, Toast.LENGTH_LONG).show();
                return false;
            }
            if (income.getValue() < 1){
                Toast.makeText(getActivity().getBaseContext(), R.string.msg_budget_item_empty_amount, Toast.LENGTH_LONG).show();
                return false;
            }
            IncomesDAO dao = new IncomesDAO(getActivity().getBaseContext());
            dao.openWritable();
            if (isNewItem){
                if (dao.addIncome(income)){
                 Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.msg_budget_item_success), Toast.LENGTH_LONG).show();
                };
            }else{
                if (dao.updateIncome(income)){
                    Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.msg_budget_item_success), Toast.LENGTH_LONG).show();
                }
            }
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, IncomesFragment.newInstance(getActivity(), getFragmentManager()))
                    .commit();
            dao.close();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            income.setName(charSequence.toString());
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
                income.setValue(Double.valueOf(editable.toString()));
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
            if (position == Income.TYPE_PERIODICAL){
                income.setType(Income.TYPE_PERIODICAL);//1
                llSingleDate.setVisibility(View.GONE);
                llBeginDate.setVisibility(View.VISIBLE);
                llEndDate.setVisibility(View.VISIBLE);
                llPeriod.setVisibility(View.VISIBLE);
            }else {
                income.setType(Income.TYPE_SINGLE);//0
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
            DatePickerFragment datePicker = new DatePickerFragment(income.getSingle_date()) {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    tvSingleDateValue.setText(sdf.format(calendar.getTime()));
                    income.setSingle_date(calendar);
                }
            };
            datePicker.show(getFragmentManager(), "datePicker");


        }
    };

    private View.OnClickListener beginDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerFragment datePicker = new DatePickerFragment(income.getBegin_date()) {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    tvBeginDateValue.setText(sdf.format(calendar.getTime()));
                    income.setBegin_date(calendar);
                }
            };

            datePicker.show(getFragmentManager(), "datePicker");
        }
    };

    private View.OnClickListener endDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerFragment datePicker = new DatePickerFragment(income.getEnd_date()) {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    tvEndDateValue.setText(sdf.format(calendar.getTime()));
                    income.setEnd_date(calendar);
                }
            };

            datePicker.show(getFragmentManager(), "datePicker");
        }
    };

    private AdapterView.OnItemSelectedListener periodTypeSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            income.setPeriod_type(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private TextWatcher periodValueWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() > 0){
                income.setPeriod_value(Integer.parseInt(charSequence.toString()));
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {}
    };
}
