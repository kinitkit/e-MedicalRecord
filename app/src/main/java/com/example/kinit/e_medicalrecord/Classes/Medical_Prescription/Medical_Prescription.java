package com.example.kinit.e_medicalrecord.Classes.Medical_Prescription;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Medical_Prescription {
    public int medicalPrescription_id, patient_id, user_data_id;
    public String clinic_name, physicianName, date;

    public void setDateAndTime(String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            this.date = new SimpleDateFormat("MMM dd, yyyy").format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
