package com.example.kinit.e_medicalrecord.BusStation.Admission;

import com.example.kinit.e_medicalrecord.Classes.Admission.Admission;

public class Bus_Admission_OnLongClick {
    public int position;
    public Admission admission;

    public Bus_Admission_OnLongClick(int position, Admission admission) {
        this.position = position;
        this.admission = admission;
    }
}
