package com.baraa.bsoft.taskstimer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by baraa on 29/06/2017.
 */

class AppDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TasksTimer.db";
    public static final int DATABASE_VERSION = 1;
    private static AppDatabase instance =null;
    private AppDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    /**
     * get instance of appDatabase singleton SQLiteOpenHelper Class
     *
     * @param context content provider Context
     * @return AppDatabase
     */
     static AppDatabase getDatabase(Context context){
        if(instance ==null){
            instance = new AppDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlStr = "CREATE TABLE " + TasksContract.TABLE_NAME + " ("
                + TasksContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + TasksContract.Columns.TASKS_NAME + " TEXT NOT NULL, "
                + TasksContract.Columns.TASKS_DESCRIPTION + " TEXT, "
                + TasksContract.Columns.TASKS_SORTORDER + " INTEGER);";
//                String.format("create table %s(%s integer primary key not null,%s text not null,%s text,%s integer);",
//                TasksContract.TABLE_NAME,
//                TasksContract.Columns._ID,
//                TasksContract.Columns.TASKS_NAME,
//                TasksContract.Columns.TASKS_DESCRIPTION,
//                TasksContract.Columns.TASKS_SORTORDER);
        db.execSQL(sqlStr);
        Log.d(TAG, "onCreate: \n"+sqlStr);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}