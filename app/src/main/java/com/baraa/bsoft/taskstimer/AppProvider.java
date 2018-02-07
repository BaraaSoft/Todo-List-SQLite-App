package com.baraa.bsoft.taskstimer;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by baraa on 02/07/2017.
 */

public class AppProvider extends ContentProvider {
    static final String CONTENT_AUTHORITY = "com.baraa.bsoft.taskstimer.provider";
    public static final Uri  CONTENT_AUTHORITY_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    private static final UriMatcher  uriMatcher = getUriMatcher();
    private AppDatabase appDatabase;

    public static final int TASKS = 100;
    public static final int TASKS_ID =101;
    public static final int TIMER = 200;
    public static final int TIMER_ID = 201;
    public static final int DURATION = 400;
    public static final int DURATION_ID =401;

    private static UriMatcher getUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(CONTENT_AUTHORITY,TasksContract.TABLE_NAME,TASKS);
        matcher.addURI(CONTENT_AUTHORITY,TasksContract.TABLE_NAME+"/#",TASKS_ID);

//        uriMatcher.addURI(CONTENT_AUTHORITY,TimerContract.TABLE_NAME,TIMER);
//        uriMatcher.addURI(CONTENT_AUTHORITY,TimerContract.TABLE_NAME+"/#",TIMER_ID);
//
//        uriMatcher.addURI(CONTENT_AUTHORITY,DurationContract.TABLE_NAME,DURATION);
//        uriMatcher.addURI(CONTENT_AUTHORITY,DurationContract.TABLE_NAME+"/#",DURATION_ID);

        return matcher;

    }

    @Override
    public boolean onCreate() {
        this.appDatabase = AppDatabase.getDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int matching = uriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (matching){
            case TASKS:
                queryBuilder.setTables(TasksContract.TABLE_NAME);
                break;
            case TASKS_ID:
                queryBuilder.setTables(TasksContract.TABLE_NAME);
                queryBuilder.appendWhere(TasksContract.Columns._ID+"="+TasksContract.getTasksId(uri));
                break;
//            case TIMER:
//                queryBuilder.setTables(TimerContract.TABLE_NAME);
//                queryBuilder.appendWhere(TimerContract.Columns._ID+"="+TimerContract.getTimingId(uri));
//                break;
//            case DURATION:
//                queryBuilder.setTables(DurationContract.TABLE_NAME);
//                queryBuilder.appendWhere(DurationContract.Columns._ID+"="+DurationContract.getDurationId(uri));
//                break;

            default:
                throw new IllegalArgumentException("Unknown ! "+uri);
        }
        SQLiteDatabase db = appDatabase.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);
        switch(match){
            case TASKS:
                return TasksContract.CONTENT_TYPE;
            case TASKS_ID:
                return TasksContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unkown Uri! "+uri.toString());

        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = appDatabase.getWritableDatabase();
        Uri replaceUri;
        long count;
        switch (match){
            case TASKS:
                count =db.insert(TasksContract.TABLE_NAME,null,values);
                replaceUri = TasksContract.buildTasksUri(count);
                break;
            default:
                throw new android.database.SQLException("Failed to insert !  >> "+uri.toString());
        }

        if(count >0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return replaceUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match  = uriMatcher.match(uri);
        SQLiteDatabase db = appDatabase.getWritableDatabase();
        int count=0;
        String customSelection="";
        switch (match){
            case TASKS:
                count =db.delete(TasksContract.TABLE_NAME,selection,selectionArgs);
                break;
            case TASKS_ID:
                customSelection = TasksContract.Columns._ID +"="+TasksContract.getTasksId(uri);
                if(selection != null && selection.length() > 0 ){
                    customSelection += "AND ("+selection+")";
                }
                count = db.delete(TasksContract.TABLE_NAME,customSelection,selectionArgs);
                break;

            default:
                throw new android.database.SQLException("Failed to Delete ! >>"+uri.toString());

        }

        if(count >0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        String customSelection;
        SQLiteDatabase db = appDatabase.getWritableDatabase();
        int count=0;

        switch (match){
            case TASKS:
                count = db.update(TasksContract.TABLE_NAME,values,selection,selectionArgs);
                break;
            case TASKS_ID:
                customSelection = TasksContract.Columns._ID+"="+TasksContract.getTasksId(uri);
                if(selection !=null && selection.length() > 0){
                    customSelection += "AND ("+selection+")";
                }
                count = db.update(TasksContract.TABLE_NAME,values,customSelection,selectionArgs);
                break;

            default:
                throw new android.database.SQLException("Failed Updating! Unknown Uri >>"+uri.toString());

        }

        if(count >0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }
}
