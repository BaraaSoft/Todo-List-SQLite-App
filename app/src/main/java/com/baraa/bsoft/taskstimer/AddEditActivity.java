package com.baraa.bsoft.taskstimer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class AddEditActivity extends AppCompatActivity implements AddEditActivityFragment.OnSaveClickListener,
                                                                    AppDialogs.DialogEvents{
    private static final int CANCEL_DIALOG_ID =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AddEditActivityFragment addEditActivityFragment = new AddEditActivityFragment();
        Bundle bundle = getIntent().getExtras();
        addEditActivityFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment,addEditActivityFragment)
                .commit();

    }

    @Override
    public void onSavedClicked() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if(AddEditActivityFragment.STATE == AddEditActivityFragment.FragmentAddEditState.CHANGE) {
            showCancelDialog();
        }else{
            super.onBackPressed();
        }
    }

    private void showCancelDialog(){
        AppDialogs appDialogs = new AppDialogs();
        Bundle bundle = new Bundle();
        bundle.putInt(AppDialogs.DIALOG_ID, CANCEL_DIALOG_ID);
        bundle.putInt(AppDialogs.DIALOG_ACCEPT, R.string.Dialog_addEdite_continue);
        bundle.putInt(AppDialogs.DIALOG_REJECT, R.string.dailog_back);
        bundle.putString(AppDialogs.DIALOG_MESSAGE, "Your current task will be lost, Do you want to Back ?");
        appDialogs.setArguments(bundle);
        appDialogs.show(getFragmentManager(), null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                if(AddEditActivityFragment.STATE == AddEditActivityFragment.FragmentAddEditState.CHANGE) {
                    showCancelDialog();
                    return true;
                }else{
                    return super.onOptionsItemSelected(item);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    };

    // *************  Dialog Events Section **************** //

    @Override
    public void onPositiveResponse(int id, Bundle args) {
        switch (id){
            case CANCEL_DIALOG_ID:
                // do nothing;
                break;
        }

    }

    @Override
    public void onNegativeResponse(int id, Bundle args) {
        switch (id){
            case CANCEL_DIALOG_ID:
                finish();
                break;
        }

    }

    @Override
    public void onCancel(int id) {

    }
}
