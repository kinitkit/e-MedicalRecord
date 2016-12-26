package com.example.kinit.e_medicalrecord.Classes.Laboratory;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Lab_Chemistry {
    public int id, userDataId;
    public String physicianName, labName, datePerformed, remark;
    public double fbs, creatine, cholesterol, triglycerides, hdl, ldl, uricAcid, sgpt_alat, sodium, potassium, calcium;
    public Calendar calendar = Calendar.getInstance();

    public void setDate(String date){
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
            fbs = jsonObject.getDouble("FBS");
            creatine = jsonObject.getDouble("creatinine");
            cholesterol = jsonObject.getDouble("cholesterol");
            triglycerides = jsonObject.getDouble("triglycerides");
            hdl = jsonObject.getDouble("HDL");
            ldl = jsonObject.getDouble("LDL");
            uricAcid = jsonObject.getDouble("uric_acid");
            sgpt_alat = jsonObject.getDouble("SGPT_ALAT");
            sodium = jsonObject.getDouble("sodium");
            potassium = jsonObject.getDouble("potassium");
            calcium = jsonObject.getDouble("calcium");
            remark = jsonObject.getString("remark");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
