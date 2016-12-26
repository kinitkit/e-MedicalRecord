package com.example.kinit.e_medicalrecord.Classes.Laboratory;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Lab_Hematology {
    public int id, userDataId;
    public double hemoglobin, hematrocit, rbc, wbc, platelets, reticulocytes, mcv, mch, mchc, esr, segmenters, stab, lymphocytes,
            monocytes, eosinophils, basophils;
    public String physicianName, labName, datePerformed, malarialSmear, bleedingTime, clottingTime, bloodType, rh, remark;
    public Calendar calendar = Calendar.getInstance();

    public void setDate(String date) {
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            this.datePerformed = new SimpleDateFormat("MMM dd, yyyy").format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Lab_Hematology() {
    }

    public Lab_Hematology(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            userDataId = jsonObject.getInt("user_data_id");
            physicianName = jsonObject.getString("physician_name");
            labName = jsonObject.getString("lab_name");
            setDate(jsonObject.getString("date_performed"));
            hemoglobin = jsonObject.getDouble("hemoglobin");
            hematrocit = jsonObject.getDouble("hematrocit");
            rbc = jsonObject.getDouble("RBC");
            wbc = jsonObject.getDouble("WBC");
            platelets = jsonObject.getDouble("platelets");
            reticulocytes = jsonObject.getDouble("reticulocytes");
            mcv = jsonObject.getDouble("MCV");
            mch = jsonObject.getDouble("MCH");
            mchc = jsonObject.getDouble("MCHC");
            esr = jsonObject.getDouble("ESR");
            segmenters = jsonObject.getDouble("segmenters");
            stab = jsonObject.getDouble("stab");
            lymphocytes = jsonObject.getDouble("lymphocytes");
            monocytes = jsonObject.getDouble("monocytes");
            eosinophils = jsonObject.getDouble("eosinophils");
            basophils = jsonObject.getDouble("basophils");
            malarialSmear = jsonObject.getString("malarial_smear");
            bleedingTime = jsonObject.getString("bleeding_time");
            clottingTime = jsonObject.getString("clotting_time");
            bloodType = jsonObject.getString("blood_type");
            rh = jsonObject.getString("rh");
            remark = jsonObject.getString("remark");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
