package com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs;

import com.e_medicalRecord.kinit.e_medicalrecord.R;

import android.content.Context;
import android.support.v7.app.AlertDialog;

public class Custom_AlertDialog {
    public AlertDialog.Builder builder;
    AlertDialog alertDialog;

    public Custom_AlertDialog(Context context) {
        builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        setPositiveButton();
    }

    public void show(String title, String msg) {
        builder.setTitle(title);
        builder.setMessage(msg);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void setPositiveButton() {
        builder.setPositiveButton("OK", null);
    }

    public void setNegativeButton() {
        builder.setNegativeButton("Cancel", null);
    }

    public void dismiss() {
        alertDialog.dismiss();
    }
}
