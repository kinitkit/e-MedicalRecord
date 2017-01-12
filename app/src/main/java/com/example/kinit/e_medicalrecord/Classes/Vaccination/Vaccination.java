package com.example.kinit.e_medicalrecord.Classes.Vaccination;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Vaccination {

    public int id, patientId, userDataId, vaccinationId;
    public String providerName, strDateTaken, placeTaken, vaccine;
    public boolean status;
    public Calendar calendar;

    public Vaccination(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            patientId = jsonObject.getInt("patient_id");
            userDataId = jsonObject.getInt("user_data_id");
            vaccinationId = jsonObject.getInt("vaccine_id");
            vaccine = jsonObject.getString("item");
            providerName = jsonObject.getString("provider_name");
            setDate(jsonObject.getString("date_taken"));
            placeTaken = jsonObject.getString("place_taken");
            status = jsonObject.getString("status").equals("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setDate(String date) {
        try {
            calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            this.strDateTaken = new SimpleDateFormat("MMM dd, yyyy").format(calendar.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
