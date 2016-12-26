package com.example.kinit.e_medicalrecord.Classes.My_Physician;

import org.json.JSONObject;

public class Physician_List {
    public int id, patientId, medicalStaffId, userDataId;
    public String name;

    public Physician_List(int id, int patientId, int medicalStaffId, String fName, String mName, String lName) {
        this.id = id;
        this.patientId = patientId;
        this.medicalStaffId = medicalStaffId;
        setName(fName, mName, lName);
    }

    public Physician_List(JSONObject jsonObject) {
        try {
            if (jsonObject.has("id"))
                this.id = jsonObject.getInt("id");
            if (jsonObject.has("patient_id"))
                this.patientId = jsonObject.getInt("patient_id");
            this.medicalStaffId = jsonObject.getInt("medical_staff_id");
            setName(jsonObject.getString("first_name"), jsonObject.getString("middle_name"), jsonObject.getString("last_name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setName(String firstName, String middleName, String lastName) {
        this.name = firstName + " " + middleName + " " + lastName;
    }
}
