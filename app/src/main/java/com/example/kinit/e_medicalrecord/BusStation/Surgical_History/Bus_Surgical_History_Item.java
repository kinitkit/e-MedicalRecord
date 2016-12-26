package com.example.kinit.e_medicalrecord.BusStation.Surgical_History;

import com.example.kinit.e_medicalrecord.Enum.Medical_Transaction;

public class Bus_Surgical_History_Item {
    int id, patient_id;
    String title, attachName, date;
    Medical_Transaction transaction;

    public Bus_Surgical_History_Item(Medical_Transaction transaction, int patient_id) {
        this.transaction = transaction;
        this.patient_id = patient_id;
    }

    public Bus_Surgical_History_Item(Medical_Transaction transaction, String title, String date, int id, String attachName) {
        this.transaction = transaction;
        this.title = title;
        this.date = date;
        this.id = id;
        this.attachName = attachName;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public Medical_Transaction getTransaction() {
        return transaction;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAttachName() {
        return attachName;
    }
}
