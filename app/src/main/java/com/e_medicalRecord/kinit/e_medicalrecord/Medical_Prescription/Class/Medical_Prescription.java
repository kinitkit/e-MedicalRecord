package com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Class;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Medical_Prescription {
    public int medicalPrescription_id, patient_id, user_data_id;
    public String clinic_name, physicianName, date, calendarStr;

    public Medical_Prescription(JSONObject jsonObject) {
        try {
            if(jsonObject.has("id")) {
                medicalPrescription_id = Integer.parseInt(jsonObject.getString("id"));
            }
            patient_id = Integer.parseInt(jsonObject.getString("patient_id"));
            user_data_id = Integer.parseInt(jsonObject.getString("user_data_id"));
            clinic_name = jsonObject.getString("clinic_name");
            physicianName = jsonObject.getString("physician_name");
            setDateAndTime(jsonObject.getString("date_prescribed"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDateAndTime(String date) {
        calendarStr = date;
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            this.date = new SimpleDateFormat("MMM dd, yyyy").format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
