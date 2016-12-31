package com.example.kinit.e_medicalrecord.Classes.User;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.kinit.e_medicalrecord.Enum.Mode;

public class Viewer implements Parcelable {
    public static final Parcelable.Creator<Viewer> CREATOR = new Parcelable.Creator<Viewer>() {
        @Override
        public Viewer createFromParcel(Parcel source) {
            return new Viewer(source);
        }

        @Override
        public Viewer[] newArray(int size) {
            return new Viewer[size];
        }
    };
    public String name;
    public int patient_id, user_id, medicalStaff_id;
    public Mode mode;

    public Viewer() {
    }

    protected Viewer(Parcel in) {
        this.name = in.readString();
        this.patient_id = in.readInt();
        this.user_id = in.readInt();
        this.medicalStaff_id = in.readInt();
        int tmpMode = in.readInt();
        this.mode = tmpMode == -1 ? null : Mode.values()[tmpMode];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.patient_id);
        dest.writeInt(this.user_id);
        dest.writeInt(this.medicalStaff_id);
        dest.writeInt(this.mode == null ? -1 : this.mode.ordinal());
    }
}
