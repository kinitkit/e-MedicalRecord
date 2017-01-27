package com.example.kinit.e_medicalrecord.Medical_Prescription.Bus;

public class Bus_Open_MedicalPrescription_Tagged {
    int medicalPrescription_id, patient_id;

    public Bus_Open_MedicalPrescription_Tagged(int medicalPrescription_id, int patient_id) {
        this.medicalPrescription_id = medicalPrescription_id;
        this.patient_id = patient_id;
    }
}
