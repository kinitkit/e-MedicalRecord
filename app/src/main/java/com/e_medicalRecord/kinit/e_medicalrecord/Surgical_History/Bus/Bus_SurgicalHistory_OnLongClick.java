package com.e_medicalRecord.kinit.e_medicalrecord.Surgical_History.Bus;

import com.e_medicalRecord.kinit.e_medicalrecord.Surgical_History.Class.Surgical_History;

public class Bus_SurgicalHistory_OnLongClick {
    public int position;
    public Surgical_History surgicalHistory;

    public Bus_SurgicalHistory_OnLongClick(int position, Surgical_History surgicalHistory) {
        this.surgicalHistory = surgicalHistory;
        this.position = position;
    }
}
