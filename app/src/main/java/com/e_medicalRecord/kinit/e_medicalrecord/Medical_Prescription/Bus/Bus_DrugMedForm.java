package com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Bus;

import com.e_medicalRecord.kinit.e_medicalrecord.General.Enum.Query_Type;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Class.Drug;

public class Bus_DrugMedForm {
    public Query_Type queryType;
    public int position;
    public Drug drug;

    public Bus_DrugMedForm(Query_Type queryType, int position, Drug drug) {
        this.queryType = queryType;
        this.position = position;
        this.drug = drug;
    }
}
