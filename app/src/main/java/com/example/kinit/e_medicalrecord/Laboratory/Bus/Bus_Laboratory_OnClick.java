package com.example.kinit.e_medicalrecord.Laboratory.Bus;

import com.example.kinit.e_medicalrecord.Laboratory.Class.Laboratory;

public class Bus_Laboratory_OnClick {
    public int position;
    public Laboratory laboratory;

    public Bus_Laboratory_OnClick(int position, Laboratory laboratory) {
        this.position = position;
        this.laboratory = laboratory;
    }
}
