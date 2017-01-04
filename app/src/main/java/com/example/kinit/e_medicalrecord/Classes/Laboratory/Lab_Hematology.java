package com.example.kinit.e_medicalrecord.Classes.Laboratory;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Lab_Hematology extends Laboratory {
    public static final String TABLE_NAME = "lab_hematology";
    public int id;
    public String malarialSmear, bleedingTime, clottingTime, bloodType, rh,
            hemoglobin, hematrocit, rbc, wbc, platelets, reticulocytes, mcv, mch, mchc, esr, segmenters, stab, lymphocytes,
            monocytes, eosinophils, basophils;

    public Lab_Hematology(JSONObject jsonObject) {
        super(jsonObject);
        try {
            this.id = jsonObject.getInt("id");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
