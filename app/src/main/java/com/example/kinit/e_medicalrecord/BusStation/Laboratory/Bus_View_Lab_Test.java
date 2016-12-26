package com.example.kinit.e_medicalrecord.BusStation.Laboratory;

import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Chemistry;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Fecalysis;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Hematology;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Urinalysis;
import com.example.kinit.e_medicalrecord.Enum.Laboratory_Tests;

public class Bus_View_Lab_Test {
    //Enum
    public Laboratory_Tests enum_laboratoryTests;
    //Classes
    public Lab_Chemistry labChemistry;
    public Lab_Fecalysis labFecalysis;
    public Lab_Hematology labHematology;
    public Lab_Urinalysis labUrinalysis;
    public Object object;

    public Bus_View_Lab_Test(Laboratory_Tests enum_laboratoryTests, Object object) {
        this.enum_laboratoryTests = enum_laboratoryTests;
        this.object = object;
        switch (enum_laboratoryTests) {
            case BLOOD_CHEMISTRY:
                this.labChemistry = (Lab_Chemistry) object;
                break;
            case FECALYSIS:
                this.labFecalysis = (Lab_Fecalysis) object;
                break;
            case HEMATOLOGY:
                this.labHematology = (Lab_Hematology) object;
                break;
            case URINALYSIS:
                this.labUrinalysis = (Lab_Urinalysis) object;
                break;
        }
    }
}
