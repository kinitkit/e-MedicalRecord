package com.example.kinit.e_medicalrecord.Vaccination.Bus;

import com.example.kinit.e_medicalrecord.Vaccination.Class.Vaccination;

public class Bus_Vaccination_OnLongClick {
    public int position;
    public Vaccination vaccination;

    public Bus_Vaccination_OnLongClick(int position, Vaccination vaccination) {
        this.position = position;
        this.vaccination = vaccination;
    }
}
