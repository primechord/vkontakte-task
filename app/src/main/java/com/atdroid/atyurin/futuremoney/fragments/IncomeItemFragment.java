package com.atdroid.atyurin.futuremoney.fragments;

import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.Context;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.dao.IncomesDAO;
import com.atdroid.atyurin.futuremoney.serialization.Income;
import com.atdroid.atyurin.futuremoney.utils.DateFormater;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by atdroid on 14.09.2015.
 */

public class IncomeItemFragment extends ListFragment implements AdapterView.OnItemClickListener {
    final static String INCOME_KEY = "key_income";
    final static String LOG_TAG = "IncomeItemFragment";
    private IncomeItemListAdapter mAdapter;
    Income income;
    boolean isNewItem = true;

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

        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mAdapter = new IncomeItemListAdapter();
        mAdapter.addItem(IncomeItemListAdapter.ITEM_NAME);
        mAdapter.addItem(IncomeItemListAdapter.ITEM_AMOUNT);
        mAdapter.addItem(IncomeItemListAdapter.ITEM_TYPE);
        mAdapter.addItem(IncomeItemListAdapter.ITEM_SINGLE_DATE);

        setListAdapter(mAdapter);

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
/*
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        int itemType = mAdapter.getItemViewType(position);
        switch (itemType){
            case AccountItemListAdapter.ITEM_SINGLE_DATE:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
                break;
        }

    }*/
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG,"pos: " + position);
        final int type;
        type = mAdapter.getItemViewType(position);
        Log.d(LOG_TAG,"type: " + type);
        switch (type) {
            case IncomeItemListAdapter.ITEM_NAME:
                EditText editTextName = (EditText) view;
                editTextName.requestFocus();
                if(editTextName.requestFocus()) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                break;
            case IncomeItemListAdapter.ITEM_AMOUNT:
                EditText editTextValue = (EditText) view;
                editTextValue.requestFocus();
                if(editTextValue.requestFocus()) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                break;
//            case IncomeItemListAdapter.ITEM_TYPE:
//                convertView = getTypeView();
//                holder.textView = (TextView) ((ViewGroup) convertView).getChildAt(0);
//                holder.spinner = (Spinner) ((ViewGroup) convertView).getChildAt(1);
//                break;
            case IncomeItemListAdapter.ITEM_SINGLE_DATE:
                final EditText etSingleDate = (EditText) ((ViewGroup) view).getChildAt(1);
                DialogFragment datePicker = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        etSingleDate.setText(sdf.format(calendar.getTime()));
                        income.setSingle_date(calendar);
                    }
                };

                datePicker.show(getFragmentManager(), "datePicker");
                break;
