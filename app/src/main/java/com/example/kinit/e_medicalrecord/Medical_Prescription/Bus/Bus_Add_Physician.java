package com.example.kinit.e_medicalrecord.Medical_Prescription.Bus;

import com.example.kinit.e_medicalrecord.Medical_Prescription.Class.Tagged_Physician_List;

public class Bus_Add_Physician {
    public int position;
    public Tagged_Physician_List taggedPhysicianList;

    public Bus_Add_Physician(int position, Tagged_Physician_List taggedPhysicianList) {
        this.position = position;
        this.taggedPhysicianList = taggedPhysicianList;
    }
}
