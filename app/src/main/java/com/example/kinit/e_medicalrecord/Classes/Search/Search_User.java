package com.example.kinit.e_medicalrecord.Classes.Search;

import org.json.JSONObject;

public class Search_User {
    public int userId, medicalStaffId, patientId;
    public String fullName, image, userType;

    public Search_User(JSONObject jsonObject) {
        try {
            userId = jsonObject.getInt("id");
            fullName = jsonObject.getString("full_name");
            image = jsonObject.getString("image");
            if(jsonObject.has("medicalStaff_id")){
                medicalStaffId = jsonObject.getInt("medicalStaff_id");
                userType = jsonObject.getString("user_type");
            } else {
                patientId = jsonObject.getInt("patient_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
