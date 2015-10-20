package com.atdroid.atyurin.futuremoney.serialization;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by atdroid on 13.09.2015.
 */
public class Outcome implements Serializable {
    public static final String dateFormat = "dd MMMM yyyy";

    public static final int TYPE_SINGLE = 0;
    public static final int TYPE_PERIODICAL = 1;

    public static final int PERIOD_DAY = 0;
    public static final int PERIOD_WEEK = 1;
    public static final int PERIOD_MONTH = 2;
    public static final int PERIOD_YEAR = 3;

    private long id;
    private String name = "";
    private int type = TYPE_SINGLE;
    private double value = 0;
    private int period_type = PERIOD_DAY;
    private int period_value = 1;
    private Calendar single_date = Calendar.getInstance();
    private Calendar begin_date = Calendar.getInstance();
    private Calendar end_date = Calendar.getInstance();

    public Outcome() {
        single_date.setTimeInMillis(System.currentTimeMillis());
        begin_date.setTimeInMillis(System.currentTimeMillis());
        end_date.setTimeInMillis(System.currentTimeMillis());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(final double value) {
        this.value = value;
    }

    public int getPeriod_type() {
        return period_type;
    }

    public void setPeriod_type(int period_type) {
        this.period_type = period_type;
    }

    public int getPeriod_value() {
        return period_value;
    }

    public void setPeriod_value(int period_value) {
        this.period_value = period_value;
    }

    public Calendar getSingle_date() {
        return single_date;
    }

    public void setSingle_date(final Calendar single_date) {
        this.single_date = single_date;
    }

    public Calendar getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(final Calendar begin_date) {
        this.begin_date = begin_date;
    }

    public Calendar getEnd_date() {
        return end_date;
    }

    public void setEnd_date(final Calendar end_date) {
        this.end_date = end_date;
    }

    @Override
    public String toString() {
        return "Outcome{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", value=" + value +
                ", period_type=" + period_type +
                ", period_value=" + period_value +
                ", single_date=" + single_date +
                ", begin_date=" + begin_date +
                ", end_date=" + end_date +
                '}';
    }
}
