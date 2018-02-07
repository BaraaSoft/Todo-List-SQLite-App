package com.baraa.bsoft.taskstimer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditActivityFragment extends Fragment {
    private EditText edName;
    private EditText edDescription;
    private EditText edSortOrder;
    private Button btnSave;
    private Task task;
    private OnSaveClickListener onSaveClickListener;


    public enum FragmentAddEditMode{ADD,EDIT};
    public enum FragmentAddEditState{NO_CHANGE,CHANGE};
    public View view;
    private FragmentAddEditMode mode;
    public static FragmentAddEditState STATE;

    interface OnSaveClickListener{
        void onSavedClicked();
    }

    public AddEditActivityFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if(!(activity instanceof OnSaveClickListener)){
            throw new ClassCastException("Error! Activity must implements: OnSaveClickListener");
        }
        onSaveClickListener = (OnSaveClickListener)getActivity();
        STATE = FragmentAddEditState.NO_CHANGE;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSaveClickListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit, container, false);
        this.edName = (EditText) view.findViewById(R.id.edName_addEdit);
        this.edDescription = (EditText)view.findViewById(R.id.edDescription_addEdit);
        this.edSortOrder = (EditText)view.findViewById(R.id.edSortOrder_addEdit);
        this.btnSave = (Button)view.findViewById(R.id.btnSave_addEdit);

        // = getActivity().getIntent().getExtras();
//        if(getArguments() != null){
//            bundle = getArguments();
//        }else {
//            bundle = getActivity().getIntent().getExtras();
//        }
        Bundle bundle = getArguments();
        if(bundle !=null){
            task =(Task) bundle.getSerializable(Task.class.getSimpleName());
            if(task !=null){
                edName.setText(task.getName());
                edDescription.setText(task.getDescription());
                edSortOrder.setText(Integer.toString(task.getSortOrder()));
                mode = FragmentAddEditMode.EDIT;
                Log.d(TAG, "onCreateView: Edit Mode !");

            }else{
                mode = FragmentAddEditMode.ADD;
                Log.d(TAG, "onCreateView: Add Mode !");
            }
        }else {
            mode = FragmentAddEditMode.ADD;
            Log.d(TAG, "onCreateView: Add Mode !");
        }

        //this.checkChange();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Saved");
                ContentResolver contentResolver = getActivity().getContentResolver();
                ContentValues values = new ContentValues();
                switch (mode){
                    case EDIT:
                        if(!edName.getText().toString().equals(task.getName().toString())){
                            values.put(TasksContract.Columns.TASKS_NAME,edName.getText().toString());
                        }
                        if(!edDescription.getText().toString().equals(task.getDescription().toString())){
                            values.put(TasksContract.Columns.TASKS_DESCRIPTION,edDescription.getText().toString());
                        }
                        if(! new Integer(task.getSortOrder()).equals(Integer.parseInt(edSortOrder.getText().toString()))){
                            values.put(TasksContract.Columns.TASKS_SORTORDER,edSortOrder.getText().toString());
                        }
                        if(values.size() >0){
                            contentResolver.update(TasksContract.buildTasksUri(task.getId()),values,null,null);
                        }
                        Log.d(TAG, "onClick: new item to be Edited!");
                        break;
                    case ADD:
                        if(edName.getText().length()>0){
                            values.put(TasksContract.Columns.TASKS_NAME,edName.getText().toString());
                            values.put(TasksContract.Columns.TASKS_DESCRIPTION,edDescription.getText().toString());
                            values.put(TasksContract.Columns.TASKS_SORTORDER,edSortOrder.getText().toString());
                            contentResolver.insert(TasksContract.CONTENT_URI,values);
                            STATE = FragmentAddEditState.CHANGE;
                            Log.d(TAG, "onClick: new item to be Add!");
                        }
                        break;
                }
                AddEditActivityFragment.this.onSaveClickListener.onSavedClicked();
            }
        });

        TextWatcher textWatcher =   new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                AddEditActivityFragment.this.checkChange();
            }
        };

        edName.addTextChangedListener(textWatcher);
        edSortOrder.addTextChangedListener(textWatcher);
        edDescription.addTextChangedListener(textWatcher);


        return view;
    }
    public boolean canCancel(){
        return false;
    }
    private void checkChange(){

        if(task != null){
            if(!edName.getText().toString().equals(task.getName().toString())){
                STATE = FragmentAddEditState.CHANGE;
            }
            if(!edDescription.getText().toString().equals(task.getDescription().toString())){
                STATE = FragmentAddEditState.CHANGE;
            }
            if( task.getSortOrder() != Integer.parseInt(edSortOrder.getText().toString())){
                STATE = FragmentAddEditState.CHANGE;
            }

        }


    }

}
