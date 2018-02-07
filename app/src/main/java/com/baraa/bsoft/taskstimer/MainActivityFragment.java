package com.baraa.bsoft.taskstimer;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.security.InvalidParameterException;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final int LOADER_ID =1992;
    private CursorRecyclerViewAdapter cursorAdapter;

    public MainActivityFragment() {
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID,null,this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.rvTasksList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cursorAdapter = new CursorRecyclerViewAdapter(null,(CursorRecyclerViewAdapter.ItemListClickListener) getActivity());
        recyclerView.setAdapter(cursorAdapter);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[]projects = {TasksContract.Columns._ID,
                TasksContract.Columns.TASKS_NAME,
                TasksContract.Columns.TASKS_DESCRIPTION,
                TasksContract.Columns.TASKS_SORTORDER};
        String sortOrder = TasksContract.Columns.TASKS_SORTORDER +","+TasksContract.Columns.TASKS_NAME+" collate nocase";
        switch (id){
            case LOADER_ID:
                return new CursorLoader(getActivity(),TasksContract.CONTENT_URI,projects,null,null,sortOrder);
            default:
                throw new InvalidParameterException("Loader ID Not found!");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: Cursor DATA : " +loader.getId());
        cursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        cursorAdapter.swapCursor(null);
    }
}
