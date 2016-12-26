package com.example.kinit.e_medicalrecord.BusStation.Past_Medical_History;

public class Bus_Past_Medical_History_Item {
    int patient_id, year;
    String column_name, columnYr_name, medicalCondition;
    boolean isMedicalHistory;

    public Bus_Past_Medical_History_Item(int patient_id, int year, String column_name, String columnYr_name,
                                         String medicalCondition, boolean isMedicalHistory) {
        this.patient_id = patient_id;
        this.year = year;
        this.column_name = column_name;
        this.columnYr_name = columnYr_name;
        this.medicalCondition = medicalCondition;
        this.isMedicalHistory = isMedicalHistory;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public int getYear() {
        return year;
    }

    public String getColumn_name() {
        return column_name;
    }

    public String getColumnYr_name() {
        return columnYr_name;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }

    public boolean isMedicalHistory() {
        return isMedicalHistory;
    }
}
