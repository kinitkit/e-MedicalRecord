package com.example.kinit.e_medicalrecord.Medical_Prescription.Bus;

import android.os.Parcel;
import android.os.Parcelable;

public class Bus_Medical_Prescription_Click implements Parcelable {
    public static final Parcelable.Creator<Bus_Medical_Prescription_Click> CREATOR = new Parcelable.Creator<Bus_Medical_Prescription_Click>() {
        @Override
        public Bus_Medical_Prescription_Click createFromParcel(Parcel source) {
            return new Bus_Medical_Prescription_Click(source);
        }

        @Override
        public Bus_Medical_Prescription_Click[] newArray(int size) {
            return new Bus_Medical_Prescription_Click[size];
        }
    };
    public int medicalPrescription_id, patient_id;
    public String physicianName, clinicName, date;

    public Bus_Medical_Prescription_Click(int medicalPrescription_id, int patient_id, String physicianName, String clinicName, String date) {
        this.medicalPrescription_id = medicalPrescription_id;
        this.patient_id = patient_id;
        this.physicianName = physicianName;
        this.clinicName = clinicName;
        this.date = date;
    }

    protected Bus_Medical_Prescription_Click(Parcel in) {
        this.medicalPrescription_id = in.readInt();
        this.patient_id = in.readInt();
        this.physicianName = in.readString();
        this.clinicName = in.readString();
        this.date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.medicalPrescription_id);
        dest.writeInt(this.patient_id);
        dest.writeString(this.physicianName);
        dest.writeString(this.clinicName);
        dest.writeString(this.date);
    }
}
