package com.atdroid.atyurin.futuremoney.serialization;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by atdroid on 13.09.2016.
 */
public class DateTotalsMap implements Serializable{
    Map<Date, DateTotal> dateTotals = new HashMap<Date, DateTotal>();

    public void addIncome(Income income, Date date){
        //проверяем есть ли уже объект DateTotal для даты дохода, если есть, то обновляем его. Если нет, то создаем новый.
        DateTotal dateTotal = new DateTotal(date);
        if(dateTotals.containsKey(date)){
            dateTotal = dateTotals.get(date);
        }
        dateTotal.getIncomes().add(income);
        dateTotals.put(date, dateTotal);
    }

    public void addOutcome(Outcome outcome, Date date){
        //проверяем есть ли уже объект DateTotal для даты расхода, если есть, то обновляем его. Если нет, то создаем новый.
        DateTotal dateTotal = new DateTotal(date);
        if(dateTotals.containsKey(date)){
            dateTotal = dateTotals.get(date);
        }
        dateTotal.getOutcomes().add(outcome);
        dateTotals.put(date, dateTotal);
    }

    public Map<Date,DateTotal> getSortedDateTotalsMap(){
        this.dateTotals = new TreeMap<Date, DateTotal>(this.dateTotals);
        return this.dateTotals;
    }

    public DateTotal getDateTotal(Date date){
        return dateTotals.get(date);
    }

    public int getSize(){
        return this.dateTotals.size();
    }
}
