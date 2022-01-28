package com.atdroid.atyurin.futuremoney.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.atdroid.atyurin.futuremoney.serialization.Outcome;
import com.atdroid.atyurin.futuremoney.utils.DBHelper;
import com.atdroid.atyurin.futuremoney.utils.DateFormater;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by atdroid on 07.10.2015.
 */
public class OutcomesDAO {
    // Database fields
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = DBHelper.getOutcomeColumns();
    String TAG = "FutureMoneyOutcomesDAO";
    public OutcomesDAO(Context context) {
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

    public boolean addOutcome (Outcome outcome) {

        Cursor cursorSelect = database.query(DBHelper.TABLE_OUTCOMES,
                new String[]{DBHelper.COLUMN_ID},
                DBHelper.COLUMN_NAME + " = '" + outcome.getName() + "'",
                null, null, null, null, null);
        if (cursorSelect != null){//budget_item already exist
//            return false;
        }

        ContentValues values = outcomeToContentValues(outcome);

        long insertId = database.insert(DBHelper.TABLE_OUTCOMES, null,
                values);
        Log.d(TAG, "Insert outcome id = " + insertId + ". Outcome: " + outcome.toString());
//        Cursor cursor = database.query(DBHelper.TABLE_INCOMES,
//                allColumns, DBHelper.COLUMN_ID + " = " + insertId, null,
//                null, null, null);
//        cursor.moveToFirst();
//        cursor.close();
        return true;
    }

    public boolean updateOutcome(Outcome outcome)
    {
        ContentValues contentValues = outcomeToContentValues(outcome);
        database.update(DBHelper.TABLE_OUTCOMES, contentValues, DBHelper.COLUMN_ID + " = ? ", new String[]{Long.toString(outcome.getId())});
        return true;
    }
    public void deleteOutcome(Outcome outcome) {
        long id = outcome.getId();
        System.out.println("Outcome deleted with id: " + id);
        database.delete(DBHelper.TABLE_OUTCOMES, DBHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<Outcome> getAllOutcomes() {
        ArrayList<Outcome> outcomes = new ArrayList<Outcome>();

        Cursor cursor = database.query(DBHelper.TABLE_OUTCOMES,
                allColumns, null, null, null, null, null);
        Log.d(TAG, "Outcome List:");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Outcome outcome = cursorToOutcome(cursor);
            outcomes.add(outcome);
            cursor.moveToNext();
//            Log.d(TAG, "Income: " + outcome.toString());
        }
        // make sure to close the cursor
        cursor.close();
        return outcomes;
    }
    public ArrayList<Outcome> getOutcomesWithType(final int outcomeType) {
        ArrayList<Outcome> outcomes = new ArrayList<Outcome>();
        Cursor cursor = null;
        cursor = database.query(DBHelper.TABLE_OUTCOMES,
                allColumns,
                DBHelper.COLUMN_TYPE + " = ?",
                new String[]{Integer.toString(outcomeType)},
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Outcome outcome = cursorToOutcome(cursor);
            outcomes.add(outcome);
            cursor.moveToNext();
            Log.d(TAG, "Outcome: " + outcome.toString());
        }
        // make sure to close the cursor
        cursor.close();
        return outcomes;
    }
    public ArrayList<Outcome> getOutcomesInPeriodWithType(final int outcomeType, Calendar calendar_begin, Calendar calendar_end) {
        ArrayList<Outcome> outcomes = new ArrayList<Outcome>();
        Cursor cursor = null;
        if (outcomeType == Outcome.TYPE_PERIODICAL){
            cursor = database.query(DBHelper.TABLE_OUTCOMES,
                    allColumns,
                    DBHelper.COLUMN_BEGIN_DATE + " <= ? AND " + DBHelper.COLUMN_END_DATE + " >= ? " +
                            "AND " + DBHelper.COLUMN_TYPE + " = ?",
                    new String[]{Long.toString(calendar_end.getTimeInMillis()),
                            Long.toString(calendar_begin.getTimeInMillis()),
                            Integer.toString(Outcome.TYPE_PERIODICAL)},
                    null, null, null);
        } else if (outcomeType == Outcome.TYPE_SINGLE){
            cursor = database.query(DBHelper.TABLE_OUTCOMES,
                    allColumns,
                    DBHelper.COLUMN_SINGLE_DATE + " >= ? AND " + DBHelper.COLUMN_SINGLE_DATE + " <= ? AND " + DBHelper.COLUMN_TYPE + " = ?",
                    new String[]{Long.toString(calendar_begin.getTimeInMillis()),
                            Long.toString(calendar_end.getTimeInMillis()),
                            Integer.toString(Outcome.TYPE_SINGLE)},
                    null, null, null);
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Outcome outcome = cursorToOutcome(cursor);
            outcomes.add(outcome);
            cursor.moveToNext();
            Log.d(TAG, "Outcome: " + outcome.toString());
        }
        // make sure to close the cursor
        cursor.close();
        return outcomes;
    }

    private ContentValues outcomeToContentValues(Outcome outcome){
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, outcome.getName());
        values.put(DBHelper.COLUMN_VALUE, outcome.getValue());
        values.put(DBHelper.COLUMN_TYPE, outcome.getType());
        values.put(DBHelper.COLUMN_SINGLE_DATE, DateFormater.formatCalendarToLong(outcome.getSingle_date()));
        values.put(DBHelper.COLUMN_BEGIN_DATE, DateFormater.formatCalendarToLong(outcome.getBegin_date()));
        values.put(DBHelper.COLUMN_END_DATE, DateFormater.formatCalendarToLong(outcome.getEnd_date()));
        values.put(DBHelper.COLUMN_PERIOD_TYPE, outcome.getPeriod_type());
        values.put(DBHelper.COLUMN_PERIOD_VALUE, outcome.getPeriod_value());
        return values;
    }

    private Outcome cursorToOutcome(Cursor cursor) {
        Outcome outcome = new Outcome();
        outcome.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID)));
        outcome.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
        outcome.setValue(cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_VALUE)));
        outcome.setType(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_TYPE)));
        long v = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_SINGLE_DATE));
        outcome.setSingle_date(DateFormater.formatLongToCalendar(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_SINGLE_DATE))));
        outcome.setBegin_date(DateFormater.formatLongToCalendar(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_BEGIN_DATE))));
        outcome.setEnd_date(DateFormater.formatLongToCalendar(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_END_DATE))));
        outcome.setPeriod_type(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_PERIOD_TYPE)));
        outcome.setPeriod_value(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_PERIOD_VALUE)));
        return outcome;
    }
}
