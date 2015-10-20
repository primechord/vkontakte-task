package com.atdroid.atyurin.futuremoney.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by atdroid on 07.10.2015.
 */
public class DateFormater {
    public static final String DATE_FORMAT = "dd MMMM yyyy";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    public static long formatCalendarToLong(Calendar cal){
//        Date date = cal.getTime();
//        String str = dateFormat.format(date);
        return cal.getTimeInMillis();
    }

    public static Calendar formatLongToCalendar(long longDate){
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(longDate);
            return c;
    }
}
