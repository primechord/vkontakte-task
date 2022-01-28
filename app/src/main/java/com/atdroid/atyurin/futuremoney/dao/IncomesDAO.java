package com.atdroid.atyurin.futuremoney.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.atdroid.atyurin.futuremoney.serialization.Income;
import com.atdroid.atyurin.futuremoney.utils.DBHelper;
import com.atdroid.atyurin.futuremoney.utils.DateFormater;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by atdroid on 07.10.2015.
 */
public class IncomesDAO {
    // Database fields
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = DBHelper.getIncomeColumns();
    String TAG = "FutureMoneyIncomesDAO";
    public IncomesDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void openWritable() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void openReadable() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean addIncome(Income income) {

        Cursor cursorSelect = database.query(DBHelper.TABLE_INCOMES,
                new String[]{DBHelper.COLUMN_ID},
                DBHelper.COLUMN_NAME + " = '" + income.getName() + "'",
                null, null, null, null, null);
        if (cursorSelect != null){//budget_item already exist
//            return false;
        }

        ContentValues values = incomeToContentValues(income);

        long insertId = database.insert(DBHelper.TABLE_INCOMES, null,
                values);
        Log.d(TAG, "Insert budget_item id = " + insertId + ". Income: " + income.toString());
//        Cursor cursor = database.query(DBHelper.TABLE_INCOMES,
//                allColumns, DBHelper.COLUMN_ID + " = " + insertId, null,
//                null, null, null);
//        cursor.moveToFirst();
//        cursor.close();
        return true;
    }

    public boolean updateIncome (Income income)
    {
        ContentValues contentValues = incomeToContentValues(income);
        database.update(DBHelper.TABLE_INCOMES, contentValues, DBHelper.COLUMN_ID + " = ? ", new String[]{Long.toString(income.getId())});
        return true;
    }
    public void deleteIncome(Income income) {
        long id = income.getId();
        System.out.println("Income deleted with id: " + id);
        database.delete(DBHelper.TABLE_INCOMES, DBHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<Income> getAllIncomes() {
        ArrayList<Income> incomes = new ArrayList<Income>();

        Cursor cursor = database.query(DBHelper.TABLE_INCOMES,
                allColumns, null, null, null, null, null);
        Log.d(TAG, "Incomes List:");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Income income = cursorToIncome(cursor);
            incomes.add(income);
            cursor.moveToNext();
            Log.d(TAG, "Income: " + income.toString());
        }
        // make sure to close the cursor
        cursor.close();
        Collections.sort(incomes, new Comparator<Income>() {
            @Override
            public int compare(Income income1, Income income2) {
                Calendar calendar1;
                Calendar calendar2;
                if (income1.getType() == Income.TYPE_PERIODICAL) {
                    calendar1 = income1.getBegin_date();
                } else {
                    calendar1 = income1.getSingle_date();
                }
                if (income1.getType() == Income.TYPE_PERIODICAL) {
                    calendar2 = income2.getBegin_date();
                } else {
                    calendar2 = income2.getSingle_date();
                }
                return calendar2.compareTo(calendar1);
            }
        });
        return incomes;
    }

    public ArrayList<Income> getIncomesWithType(final int incomeType) {
        ArrayList<Income> incomes = new ArrayList<Income>();
        Cursor cursor = null;
        String orderBy = "";
        if (incomeType == Income.TYPE_SINGLE){
            orderBy = DBHelper.COLUMN_SINGLE_DATE + " DESC";
        }else if (incomeType == Income.TYPE_PERIODICAL){
            orderBy = DBHelper.COLUMN_BEGIN_DATE + " DESC";
        }
        cursor = database.query(DBHelper.TABLE_INCOMES,
                    allColumns,
                    DBHelper.COLUMN_TYPE + " = ?",
                    new String[]{Integer.toString(incomeType)},
                    null, null, orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Income income = cursorToIncome(cursor);
            incomes.add(income);
            cursor.moveToNext();
            Log.d(TAG, "Income: " + income.toString());
        }
        // make sure to close the cursor
        cursor.close();
        return incomes;
    }

    public ArrayList<Income> getIncomesInPeriodWithType(final int incomeType, Calendar calendar_begin, Calendar calendar_end) {
        ArrayList<Income> incomes = new ArrayList<Income>();
        Cursor cursor = null;
        if (incomeType == Income.TYPE_PERIODICAL){
            cursor = database.query(DBHelper.TABLE_INCOMES,
                    allColumns,
                    DBHelper.COLUMN_BEGIN_DATE + " <= ? " +
                            "AND " + DBHelper.COLUMN_END_DATE + " >= ? " +
                            "AND " + DBHelper.COLUMN_TYPE + " = ?",
                    new String[]{Long.toString(calendar_end.getTimeInMillis()),
                            Long.toString(calendar_begin.getTimeInMillis()),
                            Integer.toString(Income.TYPE_PERIODICAL)},
                    null, null, DBHelper.COLUMN_BEGIN_DATE + " DESC");
        } else if (incomeType == Income.TYPE_SINGLE){
            cursor = database.query(DBHelper.TABLE_INCOMES,
                    allColumns,
                    DBHelper.COLUMN_SINGLE_DATE + " >= ? AND " + DBHelper.COLUMN_SINGLE_DATE + " <= ? AND " + DBHelper.COLUMN_TYPE + " = ?",
                    new String[]{Long.toString(calendar_begin.getTimeInMillis()),
                            Long.toString(calendar_end.getTimeInMillis()),
                            Integer.toString(Income.TYPE_SINGLE)},
                    null, null, DBHelper.COLUMN_SINGLE_DATE + " DESC");
        }

        Log.d(TAG, "Incomes List:");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Income income = cursorToIncome(cursor);
            incomes.add(income);
            cursor.moveToNext();
            Log.d(TAG, "Income: " + income.toString());
        }
        // make sure to close the cursor
        cursor.close();
        return incomes;
    }

    private ContentValues incomeToContentValues(Income income){
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, income.getName());
        values.put(DBHelper.COLUMN_VALUE, income.getValue());
        values.put(DBHelper.COLUMN_TYPE, income.getType());
        values.put(DBHelper.COLUMN_SINGLE_DATE, DateFormater.formatCalendarToLong(income.getSingle_date()));
        values.put(DBHelper.COLUMN_BEGIN_DATE, DateFormater.formatCalendarToLong(income.getBegin_date()));
        values.put(DBHelper.COLUMN_END_DATE, DateFormater.formatCalendarToLong(income.getEnd_date()));
        values.put(DBHelper.COLUMN_PERIOD_TYPE, income.getPeriod_type());
        values.put(DBHelper.COLUMN_PERIOD_VALUE, income.getPeriod_value());
        return values;
    }

    private Income cursorToIncome(Cursor cursor) {
        Income income = new Income();
        income.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID)));
        income.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
        income.setValue(cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_VALUE)));
        income.setType(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_TYPE)));
        long v = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_SINGLE_DATE));
        income.setSingle_date(DateFormater.formatLongToCalendar(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_SINGLE_DATE))));
        income.setBegin_date(DateFormater.formatLongToCalendar(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_BEGIN_DATE))));
        income.setEnd_date(DateFormater.formatLongToCalendar(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_END_DATE))));
        income.setPeriod_type(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_PERIOD_TYPE)));
        income.setPeriod_value(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_PERIOD_VALUE)));
        return income;
    }
}
