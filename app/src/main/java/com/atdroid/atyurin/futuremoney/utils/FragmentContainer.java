package com.atdroid.atyurin.futuremoney.utils;

/**
 * Created by atdroid on 16.11.2015.
 */
public class FragmentContainer {
    private static String curentFragment;

    public static String getCurentFragment() {
        return curentFragment;
    }

    public static void setCurentFragment(String curentFragment) {
        FragmentContainer.curentFragment = curentFragment;
    }
}
