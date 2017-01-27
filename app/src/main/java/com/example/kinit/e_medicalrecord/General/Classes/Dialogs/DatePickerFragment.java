package com.example.kinit.e_medicalrecord.General.Classes.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {
    DatePickerDialog datePickerDialog;

    Calendar calendar, maxCalendar, minCalendar;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), onDateSetListener, yy, mm, dd);
        datePickerDialog.getDatePicker().setMaxDate((maxCalendar == null) ? new Date().getTime() : maxCalendar.getTimeInMillis());
        if(minCalendar != null){
            datePickerDialog.getDatePicker().setMinDate(minCalendar.getTimeInMillis());
        }
        return datePickerDialog;
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    public void setMaxCalendar(Calendar calendar){
        this.maxCalendar = calendar;
    }

    public void setMinCalendar(Calendar calendar){
        this.minCalendar = calendar;
    }

    public void setCurrentDate(Calendar calendar) {
        this.calendar = calendar;
    }

}
