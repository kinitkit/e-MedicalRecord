package com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs;

import android.app.ProgressDialog;
import android.content.Context;

public class Custom_ProgressDialog {
    ProgressDialog progressDialog;

    public Custom_ProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
    }

    public void show(String message){
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismiss(){
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
