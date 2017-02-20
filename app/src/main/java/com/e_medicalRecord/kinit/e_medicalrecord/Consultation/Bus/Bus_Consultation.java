package com.e_medicalRecord.kinit.e_medicalrecord.Consultation.Bus;

import com.e_medicalRecord.kinit.e_medicalrecord.Consultation.Class.Consultation;

public class Bus_Consultation {
    public int position, type;
    public Consultation consultation;

    public Bus_Consultation(int position, Consultation consultation, int type) {
        this.position = position;
        this.consultation = consultation;
        this.type = type;
    }
}
