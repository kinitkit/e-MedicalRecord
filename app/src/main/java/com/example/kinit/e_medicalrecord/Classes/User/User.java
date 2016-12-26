package com.example.kinit.e_medicalrecord.Classes.User;

import android.util.Log;

import java.util.Date;

public class User {
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
    public String nationality, civilStatus, religion;
    private boolean status;

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


    //Medical Staff
    public void setMedical_staff_type(String userType) {
        this.medical_staff_type = userType;
    }
    public String getMedical_staff_type() {
        return medical_staff_type;
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
}