package com.atdroid.atyurin.futuremoney.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.serialization.Income;

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
        TextView tvName = (TextView) view.findViewById(R.id.tv_budget_item_name);
        TextView tvValue = (TextView) view.findViewById(R.id.tv_budget_item_value);
//        TextView tvName = (TextView) view.findViewById(android.R.id.text1);
//        TextView tvValue = (TextView) view.findViewById(android.R.id.text2);
        tvName.setText(incomes.get(position).getName());
        tvValue.setText(Double.toString(incomes.get(position).getValue()));
        tvValue.setTextColor(context.getResources().getColor(R.color.income_item_value));
        return view;
    }
}
