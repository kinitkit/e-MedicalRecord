package com.example.kinit.e_medicalrecord.Classes.Vaccination;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Vaccination {

    public int id, patientId, userDataId;
    public String providerName, strDateTaken, strNextSchedule, placeTaken;
    public boolean status;
    public Calendar calendar, nxtSchedule;
    public Vaccine vaccine;

    public Vaccination(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            patientId = jsonObject.getInt("patient_id");
            userDataId = jsonObject.getInt("user_data_id");
            providerName = jsonObject.getString("provider_name");
            setDate(jsonObject.getString("date_taken"));
            placeTaken = jsonObject.getString("place_taken");
            status = jsonObject.getString("status").equals("1");
            vaccine = new Vaccine(jsonObject);
            if (vaccine.vaccineScheduleId != 0) {
                nxtSchedule = calendar;
                nxtSchedule.add(Calendar.YEAR, vaccine.frequency);
                strNextSchedule = new SimpleDateFormat("MMM dd, yyyy").format(nxtSchedule.getTime());
            }
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
