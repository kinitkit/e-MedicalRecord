package com.example.kinit.e_medicalrecord.Classes.Laboratory;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Lab_Chemistry {
    public static final String TABLE_NAME = "lab_chemistry";
    public int id, userDataId;
    public String physicianName, labName, datePerformed, remark,
            fbs, creatine, cholesterol, triglycerides, hdl, ldl, uricAcid, sgpt_alat, sodium, potassium, calcium;
    public Calendar calendar = Calendar.getInstance();

    public void setDate(String date) {
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            this.datePerformed = new SimpleDateFormat("MMM dd, yyyy").format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Lab_Chemistry() {
    }

    public Lab_Chemistry(JSONObject jsonObject) {
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
        try {
            id = jsonObject.getInt("id");
            userDataId = jsonObject.getInt("user_data_id");
            physicianName = jsonObject.getString("physician_name");
            labName = jsonObject.getString("lab_name");
            setDate(jsonObject.getString("date_performed"));
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
            remark = jsonObject.getString("remark");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
