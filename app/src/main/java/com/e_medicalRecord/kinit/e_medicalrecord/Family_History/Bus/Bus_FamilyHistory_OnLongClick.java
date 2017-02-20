package com.e_medicalRecord.kinit.e_medicalrecord.Family_History.Bus;

import com.e_medicalRecord.kinit.e_medicalrecord.Family_History.Class.Family_History;

public class Bus_FamilyHistory_OnLongClick {
    public int position;
    public Family_History familyHistory;

    public Bus_FamilyHistory_OnLongClick(int position, Family_History familyHistory) {
        this.position = position;
        this.familyHistory = familyHistory;
    }
}
