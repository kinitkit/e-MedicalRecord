package com.example.kinit.e_medicalrecord.BusStation.Social_History;

import com.example.kinit.e_medicalrecord.Enum.Social_History_Type;

public class Bus_Social_History_Item {
    boolean isCurrentLyUse, isPreviouslyUsed;
    String frequency, header;
    int length, stopped, patient_id;
    Social_History_Type socialHistoryType;

    public Bus_Social_History_Item(int patient_id, String header, Social_History_Type socialHistoryType, boolean isCurrentLyUse, boolean isPreviouslyUsed,
                                   String frequency, int length, int stopped) {
        this.isCurrentLyUse = isCurrentLyUse;
        this.isPreviouslyUsed = isPreviouslyUsed;
        this.frequency = frequency;
        this.header = header;
        this.length = length;
        this.stopped = stopped;
        this.patient_id = patient_id;
        this.socialHistoryType = socialHistoryType;
    }

    public boolean isCurrentLyUse() {
        return isCurrentLyUse;
    }

    public boolean isPreviouslyUsed() {
        return isPreviouslyUsed;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getHeader() {
        return header;
    }

    public int getLength() {
        return length;
    }

    public int getStopped() {
        return stopped;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public Social_History_Type getSocialHistoryType() {
        return socialHistoryType;
    }
}
