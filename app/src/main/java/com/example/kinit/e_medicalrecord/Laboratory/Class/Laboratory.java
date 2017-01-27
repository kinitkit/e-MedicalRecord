package com.example.kinit.e_medicalrecord.Laboratory.Class;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Laboratory implements Parcelable {
    public static final Parcelable.Creator<Laboratory> CREATOR = new Parcelable.Creator<Laboratory>() {
        @Override
        public Laboratory createFromParcel(Parcel source) {
            return new Laboratory(source);
        }

        @Override
        public Laboratory[] newArray(int size) {
            return new Laboratory[size];
        }
    };
    public int lab_id, patient_id, user_data_id;
    public String physician_name, lab_name, strDatePerformed, remarks;
    public boolean status;
    public Calendar datePerformed = Calendar.getInstance();

    public Laboratory(JSONObject jsonObject) {
        try {
            lab_id = jsonObject.getInt("lab_id");
            user_data_id = jsonObject.getInt("user_data_id");
            physician_name = jsonObject.getString("physician_name");
            lab_name = jsonObject.getString("lab_name");
            setDate(jsonObject.getString("date_performed"));
            remarks = jsonObject.getString("remarks");
            status = jsonObject.getString("status").equals("1");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected Laboratory(Parcel in) {
        this.lab_id = in.readInt();
        this.patient_id = in.readInt();
        this.user_data_id = in.readInt();
        this.physician_name = in.readString();
        this.lab_name = in.readString();
        this.strDatePerformed = in.readString();
        this.remarks = in.readString();
        this.status = in.readByte() != 0;
        this.datePerformed = (Calendar) in.readSerializable();
    }

    public void setDate(String date) {
        try {
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
        dest.writeInt(this.lab_id);
        dest.writeInt(this.patient_id);
        dest.writeInt(this.user_data_id);
        dest.writeString(this.physician_name);
        dest.writeString(this.lab_name);
        dest.writeString(this.strDatePerformed);
        dest.writeString(this.remarks);
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.datePerformed);
    }
}
