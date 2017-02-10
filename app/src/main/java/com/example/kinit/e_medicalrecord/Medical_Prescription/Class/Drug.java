package com.example.kinit.e_medicalrecord.Medical_Prescription.Class;

import org.json.JSONObject;

public class Drug {
    public String drug, strength, dosage, route, frequency, why, quantity;

    public Drug(String drug, String strength, String dosage, String route, String frequency, String why, String quantity) {
        this.drug = drug;
        this.strength = strength;
        this.dosage = dosage;
        this.route = route;
        this.frequency = frequency;
        this.why = why;
        this.quantity = quantity;
    }

    public Drug(JSONObject jsonObject) {
        try{
            this.drug = jsonObject.getString("drug");
            this.strength = jsonObject.getString("strength");
            this.dosage = jsonObject.getString("dosage");
            this.route = jsonObject.getString("route");
            this.frequency = jsonObject.getString("frequency");
            this.why = jsonObject.getString("indication");
            this.quantity = jsonObject.getString("how_many");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
