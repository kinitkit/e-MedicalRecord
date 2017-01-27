package com.example.kinit.e_medicalrecord.Profile.Bus;

import com.example.kinit.e_medicalrecord.Profile.Class.Search_User;

public class Bus_Search_OnClick {
    public int position;
    public Search_User searchUser;

    public Bus_Search_OnClick(int position, Search_User searchUser) {
        this.position = position;
        this.searchUser = searchUser;
    }
}
