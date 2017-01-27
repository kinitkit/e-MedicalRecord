package com.example.kinit.e_medicalrecord.My_Physician.Bus;

public class Bus_Search_Physician {
    public String name;
    public int user_id;
    public int patient_id;

    public Bus_Search_Physician(String name, int user_id, int patient_id) {
        this.name = name;
        this.user_id = user_id;
        this.patient_id = patient_id;
    }
}
