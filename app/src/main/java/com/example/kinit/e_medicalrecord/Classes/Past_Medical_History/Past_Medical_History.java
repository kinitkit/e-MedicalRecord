package com.example.kinit.e_medicalrecord.Classes.Past_Medical_History;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Past_Medical_History implements Parcelable {
    public static final Parcelable.Creator<Past_Medical_History> CREATOR = new Parcelable.Creator<Past_Medical_History>() {
        @Override
        public Past_Medical_History createFromParcel(Parcel source) {
            return new Past_Medical_History(source);
        }

        @Override
        public Past_Medical_History[] newArray(int size) {
            return new Past_Medical_History[size];
        }
    };
    public int id, patientId, userDataId;
    public String medicalCondition, strDateTime;
    public Calendar dateTime;

    public Past_Medical_History(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            patientId = jsonObject.getInt("patient_id");
            userDataId = jsonObject.getInt("user_data_id");
            medicalCondition = jsonObject.getString("medical_condition");
            setDate(jsonObject.getString("date_time"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Past_Medical_History(Parcel in) {
        this.id = in.readInt();
        this.patientId = in.readInt();
        this.userDataId = in.readInt();
        this.medicalCondition = in.readString();
        this.strDateTime = in.readString();
        this.dateTime = (Calendar) in.readSerializable();
    }

    void setDate(String date) {
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
        dest.writeString(this.medicalCondition);
        dest.writeString(this.strDateTime);
        dest.writeSerializable(this.dateTime);
    }
}
