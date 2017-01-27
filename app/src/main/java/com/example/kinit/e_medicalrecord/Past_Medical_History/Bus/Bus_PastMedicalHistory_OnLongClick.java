package com.example.kinit.e_medicalrecord.Past_Medical_History.Bus;

import com.example.kinit.e_medicalrecord.Past_Medical_History.Class.Past_Medical_History;

public class Bus_PastMedicalHistory_OnLongClick {
    public int position;
    public Past_Medical_History pastMedicalHistory;

    public Bus_PastMedicalHistory_OnLongClick(int position, Past_Medical_History pastMedicalHistory) {
        this.position = position;
        this.pastMedicalHistory = pastMedicalHistory;
    }
}
