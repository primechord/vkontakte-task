package com.atdroid.atyurin.futuremoney.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.serialization.Total;
import com.atdroid.atyurin.futuremoney.utils.DateFormater;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by atdroid on 14.09.2015.
 */

public class TotalsFragment extends Fragment{
    final static String INCOME_KEY = "key_income";
    final static String LOG_TAG = "TotalsFragment";
    private TotalsListAdapter mAdapter;
    Total total;
    FragmentManager fragmentManager;
    Activity activity;

    public static TotalsFragment newInstance(Activity activity, FragmentManager fragmentManager) {
        Log.d(LOG_TAG, "newInstance");
        TotalsFragment totalsFragment = new TotalsFragment();
        totalsFragment.activity = activity;
        totalsFragment.fragmentManager = fragmentManager;

        return totalsFragment;
    }

    public TotalsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);//switch off menu for fragment
        total = new Total();
        View rootView = inflater.inflate(R.layout.fragment_totals, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.lv_totals);
        mAdapter = new TotalsListAdapter();
        mAdapter.addItem(TotalsListAdapter.ITEM_BEGIN_TYPE);
        mAdapter.addItem(TotalsListAdapter.ITEM_END_DATE);
        lv.setAdapter(mAdapter);

        Button btnCalc = (Button) rootView.findViewById(R.id.btn_calc_totals);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcTotals();
            }
        });
        return rootView;

    }

    private void calcTotals(){

    }

    public class TotalsListAdapter extends BaseAdapter {
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

        private static final int ITEM_BEGIN_TYPE = 0;
        private static final int ITEM_BEGIN_DATE = 1;
        private static final int ITEM_END_DATE = 2;
        private static final int ITEM_MAX_COUNT = ITEM_END_DATE + 1;
        Context context = getActivity().getBaseContext();

        private ArrayList<Integer> itemsTypes = new ArrayList<Integer>();
        private LayoutInflater mInflater;

        public TotalsListAdapter() {
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
                    case ITEM_BEGIN_TYPE:
                        convertView = getBeginTypeView();
                        holder.textView = (TextView) ((ViewGroup) convertView).getChildAt(0);
                        holder.spinner = (Spinner) ((ViewGroup) convertView).getChildAt(1);
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

                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

        private View getBeginTypeView(){
            View view = (View) mInflater.inflate(R.layout.list_item_spinner, null);
            TextView textView = (TextView) ((ViewGroup) view).getChildAt(0);
            textView.setText(R.string.total_begin_type);

            Spinner spinner = (Spinner) ((ViewGroup) view).getChildAt(1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                    getResources().getStringArray(R.array.total_begin_types));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(0);
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
                case ITEM_BEGIN_DATE:
                    textView.setText(R.string.budget_item_begin_date);
                    dateEditText.setText(sdf.format(total.getBegin_date().getTime()));
//                    budget_item.setBegin_date(currentDateAndTime);
                    break;
                case ITEM_END_DATE:
                    textView.setText(R.string.budget_item_end_date);
                    dateEditText.setText(sdf.format(total.getEnd_date().getTime()));
//                    budget_item.setEnd_date(currentDateAndTime);
                    break;
            }

            dateEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View editText) {
                    Calendar calendar = Calendar.getInstance();
                    DialogFragment datePicker = new DatePickerFragment(calendar) {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, day);
                            ((EditText) editText).setText(sdf.format(calendar.getTime()));
                            switch (type) {
                                case ITEM_BEGIN_DATE:
                                    total.setBegin_date(calendar);
                                    break;
                                case ITEM_END_DATE:
                                    total.setEnd_date(calendar);
                                    Log.d(LOG_TAG, sdf.format(calendar.getTimeInMillis()) + ";" + calendar.getTimeInMillis());
                                    break;
                            }
                        }
                    };

                    datePicker.show(getFragmentManager(), "datePicker");
                }
            });
            return view;
        };

        private AdapterView.OnItemSelectedListener typeSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.d("typeSelectedListener", "" + position);
                if (position == Total.TYPE_ALL){
                    Calendar c = Calendar.getInstance();
                    c.set(1, 1, 1);
                    total.setBegin_date(c);
                    mAdapter.deleteItem(ITEM_BEGIN_DATE);
                    mAdapter.notifyDataSetChanged();
                }else {
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(System.currentTimeMillis());
                    total.setBegin_date(c);
                    mAdapter.deleteItem(ITEM_END_DATE);
                    mAdapter.addUniqItem(ITEM_BEGIN_DATE);
                    mAdapter.addUniqItem(ITEM_END_DATE);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

    }

    public static class ViewHolder {
        EditText editText;
        TextView textView;
        Spinner spinner;
    }

    public class CalculateTotalsTask extends AsyncTask<Void,Void,Void>{
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
