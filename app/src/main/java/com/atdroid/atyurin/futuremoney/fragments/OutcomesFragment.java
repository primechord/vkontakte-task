package com.atdroid.atyurin.futuremoney.fragments;

import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.adapters.OutcomesAdapter;
import com.atdroid.atyurin.futuremoney.dao.OutcomesDAO;
import com.atdroid.atyurin.futuremoney.serialization.Outcome;

import java.util.ArrayList;

/**
 * Created by atdroid on 14.09.2015.
 */

public class OutcomesFragment extends Fragment {
    ArrayList<Outcome> outcomes;
    OutcomesAdapter adapter;
    OutcomesDAO dao;
    View rootView;
    int pageType;
    ViewGroup container;
    FragmentManager fragmentManager;
    final static String LOG_TAG = "IncomesFragment";
    static final String ARGUMENT_PAGE_TYPE = "arg_page_type";

    public static OutcomesFragment newInstance(final int type, FragmentManager fragmentManager) {
        Log.d(LOG_TAG, "newInstance");

        OutcomesFragment outcomesFragment = new OutcomesFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_TYPE, type);
        outcomesFragment.setArguments(arguments);
        outcomesFragment.fragmentManager = fragmentManager;
        return outcomesFragment;
    }

    public OutcomesFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageType = getArguments().getInt(ARGUMENT_PAGE_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);//switch on menu for fragment
        outcomes = new ArrayList<>();
        Log.d(LOG_TAG, "onCreateView");
        this.container = container;
        rootView = inflater.inflate(R.layout.fragment_budget_items_list, container, false);
        ListView lvIncomes = (ListView) rootView.findViewById(R.id.lv_budget_items);
        loadData();
        adapter = new OutcomesAdapter(getActivity().getBaseContext(), outcomes);
        lvIncomes.setAdapter(adapter);
        lvIncomes.setOnItemClickListener(itemClickListener);
        registerForContextMenu(lvIncomes);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(LOG_TAG, "onViewCreated");
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

    public void updateIncome(Outcome outcome){
//        Bundle args = new Bundle();
//        args.putSerializable(IncomeItemFragment.INCOME_KEY, budget_item);
        Log.e(LOG_TAG, "getChildFragmentManager: " + getChildFragmentManager().toString());
        fragmentManager.beginTransaction()
                .replace(R.id.container, OutcomeItemFragment.newInstance(outcome))
                .commit();
    }

    public void updateDisplay(){
        loadData();
        adapter.notifyDataSetChanged();
    }

    public void loadData(){
        dao = new OutcomesDAO(getActivity().getBaseContext());
        dao.openReadable();
        if (pageType == 0){
            outcomes = dao.getOutcomesWithType(Outcome.TYPE_PERIODICAL);
        }else if (pageType == 1){
            outcomes = dao.getAllOutcomes();
        }else if (pageType == 2){
            outcomes = dao.getOutcomesWithType(Outcome.TYPE_SINGLE);
        }
        dao.close();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.lv_budget_items) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_budget_item_list_context, menu);
        }
    }

    public AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            updateIncome(outcomes.get(position));
        }
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.menu_delete_budget_item:
                dao.openWritable();
                dao.deleteOutcome(outcomes.get(info.position));
                dao.close();
                outcomes.remove(info.position);
                adapter.notifyDataSetChanged();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, OutcomesFragmentContainer.newInstance())
                        .commit();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
