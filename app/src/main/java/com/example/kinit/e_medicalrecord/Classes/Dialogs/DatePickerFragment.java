package com.example.kinit.e_medicalrecord.Classes.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {
    DatePickerDialog datePickerDialog;
    Calendar calendar;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), onDateSetListener, yy, mm, dd);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        return datePickerDialog;
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    public void setCurrentDate(Calendar calendar) {
        this.calendar = calendar;
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener;

}
