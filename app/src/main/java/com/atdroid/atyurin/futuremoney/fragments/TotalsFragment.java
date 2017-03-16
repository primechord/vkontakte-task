package com.atdroid.atyurin.futuremoney.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.dao.AccountsDAO;
import com.atdroid.atyurin.futuremoney.dao.IncomesDAO;
import com.atdroid.atyurin.futuremoney.dao.OutcomesDAO;
import com.atdroid.atyurin.futuremoney.listeners.MyDataPointTapListener;
import com.atdroid.atyurin.futuremoney.serialization.Account;
import com.atdroid.atyurin.futuremoney.serialization.DateTotal;
import com.atdroid.atyurin.futuremoney.serialization.Income;
import com.atdroid.atyurin.futuremoney.serialization.Outcome;
import com.atdroid.atyurin.futuremoney.serialization.Total;
import com.atdroid.atyurin.futuremoney.utils.DateFormater;
import com.atdroid.atyurin.futuremoney.utils.FragmentContainer;
import com.atdroid.atyurin.futuremoney.utils.KeyboardManager;
import com.atdroid.atyurin.futuremoney.utils.StringUtil;
import com.atdroid.atyurin.futuremoney.utils.TotalsCalculator;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

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
    final static String LOG_TAG = "TotalsFragment";
    Total total;
    View rootView;
    Spinner spType;
    TextView tvCalculateDateValue,tvCalculateDateTitle, tvBeginDateTitle, tvBeginDateValue, tvAccountsTotalValue, tvOutcomesTotalValue, tvIncomesTotalValue, tvTotalValue;
    ArrayAdapter<String> adapterType;
    LinearLayout llTotalsLayout;
    RelativeLayout llCalculateDate, llBeginDate;
    Activity activity;
    FragmentManager fragmentManager;
    GraphView graph;
    LineGraphSeries<DataPoint> dataSeries;
    DataPoint[] dataPoints;

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
        setHasOptionsMenu(true);
        FragmentContainer.setCurentFragment(this.getClass().toString());
        new KeyboardManager(this).closeKeyboard();
        rootView =  inflater.inflate(R.layout.fragment_totals, container, false);
        //begin date type
        spType = (Spinner) rootView.findViewById(R.id.sp_begin_date_type);
        adapterType = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.total_begin_types));
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapterType);
        spType.setSelection(total.getBeginDateType());
        adapterType.notifyDataSetChanged();
        spType.setOnItemSelectedListener(typeSelectedListener);

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
        //begin date
        llBeginDate = (RelativeLayout) rootView.findViewById(R.id.ll_begin_date);
        llBeginDate.setOnClickListener(beginDateListener);
        tvBeginDateTitle = (TextView) llBeginDate.findViewById(R.id.tv_date_title);
        tvBeginDateTitle.setText(R.string.total_begin_date_title);
        tvBeginDateValue = (TextView) llBeginDate.findViewById(R.id.tv_date_value);
        tvBeginDateValue.setText(sdf.format(total.getBegin_date().getTime()));

        //calculate date

        llCalculateDate = (RelativeLayout) rootView.findViewById(R.id.ll_calculate_date);
        llCalculateDate.setOnClickListener(endDateListener);
        tvCalculateDateTitle = (TextView) llCalculateDate.findViewById(R.id.tv_date_title);
        tvCalculateDateTitle.setText(R.string.total_calculate_date_title);
        tvCalculateDateValue = (TextView) llCalculateDate.findViewById(R.id.tv_date_value);
        tvCalculateDateValue.setText(sdf.format(total.getEnd_date().getTime()));

        //graph view totals
        graph = (GraphView) rootView.findViewById(R.id.graph_view_totals);
        graph.getGridLabelRenderer().setNumHorizontalLabels(2); // only 4 because of the space

        /*Button btnCalculate = (Button) rootView.findViewById(R.id.btn_calc_totals);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcTotals();
            }
        });*/
        return rootView;

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        Log.d("TotalsFragme", "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_totals, menu);
    }
    public void onPrepareOptionsMenu(Menu menu) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("TotalsFragme", "onOptionsItemSelected");
        if (item.getItemId() == R.id.action_btn_calc_totals) {
            calcTotals();
        }

        return super.onOptionsItemSelected(item);
    }
    private AdapterView.OnItemSelectedListener typeSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            Log.d("typeSelectedListener", "" + position);
            if (position == Total.TYPE_ALL){
                total.setBeginDateType(Total.TYPE_ALL);
                llBeginDate.setVisibility(View.GONE);
            }else if (position == Total.TYPE_SELECTED_DATE) {
                total.setBeginDateType(Total.TYPE_SELECTED_DATE);
                SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                tvBeginDateValue.setText(sdf.format(calendar.getTime()));
                llBeginDate.setVisibility(View.VISIBLE);
                tvBeginDateValue.setText(sdf.format(calendar.getTime()));
                total.setBegin_date(calendar);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    public View.OnClickListener beginDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerFragment datePicker = new DatePickerFragment() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    tvBeginDateValue.setText(sdf.format(calendar.getTime()));
                    total.setBegin_date(calendar);
                }
            };
            datePicker.setCalendar(total.getBegin_date());
            datePicker.show(getFragmentManager(), "datePicker");
        }
    };
    public View.OnClickListener endDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerFragment datePicker = new DatePickerFragment() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    tvCalculateDateValue.setText(sdf.format(calendar.getTime()));
                    total.setEnd_date(calendar);
                }
            };
            datePicker.setCalendar(total.getEnd_date());
            datePicker.show(getFragmentManager(), "datePicker");
