package com.atdroid.atyurin.futuremoney;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.atdroid.atyurin.futuremoney.fragments.AccountsFragment;
import com.atdroid.atyurin.futuremoney.fragments.IncomesFragment;
import com.atdroid.atyurin.futuremoney.fragments.OutcomesFragment;
import com.atdroid.atyurin.futuremoney.fragments.TotalsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;
    FragmentManager fragmentManager = null;
    Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        drawer.setDrawerListener(toggle);
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
//            /*if (!fragment.getClass()
//                    .equals(TotalsFragment.class)){
//                toolbar.setTitle(R.string.title_section_totals);
//                fragment =  TotalsFragment.newInstance(this, fragmentManager);
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment)
//                        .commit();
//            }*/
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        fragmentManager = getFragmentManager();
        fragment =  TotalsFragment.newInstance(this, fragmentManager);
        int id = item.getItemId();

        if (id == R.id.nav_totals) {
            toolbar.setTitle(R.string.title_section_totals);
            fragment =  TotalsFragment.newInstance(this, fragmentManager);
        } else if (id == R.id.nav_accounts) {
            toolbar.setTitle(R.string.title_section_accounts);
            fragment =  AccountsFragment.newInstance(this, fragmentManager);
        } else if (id == R.id.nav_incomes) {
            toolbar.setTitle(R.string.title_section_incomes);
            fragment =  IncomesFragment.newInstance(this, fragmentManager);
        } else if (id == R.id.nav_outcomes) {
            toolbar.setTitle(R.string.title_section_outcomes);
            fragment = OutcomesFragment.newInstance(this, fragmentManager);
        } else if (id == R.id.nav_about) {
            toolbar.setTitle(R.string.title_section_about);
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
