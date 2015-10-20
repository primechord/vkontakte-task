package com.atdroid.atyurin.futuremoney.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by atdroid on 07.10.2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static String LOG_TAG = "FutureMoneyDBHelper";

    public final static String TABLE_INCOMES = "incomes";
    public final static String TABLE_OUTCOMES = "outcomes";
    public final static String TABLE_ACCOUNTS = "accounts";

    public final static String COLUMN_ID = "_id";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_TYPE = "type";
    public final static String COLUMN_VALUE = "value";
    public final static String COLUMN_SINGLE_DATE = "single_date";
    public final static String COLUMN_BEGIN_DATE = "begin_date";
    public final static String COLUMN_END_DATE = "end_date";
    public final static String COLUMN_PERIOD_TYPE = "period_type";
    public final static String COLUMN_PERIOD_VALUE = "period_date";


    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "FutureMoneyDB", null, 1);
    }

    public static String[] getIncomeColumns(){
        String[] allColumns = {COLUMN_ID, COLUMN_NAME, COLUMN_TYPE, COLUMN_VALUE, COLUMN_SINGLE_DATE,
                COLUMN_BEGIN_DATE, COLUMN_END_DATE, COLUMN_PERIOD_TYPE, COLUMN_PERIOD_VALUE};
        return allColumns;
    }

    public static String[] getOutcomeColumns(){
        String[] allColumns = {COLUMN_ID, COLUMN_NAME, COLUMN_TYPE, COLUMN_VALUE, COLUMN_SINGLE_DATE,
                COLUMN_BEGIN_DATE, COLUMN_END_DATE, COLUMN_PERIOD_TYPE, COLUMN_PERIOD_VALUE};
        return allColumns;
    }

    public static String[] getAccountColumns(){
        String[] allColumns = {COLUMN_ID, COLUMN_NAME, COLUMN_VALUE};
        return allColumns;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицы с полями
        db.execSQL("create table " + TABLE_INCOMES + " ("
                + COLUMN_ID             + " integer primary key autoincrement, "
                + COLUMN_NAME           + " text, "
                + COLUMN_TYPE           + " integer, "
                + COLUMN_VALUE          + " real, "
                + COLUMN_SINGLE_DATE    + " integer, "
                + COLUMN_BEGIN_DATE     + " integer, "
                + COLUMN_END_DATE       + " integer, "
                + COLUMN_PERIOD_TYPE    + " integer, "
                + COLUMN_PERIOD_VALUE   + " integer "
                + ");");

        db.execSQL("create table " + TABLE_OUTCOMES + " ("
                + COLUMN_ID             + " integer primary key autoincrement, "
                + COLUMN_NAME           + " text, "
                + COLUMN_TYPE           + " integer, "
                + COLUMN_VALUE          + " real, "
                + COLUMN_SINGLE_DATE    + " integer, "
                + COLUMN_BEGIN_DATE     + " integer, "
                + COLUMN_END_DATE       + " integer, "
                + COLUMN_PERIOD_TYPE    + " integer, "
                + COLUMN_PERIOD_VALUE   + " integer "
                + ");");

        db.execSQL("create table " + TABLE_ACCOUNTS + " ("
                + COLUMN_ID             + " integer primary key autoincrement, "
                + COLUMN_NAME           + " text, "
                + COLUMN_VALUE          + " real "
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