//            new CloseKeyboard().execute(datePicker);

        }
    };
    public void calcTotals(){
        new CalculateTotalsTask(getActivity()).execute();
    }

    public class CalculateTotalsTask extends AsyncTask<Void,Void, TotalsCalculator> {
        ProgressDialog progressDialog;

        public CalculateTotalsTask(Activity activity) {
            this.progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(LOG_TAG, "CalculateTotalsTask");
            progressDialog.setMessage("Start");
            progressDialog.show();
        }

        @Override
        protected TotalsCalculator doInBackground(Void... params) {
            Calendar begin = total.getBegin_date();
            Calendar end= total.getEnd_date();
            //incomes
            IncomesDAO incomesDAO = new IncomesDAO(getActivity().getBaseContext());
            incomesDAO.openReadable();
            ArrayList<Income> singleIncomes = incomesDAO.getIncomesInPeriodWithType(Income.TYPE_SINGLE, total.getBegin_date(), total.getEnd_date());
            ArrayList<Income> periodicalIncomes = incomesDAO.getIncomesInPeriodWithType(Income.TYPE_PERIODICAL, total.getBegin_date(), total.getEnd_date());
            incomesDAO.close();

            OutcomesDAO outcomesDAO = new OutcomesDAO(getActivity().getBaseContext());
            outcomesDAO.openReadable();
            ArrayList<Outcome> singleOutcomes = outcomesDAO.getOutcomesInPeriodWithType(Outcome.TYPE_SINGLE, total.getBegin_date(), total.getEnd_date());
            ArrayList<Outcome> periodicalOutcomes = outcomesDAO.getOutcomesInPeriodWithType(Outcome.TYPE_PERIODICAL, total.getBegin_date(), total.getEnd_date());
            incomesDAO.close();

            AccountsDAO accountsDAO = new AccountsDAO(getActivity().getBaseContext());
            accountsDAO.openReadable();
            ArrayList<Account> accounts = accountsDAO.getAllAccounts();
            accountsDAO.close();

            TotalsCalculator totalsCalc = new TotalsCalculator(total);
            totalsCalc.setSingleIncomes(singleIncomes);
            totalsCalc.setPeriodicalIncomes(periodicalIncomes);
            totalsCalc.setSingleOutcomes(singleOutcomes);
            totalsCalc.setPeriodicalOutcomes(periodicalOutcomes);
            totalsCalc.setAccounts(accounts);
            totalsCalc.calculateTotals();
            total.setAccountsAmount(totalsCalc.getAccountsAmount());
            total.setIncomeAmount(totalsCalc.getIncomesAmount());
            total.setOutcomeAmount(totalsCalc.getOutcomesAmount());
            total.setTotalAmount(totalsCalc.getTotalAmount());
            dataPoints = new DataPoint[totalsCalc.getDateTotalsMap().getSize()];
            int i = 0;
            double currentAmount = totalsCalc.getAccountsAmount();
            for (Map.Entry<Date,DateTotal> dateTotal : totalsCalc.getDateTotalsMap().getSortedDateTotalsMap().entrySet()){
                Log.d(LOG_TAG, dateTotal.getKey().toString() + " ---- " + dateTotal.getValue().toString());
                dataPoints[i] = new DataPoint(dateTotal.getKey(), dateTotal.getValue().getDateTotalValue() + currentAmount);
                currentAmount +=  dateTotal.getValue().getDateTotalValue();
                i++;
            }
            Log.w(LOG_TAG, dataPoints.toString());
            dataSeries = new LineGraphSeries<DataPoint>(dataPoints);


            return totalsCalc;
        }

        @Override
        protected void onPostExecute(TotalsCalculator totalsCalc) {
            super.onPostExecute(totalsCalc);
            llTotalsLayout = (LinearLayout) rootView.findViewById(R.id.ll_totals_values);
            tvAccountsTotalValue = (TextView) rootView.findViewById(R.id.tv_accounts_total_value);
            tvAccountsTotalValue.setText(StringUtil.formatDouble(total.getAccountsAmount()));
            tvIncomesTotalValue = (TextView) rootView.findViewById(R.id.tv_incomes_total_value);
            tvIncomesTotalValue.setText(StringUtil.formatDouble(total.getIncomeAmount()));
            tvOutcomesTotalValue = (TextView) rootView.findViewById(R.id.tv_outcomes_total_value);
            tvOutcomesTotalValue.setText(StringUtil.formatDouble(total.getOutcomeAmount()));
            tvTotalValue = (TextView) rootView.findViewById(R.id.tv_total_value);
            if (total.getTotalAmount() > 0){
                tvTotalValue.setTextColor(getResources().getColor(R.color.income_item_value));
            }else {
                tvTotalValue.setTextColor(getResources().getColor(R.color.outcome_item_value));
            }
            tvTotalValue.setText(StringUtil.formatDouble(total.getTotalAmount()));


            dataSeries.setOnDataPointTapListener(new MyDataPointTapListener(getActivity(), totalsCalc));
            dataSeries.setDrawDataPoints(true);
            Log.w(LOG_TAG, dataSeries.toString());
            Log.w(LOG_TAG, graph.toString());
            graph.removeAllSeries();
            graph.destroyDrawingCache();
            graph.addSeries(dataSeries);
            // set date label formatter
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
            // as we use dates as labels, the human rounding to nice readable numbers
            // is not necessary
            graph.getGridLabelRenderer().setHumanRounding(false);
            //graph.getGridLabelRenderer(). нужно ограничить число линиий по оси x = 3
            if (dataPoints.length > 0) {
                double minY = dataPoints[0].getY();
                double maxY = dataPoints[0].getY();
                for (int i= 0; i < dataPoints.length; i++){
                    if (minY > dataPoints[i].getY()){
                        minY = dataPoints[i].getY();
                    }
                    if (maxY < dataPoints[i].getY()){
                        maxY = dataPoints[i].getY();
                    }
                }
                // set manual x bounds to have nice steps
                graph.getViewport().setMinX(dataPoints[0].getX());

                SimpleDateFormat sdf2 = new SimpleDateFormat(DateFormater.DATE_FORMAT);
                Log.d(LOG_TAG, sdf2.format(dataPoints[0].getX()));
                graph.getViewport().setMaxX(dataPoints[dataPoints.length - 1].getX());
                Log.d(LOG_TAG, sdf2.format(dataPoints[dataPoints.length - 1].getX()));
                graph.getViewport().setMinY(minY - 10);
                graph.getViewport().setMaxY(maxY + 10);
                graph.getViewport().setXAxisBoundsManual(true);

                // activate scaling and zooming
                // activate horizontal zooming and scrolling
                graph.getViewport().setScalable(true);

                // activate horizontal scrolling
                graph.getViewport().setScrollable(true);

                // activate horizontal and vertical zooming and scrolling
                graph.getViewport().setScalableY(true);

                // activate vertical scrolling
                graph.getViewport().setScrollableY(true);
                graph.setVisibility(View.VISIBLE);
                //graph.getGridLabelRenderer().setGridColor();
            }else{
                Toast.makeText(getActivity().getBaseContext(), R.string.msg_no_data_found, Toast.LENGTH_SHORT).show();
                graph.setVisibility(View.GONE);
            }

            llTotalsLayout.setVisibility(View.VISIBLE);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Log.d(LOG_TAG, "Total Amt: " + Double.toString(total.getTotalAmount()));


        }
    }
}