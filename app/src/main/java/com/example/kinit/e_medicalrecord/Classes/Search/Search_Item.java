package com.example.kinit.e_medicalrecord.Classes.Search;

import java.util.ArrayList;

public class Search_Item {
    public ArrayList<Integer> user_id, patient_id, medicalStaff_id;
    public ArrayList<String> medicalType, name;

    public Search_Item() {
        this.patient_id = new ArrayList<>();
        this.user_id = new ArrayList<>();
        this.medicalStaff_id = new ArrayList<>();
        this.medicalType = new ArrayList<>();
        this.name = new ArrayList<>();
    }

    public void setName(String fName, String mName, String lName) {
        this.name.add(fName + " " + mName + " " + lName);
    }

    public int setId(String id) {
        if (!id.equals("null"))
            return Integer.parseInt(id);
        else
            return 0;
    }

    public void setMedicalType(String type) {
        if (type.equals("null"))
            medicalType.add("");
        else
            medicalType.add(type);
    }

}
