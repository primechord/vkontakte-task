package com.atdroid.atyurin.futuremoney.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.utils.FragmentContainer;

/**
 * Created by atdroid on 14-Nov-15.
 */
public class AboutFragment extends Fragment {
    public static androidx.fragment.app.Fragment newInstance(){
        AboutFragment aboutFragment = new AboutFragment();
        return aboutFragment;
    }

    public AboutFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container,false);
        FragmentContainer.setCurentFragment(this.getClass().toString());
        TextView tvHowTo = (TextView) rootView.findViewById(R.id.tv_how_to_description);
        tvHowTo.setText(Html.fromHtml(getString(R.string.about_description)));
        return  rootView;
    }
}
