package com.example.kinit.e_medicalrecord.My_Physician.Bus;

import com.example.kinit.e_medicalrecord.My_Physician.Class.Physician_List;

public class Bus_Remove_Physician {
    public int position;
    public Physician_List physicianList;

    public Bus_Remove_Physician(int position, Physician_List physicianList) {
        this.position = position;
        this.physicianList = physicianList;
    }
}
