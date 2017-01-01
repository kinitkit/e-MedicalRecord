package com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription;

import com.example.kinit.e_medicalrecord.Classes.Medical_Prescription.Tagged_Physician_List;

public class Bus_Remove_Physician {
    public int position;
    public Tagged_Physician_List taggedPhysicianList;

    public Bus_Remove_Physician(int position, Tagged_Physician_List taggedPhysicianList) {
        this.position = position;
        this.taggedPhysicianList = taggedPhysicianList;
    }
}
