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
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_budget_items, container, false);
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

/*    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        Log.d("IncomesFragment", "onCreateOptionsMenu");
        inflater.inflate(R.menu.budget_item, menu);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        Log.d("Main Activity", "onPrepareOptionsMenu");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Incomer fragment", "onOptionsItemSelected");
        if (item.getItemId() == R.id.action_btn_add_item) {
            addIncome();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void addIncome(){
        fragmentManager.beginTransaction()
                .replace(R.id.container, IncomeItemFragment.newInstance())
                .commit();
    }

    public void updateIncome(Income income){
//        Bundle args = new Bundle();
//        args.putSerializable(IncomeItemFragment.INCOME_KEY, budget_item);
        fragmentManager.beginTransaction()
                .replace(R.id.container, IncomeItemFragment.newInstance(income))
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
