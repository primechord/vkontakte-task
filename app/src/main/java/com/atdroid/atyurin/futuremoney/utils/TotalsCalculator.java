package com.atdroid.atyurin.futuremoney.utils;

import android.util.Log;

import com.atdroid.atyurin.futuremoney.serialization.Account;
import com.atdroid.atyurin.futuremoney.serialization.Income;
import com.atdroid.atyurin.futuremoney.serialization.Outcome;
import com.atdroid.atyurin.futuremoney.serialization.Total;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.Years;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by atdroid on 31.10.2015.
 */
public class TotalsCalculator {
    final static String LOG_TAG = "TotalsCalculator";
    Total total;
    ArrayList<Income> singleIncomes;
    ArrayList<Income> periodicalIncomes;
    ArrayList<Outcome> singleOutcomes;
    ArrayList<Outcome> periodicalOutcomes;
    ArrayList<Account> accounts;

    double incomesAmount = 0;
    double outcomesAmount = 0;
    double accountsAmount = 0;
    double totalAmount = 0;

    public TotalsCalculator(Total total) {
        this.total = total;
    }

    public void calculateTotals() {
        for (Income incomeItem : singleIncomes){
            addIncomesAmount(incomeItem.getValue());
        }
        for (Income incomeItem : periodicalIncomes){
            Calendar endCalendar;
            int periodCount = 0;
            if (incomeItem.getEnd_date().after(total.getEnd_date())){
                endCalendar = total.getEnd_date();
            }else {
                endCalendar = incomeItem.getEnd_date();
            }

            if (incomeItem.getPeriod_type() == Income.PERIOD_DAY){
                periodCount = Days.daysBetween(new DateTime(incomeItem.getBegin_date()), new DateTime(endCalendar)).getDays();
            }else if (incomeItem.getPeriod_type() == Income.PERIOD_WEEK){
                periodCount = Weeks.weeksBetween(new DateTime(incomeItem.getBegin_date()), new DateTime(endCalendar)).getWeeks();
            }else if (incomeItem.getPeriod_type() == Income.PERIOD_MONTH){
                periodCount = Months.monthsBetween(new DateTime(incomeItem.getBegin_date()), new DateTime(endCalendar)).getMonths();
            }else if (incomeItem.getPeriod_type() == Income.PERIOD_YEAR){
                periodCount = Years.yearsBetween(new DateTime(incomeItem.getBegin_date()), new DateTime(endCalendar)).getYears();
            }
            periodCount++;
            Log.d(LOG_TAG, "periodCounts: " + periodCount);
            addIncomesAmount(periodCount * incomeItem.getValue());
        }
        for (Outcome outcomeItem : singleOutcomes){
            addOutcomesAmount(outcomeItem.getValue());
        }
        for (Outcome outcomeItem : periodicalOutcomes){
            Calendar endCalendar;
            int periodCount = 0;
            if (outcomeItem.getEnd_date().after(total.getEnd_date())){
                endCalendar = total.getEnd_date();
            }else {
                endCalendar = outcomeItem.getEnd_date();
            }
            if (outcomeItem.getPeriod_type() == Outcome.PERIOD_DAY){
                periodCount = Days.daysBetween(new DateTime(outcomeItem.getBegin_date()), new DateTime(endCalendar)).getDays();
            }else if (outcomeItem.getPeriod_type() == Outcome.PERIOD_WEEK){
                periodCount = Weeks.weeksBetween(new DateTime(outcomeItem.getBegin_date()), new DateTime(endCalendar)).getWeeks();
            }else if (outcomeItem.getPeriod_type() == Outcome.PERIOD_MONTH){
                periodCount = Months.monthsBetween(new DateTime(outcomeItem.getBegin_date()), new DateTime(endCalendar)).getMonths();
            }else if (outcomeItem.getPeriod_type() == Outcome.PERIOD_YEAR){
                periodCount = Years.yearsBetween(new DateTime(outcomeItem.getBegin_date()), new DateTime(endCalendar)).getYears();
            }
            periodCount++;
            addOutcomesAmount(periodCount * outcomeItem.getValue());
        }
        for (Account accountItem : accounts){
            addAccountsAmount(accountItem.getValue());
        }
        this.totalAmount = this.incomesAmount + this.accountsAmount - this.outcomesAmount;
        Log.d(LOG_TAG, toString());
    }

    public void setPeriodicalIncomes(ArrayList<Income> periodicalIncomes) {
        this.periodicalIncomes = periodicalIncomes;
    }

    public void setSingleIncomes(ArrayList<Income> singleIncomes) {
        this.singleIncomes = singleIncomes;
    }

    public void setPeriodicalOutcomes(ArrayList<Outcome> periodicalOutcomes) {
        this.periodicalOutcomes = periodicalOutcomes;
    }

    public void setSingleOutcomes(ArrayList<Outcome> singleOutcomes) {
        this.singleOutcomes = singleOutcomes;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public double getIncomesAmount() {
        return incomesAmount;
    }

    public double getOutcomesAmount() {
        return outcomesAmount;
    }

    public double getAccountsAmount() {
        return accountsAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    private void setIncomesAmount(double incomesAmount) {
        this.incomesAmount = incomesAmount;
    }

    private void setOutcomesAmount(double outcomesAmount) {
        this.outcomesAmount = outcomesAmount;
    }

    private void setAccountsAmount(double accountsAmount) {
        this.accountsAmount = accountsAmount;
    }

    private void addIncomesAmount(double incomesAmount) {
        this.incomesAmount += incomesAmount;
    }

    private void addOutcomesAmount(double outcomesAmount) {
        this.outcomesAmount += outcomesAmount;
    }

    private void addAccountsAmount(double accountsAmount) {
        this.accountsAmount += accountsAmount;
    }

    @Override
    public String toString() {
        return "TotalsCalculator{" +
                "totalAmount=" + totalAmount +
                ", accountsAmount=" + accountsAmount +
                ", outcomesAmount=" + outcomesAmount +
                ", incomesAmount=" + incomesAmount +
                '}';
    }
}
