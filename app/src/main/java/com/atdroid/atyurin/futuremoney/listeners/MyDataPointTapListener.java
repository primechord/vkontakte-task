package com.atdroid.atyurin.futuremoney.listeners;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.atdroid.atyurin.futuremoney.activity.DateInfoActivity;
import com.atdroid.atyurin.futuremoney.serialization.DateTotal;
import com.atdroid.atyurin.futuremoney.utils.DateFormater;
import com.atdroid.atyurin.futuremoney.utils.TotalsCalculator;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.PendingIntent.getActivity;

/**
 * Created by atyurin on 2/22/2017.
 */

public class MyDataPointTapListener implements OnDataPointTapListener {
    Activity currentActivity;
    TotalsCalculator totalsCalc;

    public MyDataPointTapListener(Activity currentActivity, TotalsCalculator totalsCalc) {
        this.currentActivity = currentActivity;
        this.totalsCalc = totalsCalc;
    }

    @Override
    public void onTap(Series series, DataPointInterface dataPoint) {
        Intent intent = new Intent(currentActivity.getApplicationContext(), DateInfoActivity.class);
        SimpleDateFormat sdf2 = new SimpleDateFormat(DateFormater.DATE_FORMAT);
        Date date = new Date((long) dataPoint.getX());
        Log.d("MyDataPointTapListener", sdf2.format(date));
        DateTotal dateTotal = totalsCalc.getDateTotalsMap().getDateTotal(date);
        Log.d("MyDataPointTapListener", dateTotal.toString());
        Toast.makeText(currentActivity, "Date: "+sdf2.format(date) + ", amount: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();

        intent.putExtra(DateInfoActivity.DATE_TOTALS_EXTRA, dateTotal);
        currentActivity.startActivity(intent);
    }
}
