package com.atdroid.atyurin.futuremoney.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by atdroid on 14-Nov-15.
 */
public class KeyboardManager {
    Activity actitvity;

    public KeyboardManager(Fragment fragment) {
        this.actitvity = fragment.getActivity();
    }

    public void closeKeyboard(){
        View view = actitvity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) actitvity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
