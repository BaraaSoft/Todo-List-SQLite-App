package com.baraa.bsoft.taskstimer;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity implements CursorRecyclerViewAdapter.ItemListClickListener,
                                                                AddEditActivityFragment.OnSaveClickListener,
                                                                AppDialogs.DialogEvents{
    private static final String TAG = "MainActivity";
    private boolean twoPane = false;

    private static final String ADD_EDIT_FRAGMENT = "AddEditFragment";
    private static final int DELETE_DIALOG_ID = 1;
    public static final int ADD_EDIT_DIALOG_ID =2;
    public static final String TASK_ID = "TASK_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        AppDatabase appDataBase = AppDatabase.getDatabase(this);
//        SQLiteDatabase db = appDataBase.getReadableDatabase();

        ContentResolver contentResolver = getContentResolver();
//        #Insertion To The Database:
//
//        ContentValues values = new ContentValues();
//        values.put(TasksContract.Columns.TASKS_NAME,"Electrical Machine");
//        values.put(TasksContract.Columns.TASKS_DESCRIPTION,"study for the Tests");
//        values.put(TasksContract.Columns.TASKS_SORTORDER,2);
//        contentResolver.insert(TasksContract.CONTENT_URI,values);
//        #Update The Database:
//
//        ContentValues values = new ContentValues();
//        values.put(TasksContract.Columns.TASKS_DESCRIPTION,"Completed");
//        values.put(TasksContract.Columns.TASKS_SORTORDER,"99");
//        String selection = TasksContract.Columns.TASKS_SORTORDER + "="+"?";
//        String[] args = {"2"};
//        contentResolver.update(TasksContract.CONTENT_URI,values,selection,args);
//        #Delete from the Database:
//
//        String whereSelection = TasksContract.Columns.TASKS_SORTORDER +"= ?";
//        String[]args = {"99"};
//        contentResolver.delete(TasksContract.CONTENT_URI,whereSelection,args);


        String[] projections = {TasksContract.Columns._ID,
                TasksContract.Columns.TASKS_NAME,
                TasksContract.Columns.TASKS_DESCRIPTION,
                TasksContract.Columns.TASKS_SORTORDER};

        Cursor cursor = contentResolver.query(TasksContract.CONTENT_URI,projections,null,null,TasksContract.Columns.TASKS_SORTORDER);
        if(cursor !=null){
            while(cursor.moveToNext()){
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    Log.d(TAG, "onCreate:  "+cursor.getColumnName(i)+" : "+cursor.getString(i));
                }
                Log.d(TAG, "onCreate: ===============================\n");
            }
            cursor.close();
        }

        // looking weather the view is in landscape or not by checking if frameLayout exists
        if(findViewById(R.id.addEdit_frameLayout) != null){
            twoPane = true;
        }else {
            twoPane = false;
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.mnuMain_addTasks:
                taskEditRequest(null);
                break;
            case R.id.mnuMain_showDuration:
                break;
            case R.id.mnuMain_showAbout:
                break;
            case R.id.mnuMain_settings:
                break;
            case R.id.mnuMain_generate:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSavedClicked() {
        Log.d(TAG, "onSavedClicked: Clicked !");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.addEdit_frameLayout);
        if(fragment !=null){
            Log.d(TAG, "onSavedClicked: Edit Fragment Found!");
            fragmentManager.beginTransaction().remove(fragment).commit();
        }else {
            Log.d(TAG, "onSavedClicked: fragment not Exists!");
        }

    }

    @Override
    public void onDeleteListClicked(Task task) {
        AppDialogs appDialogs = new AppDialogs();
        Bundle bundle = new Bundle();
        bundle.putInt(AppDialogs.DIALOG_ID,DELETE_DIALOG_ID);
        bundle.putInt(appDialogs.DIALOG_ACCEPT,R.string.dialog_ok);
        bundle.putInt(appDialogs.DIALOG_REJECT,R.string.dialog_cancel);
        bundle.putString(appDialogs.DIALOG_MESSAGE,getString(R.string.dialog_deleteMessage,task.getId()));
        bundle.putInt(this.TASK_ID,task.getId());
        appDialogs.setArguments(bundle);
        appDialogs.show(getFragmentManager(),null);
    }

    @Override
    public void onEditListClicked(Task task) {
        taskEditRequest(task);
    }

    private void taskEditRequest(Task task){
        if(twoPane == true){
            Log.d(TAG, "taskEditRequest: Running in Tablet mode...");
            AddEditActivityFragment addEditActivityFragment = new AddEditActivityFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Task.class.getSimpleName(),task);
            addEditActivityFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.addEdit_frameLayout,addEditActivityFragment);
            //fragmentTransaction.add(R.id.addEdit_frameLayout,addEditActivityFragment);
            fragmentTransaction.commit();

        }else{
            Log.d(TAG, "taskEditRequest: Running in phone mode...");
            Intent intent = new Intent(this,AddEditActivity.class);
            if(task !=null){
                intent.putExtra(Task.class.getSimpleName(),task);
                Log.d(TAG, "taskEditRequest: Intet Name :"+Task.class.getSimpleName());
            }
            startActivity(intent);

        }

    }

// *************  Dialog Events Section **************** //
    @Override
    public void onPositiveResponse(int id, Bundle args) {
        if(BuildConfig.DEBUG && id == 0) throw new AssertionError("Task ID is 0 !");
        switch (id){
            case DELETE_DIALOG_ID:
                getContentResolver().delete(TasksContract.buildTasksUri(args.getInt(this.TASK_ID)),null,null);
                break;
            case ADD_EDIT_DIALOG_ID:
                finish();
                break;
        }

    }

    @Override
    public void onNegativeResponse(int id, Bundle args) {

    }

    @Override
    public void onCancel(int id) {

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        AddEditActivityFragment fragment = (AddEditActivityFragment)getSupportFragmentManager()
                .findFragmentById(R.id.addEdit_frameLayout);
        if(fragment ==null || fragment.canCancel()){
            super.onBackPressed();
        }else{
            AppDialogs appDialog = new AppDialogs();
            Bundle bundle = new Bundle();
            bundle.putInt(AppDialogs.DIALOG_ID,2);
            bundle.putInt(AppDialogs.DIALOG_ACCEPT,R.string.Dialog_addEdite_continue);
            bundle.putInt(AppDialogs.DIALOG_REJECT,R.string.dialog_cancel);
            bundle.putString(AppDialogs.DIALOG_MESSAGE,"Your current task will be lost, Do you want to continue ?");
            appDialog.setArguments(bundle);
            appDialog.show(getFragmentManager(),null);
        }

        //super.onBackPressed();
    }
}
