package com.atdroid.atyurin.futuremoney.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.serialization.Outcome;
import com.atdroid.atyurin.futuremoney.utils.DateFormater;
import com.atdroid.atyurin.futuremoney.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by atdroid on 08.10.2015.
 */
public class OutcomesAdapter extends BaseAdapter {
    ArrayList<Outcome> outcomes;
    LayoutInflater inflater;
    Context context;

    public OutcomesAdapter(Context context, ArrayList<Outcome> outcomes) {
        this.outcomes = outcomes;
        this.context = context;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return outcomes.size();
    }

    @Override
    public Object getItem(int position) {
        return outcomes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        Outcome outcome = outcomes.get(position);
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_budget_item, viewGroup, false);
//            view = inflater.inflate(android.R.layout.two_line_list_item, viewGroup, false);
        }
        TextView tvName = (TextView) view.findViewById(R.id.tv_budget_item_name);
        TextView tvValue = (TextView) view.findViewById(R.id.tv_budget_item_value);
        TextView tvDate = (TextView) view.findViewById(R.id.tv_budget_item_date);
        tvName.setText(outcome.getName());
        tvValue.setText(StringUtil.formatDouble(outcome.getValue()));
        tvValue.setTextColor(context.getResources().getColor(R.color.outcome_item_value));
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.SHORT_DATE_FORMAT);
        if (outcome.getType() == Outcome.TYPE_PERIODICAL){
            tvDate.setText(context.getResources().getString(R.string.budget_item_begin_date_short) + " " + sdf.format(outcome.getBegin_date().getTime()) + " "
                    + context.getResources().getString(R.string.budget_item_end_date_short) + " " + sdf.format(outcome.getEnd_date().getTime()));
        }else if(outcome.getType() == Outcome.TYPE_SINGLE){
            tvDate.setText(sdf.format(outcome.getSingle_date().getTime()));
        }
        return view;
    }
}
