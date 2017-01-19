package com.example.kinit.e_medicalrecord.BusStation.Surgical_History;

import com.example.kinit.e_medicalrecord.Classes.Surgical_History.Surgical_History;

public class Bus_SurgicalHistory_OnLongClick {
    public int position;
    public Surgical_History surgicalHistory;

    public Bus_SurgicalHistory_OnLongClick(int position, Surgical_History surgicalHistory) {
        this.surgicalHistory = surgicalHistory;
        this.position = position;
    }
}
