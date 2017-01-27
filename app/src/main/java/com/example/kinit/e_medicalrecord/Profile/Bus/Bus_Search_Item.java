package com.example.kinit.e_medicalrecord.Profile.Bus;

import com.example.kinit.e_medicalrecord.General.Enum.Mode;

public class Bus_Search_Item {
    public String name;
    public int user_id;
    public Mode mode;

    public Bus_Search_Item(String name, int user_id, Mode mode) {
        this.name = name;
        this.user_id = user_id;
        this.mode = mode;
    }
}
