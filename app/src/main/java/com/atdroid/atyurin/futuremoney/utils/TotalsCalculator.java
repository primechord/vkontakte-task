package com.atdroid.atyurin.futuremoney.utils;

import android.util.Log;

import com.atdroid.atyurin.futuremoney.serialization.Account;
import com.atdroid.atyurin.futuremoney.serialization.DateTotalsMap;
import com.atdroid.atyurin.futuremoney.serialization.Income;
import com.atdroid.atyurin.futuremoney.serialization.Outcome;
import com.atdroid.atyurin.futuremoney.serialization.Total;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.Years;

import java.util.ArrayList;
import java.util.Date;

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
    DateTotalsMap dateTotalsMap = new DateTotalsMap();
    double incomesAmount = 0;
    double outcomesAmount = 0;
    double accountsAmount = 0;
    double totalAmount = 0;


    public TotalsCalculator(Total total) {
        this.total = total;
    }

    public void calculateTotals() {
        Date calcPeriodBeginDate =  new DateTime(total.getBegin_date()).withTimeAtStartOfDay().toDate();
        Date calcPeriodEndDate =  new DateTime(total.getEnd_date()).withTimeAtStartOfDay().toDate();

        for (Income incomeItem : singleIncomes){
            addIncomesAmount(incomeItem.getValue());
            dateTotalsMap.addIncome(incomeItem, new DateTime(incomeItem.getSingle_date()).withTimeAtStartOfDay().toDate());
        }
        for (Income incomeItem : periodicalIncomes){
            Date incomeBeginDate = new DateTime(incomeItem.getBegin_date()).withTimeAtStartOfDay().toDate();//начальная дата получения дохода
            Date incomeEndDate = new DateTime(incomeItem.getEnd_date()).withTimeAtStartOfDay().toDate();//конечная дата получения дохода
            Date incomeCalcPeriodBeginDate = incomeBeginDate; //начальная дата получения дохода в рассчетный период
            Date incomeCalcPeriodEndDate;//конечная дата получения дохода в рассчетный период

            int periodCount = 0;
            int beforeCalcPeriodCount = 0;//число периодов получения дохода до начальной даты расчета

            //возможно стоит заменить на сравнение дат
            //если начальная дата получения дохода меньше начальной даты расчного периода
            if (incomeBeginDate.before(calcPeriodBeginDate)){
                if (incomeItem.getPeriod_type() == Income.PERIOD_DAY){
                    beforeCalcPeriodCount = (Days.daysBetween(new DateTime(incomeBeginDate), new DateTime(calcPeriodBeginDate)).getDays())
                            /incomeItem.getPeriod_value();
                    //проверяем, что полученная дата не меньше начальной даты расчетного периода
                    if ((new DateTime(incomeBeginDate).plusDays(incomeItem.getPeriod_value() * beforeCalcPeriodCount).toDate())
                            .compareTo(calcPeriodBeginDate) < 0 ){
                        //если она меньше, то добавляем еще один период получения дохода к дате начала расчета дохода
                        beforeCalcPeriodCount++;
                    }
                    //дата первого получения дохода, после начала расчетного периода
                    //она может быть больше конечной даты расчетного периода, тогда доход не учитывается. Эта проверка выполняется позже
                    incomeCalcPeriodBeginDate = new DateTime(incomeBeginDate).plusDays(incomeItem.getPeriod_value()*beforeCalcPeriodCount).toDate();
                }else if (incomeItem.getPeriod_type() == Income.PERIOD_WEEK){
                    beforeCalcPeriodCount = (Weeks.weeksBetween(new DateTime(incomeBeginDate), new DateTime(calcPeriodBeginDate)).getWeeks())
                            /incomeItem.getPeriod_value();
                    //проверяем, что полученная дата не меньше начальной даты расчетного периода
                    if ((new DateTime(incomeBeginDate).plusWeeks(incomeItem.getPeriod_value() * beforeCalcPeriodCount).toDate())
                            .compareTo(calcPeriodBeginDate) < 0 ){
                        //если она меньше, то добавляем еще один период получения дохода к дате начала расчета дохода
                        beforeCalcPeriodCount++;
                    }
                    //дата первого получения дохода, после начала расчетного периода
                    //она может быть больше конечной даты расчетного периода, тогда доход не учитывается. Эта проверка выполняется позже
                    incomeCalcPeriodBeginDate = new DateTime(incomeBeginDate).plusWeeks(incomeItem.getPeriod_value()*beforeCalcPeriodCount).toDate();
                }else if (incomeItem.getPeriod_type() == Income.PERIOD_MONTH){
                    beforeCalcPeriodCount = (Months.monthsBetween(new DateTime(incomeBeginDate), new DateTime(calcPeriodBeginDate)).getMonths())
                            /incomeItem.getPeriod_value();
                    //проверяем, что полученная дата не меньше начальной даты расчетного периода
                    if ((new DateTime(incomeBeginDate).plusMonths(incomeItem.getPeriod_value() * beforeCalcPeriodCount).toDate())
                            .compareTo(calcPeriodBeginDate) < 0 ){
                        //если она меньше, то добавляем еще один период получения дохода к дате начала расчета дохода
                        beforeCalcPeriodCount++;
                    }
                    //дата первого получения дохода, после начала расчетного периода
                    //она может быть больше конечной даты расчетного периода, тогда доход не учитывается. Эта проверка выполняется позже
                    incomeCalcPeriodBeginDate = new DateTime(incomeBeginDate).plusMonths(incomeItem.getPeriod_value()*beforeCalcPeriodCount).toDate();
                }else if (incomeItem.getPeriod_type() == Income.PERIOD_YEAR){
                    beforeCalcPeriodCount = (Years.yearsBetween(new DateTime(incomeBeginDate), new DateTime(calcPeriodBeginDate)).getYears())
                            /incomeItem.getPeriod_value();
                    //проверяем, что полученная дата не меньше начальной даты расчетного периода
                    if ((new DateTime(incomeBeginDate).plusYears(incomeItem.getPeriod_value() * beforeCalcPeriodCount).toDate())
                            .compareTo(calcPeriodBeginDate) < 0 ){
                        //если она меньше, то добавляем еще один период получения дохода к дате начала расчета дохода
                        beforeCalcPeriodCount++;
                    }
                    //дата первого получения дохода, после начала расчетного периода
                    //она может быть больше конечной даты расчетного периода, тогда доход не учитывается. Эта проверка выполняется позже
                    incomeCalcPeriodBeginDate = new DateTime(incomeBeginDate).plusYears(incomeItem.getPeriod_value()*beforeCalcPeriodCount).toDate();
                }
            }else {
                incomeCalcPeriodBeginDate = incomeBeginDate;
            }
            //если дата конца получения дохода больше конечной даты расчетного периода, то
            if (incomeEndDate.compareTo(calcPeriodEndDate) > 0){
                incomeCalcPeriodEndDate = calcPeriodEndDate;//установить в качестве расчетной даты, дату конца расчетного периода
            }else {//если получение дохода прекращается раньше даты конца расчетного периода , то
                incomeCalcPeriodEndDate = incomeEndDate;//установить в качестве расчетной даты, дату конца получения дохода
            }

            //если в течении расчетного периода не было получения дохода, конда начальная дата получения дохода больше конечной даты расчетного периода (например, период дохода больше расчетного)
            if (incomeCalcPeriodBeginDate.compareTo(calcPeriodEndDate) > 0){
                addIncomesAmount(0 * incomeItem.getValue());
                continue;
            }

            if (incomeItem.getPeriod_type() == Income.PERIOD_DAY){
                periodCount = Days.daysBetween(new DateTime(incomeCalcPeriodBeginDate), new DateTime(incomeCalcPeriodEndDate)).getDays()/incomeItem.getPeriod_value();
            }else if (incomeItem.getPeriod_type() == Income.PERIOD_WEEK){
                periodCount = Weeks.weeksBetween(new DateTime(incomeCalcPeriodBeginDate), new DateTime(incomeCalcPeriodEndDate)).getWeeks()/incomeItem.getPeriod_value();
            }else if (incomeItem.getPeriod_type() == Income.PERIOD_MONTH){
                periodCount = Months.monthsBetween(new DateTime(incomeCalcPeriodBeginDate), new DateTime(incomeCalcPeriodEndDate)).getMonths()/incomeItem.getPeriod_value();
            }else if (incomeItem.getPeriod_type() == Income.PERIOD_YEAR){
                periodCount = Years.yearsBetween(new DateTime(incomeCalcPeriodBeginDate), new DateTime(incomeCalcPeriodEndDate)).getYears()/incomeItem.getPeriod_value();
            }
            periodCount++;
            Log.d(LOG_TAG, "periodCounts: " + periodCount);
            addIncomesAmount(periodCount * incomeItem.getValue());
            for (int periodNumber = 1; periodNumber <= periodCount; periodNumber++){
                Date incomeDate = incomeBeginDate;
                DateTime beginDateTime = new DateTime(incomeBeginDate);
                if (incomeItem.getPeriod_type() == Income.PERIOD_DAY){
                    incomeDate = beginDateTime.plusDays(periodNumber*incomeItem.getPeriod_value()).withTimeAtStartOfDay().toDate();
                }else if (incomeItem.getPeriod_type() == Income.PERIOD_WEEK){
                    incomeDate = beginDateTime.plusWeeks(periodNumber*incomeItem.getPeriod_value()).withTimeAtStartOfDay().toDate();
                }else if (incomeItem.getPeriod_type() == Income.PERIOD_MONTH){
                    incomeDate = beginDateTime.plusMonths(periodNumber*incomeItem.getPeriod_value()).withTimeAtStartOfDay().toDate();
                }else if (incomeItem.getPeriod_type() == Income.PERIOD_YEAR){
                    incomeDate = beginDateTime.plusYears(periodNumber*incomeItem.getPeriod_value()).withTimeAtStartOfDay().toDate();
                }
                dateTotalsMap.addIncome(incomeItem, incomeDate);
            }
        }
        for (Outcome outcomeItem : singleOutcomes){
            addOutcomesAmount(outcomeItem.getValue());
            dateTotalsMap.addOutcome(outcomeItem, new DateTime(outcomeItem.getSingle_date()).withTimeAtStartOfDay().toDate());
        }
        for (Outcome outcomeItem : periodicalOutcomes){
            Date outcomeBeginDate = new DateTime(outcomeItem.getBegin_date()).withTimeAtStartOfDay().toDate();
            Date outcomeEndDate = new DateTime(outcomeItem.getEnd_date()).withTimeAtStartOfDay().toDate();
            Date outcomeCalcPeriodBeginDate = outcomeBeginDate;
            Date outcomeCalcPeriodEndDate;

            int periodCount = 0;
            int beforeCalcPeriodCount = 0;//число периодов  расхода до начальной даты расчета
            if (outcomeItem.getBegin_date().before(total.getBegin_date())){//если начальная дата  расхода меньше начальной даты расчета
                if (outcomeItem.getPeriod_type() == Outcome.PERIOD_DAY){
                    beforeCalcPeriodCount = (Days.daysBetween(new DateTime(outcomeBeginDate), new DateTime(calcPeriodBeginDate)).getDays())
                            /outcomeItem.getPeriod_value();
                    //проверяем, что полученная дата не меньше начальной даты расчетного периода
                    if ((new DateTime(outcomeBeginDate).plusDays(outcomeItem.getPeriod_value() * beforeCalcPeriodCount).toDate())
                            .compareTo(calcPeriodBeginDate) < 0 ){
                        //если она меньше, то добавляем еще один период получения расхода к дате начала расчета расхода
                        beforeCalcPeriodCount++;
                    }
                    //дата первого получения расхода, после начала расчетного периода
                    //она может быть больше конечной даты расчетного периода, тогда расход не учитывается. Эта проверка выполняется позже
                    outcomeCalcPeriodBeginDate = new DateTime(outcomeBeginDate).plusDays(outcomeItem.getPeriod_value()*beforeCalcPeriodCount).toDate();
                }else if (outcomeItem.getPeriod_type() == Outcome.PERIOD_WEEK){
                    beforeCalcPeriodCount = (Weeks.weeksBetween(new DateTime(outcomeBeginDate), new DateTime(calcPeriodBeginDate)).getWeeks())
                            /outcomeItem.getPeriod_value();
                    //проверяем, что полученная дата не меньше начальной даты расчетного периода
                    if ((new DateTime(outcomeBeginDate).plusWeeks(outcomeItem.getPeriod_value() * beforeCalcPeriodCount).toDate())
                            .compareTo(calcPeriodBeginDate) < 0 ){
                        //если она меньше, то добавляем еще один период получения расхода к дате начала расчета расхода
                        beforeCalcPeriodCount++;
                    }
                    //дата первого получения расхода, после начала расчетного периода
                    //она может быть больше конечной даты расчетного периода, тогда расход не учитывается. Эта проверка выполняется позже
                    outcomeCalcPeriodBeginDate = new DateTime(outcomeBeginDate).plusWeeks(outcomeItem.getPeriod_value()*beforeCalcPeriodCount).toDate();
                }else if (outcomeItem.getPeriod_type() == Outcome.PERIOD_MONTH){
                    beforeCalcPeriodCount = (Months.monthsBetween(new DateTime(outcomeBeginDate), new DateTime(calcPeriodBeginDate)).getMonths())
                            /outcomeItem.getPeriod_value();
                    //проверяем, что полученная дата не меньше начальной даты расчетного периода
                    if ((new DateTime(outcomeBeginDate).plusMonths(outcomeItem.getPeriod_value() * beforeCalcPeriodCount).toDate())
                            .compareTo(calcPeriodBeginDate) < 0 ){
                        //если она меньше, то добавляем еще один период получения расхода к дате начала расчета расхода
                        beforeCalcPeriodCount++;
                    }
                    //дата первого получения расхода, после начала расчетного периода
                    //она может быть больше конечной даты расчетного периода, тогда расход не учитывается. Эта проверка выполняется позже
                    outcomeCalcPeriodBeginDate = new DateTime(outcomeBeginDate).plusMonths(outcomeItem.getPeriod_value()*beforeCalcPeriodCount).toDate();
                }else if (outcomeItem.getPeriod_type() == Outcome.PERIOD_YEAR){
                    beforeCalcPeriodCount = (Years.yearsBetween(new DateTime(outcomeBeginDate), new DateTime(calcPeriodBeginDate)).getYears())
                            /outcomeItem.getPeriod_value();
                    //проверяем, что полученная дата не меньше начальной даты расчетного периода
                    if ((new DateTime(outcomeBeginDate).plusYears(outcomeItem.getPeriod_value() * beforeCalcPeriodCount).toDate())
                            .compareTo(calcPeriodBeginDate) < 0 ){
                        //если она меньше, то добавляем еще один период получения расхода к дате начала расчета расхода
                        beforeCalcPeriodCount++;
                    }
                    //дата первого получения расхода, после начала расчетного периода
                    //она может быть больше конечной даты расчетного периода, тогда расход не учитывается. Эта проверка выполняется позже
                    outcomeCalcPeriodBeginDate = new DateTime(outcomeBeginDate).plusYears(outcomeItem.getPeriod_value()*beforeCalcPeriodCount).toDate();
                }
            }else {
                outcomeCalcPeriodBeginDate = outcomeBeginDate;
            }
            //если дата конца  расхода больше конечной даты расчетного периода, то
            if (outcomeEndDate.compareTo(calcPeriodEndDate) > 0){
                outcomeCalcPeriodEndDate = calcPeriodEndDate;//установить в качестве расчетной даты, дату конца расчетного периода
            }else {//если получение расхода прекращается раньше даты конца расчетного периода , то
                outcomeCalcPeriodEndDate = outcomeEndDate;//установить в качестве расчетной даты, дату конца  расхода
            }

            //если в течении расчетного периода не было  расхода, конда начальная дата  расхода больше конечной даты расчетного периода (например, период расхода больше расчетного)
            if (outcomeCalcPeriodBeginDate.compareTo(calcPeriodEndDate) > 0){
                addOutcomesAmount(0 * outcomeItem.getValue());
                continue;
            }

            if (outcomeItem.getPeriod_type() == Outcome.PERIOD_DAY){
                periodCount = Days.daysBetween(new DateTime(outcomeCalcPeriodBeginDate), new DateTime(outcomeCalcPeriodEndDate)).getDays()/outcomeItem.getPeriod_value();
            }else if (outcomeItem.getPeriod_type() == Outcome.PERIOD_WEEK){
                periodCount = Weeks.weeksBetween(new DateTime(outcomeCalcPeriodBeginDate), new DateTime(outcomeCalcPeriodEndDate)).getWeeks()/outcomeItem.getPeriod_value();
            }else if (outcomeItem.getPeriod_type() == Outcome.PERIOD_MONTH){
                periodCount = Months.monthsBetween(new DateTime(outcomeCalcPeriodBeginDate), new DateTime(outcomeCalcPeriodEndDate)).getMonths()/outcomeItem.getPeriod_value();
            }else if (outcomeItem.getPeriod_type() == Outcome.PERIOD_YEAR){
                periodCount = Years.yearsBetween(new DateTime(outcomeCalcPeriodBeginDate), new DateTime(outcomeCalcPeriodEndDate)).getYears()/outcomeItem.getPeriod_value();
            }
            periodCount++;
            Log.d(LOG_TAG, "periodCounts: " + periodCount);
            addOutcomesAmount(periodCount * outcomeItem.getValue());
            for (int periodNumber = 1; periodNumber <= periodCount; periodNumber++){
                Date outcomeDate = outcomeBeginDate;
                DateTime beginDateTime = new DateTime(outcomeBeginDate);
                if (outcomeItem.getPeriod_type() == Income.PERIOD_DAY){
                    outcomeDate = beginDateTime.plusDays(periodNumber*outcomeItem.getPeriod_value()).withTimeAtStartOfDay().toDate();
                }else if (outcomeItem.getPeriod_type() == Income.PERIOD_WEEK){
                    outcomeDate = beginDateTime.plusWeeks(periodNumber*outcomeItem.getPeriod_value()).withTimeAtStartOfDay().toDate();
                }else if (outcomeItem.getPeriod_type() == Income.PERIOD_MONTH){
                    outcomeDate = beginDateTime.plusMonths(periodNumber*outcomeItem.getPeriod_value()).withTimeAtStartOfDay().toDate();
                }else if (outcomeItem.getPeriod_type() == Income.PERIOD_YEAR){
                    outcomeDate = beginDateTime.plusYears(periodNumber*outcomeItem.getPeriod_value()).withTimeAtStartOfDay().toDate();
                }
                dateTotalsMap.addOutcome(outcomeItem, outcomeDate);
            }
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



    public DateTotalsMap getDateTotalsMap() {
        return dateTotalsMap;
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
