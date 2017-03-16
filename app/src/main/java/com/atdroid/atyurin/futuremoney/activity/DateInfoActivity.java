package com.atdroid.atyurin.futuremoney.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.serialization.DateTotal;
import com.atdroid.atyurin.futuremoney.utils.DateFormater;

import java.text.SimpleDateFormat;

/**
 * Created by atyurin on 2/27/2017.
 */

public class DateInfoActivity extends Activity {
    DateTotal dateTotal;
    TextView tvDateHeader;
    TextView tvContent;
    public static final String DATE_TOTALS_EXTRA = "date_totals";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dateinfo);

        Intent intent = getIntent();
        Log.d("DateInfoActivity", intent.toString());
        dateTotal = (DateTotal) intent.getSerializableExtra(DATE_TOTALS_EXTRA);
        Log.d("DateInfoActivity", dateTotal.toString());
        tvDateHeader = (TextView) findViewById(R.id.tv_date_header);
        tvContent = (TextView) findViewById(R.id.tv_date_content);

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.DATE_FORMAT);
        tvDateHeader.setText(sdf.format(dateTotal.getDate()));
        tvContent.setText(dateTotal.toString());
    }
}
