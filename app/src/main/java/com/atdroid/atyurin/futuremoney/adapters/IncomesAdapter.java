package com.atdroid.atyurin.futuremoney.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.serialization.Income;
import com.atdroid.atyurin.futuremoney.utils.DateFormater;
import com.atdroid.atyurin.futuremoney.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by atdroid on 08.10.2015.
 */
public class IncomesAdapter extends BaseAdapter {
    ArrayList<Income> incomes;
    LayoutInflater inflater;
    Context context;

    public IncomesAdapter(Context context, ArrayList<Income> incomes) {
        this.incomes = incomes;
        this.context = context;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return incomes.size();
    }

    @Override
    public Object getItem(int position) {
        return incomes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_budget_item, viewGroup, false);
//            view = inflater.inflate(android.R.layout.two_line_list_item, viewGroup, false);
        }
        Income income = incomes.get(position);
        TextView tvName = (TextView) view.findViewById(R.id.tv_budget_item_name);
        TextView tvValue = (TextView) view.findViewById(R.id.tv_budget_item_value);
        TextView tvBeginDate = (TextView) view.findViewById(R.id.tv_budget_item_begin_date);
        TextView tvEndDate = (TextView) view.findViewById(R.id.tv_budget_item_end_date);
        tvName.setText(income.getName());
        tvValue.setText(StringUtil.formatDouble(income.getValue()));
        tvValue.setTextColor(context.getResources().getColor(R.color.income_item_value));
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormater.SHORT_DATE_FORMAT);
        if (income.getType() == Income.TYPE_PERIODICAL){
            tvBeginDate.setText(context.getResources().getString(R.string.budget_item_begin_date_short) + " " + sdf.format(income.getBegin_date().getTime()));
            tvEndDate.setText(context.getResources().getString(R.string.budget_item_end_date_short) + " " + sdf.format(income.getEnd_date().getTime()));
        }else if(income.getType() == Income.TYPE_SINGLE){
            tvBeginDate.setText(sdf.format(income.getSingle_date().getTime()));
            tvEndDate.setText("");
        }
        return view;
    }
}
