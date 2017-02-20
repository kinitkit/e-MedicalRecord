package com.e_medicalRecord.kinit.e_medicalrecord.Profile.Class;

import android.os.Parcel;
import android.os.Parcelable;

public class Patient implements Parcelable {
    public static final Parcelable.Creator<Patient> CREATOR = new Parcelable.Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel source) {
            return new Patient(source);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };
    public int id, user_data_id;
    public String name;

    public Patient() {
    }

    protected Patient(Parcel in) {
        this.id = in.readInt();
        this.user_data_id = in.readInt();
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.user_data_id);
        dest.writeString(this.name);
    }
}