//            case IncomeItemListAdapter.ITEM_BEGIN_DATE:
//                convertView = getDateView(ITEM_BEGIN_DATE);
//                holder.textView = (TextView) ((ViewGroup) convertView).getChildAt(0);
//                holder.editText = (EditText) ((ViewGroup) convertView).getChildAt(1);
//                break;
//            case IncomeItemListAdapter.ITEM_END_DATE:
//                convertView = getDateView(ITEM_END_DATE);
//                holder.textView = (TextView) ((ViewGroup) convertView).getChildAt(0);
//                holder.editText = (EditText) ((ViewGroup) convertView).getChildAt(1);
//                break;
//            case IncomeItemListAdapter.ITEM_PERIOD:
//                convertView = getPeriodView();
//                holder.textView = (TextView) ((ViewGroup) convertView).findViewById(R.id.period_title);
//                holder.spinner = (Spinner) ((ViewGroup) convertView).findViewById(R.id.period_type_spinner);
//                holder.editText = (EditText) ((ViewGroup) convertView).findViewById(R.id.period_value);
//                break;
        }
    }

    public class IncomeItemListAdapter extends BaseAdapter {
        /**
         * position description
         * 0 - name (EditText)
         * 1 - amount (EditText)
         * 2 - type (LinearLayout{Text, Spinner})
         * 3 - single date (Date Picker)
         * 4 - begin date (Date Picker)
         * 5 - end date (Date Picker)
         * 6 - period (LinearLayout{Text, Spinner, EditText(optional - depende on spinner)})
         * */

        private static final int ITEM_NAME = 0;
        private static final int ITEM_AMOUNT = 1;
        private static final int ITEM_TYPE = 2;
        private static final int ITEM_SINGLE_DATE = 3;
        private static final int ITEM_BEGIN_DATE = 4;
        private static final int ITEM_END_DATE = 5;
        private static final int ITEM_PERIOD = 6;
        private static final int ITEM_MAX_COUNT = ITEM_PERIOD + 1;
        Context context = getActivity().getBaseContext();

        private ArrayList<Integer> itemsTypes = new ArrayList<Integer>();
        private LayoutInflater mInflater;

        public IncomeItemListAdapter() {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final int itemType) {
            itemsTypes.add(itemType);
            notifyDataSetChanged();
        }

        public void addUniqItem(final int itemType) {
            boolean isExist = false;
            for (int index = 0; index <  itemsTypes.size(); index++){
                if (itemsTypes.get(index) == itemType){
                    isExist = true;
                }
            }
            if (!isExist) itemsTypes.add(itemType);
            notifyDataSetChanged();
        }

        public void deleteItem(final int itemType){
            for (int index = 0; index <  itemsTypes.size(); index++){
                if (itemsTypes.get(index) == itemType){
                    itemsTypes.remove(index);
                }
            }
        }
//        public void addSeparatorItem(final String item) {
//            itemsTypes.add(item);
//            notifyDataSetChanged();
//        }

        @Override
        public int getItemViewType(int position) {
            return itemsTypes.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return ITEM_MAX_COUNT;
        }

        @Override
        public int getCount() {
            return itemsTypes.size();
        }

        @Override
        public Integer getItem(int position) {
            return itemsTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int type = getItemViewType(position);
            System.out.println("getView " + position + " " + convertView + " type = " + type);
            if (convertView == null) {
                holder = new ViewHolder();
                switch (type) {
                    case ITEM_NAME:
                        convertView = getNameView();
                        holder.editText = (EditText) convertView;
                        break;
                    case ITEM_AMOUNT:
                        convertView = getAmountView();
                        holder.editText = (EditText) convertView;
                        break;
                    case ITEM_TYPE:
                        convertView = getTypeView();
                        holder.textView = (TextView) ((ViewGroup) convertView).getChildAt(0);
                        holder.spinner = (Spinner) ((ViewGroup) convertView).getChildAt(1);
                        break;
                    case ITEM_SINGLE_DATE:
                        convertView = getDateView(ITEM_SINGLE_DATE);
                        holder.textView = (TextView) ((ViewGroup) convertView).getChildAt(0);
                        holder.editText = (EditText) ((ViewGroup) convertView).getChildAt(1);
                        break;
                    case ITEM_BEGIN_DATE:
                        convertView = getDateView(ITEM_BEGIN_DATE);
                        holder.textView = (TextView) ((ViewGroup) convertView).getChildAt(0);
                        holder.editText = (EditText) ((ViewGroup) convertView).getChildAt(1);
                        break;
                    case ITEM_END_DATE:
                        convertView = getDateView(ITEM_END_DATE);
                        holder.textView = (TextView) ((ViewGroup) convertView).getChildAt(0);
                        holder.editText = (EditText) ((ViewGroup) convertView).getChildAt(1);
                        break;
                    case ITEM_PERIOD:
                        convertView = getPeriodView();
                        holder.textView = (TextView) ((ViewGroup) convertView).findViewById(R.id.period_title);
                        holder.spinner = (Spinner) ((ViewGroup) convertView).findViewById(R.id.period_type_spinner);
                        holder.editText = (EditText) ((ViewGroup) convertView).findViewById(R.id.period_value);
                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

        private View getNameView (){
            EditText editText = (EditText) mInflater.inflate(R.layout.list_item_edit_text, null);
            editText.setHint(R.string.budget_item_name);
            if (income.getName().length() > 0){
                editText.setText(income.getName());
            }
            editText.addTextChangedListener(nameTextWatcher);
//            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (!hasFocus) {
//                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                    }
//                }
//            });
            return editText;
        }

        private View getAmountView (){
            EditText editText = (EditText) mInflater.inflate(R.layout.list_item_edit_text, null);
            editText.setRawInputType(Configuration.KEYBOARD_QWERTY);
            editText.setHint(R.string.budget_item_amount);
            if (income.getValue() > 0){
                editText.setText(Double.toString(income.getValue()));
            }
            editText.addTextChangedListener(amountTextWatcher);
//            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (!hasFocus) {
//                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                    }
//                }
//            });
            return editText;
        }

        private View getTypeView(){
            View view = (View) mInflater.inflate(R.layout.list_item_spinner, null);
            TextView textView = (TextView) ((ViewGroup) view).getChildAt(0);
            textView.setText(R.string.budget_item_type);

            Spinner spinner = (Spinner) ((ViewGroup) view).getChildAt(1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                    getResources().getStringArray(R.array.budget_item_types));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(income.getType());
            spinner.setOnItemSelectedListener(typeSelectedListener);
            return view;
        }

        private View getDateView(final int type){
            View view = (View) mInflater.inflate(R.layout.list_item_date_selector, null);
            TextView textView = (TextView) ((ViewGroup) view).getChildAt(0);

            SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
            Calendar currentDateAndTime = Calendar.getInstance();
            currentDateAndTime.setTime(new Date());
            sdf.format(new Date());

            EditText dateEditText = (EditText) ((ViewGroup) view).getChildAt(1);
            dateEditText.setHint(sdf.format(currentDateAndTime.getTime()));

            switch (type){
                case ITEM_SINGLE_DATE:
                    textView.setText(R.string.budget_item_single_date);
                    dateEditText.setText(sdf.format(income.getSingle_date().getTime()));
//                    budget_item.setSingle_date(currentDateAndTime);
                    break;
                case ITEM_BEGIN_DATE:
                    textView.setText(R.string.budget_item_begin_date);
                    dateEditText.setText(sdf.format(income.getBegin_date().getTime()));
//                    budget_item.setBegin_date(currentDateAndTime);
                    break;
                case ITEM_END_DATE:
                    textView.setText(R.string.budget_item_end_date);
                    dateEditText.setText(sdf.format(income.getEnd_date().getTime()));
//                    budget_item.setEnd_date(currentDateAndTime);
                    break;
            }

            dateEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View editText) {
//                    DialogFragment datePicker = new DatePickerFragment() {
//                        @Override
//                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                            SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.set(year, month, day);
//                            ((EditText) editText).setText(sdf.format(calendar.getTime()));
//                            switch (type) {
//                                case ITEM_SINGLE_DATE:
//                                    income.setSingle_date(calendar);
//                                    break;
//                                case ITEM_BEGIN_DATE:
//                                    income.setBegin_date(calendar);
//                                    break;
//                                case ITEM_END_DATE:
//                                    income.setEnd_date(calendar);
//                                    break;
//                            }
//                        }
//                    };
//
//                    datePicker.show(getFragmentManager(), "datePicker");
                }
            });
            return view;
        };

        private View getPeriodView (){
            View view = (View) mInflater.inflate(R.layout.list_item_period, null);
            TextView textView = (TextView) ((ViewGroup) view).findViewById(R.id.period_title);
            textView.setText(R.string.budget_item_period);

//            Spinner spinner = (Spinner) ((ViewGroup) view).getChildAt(1);
            Spinner spinner = (Spinner) ((ViewGroup) view).findViewById(R.id.period_type_spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                    getResources().getStringArray(R.array.budget_item_period_items));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(income.getPeriod_type());
            spinner.setOnItemSelectedListener(periodTypeSelectedListener);

            EditText editText = (EditText) ((ViewGroup) view).findViewById(R.id.period_value);
            editText.setRawInputType(Configuration.KEYBOARD_QWERTY);
            editText.setText(Integer.toString(income.getPeriod_value()));
            editText.addTextChangedListener(periodValueWatcher);
            return view;
        }

        private TextWatcher nameTextWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                income.setName(charSequence.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        private TextWatcher amountTextWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0){
                    income.setValue(Double.valueOf(charSequence.toString()));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        private AdapterView.OnItemSelectedListener typeSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.d("typeSelectedListener", "" + position);
                if (position == Income.TYPE_PERIODICAL){
                    income.setType(Income.TYPE_PERIODICAL);//1
                    mAdapter.deleteItem(ITEM_SINGLE_DATE);
                    mAdapter.addUniqItem(ITEM_BEGIN_DATE);
                    mAdapter.addUniqItem(ITEM_END_DATE);
                    mAdapter.addUniqItem(ITEM_PERIOD);
                    mAdapter.notifyDataSetChanged();
                }else {
                    income.setType(Income.TYPE_SINGLE);//0
                    mAdapter.deleteItem(ITEM_BEGIN_DATE);
                    mAdapter.deleteItem(ITEM_END_DATE);
                    mAdapter.deleteItem(ITEM_PERIOD);
                    mAdapter.addUniqItem(ITEM_SINGLE_DATE);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

    public static class ViewHolder {
        EditText editText;
        TextView textView;
        Spinner spinner;
    }
}
