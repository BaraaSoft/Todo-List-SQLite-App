package com.baraa.bsoft.taskstimer;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.baraa.bsoft.taskstimer.AppProvider.CONTENT_AUTHORITY;
import static com.baraa.bsoft.taskstimer.AppProvider.CONTENT_AUTHORITY_URI;

/**
 * Created by baraa on 29/06/2017.
 */

public class TasksContract {
    public static final String TABLE_NAME = "Tasks";

    public static class Columns{
        private Columns(){

        }
        public static final String _ID = BaseColumns._ID;
        public static final String TASKS_NAME = "Name";
        public static final String TASKS_DESCRIPTION = "Description";
        public static final String TASKS_SORTORDER = "SortOrder";
    }

    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI,TABLE_NAME);
    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;

    public static Uri buildTasksUri(long id){
        return ContentUris.withAppendedId(CONTENT_URI,id);
    }
    public static long getTasksId(Uri uri){
        return ContentUris.parseId(uri);
    }
}
