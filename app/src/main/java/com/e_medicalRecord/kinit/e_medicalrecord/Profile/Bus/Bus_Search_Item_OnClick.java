package com.e_medicalRecord.kinit.e_medicalrecord.Profile.Bus;

public class Bus_Search_Item_OnClick {
    public int user_id, patient_id, medicalStaff_id;

    public Bus_Search_Item_OnClick(int user_id, int patient_id, int medicalStaff_id) {
        this.user_id = user_id;
        this.patient_id = patient_id;
        this.medicalStaff_id = medicalStaff_id;
    }
}
