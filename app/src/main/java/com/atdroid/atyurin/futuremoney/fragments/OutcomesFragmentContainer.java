package com.atdroid.atyurin.futuremoney.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.utils.FragmentContainer;
import com.atdroid.atyurin.futuremoney.utils.KeyboardManager;

/**
 * Created by atdroid on 10.11.2015.
 */
public class OutcomesFragmentContainer extends Fragment {
    final static String LOG_TAG = "IncomesFragmentConta";
    public static OutcomesFragmentContainer newInstance(){
        return new OutcomesFragmentContainer();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentContainer.setCurentFragment(this.getClass().toString());
        new KeyboardManager(this).closeKeyboard();
        View rootView = inflater.inflate(R.layout.budget_item_view_pager, container , false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOutcome();
            }
        });
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        final ViewPager pager = (ViewPager) view.findViewById(R.id.view_pager);
        final PagerAdapter pagerAdapter = new OutcomeFragmentPagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(1);

    }

    public void addOutcome(){
        Log.e(LOG_TAG, "getFragmentManager: " + getFragmentManager().toString());
        getFragmentManager().beginTransaction()
                .replace(R.id.container, OutcomeItemFragment.newInstance())
                .commit();
    }

    public class OutcomeFragmentPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = getResources().getStringArray(R.array.budget_item_tabs);

        public OutcomeFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(LOG_TAG, "OutcomeFragmentPagerAdapter - getItem");
            return OutcomesFragment.newInstance(position, getFragmentManager());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }
}
