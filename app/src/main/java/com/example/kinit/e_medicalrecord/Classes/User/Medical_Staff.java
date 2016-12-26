package com.example.kinit.e_medicalrecord.Classes.User;

import android.os.Parcel;
import android.os.Parcelable;

public class Medical_Staff extends User implements Parcelable{
    private int medical_staff_id;
    private String licenseNumber;
    private String userType;
    private boolean activated;

    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getUserType() {
        return userType;
    }

    public int getMedical_staff_id() {
        return medical_staff_id;
    }
    public void setMedical_staff_id(int id) {
        this.medical_staff_id = id;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public boolean isActivated() {
        return activated;
    }
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getUser_data_id());
        dest.writeString(getFirstName());
        dest.writeString(getMiddleName());
        dest.writeString(getLastName());
        dest.writeString(getGender());
        //dest.writeLong(getBirthday().getTime());
        dest.writeString(getContactNumber());
        dest.writeString(getAddress());
        dest.writeByte((byte) (isActive() ? 1 : 0));
        dest.writeString(getUsername());
        dest.writeString(getPassword());
        dest.writeString(getAddress());
        dest.writeString(getEmailAddress());

        dest.writeInt(getMedical_staff_id());
        dest.writeString(getUserType());
        dest.writeByte((byte) (isActivated() ? 1 : 0));
    }

    protected Medical_Staff(Parcel in) {
        setUser_data_id(in.readInt());
        setFirstName(in.readString());
        setMiddleName(in.readString());
        setLastName(in.readString());
        setGender(in.readString());
        //setBirthday(new Date(in.readLong()));
        setContactNumber(in.readString());
        setAddress(in.readString());
        setActive(in.readByte() != 0);
        setUsername(in.readString());
        setPassword(in.readString());
        setAddress(in.readString());
        setEmailAddress(in.readString());

        setMedical_staff_id(in.readInt());
        setUserType(in.readString());
        setActivated(in.readByte() != 0);
    }

    public static final Creator<Medical_Staff> CREATOR = new Creator<Medical_Staff>() {
        @Override
        public Medical_Staff createFromParcel(Parcel in) {
            return new Medical_Staff(in);
        }

        @Override
        public Medical_Staff[] newArray(int size) {
            return new Medical_Staff[size];
        }
    };
}
