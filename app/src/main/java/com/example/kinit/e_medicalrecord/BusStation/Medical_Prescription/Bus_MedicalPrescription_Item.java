package com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription;

public class Bus_MedicalPrescription_Item {
    public int medicalPrescription_id, patient_id;
    public String physicianName, clinicName, date;

    public Bus_MedicalPrescription_Item(int medicalPrescription_id, int patient_id, String physicianName, String clinicName, String date) {
        this.medicalPrescription_id = medicalPrescription_id;
        this.patient_id = patient_id;
        this.physicianName = physicianName;
        this.clinicName = clinicName;
        this.date = date;
    }
}
