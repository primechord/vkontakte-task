package com.atdroid.atyurin.futuremoney.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.atdroid.atyurin.futuremoney.serialization.Account;
import com.atdroid.atyurin.futuremoney.utils.DBHelper;

import java.util.ArrayList;

/**
 * Created by atdroid on 11.10.2015.
 */
public class AccountsDAO {
    // Database fields
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = DBHelper.getAccountColumns();
    String TAG = "FutureMoneyAccountsDAO";
    public AccountsDAO(Context context) {
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

    public boolean addAccount (Account account) {

        Cursor cursorSelect = database.query(DBHelper.TABLE_ACCOUNTS,
                new String[]{DBHelper.COLUMN_ID},
                DBHelper.COLUMN_NAME + " = '" + account.getName() + "'",
                null, null, null, null, null);
        if (cursorSelect != null){//account already exist
//            return false;
        }

        ContentValues values = accountToContentValues(account);

        long insertId = database.insert(DBHelper.TABLE_ACCOUNTS, null,
                values);
        Log.d(TAG, "Insert account id = " + insertId + ". Account: " + account.toString());
        return true;
    }

    public boolean updateAccount(Account account)
    {
        ContentValues contentValues = accountToContentValues(account);
        database.update(DBHelper.TABLE_ACCOUNTS, contentValues, DBHelper.COLUMN_ID + " = ? ", new String[]{Long.toString(account.getId())});
        return true;
    }
    public void deleteAccount(Account account) {
        long id = account.getId();
        System.out.println("Account deleted with id: " + id);
        database.delete(DBHelper.TABLE_ACCOUNTS, DBHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<Account> getAllAccounts() {
        ArrayList<Account> accounts = new ArrayList<Account>();

        Cursor cursor = database.query(DBHelper.TABLE_ACCOUNTS,
                allColumns, null, null, null, null, null);
        Log.d(TAG, "Account List:");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Account account = cursorToAccount(cursor);
            accounts.add(account);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return accounts;
    }

    private ContentValues accountToContentValues(Account account){
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, account.getName());
        values.put(DBHelper.COLUMN_VALUE, account.getValue());
        return values;
    }

    private Account cursorToAccount(Cursor cursor) {
        Account account = new Account();
        account.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID)));
        account.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
        account.setValue(cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_VALUE)));
        return account;
    }
}
