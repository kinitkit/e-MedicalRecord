package com.example.kinit.e_medicalrecord.Vaccination.Class;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Vaccination implements Parcelable {

    public static final Parcelable.Creator<Vaccination> CREATOR = new Parcelable.Creator<Vaccination>() {
        @Override
        public Vaccination createFromParcel(Parcel source) {
            return new Vaccination(source);
        }

        @Override
        public Vaccination[] newArray(int size) {
            return new Vaccination[size];
        }
    };
    public int id, patientId, userDataId;
    public String providerName, strDateTaken, strNextSchedule, placeTaken;
    public boolean status;
    public Calendar calendar, nxtSchedule;
    public Vaccine vaccine;

    public Vaccination(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            patientId = jsonObject.getInt("patient_id");
            userDataId = jsonObject.getInt("user_data_id");
            providerName = jsonObject.getString("provider_name");
            setDate(jsonObject.getString("date_taken"));
            placeTaken = jsonObject.getString("place_taken");
            status = jsonObject.getString("status").equals("1");
            vaccine = new Vaccine(jsonObject);
            if (vaccine.vaccineScheduleId != 0) {
                nxtSchedule = (Calendar)calendar.clone();
                nxtSchedule.add(Calendar.YEAR, vaccine.frequency_year);
                nxtSchedule.add(Calendar.MONTH, vaccine.frequency_month);
                nxtSchedule.add(Calendar.DAY_OF_MONTH, vaccine.frequency_day);
                strNextSchedule = new SimpleDateFormat("MMM dd, yyyy").format(nxtSchedule.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Vaccination(Parcel in) {
        this.id = in.readInt();
        this.patientId = in.readInt();
        this.userDataId = in.readInt();
        this.providerName = in.readString();
        this.strDateTaken = in.readString();
        this.strNextSchedule = in.readString();
        this.placeTaken = in.readString();
        this.status = in.readByte() != 0;
        this.calendar = (Calendar) in.readSerializable();
        this.nxtSchedule = (Calendar) in.readSerializable();
        this.vaccine = in.readParcelable(Vaccine.class.getClassLoader());
    }

    void setDate(String date) {
        try {
            calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            this.strDateTaken = new SimpleDateFormat("MMM dd, yyyy").format(calendar.getTime());

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
        dest.writeString(this.providerName);
        dest.writeString(this.strDateTaken);
        dest.writeString(this.strNextSchedule);
        dest.writeString(this.placeTaken);
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.calendar);
        dest.writeSerializable(this.nxtSchedule);
        dest.writeParcelable(this.vaccine, flags);
    }
}
