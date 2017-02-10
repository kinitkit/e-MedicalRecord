package com.example.kinit.e_medicalrecord.Medical_Prescription.Bus;

import com.example.kinit.e_medicalrecord.General.Enum.Query_Type;

public class Bus_Open_Add_Drug {
    public String drug, strength, amount, route, frequency, why, many;
    public Query_Type queryType;
    public int position;

    public Bus_Open_Add_Drug(String drug, String strength, String amount, String route, String frequency, String why, String many, Query_Type queryType, int position) {
        this.drug = drug;
        this.strength = strength;
        this.amount = amount;
        this.route = route;
        this.frequency = frequency;
        this.why = why;
        this.many = many;
        this.queryType = queryType;
        this.position = position;
    }

    public Bus_Open_Add_Drug() {
    }
}
