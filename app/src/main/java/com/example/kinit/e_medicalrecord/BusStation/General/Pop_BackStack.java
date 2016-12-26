package com.example.kinit.e_medicalrecord.BusStation.General;

import com.example.kinit.e_medicalrecord.Enum.Medical_Transaction;

public class Pop_BackStack {
    Medical_Transaction transaction;

    public Pop_BackStack(Medical_Transaction transaction) {
        this.transaction = transaction;
    }

    public Pop_BackStack() {

    }

    public Medical_Transaction getTransaction() {
        return transaction;
    }
}
