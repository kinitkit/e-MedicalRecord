package com.e_medicalRecord.kinit.e_medicalrecord.Allergy.Bus;

import com.e_medicalRecord.kinit.e_medicalrecord.Allergy.Class.Allergy;

public class Bus_Allergy {
    public int position;
    public Allergy allergy;

    public Bus_Allergy(int position, Allergy allergy) {
        this.position = position;
        this.allergy = allergy;
    }
}
