package com.example.kinit.e_medicalrecord.Classes.Laboratory;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Lab_Chemistry extends Laboratory {
    public static final String TABLE_NAME = "lab_chemistry";
    public int id;
    public String fbs, creatine, cholesterol, triglycerides, hdl, ldl, uricAcid, sgpt_alat, sodium, potassium, calcium;

    public Lab_Chemistry(JSONObject jsonObject) {
        super(jsonObject);
        try {
            this.id = jsonObject.getInt("id");
            fbs = jsonObject.getString("FBS");
            creatine = jsonObject.getString("creatinine");
            cholesterol = jsonObject.getString("cholesterol");
            triglycerides = jsonObject.getString("triglycerides");
            hdl = jsonObject.getString("HDL");
            ldl = jsonObject.getString("LDL");
            uricAcid = jsonObject.getString("uric_acid");
            sgpt_alat = jsonObject.getString("SGPT_ALAT");
            sodium = jsonObject.getString("sodium");
            potassium = jsonObject.getString("potassium");
            calcium = jsonObject.getString("calcium");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
