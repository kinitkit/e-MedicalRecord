package com.example.kinit.e_medicalrecord.Classes.Medical_Prescription;

import android.util.Log;

import org.json.JSONObject;

public class Tagged_Physician_List {
    public int id, medical_staff_id;
    public String name;

    public Tagged_Physician_List(JSONObject jsonObject) {
        try {
            if (jsonObject.has("id")) {
                id = jsonObject.getInt("id");
            }
            medical_staff_id = jsonObject.getInt("medical_staff_id");
            name = jsonObject.getString("first_name") + " " + jsonObject.getString("middle_name") + " " + jsonObject.getString("last_name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
