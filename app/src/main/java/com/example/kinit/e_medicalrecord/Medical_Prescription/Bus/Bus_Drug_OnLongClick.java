package com.example.kinit.e_medicalrecord.Medical_Prescription.Bus;

import com.example.kinit.e_medicalrecord.Medical_Prescription.Class.Drug;

public class Bus_Drug_OnLongClick {
    public int position;
    public Drug drug;

    public Bus_Drug_OnLongClick(int position, Drug drug) {
        this.position = position;
        this.drug = drug;
    }
}
