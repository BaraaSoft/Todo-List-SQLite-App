//package com.baraa.bsoft.taskstimer;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//
///**
// * Created by baraa on 07/08/2017.
// */
//
//public class AppDialog extends DialogFragment {
//    private DialogEvents dialogEvents;
//    public static final String DIALOG_MESSAGE="DIALOG_MESSAGE";
//    public static final String DIALOG_ACCEPT="DIALOG_ACCEPT";
//    public static final String DIALOG_REJECT="DIALOG_REJECT";
//    public static final String DIALOG_ID="DIALOG_ID";
//    interface DialogEvents{
//        void onDialogAcceptResponse(int dialogId,Bundle args);
//        void onDialogRejectResponse(int dialogId,Bundle args);
//        void onDialogCancel(int dialogId);
//    }
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if(!(context instanceof DialogEvents)){
//            throw new ClassCastException("Error! Class: "+context.toString()+"\nClass must implements DialogEvents Interface !");
//        }
//        this.dialogEvents = (DialogEvents) context;
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        if(dialogEvents !=null)
//            dialogEvents = null;
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        final Bundle bundle = getArguments();
//        if(bundle == null){
//            throw new IllegalArgumentException("Error! Dialog info is not define...");
//        }
//        final int dialogId = bundle.getInt(DIALOG_ID);
//        int acceptRID = bundle.getInt(DIALOG_ACCEPT);
//        if(acceptRID == 0)
//            acceptRID = R.string.dialog_ok;
//        int rejectRID = bundle.getInt(DIALOG_REJECT);
//        if(rejectRID == 0)
//            rejectRID = R.string.dialog_cancel;
//        String message = bundle.getString(DIALOG_MESSAGE);
//
//        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
//        builder.setMessage(message).setPositiveButton(acceptRID, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialogEvents.onDialogAcceptResponse(dialogId,bundle);
//            }
//        })
//        .setNegativeButton(rejectRID, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialogEvents.onDialogRejectResponse(dialogId,bundle);
//            }
//        });
//
//        return builder.create();
//    }
//
//    @Override
//    public void onCancel(DialogInterface dialog) {
//        super.onCancel(dialog);
//        int dialogId = getArguments().getInt(DIALOG_ID);
//        dialogEvents.onDialogCancel(dialogId);
//    }
//
//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//    }
//}
