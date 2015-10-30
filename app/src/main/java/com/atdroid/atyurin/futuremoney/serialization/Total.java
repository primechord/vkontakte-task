package com.atdroid.atyurin.futuremoney.serialization;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by atdroid on 13.09.2015.
 */
public class Total implements Serializable {
    public static final int TYPE_ALL = 0;
    public static final int TYPE_SELECTED_DATE = 1;

    private int beginDateType;
    private double summ = 0;
    private Calendar begin_date = Calendar.getInstance();
    private Calendar end_date = Calendar.getInstance();

    public Total() {
        System.out.println(begin_date.getActualMinimum(Calendar.YEAR));
        begin_date.set(1,1,1);
        end_date.setTimeInMillis(System.currentTimeMillis());
        beginDateType = TYPE_ALL;

    }

    public int getBeginDateType() {
        return beginDateType;
    }

    public void setBeginDateType(int beginDateType) {
        this.beginDateType = beginDateType;
    }

    public double getSumm() {
        return summ;
    }

    public void setSumm(double summ) {
        this.summ = summ;
    }

    public Calendar getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(Calendar begin_date) {
        this.begin_date = begin_date;
    }

    public Calendar getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Calendar end_date) {
        this.end_date = end_date;
    }

    @Override
    public String toString() {
        return "Total{" +
                "summ=" + summ +
                ", begin_date=" + begin_date +
                ", end_date=" + end_date +
                '}';
    }
}
