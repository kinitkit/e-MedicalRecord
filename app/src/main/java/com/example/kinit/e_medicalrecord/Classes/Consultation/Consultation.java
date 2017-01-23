package com.example.kinit.e_medicalrecord.Classes.Consultation;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Consultation {
    public int id, patientId, userDataId, respirationRate, pulseRate;
    public String physicianName, strDateTime, chiefComplaint, presentIllness, diagnosis, bloodPressure;
    public double height, weight, temperature;
    public boolean status;
    public Calendar dateTime;

    public Consultation(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            patientId = jsonObject.getInt("patient_id");
            userDataId = jsonObject.getInt("user_data_id");
            physicianName = jsonObject.getString("physician_name");
            setDateAndTime(jsonObject.getString("date_time"));
            chiefComplaint = jsonObject.getString("chief_complaint");
            presentIllness = jsonObject.getString("present_illness");
            height = jsonObject.getDouble("height");
            weight = jsonObject.getDouble("weight");
            bloodPressure = jsonObject.getString("blood_pressure");
            respirationRate = jsonObject.getInt("respiration_rate");
            temperature = jsonObject.getDouble("temperature");
            pulseRate = jsonObject.getInt("pulse_rate");
            status = jsonObject.getString("status").equals("1");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDateAndTime(String date) {
        try {
            dateTime = Calendar.getInstance();
            dateTime.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            this.strDateTime = new SimpleDateFormat("MMM dd, yyyy").format(dateTime.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
