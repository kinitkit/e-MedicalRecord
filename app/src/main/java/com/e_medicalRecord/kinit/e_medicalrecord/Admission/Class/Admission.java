package com.e_medicalRecord.kinit.e_medicalrecord.Admission.Class;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Admission implements Parcelable {
    public static final Parcelable.Creator<Admission> CREATOR = new Parcelable.Creator<Admission>() {
        @Override
        public Admission createFromParcel(Parcel source) {
            return new Admission(source);
        }

        @Override
        public Admission[] newArray(int size) {
            return new Admission[size];
        }
    };
    public int id, patientId, userDataId;
    public String physicianName, hospital, strDateAdmitted, strDateDischarged, admittingImpression,
            procedures, futurePlan, finalDiagnosis;
    public Calendar calendarDateAdmitted, calendarDateDischarged;

    public Admission(JSONObject jsonObject) {
        try {
            calendarDateAdmitted = Calendar.getInstance();
            calendarDateDischarged = calendarDateAdmitted;
            id = jsonObject.getInt("id");
            patientId = jsonObject.getInt("patient_id");
            userDataId = jsonObject.getInt("user_data_id");
            physicianName = jsonObject.getString("physician_name");
            hospital = jsonObject.getString("hospital");
            setDate(jsonObject.getString("date_admitted"), 0);
            setDate(jsonObject.getString("date_discharged"), 1);
            finalDiagnosis = jsonObject.getString("final_diagnosis");
            if (jsonObject.has("admitting_impression")) {
                admittingImpression = jsonObject.getString("admitting_impression");
                procedures = jsonObject.getString("procedures");
                futurePlan = jsonObject.getString("future_plan");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Admission(Parcel in) {
        this.id = in.readInt();
        this.patientId = in.readInt();
        this.userDataId = in.readInt();
        this.physicianName = in.readString();
        this.hospital = in.readString();
        this.strDateAdmitted = in.readString();
        this.strDateDischarged = in.readString();
        this.admittingImpression = in.readString();
        this.procedures = in.readString();
        this.futurePlan = in.readString();
        this.finalDiagnosis = in.readString();
        this.calendarDateAdmitted = (Calendar) in.readSerializable();
        this.calendarDateDischarged = (Calendar) in.readSerializable();
    }

    void setDate(String date, int corresponding) {
        try {
            if (corresponding == 0) {
                calendarDateAdmitted.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
                this.strDateAdmitted = new SimpleDateFormat("MMM dd, yyyy").format(calendarDateAdmitted.getTime());
            } else if (corresponding == 1) {
                calendarDateDischarged.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
                this.strDateDischarged = new SimpleDateFormat("MMM dd, yyyy").format(calendarDateDischarged.getTime());
            }
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
        dest.writeString(this.physicianName);
        dest.writeString(this.hospital);
        dest.writeString(this.strDateAdmitted);
        dest.writeString(this.strDateDischarged);
        dest.writeString(this.admittingImpression);
        dest.writeString(this.procedures);
        dest.writeString(this.futurePlan);
        dest.writeString(this.finalDiagnosis);
        dest.writeSerializable(this.calendarDateAdmitted);
        dest.writeSerializable(this.calendarDateDischarged);
    }
}
