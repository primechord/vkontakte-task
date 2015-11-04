package com.atdroid.atyurin.futuremoney.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.adapters.IncomesAdapter;
import com.atdroid.atyurin.futuremoney.dao.IncomesDAO;
import com.atdroid.atyurin.futuremoney.serialization.Income;

import java.util.ArrayList;

/**
 * Created by atdroid on 14.09.2015.
 */

public class IncomesFragment extends Fragment {
    Activity activity;
    FragmentManager fragmentManager;
    ArrayList<Income> incomes;
    IncomesAdapter adapter;
    IncomesDAO dao;
    View rootView;
    final static String LOG_TAG = "IncomesFragment";
    public static IncomesFragment newInstance(Activity activity, FragmentManager fragmentManager) {
        Log.d("Incomer fragment", "newInstance");

        IncomesFragment incomesFragment = new IncomesFragment();
        incomesFragment.activity = activity;
        incomesFragment.fragmentManager = fragmentManager;


        return incomesFragment;
    }

    public IncomesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);//switch on menu for fragment

        rootView = inflater.inflate(R.layout.fragment_budget_items_list, container, false);
        ListView lvIncomes = (ListView) rootView.findViewById(R.id.lv_budget_items);
        dao = new IncomesDAO(activity.getBaseContext());
        dao.openReadable();
        incomes = dao.getAllIncomes();
        dao.close();
        adapter = new IncomesAdapter(activity.getBaseContext(), incomes);
        lvIncomes.setAdapter(adapter);
        lvIncomes.setOnItemClickListener(itemClickListener);
        registerForContextMenu(lvIncomes);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIncome();
            }
        });

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvNoDataFound = (TextView) rootView.findViewById(R.id.tv_no_data_found);
        View vDevider = (View) rootView.findViewById(R.id.view_devider);
        if (adapter.getCount() == 0){
            tvNoDataFound.setVisibility(View.VISIBLE);
            vDevider.setVisibility(View.GONE);
        } else {
            tvNoDataFound.setVisibility(View.GONE);
            vDevider.setVisibility(View.VISIBLE);
        }
    }

    public void addIncome(){
        fragmentManager.beginTransaction()
                .replace(R.id.container, IncomeItemFragment.newInstance())
                .addToBackStack(this.getClass().toString())
                .commit();
    }

    public void updateIncome(Income income){
//        Bundle args = new Bundle();
//        args.putSerializable(IncomeItemFragment.INCOME_KEY, budget_item);
        fragmentManager.beginTransaction()
                .replace(R.id.container, IncomeItemFragment.newInstance(income))
                .addToBackStack(this.getClass().toString())
                .commit();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.lv_budget_items) {
            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.menu_budget_item_list_context, menu);
        }
    }

    public AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            updateIncome(incomes.get(position));
        }
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.menu_delete_budget_item:
                Log.d("e", incomes.get(info.position).getName());
                dao.openWritable();
                dao.deleteIncome(incomes.get(info.position));
                dao.close();
                incomes.remove(info.position);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
