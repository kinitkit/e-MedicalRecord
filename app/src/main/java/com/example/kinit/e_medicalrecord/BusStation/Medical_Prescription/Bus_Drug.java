package com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription;

import com.example.kinit.e_medicalrecord.Enum.Query_Type;

public class Bus_Drug {
    public String drug, strength, amount, route, frequency, why, many, refill;
    public Query_Type queryType;
    public int position;

    public Bus_Drug(String drug, String strength, String amount, String route, String frequency, String why, String many, String refill, Query_Type queryType, int position) {
        this.drug = drug;
        this.strength = strength;
        this.amount = amount;
        this.route = route;
        this.frequency = frequency;
        this.why = why;
        this.many = many;
        this.refill = refill;
        this.queryType = queryType;
        this.position = position;
    }

    public Bus_Drug(String drug, String strength, String amount, String route, String frequency, String why, String many, String refill) {
        this.drug = drug;
        this.strength = strength;
        this.amount = amount;
        this.route = route;
        this.frequency = frequency;
        this.why = why;
        this.many = many;
        this.refill = refill;
    }
}
