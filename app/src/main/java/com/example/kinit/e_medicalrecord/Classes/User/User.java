package com.example.kinit.e_medicalrecord.Classes.User;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class User implements Parcelable {
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    public Calendar calendarBirthday = Calendar.getInstance();
    public String nationality, civilStatus, religion;
    public int user_data_id;
    public String contactNumber;
    public String username;
    public String password;
    public String firstName;
    public String middleName;
    public String lastName;
    public String gender;
    public String address;
    public String emailAddress;
    public String image;
    public String birthday;
    public boolean active;
    //Medical Staff
    public int medical_staff_id;
    public String licenseNumber;
    public String medical_staff_type;
    public boolean activated;
    //Patient
    public int patient_id;
    public String occupation;
    public boolean status;
    String[] month = { "", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public User() {
    }

    protected User(Parcel in) {
        this.month = in.createStringArray();
        this.user_data_id = in.readInt();
        this.contactNumber = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.firstName = in.readString();
        this.middleName = in.readString();
        this.lastName = in.readString();
        this.gender = in.readString();
        this.address = in.readString();
        this.emailAddress = in.readString();
        this.image = in.readString();
        this.birthday = in.readString();
        this.active = in.readByte() != 0;
        this.medical_staff_id = in.readInt();
        this.licenseNumber = in.readString();
        this.medical_staff_type = in.readString();
        this.activated = in.readByte() != 0;
        this.patient_id = in.readInt();
        this.occupation = in.readString();
        this.calendarBirthday = (Calendar) in.readSerializable();
        this.nationality = in.readString();
        this.civilStatus = in.readString();
        this.religion = in.readString();
        this.status = in.readByte() != 0;
    }

    public void setBirthday(String date){
        try {
            calendarBirthday.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] str = date.substring(0, 10).split("-");
        this.birthday =  month[Integer.valueOf(str[1])]+ " "+ str[2] +", "+ str[0];
    }

    public String getFullName(){
        return firstName +" "+ middleName +" "+ lastName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(this.month);
        dest.writeInt(this.user_data_id);
        dest.writeString(this.contactNumber);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.firstName);
        dest.writeString(this.middleName);
        dest.writeString(this.lastName);
        dest.writeString(this.gender);
        dest.writeString(this.address);
        dest.writeString(this.emailAddress);
        dest.writeString(this.image);
        dest.writeString(this.birthday);
        dest.writeByte(this.active ? (byte) 1 : (byte) 0);
        dest.writeInt(this.medical_staff_id);
        dest.writeString(this.licenseNumber);
        dest.writeString(this.medical_staff_type);
        dest.writeByte(this.activated ? (byte) 1 : (byte) 0);
        dest.writeInt(this.patient_id);
        dest.writeString(this.occupation);
        dest.writeSerializable(this.calendarBirthday);
        dest.writeString(this.nationality);
        dest.writeString(this.civilStatus);
        dest.writeString(this.religion);
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
    }
}