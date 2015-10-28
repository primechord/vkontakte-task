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
import android.widget.Toast;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.adapters.OutcomesAdapter;
import com.atdroid.atyurin.futuremoney.dao.OutcomesDAO;
import com.atdroid.atyurin.futuremoney.serialization.Outcome;

import java.util.ArrayList;

/**
 * Created by atdroid on 14.09.2015.
 */

public class OutcomesFragment extends Fragment {
    Activity activity;
    FragmentManager fragmentManager;
    ArrayList<Outcome> outcomes;
    OutcomesAdapter adapter;
    OutcomesDAO dao;
    final static String LOG_TAG = "OutcomesFragment";
    public static OutcomesFragment newInstance(Activity activity, FragmentManager fragmentManager) {
        Log.d(LOG_TAG, "newInstance");

        OutcomesFragment outcomesFragment = new OutcomesFragment();
        outcomesFragment.activity = activity;
        outcomesFragment.fragmentManager = fragmentManager;


        return outcomesFragment;
    }

    public OutcomesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);//switch on menu for fragment
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_budget_items_list, container, false);
        ListView lvOutcomes = (ListView) rootView.findViewById(R.id.lv_budget_items);
        dao = new OutcomesDAO(activity.getBaseContext());
        dao.openReadable();
        outcomes = dao.getAllOutcomes();
        dao.close();
        adapter = new OutcomesAdapter(activity.getBaseContext(), outcomes);
        lvOutcomes.setAdapter(adapter);
        lvOutcomes.setOnItemClickListener(itemClickListener);
        registerForContextMenu(lvOutcomes);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOutcome();
            }
        });

        return rootView;

    }

/*    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
//        Log.d(LOG_TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.budget_item, menu);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        Log.d("Main Activity", "onPrepareOptionsMenu");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.d(LOG_TAG, "onOptionsItemSelected");
        if (item.getItemId() == R.id.action_btn_add_item) {
            addOutcome();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void addOutcome(){
        fragmentManager.beginTransaction()
                .replace(R.id.container, OutcomeItemFragment.newInstance())
                .commit();
    }

    public void updateOutcome(Outcome outcome){
//        Bundle args = new Bundle();
//        args.putSerializable(IncomeItemFragment.INCOME_KEY, budget_item);
        fragmentManager.beginTransaction()
                .replace(R.id.container, OutcomeItemFragment.newInstance(outcome))
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
            updateOutcome(outcomes.get(position));
        }
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.menu_delete_budget_item:
                Log.d(LOG_TAG, outcomes.get(info.position).getName());
                dao.openWritable();
                dao.deleteOutcome(outcomes.get(info.position));
                dao.close();
                outcomes.remove(info.position);
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.msg_budget_item_success), Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
