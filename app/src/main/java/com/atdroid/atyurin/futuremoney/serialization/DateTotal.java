package com.atdroid.atyurin.futuremoney.serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by atdroid on 24.08.2016.
 */
public class DateTotal implements Serializable{

    public DateTotal(Date date) {
        this.date = date;
    }

    Date date;
    ArrayList<Income> incomes = new ArrayList<Income>();
    ArrayList<Outcome> outcomes = new ArrayList<Outcome>();

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Income> getIncomes() {
        return incomes;
    }

    public void setIncomes(ArrayList<Income> incomes) {
        this.incomes = incomes;
    }

    public ArrayList<Outcome> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(ArrayList<Outcome> outcomes) {
        this.outcomes = outcomes;
    }

    public double getDateTotalValue(){
        double total = 0;
        for (Income income : incomes){
            total += income.getValue();
        }
        for (Outcome outcome : outcomes){
            total -= outcome.getValue();
        }
        return total;
    }

    @Override
    public String toString() {
        return "DateTotal{" +
                "date=" + date +
                ", incomes=" + incomes +
                ", outcomes=" + outcomes +
                '}';
    }
}
