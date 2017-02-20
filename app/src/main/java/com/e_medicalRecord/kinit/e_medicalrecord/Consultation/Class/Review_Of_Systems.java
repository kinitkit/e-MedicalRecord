package com.e_medicalRecord.kinit.e_medicalrecord.Consultation.Class;

import org.json.JSONObject;

public class Review_Of_Systems {
    public int rosId, rosCategoryId;
    public String item, category;

    public Review_Of_Systems(JSONObject jsonObject) {
        try {
            rosId = jsonObject.getInt("ros_id");
            rosCategoryId = jsonObject.getInt("ros_category_id");
            item = jsonObject.getString("item");
            category = jsonObject.getString("title");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Review_Of_Systems() {
    }
}
