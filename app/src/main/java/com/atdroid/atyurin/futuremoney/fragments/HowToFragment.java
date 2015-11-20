package com.atdroid.atyurin.futuremoney.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.utils.FragmentContainer;

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
        FragmentContainer.setCurentFragment(this.getClass().toString());
        TextView tvHowTo = (TextView) rootView.findViewById(R.id.tv_how_to_description);
        tvHowTo.setText(Html.fromHtml(getString(R.string.how_to_description)));
        return  rootView;
    }
}
