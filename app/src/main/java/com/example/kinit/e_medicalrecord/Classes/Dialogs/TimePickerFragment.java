package com.example.kinit.e_medicalrecord.Classes.Dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    TimePickerDialog timePickerDialog;
    TimePickerDialog.OnTimeSetListener onTimeSetListener;
    Calendar calendar;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle onSavedInstanceState){
        int hour = calendar.get(Calendar.HOUR_OF_DAY),
         min = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, min, true);
        timePickerDialog.setMessage("Select Time");
        return timePickerDialog;
    }
}
