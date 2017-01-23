package com.example.kinit.e_medicalrecord.Classes.Consultation;

import org.json.JSONObject;

public class Consultation_ROS extends Review_Of_Systems {
    public int consultationRosId;
    public String note;

    public Consultation_ROS(JSONObject jsonObject) {
        super(jsonObject);
        try {
            consultationRosId = jsonObject.getInt("consultation_ros_id");
            note = jsonObject.getString("note");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
