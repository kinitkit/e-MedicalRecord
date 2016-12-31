package com.example.kinit.e_medicalrecord.Classes.Medical_Prescription;

import org.json.JSONObject;

public class Drug_List {
    public int id, medical_prescription_id, refill, many;
    public String drug, strength, dosage, route, frequency, indication;

    public Drug_List() {

    }

    public Drug_List(JSONObject jsonObject) {
        try {
            id = Integer.parseInt(jsonObject.getString("id"));
            medical_prescription_id = Integer.parseInt(jsonObject.getString("medical_prescription_id"));
            drug = jsonObject.getString("drug");
            strength = jsonObject.getString("strength");
            dosage = jsonObject.getString("dosage");
            route = jsonObject.getString("route");
            frequency = jsonObject.getString("frequency");
            indication = jsonObject.getString("indication");
            many = Integer.parseInt(jsonObject.getString("how_many"));
            refill = Integer.parseInt(jsonObject.getString("refill"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
