package com.e_medicalRecord.kinit.e_medicalrecord.Social_History.Bus;

import com.e_medicalRecord.kinit.e_medicalrecord.Social_History.Class.Social_History;

public class Bus_SocialHistory_OnLongClick {
    public int position;
    public Social_History socialHistory;

    public Bus_SocialHistory_OnLongClick(int position, Social_History socialHistory) {
        this.position = position;
        this.socialHistory = socialHistory;
    }
}
