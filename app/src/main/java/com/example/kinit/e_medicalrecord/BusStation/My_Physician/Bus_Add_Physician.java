package com.example.kinit.e_medicalrecord.BusStation.My_Physician;

import com.example.kinit.e_medicalrecord.Classes.My_Physician.Physician_List;

public class Bus_Add_Physician {
    public int position;
    public Physician_List physicianList;

    public Bus_Add_Physician(int position, Physician_List physicianList) {
        this.position = position;
        this.physicianList = physicianList;
    }
}
