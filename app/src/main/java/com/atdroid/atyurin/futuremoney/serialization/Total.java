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
    private Calendar begin_date = Calendar.getInstance();
    private Calendar end_date = Calendar.getInstance();
    private double incomeAmount = 0;
    private double outcomeAmount = 0;
    private double accountsAmount = 0;
    private double totalAmount = 0;



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
        if (beginDateType == TYPE_ALL){
            this.begin_date.set(1,1,1);
        }
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
    public double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public double getOutcomeAmount() {
        return outcomeAmount;
    }

    public void setOutcomeAmount(double outcomeAmount) {
        this.outcomeAmount = outcomeAmount;
    }

    public double getAccountsAmount() {
        return accountsAmount;
    }

    public void setAccountsAmount(double accountsAmount) {
        this.accountsAmount = accountsAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Total{" +
                "beginDateType=" + beginDateType +
                ", begin_date=" + begin_date +
                ", end_date=" + end_date +
                ", incomeAmount=" + incomeAmount +
                ", outcomeAmount=" + outcomeAmount +
                ", accountsAmount=" + accountsAmount +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
