package com.atdroid.atyurin.futuremoney.activity;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.fragments.AboutFragment;
import com.atdroid.atyurin.futuremoney.fragments.AccountItemFragment;
import com.atdroid.atyurin.futuremoney.fragments.AccountsFragment;
import com.atdroid.atyurin.futuremoney.fragments.IncomeItemFragment;
import com.atdroid.atyurin.futuremoney.fragments.IncomesFragmentContainer;
import com.atdroid.atyurin.futuremoney.fragments.OutcomeItemFragment;
import com.atdroid.atyurin.futuremoney.fragments.OutcomesFragmentContainer;
import com.atdroid.atyurin.futuremoney.fragments.TotalsFragment;
import com.atdroid.atyurin.futuremoney.utils.BackupAgent;
import com.atdroid.atyurin.futuremoney.utils.FragmentContainer;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final static String LOG_TAG = "MainActivity";
    Fragment fragment = null;
    FragmentManager fragmentManager = null;
    Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BackupAgent.requestBackup(this);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //show totals fragment then activity created
        fragmentManager = getSupportFragmentManager();
        toolbar.setTitle(R.string.title_section_totals);
        fragment =  TotalsFragment.newInstance(this, fragmentManager);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
//                this.onDrawerOpened(drawerView);
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        };
        drawer.setDrawerListener((DrawerLayout.DrawerListener) toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//          Log.d(LOG_TAG, "CurFrag: '" + FragmentContainer.getCurentFragment() + "', \nIncomeItemFragment.class: '" + IncomeItemFragment.class.toString() + "'");
            if (FragmentContainer.getCurentFragment().equals(IncomeItemFragment.class.toString())){
                toolbar.setTitle(R.string.title_section_incomes);
                fragment =  IncomesFragmentContainer.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                return;
            } else if (FragmentContainer.getCurentFragment().equals(OutcomeItemFragment.class.toString())){
                toolbar.setTitle(R.string.title_section_outcomes);
                fragment = OutcomesFragmentContainer.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                return;
            }else if (FragmentContainer.getCurentFragment().equals(AccountItemFragment.class.toString())){
                toolbar.setTitle(R.string.title_section_accounts);
                fragment =  AccountsFragment.newInstance(this, fragmentManager);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                return;
            }/*else if (!FragmentContainer.getCurentFragment().equals(TotalsFragment.class.toString())){
                toolbar.setTitle(R.string.title_section_totals);
                fragment = TotalsFragment.newInstance(this, fragmentManager);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }*/
            else{
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();

        if (id == R.id.nav_totals) {
            toolbar.setTitle(R.string.title_section_totals);
            fragment = TotalsFragment.newInstance(this, fragmentManager);
        } else if (id == R.id.nav_accounts) {
            toolbar.setTitle(R.string.title_section_accounts);
            fragment = AccountsFragment.newInstance(this, fragmentManager);
        } else if (id == R.id.nav_incomes) {
            toolbar.setTitle(R.string.title_section_incomes);
            fragment = IncomesFragmentContainer.newInstance();
        } else if (id == R.id.nav_outcomes) {
            toolbar.setTitle(R.string.title_section_outcomes);
            fragment = OutcomesFragmentContainer.newInstance();
        } else if (id == R.id.nav_about) {
            toolbar.setTitle(R.string.title_section_about);
            fragment = AboutFragment.newInstance();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
