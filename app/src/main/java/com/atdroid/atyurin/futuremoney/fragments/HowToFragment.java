package com.atdroid.atyurin.futuremoney.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atdroid.atyurin.futuremoney.R;

/**
 * Created by atdroid on 14-Nov-15.
 */
public class HowToFragment extends Fragment {
    public static HowToFragment newInstance(){
        HowToFragment howToFragment = new HowToFragment();
        return howToFragment;
    }

    public HowToFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_how_to, container,false);
        return  rootView;
    }
}
