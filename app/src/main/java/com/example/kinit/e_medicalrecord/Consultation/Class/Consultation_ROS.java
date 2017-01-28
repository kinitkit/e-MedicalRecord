package com.example.kinit.e_medicalrecord.Consultation.Class;

import org.json.JSONObject;

public class Consultation_ROS extends Review_Of_Systems {
    public int consultationRosId;
    public String remarks;

    public Consultation_ROS(JSONObject jsonObject) {
        super(jsonObject);
        try {
            if (jsonObject.has("consultation_ros_id")) {
                consultationRosId = jsonObject.getInt("consultation_ros_id");
            }
            remarks = jsonObject.getString("remarks");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Consultation_ROS(String remarks, int rosId, String item, int rosCategoryId, String category) {
        this.remarks = remarks;
        this.rosId = rosId;
        this.item = item;
        this.rosCategoryId = rosCategoryId;
        this.category = category;
    }

    public Consultation_ROS() {

    }
}
