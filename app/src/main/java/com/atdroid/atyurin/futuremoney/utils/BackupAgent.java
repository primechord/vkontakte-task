package com.atdroid.atyurin.futuremoney.utils;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;
import android.content.Context;
import android.app.backup.BackupManager;
//import android.app.backup.SharedPreferencesBackupHelper;
//import android.content.SharedPreferences;

/**
 * Created by atdroid on 28.07.2016.
 */
public class BackupAgent extends BackupAgentHelper {
    @Override
    public void onCreate() {
        // DatabaseHelper.DATABASE_NAME – константа с названием базы данных.
        FileBackupHelper fileBackupHelper = new FileBackupHelper(this, "../databases/" + DBHelper.DATABASE_NAME);
        addHelper(DBHelper.DATABASE_NAME, fileBackupHelper);
        //здесь мы просто, через запятую указываем названия файлов, в которых хранятся ваши настройки. Обратите внимание на последний параметр, в файле под таким названием хранит данные дефолтный PreferenceManager(PreferenceManager.getDefaultSharedPreferences(context))
        //SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, SharedPreferenceHelper.PREFERENCES, getPackageName() + "_preferences");
        //addHelper("prefs", helper);
    }

    //метод для запроса бэкапа. Согласно документации следует вызывать этот метод всякий раз, когда данные изменились.
    public static void requestBackup(Context context) {
        BackupManager bm = new BackupManager(context);
        bm.dataChanged();
    }
}
