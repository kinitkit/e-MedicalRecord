package com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription;

import android.os.Parcel;
import android.os.Parcelable;

public class Bus_Medical_Prescription_LongClick implements Parcelable {
    public static final Parcelable.Creator<Bus_Medical_Prescription_LongClick> CREATOR = new Parcelable.Creator<Bus_Medical_Prescription_LongClick>() {
        @Override
        public Bus_Medical_Prescription_LongClick createFromParcel(Parcel source) {
            return new Bus_Medical_Prescription_LongClick(source);
        }

        @Override
        public Bus_Medical_Prescription_LongClick[] newArray(int size) {
            return new Bus_Medical_Prescription_LongClick[size];
        }
    };
    public int medicalPrescription_id, patient_id, position;
    public String physicianName, clinicName, date, calendarStr;


    public Bus_Medical_Prescription_LongClick(int medicalPrescription_id, int patient_id, String physicianName, String clinicName, String date, String calendarStr, int position) {
        this.medicalPrescription_id = medicalPrescription_id;
        this.patient_id = patient_id;
        this.position = position;
        this.physicianName = physicianName;
        this.clinicName = clinicName;
        this.date = date;
        this.calendarStr = calendarStr;
    }

    protected Bus_Medical_Prescription_LongClick(Parcel in) {
        this.medicalPrescription_id = in.readInt();
        this.patient_id = in.readInt();
        this.position = in.readInt();
        this.physicianName = in.readString();
        this.clinicName = in.readString();
        this.date = in.readString();
        this.calendarStr = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.medicalPrescription_id);
        dest.writeInt(this.patient_id);
        dest.writeInt(this.position);
        dest.writeString(this.physicianName);
        dest.writeString(this.clinicName);
        dest.writeString(this.date);
        dest.writeString(this.calendarStr);
    }
}
