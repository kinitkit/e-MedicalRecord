package com.example.kinit.e_medicalrecord.Classes.My_Physician;

import org.json.JSONObject;

public class Physician_List {
    public int id, patientId, medicalStaffId, userDataId;
    public String name, image;

    public Physician_List(JSONObject jsonObject) {
        try {
            if (jsonObject.has("id"))
                this.id = jsonObject.getInt("id");
            if (jsonObject.has("patient_id"))
                this.patientId = jsonObject.getInt("patient_id");
            this.medicalStaffId = jsonObject.getInt("medical_staff_id");
            name = jsonObject.getString("full_name");
            image = jsonObject.getString("image");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
