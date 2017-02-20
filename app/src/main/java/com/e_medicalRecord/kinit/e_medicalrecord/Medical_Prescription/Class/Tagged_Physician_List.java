package com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Class;

import org.json.JSONObject;

public class Tagged_Physician_List {
    public int id, medical_staff_id;
    public String name, image;

    public Tagged_Physician_List(JSONObject jsonObject) {
        try {
            if (jsonObject.has("id")) {
                id = jsonObject.getInt("id");
            }
            name = jsonObject.getString("full_name");
            image = jsonObject.getString("image");
            medical_staff_id = jsonObject.getInt("medical_staff_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
