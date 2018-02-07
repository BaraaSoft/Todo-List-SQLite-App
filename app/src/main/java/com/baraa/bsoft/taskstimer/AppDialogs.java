package com.baraa.bsoft.taskstimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;


/**
 * Created by baraa on 06/09/2017.
 */

public class AppDialogs extends DialogFragment {
    public static final String DIALOG_ACCEPT = "DIALOG_ACCEPT";
    public static final String DIALOG_REJECT = "DIALOG_REJECT";
    public static final String DIALOG_MESSAGE = "DIALOG_MESSAGE";
    public static final String DIALOG_ID = "DIALOG_ID";

    interface DialogEvents{
        void onPositiveResponse(int id,Bundle args);
        void onNegativeResponse(int id,Bundle args);
        void onCancel(int id);
    }
    private DialogEvents dialogEvents;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!(context instanceof DialogEvents)){
            throw new ClassCastException("Error! class must implement DialogEvents...");
        }
        this.dialogEvents = (DialogEvents)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.dialogEvents = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle bundle = getArguments();
        int posRID = bundle.getInt(DIALOG_ACCEPT);
        if(posRID ==0){
            posRID = R.string.dialog_ok;
        }
        int negRID = bundle.getInt(DIALOG_REJECT);
        if(negRID == 0){
            negRID = R.string.dialog_cancel;
        }
        String messageText = bundle.getString(DIALOG_MESSAGE);
        final int id = bundle.getInt(DIALOG_ID);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage(messageText)
                .setPositiveButton(posRID,   new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogEvents.onPositiveResponse(id,bundle);
                    }
                })
                .setNegativeButton(negRID, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogEvents.onNegativeResponse(id,bundle);
                    }
                });
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        int id  = getArguments().getInt(DIALOG_ID);
        dialogEvents.onCancel(id);
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        int id  = getArguments().getInt(DIALOG_ID);
        dialogEvents.onCancel(id);
        super.onDismiss(dialog);
    }
}
