package com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Bus;

public class Bus_Search_Tagged_MedicalPrescription {
    public String name;
    public int user_id;
    public int id;

    public Bus_Search_Tagged_MedicalPrescription(String name, int user_id, int medical_prescription_id) {
        this.name = name;
        this.user_id = user_id;
        this.id = medical_prescription_id;
    }
}
