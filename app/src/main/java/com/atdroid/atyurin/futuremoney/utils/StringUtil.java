package com.atdroid.atyurin.futuremoney.utils;

import java.text.DecimalFormat;

/**
 * Created by atdroid on 18.11.2015.
 */
public class StringUtil {
    public static String formatDouble(Double value){
        String result= "";
        DecimalFormat df = new DecimalFormat("#,###.##");
        df.setDecimalSeparatorAlwaysShown(true);
        DecimalFormat dfnd = new DecimalFormat("#,###");
        if(value % 1 == 0){
            result = dfnd.format((Number) value);
        }else{
            result = df.format((Number) value);
        }
        return result;
    }
}
