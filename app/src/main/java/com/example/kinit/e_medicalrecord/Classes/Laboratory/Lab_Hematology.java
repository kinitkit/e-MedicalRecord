package com.example.kinit.e_medicalrecord.Classes.Laboratory;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Lab_Hematology {
    public static final String TABLE_NAME = "lab_hematology";
    public int id, userDataId;
    public String physicianName, labName, datePerformed, malarialSmear, bleedingTime, clottingTime, bloodType, rh, remark,
            hemoglobin, hematrocit, rbc, wbc, platelets, reticulocytes, mcv, mch, mchc, esr, segmenters, stab, lymphocytes,
            monocytes, eosinophils, basophils;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setValues(JSONObject jsonObject) {
        try{
            hemoglobin = jsonObject.getString("hemoglobin");
            hematrocit = jsonObject.getString("hematrocit");
            rbc = jsonObject.getString("RBC");
            wbc = jsonObject.getString("WBC");
            platelets = jsonObject.getString("platelets");
            reticulocytes = jsonObject.getString("reticulocytes");
            mcv = jsonObject.getString("MCV");
            mch = jsonObject.getString("MCH");
            mchc = jsonObject.getString("MCHC");
            esr = jsonObject.getString("ESR");
            segmenters = jsonObject.getString("segmenters");
            stab = jsonObject.getString("stab");
            lymphocytes = jsonObject.getString("lymphocytes");
            monocytes = jsonObject.getString("monocytes");
            eosinophils = jsonObject.getString("eosinophils");
            basophils = jsonObject.getString("basophils");
            malarialSmear = jsonObject.getString("malarial_smear");
            bleedingTime = jsonObject.getString("bleeding_time");
            clottingTime = jsonObject.getString("clotting_time");
            bloodType = jsonObject.getString("blood_type");
            rh = jsonObject.getString("rh");
            remark = jsonObject.getString("remark");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
