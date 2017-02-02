package com.example.kinit.e_medicalrecord.Consultation.Class;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Consultation implements Parcelable {
    public int id, patientId, userDataId, respirationRate, pulseRate;
    public String physicianName, strDateTime, chiefComplaint, presentIllness, diagnosis, bloodPressure;
    public double height, weight, temperature;
    public boolean status;
    public Calendar dateTime;

    public Consultation(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            patientId = jsonObject.getInt("patient_id");
            userDataId = jsonObject.getInt("user_data_id");
            physicianName = jsonObject.getString("physician_name");
            setDateAndTime(jsonObject.getString("date_time"));
            chiefComplaint = jsonObject.getString("chief_complaint");
            presentIllness = jsonObject.getString("present_illness");
            height = jsonObject.getDouble("height");
            weight = jsonObject.getDouble("weight");
            bloodPressure = jsonObject.getString("blood_pressure");
            respirationRate = jsonObject.getInt("respiration_rate");
            temperature = jsonObject.getDouble("temperature");
            pulseRate = jsonObject.getInt("pulse_rate");
            status = jsonObject.getString("status").equals("1");
            diagnosis = jsonObject.getString("diagnosis");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setDateAndTime(String date) {
        try {
            dateTime = Calendar.getInstance();
            dateTime.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            this.strDateTime = new SimpleDateFormat("MMM dd, yyyy").format(dateTime.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.patientId);
        dest.writeInt(this.userDataId);
        dest.writeInt(this.respirationRate);
        dest.writeInt(this.pulseRate);
        dest.writeString(this.physicianName);
        dest.writeString(this.strDateTime);
        dest.writeString(this.chiefComplaint);
        dest.writeString(this.presentIllness);
        dest.writeString(this.diagnosis);
        dest.writeString(this.bloodPressure);
        dest.writeDouble(this.height);
        dest.writeDouble(this.weight);
        dest.writeDouble(this.temperature);
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.dateTime);
    }

    protected Consultation(Parcel in) {
        this.id = in.readInt();
        this.patientId = in.readInt();
        this.userDataId = in.readInt();
        this.respirationRate = in.readInt();
        this.pulseRate = in.readInt();
        this.physicianName = in.readString();
        this.strDateTime = in.readString();
        this.chiefComplaint = in.readString();
        this.presentIllness = in.readString();
        this.diagnosis = in.readString();
        this.bloodPressure = in.readString();
        this.height = in.readDouble();
        this.weight = in.readDouble();
        this.temperature = in.readDouble();
        this.status = in.readByte() != 0;
        this.dateTime = (Calendar) in.readSerializable();
    }

    public static final Parcelable.Creator<Consultation> CREATOR = new Parcelable.Creator<Consultation>() {
        @Override
        public Consultation createFromParcel(Parcel source) {
            return new Consultation(source);
        }

        @Override
        public Consultation[] newArray(int size) {
            return new Consultation[size];
        }
    };
}
