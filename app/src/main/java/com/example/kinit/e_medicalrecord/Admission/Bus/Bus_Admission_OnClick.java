package com.example.kinit.e_medicalrecord.Admission.Bus;

import com.example.kinit.e_medicalrecord.Admission.Class.Admission;

public class Bus_Admission_OnClick {
    public int position;
    public Admission admission;

    public Bus_Admission_OnClick(int position, Admission admission) {
        this.position = position;
        this.admission = admission;
    }
}