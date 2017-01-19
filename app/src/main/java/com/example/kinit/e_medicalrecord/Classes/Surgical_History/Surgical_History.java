package com.example.kinit.e_medicalrecord.Classes.Surgical_History;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Surgical_History implements Parcelable {
    public static final Parcelable.Creator<Surgical_History> CREATOR = new Parcelable.Creator<Surgical_History>() {
        @Override
        public Surgical_History createFromParcel(Parcel source) {
            return new Surgical_History(source);
        }

        @Override
        public Surgical_History[] newArray(int size) {
            return new Surgical_History[size];
        }
    };
    public int id, patientId, userDataId;
    public String surgeryTitle, strDatePerformed;
    public Calendar datePerformed;

    public Surgical_History(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            patientId = jsonObject.getInt("patient_id");
            userDataId = jsonObject.getInt("user_data_id");
            surgeryTitle = jsonObject.getString("surgery_title");
            setDate(jsonObject.getString("date_performed"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Surgical_History(Parcel in) {
        this.id = in.readInt();
        this.patientId = in.readInt();
        this.userDataId = in.readInt();
        this.surgeryTitle = in.readString();
        this.strDatePerformed = in.readString();
        this.datePerformed = (Calendar) in.readSerializable();
    }

    void setDate(String date) {
        try {
            datePerformed = Calendar.getInstance();
            datePerformed.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            this.strDatePerformed = new SimpleDateFormat("MMM dd, yyyy").format(datePerformed.getTime());
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
        dest.writeString(this.surgeryTitle);
        dest.writeString(this.strDatePerformed);
        dest.writeSerializable(this.datePerformed);
    }
}
