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
    String[] month = { "", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private int user_data_id;
    private String contactNumber;
    private String username;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String address;
    private String emailAddress;
    private String image;
    private String birthday;
    private boolean active;
    //Medical Staff
    private int medical_staff_id;
    private String licenseNumber;
    private String medical_staff_type;
    private boolean activated;
    //Patient
    private int patient_id;
    private String occupation;
    private boolean status;

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

    public int getUser_data_id() {
        return user_data_id;
    }

    public void setUser_data_id(int id) {
        this.user_data_id = id;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBirthday() {
        return birthday;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFullName(){
        return getFirstName() +" "+ getMiddleName() +" "+ getLastName();
    }

    public String getMedical_staff_type() {
        return medical_staff_type;
    }

    //Medical Staff
    public void setMedical_staff_type(String userType) {
        this.medical_staff_type = userType;
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

    //Patient
    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int id) {
        this.patient_id = id;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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