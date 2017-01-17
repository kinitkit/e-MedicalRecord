package com.example.kinit.e_medicalrecord.Classes.Vaccination;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Vaccine implements Parcelable {

    public static final Creator<Vaccine> CREATOR = new Creator<Vaccine>() {
        @Override
        public Vaccine createFromParcel(Parcel source) {
            return new Vaccine(source);
        }

        @Override
        public Vaccine[] newArray(int size) {
            return new Vaccine[size];
        }
    };
    public int vaccineId, vaccineScheduleId, ageStart, ageEnd, frequency_year, frequency_month, frequency_day;
    public String item;

    public Vaccine(JSONObject jsonObject) {
        try {
            vaccineId = jsonObject.getInt("vaccine_id");
            item = jsonObject.getString("item");
            if (jsonObject.has("vaccine_schedule_id")) {
                if (!jsonObject.getString("vaccine_schedule_id").equals("null")) {
                    vaccineScheduleId = jsonObject.getInt("vaccine_schedule_id");
                    ageStart = jsonObject.getInt("age_start");
                    ageEnd = jsonObject.getInt("age_end");
                    frequency_year = jsonObject.getInt("frequency_year");
                    frequency_month = jsonObject.getInt("frequency_month");
                    frequency_day = jsonObject.getInt("frequency_day");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Vaccine(Parcel in) {
        this.vaccineId = in.readInt();
        this.vaccineScheduleId = in.readInt();
        this.ageStart = in.readInt();
        this.ageEnd = in.readInt();
        this.frequency_year = in.readInt();
        this.frequency_month = in.readInt();
        this.frequency_day = in.readInt();
        this.item = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.vaccineId);
        dest.writeInt(this.vaccineScheduleId);
        dest.writeInt(this.ageStart);
        dest.writeInt(this.ageEnd);
        dest.writeInt(this.frequency_year);
        dest.writeInt(this.frequency_month);
        dest.writeInt(this.frequency_day);
        dest.writeString(this.item);
    }
}
