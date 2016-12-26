package com.example.kinit.e_medicalrecord.BusStation.Laboratory;

import com.example.kinit.e_medicalrecord.Enum.Laboratory_Tests;

public class Bus_Open_New_Lab_Test {
    public Laboratory_Tests laboratoryTests;

    public Bus_Open_New_Lab_Test(int lab_ordinal) {
        this.laboratoryTests = Laboratory_Tests.values()[lab_ordinal];
    }
}
