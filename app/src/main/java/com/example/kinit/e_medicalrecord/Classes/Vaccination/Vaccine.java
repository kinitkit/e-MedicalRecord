package com.example.kinit.e_medicalrecord.Classes.Vaccination;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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
    public int vaccineId, vaccineScheduleId, ageStart, ageEnd;
    public String item, frequency;

    public Vaccine(JSONObject jsonObject) {
        try {
            //Log.d("error", jsonObject.getString("item"));
            vaccineId = jsonObject.getInt("vaccine_id");
            item = jsonObject.getString("item");
            if (jsonObject.has("vaccine_schedule_id")) {
                if(!jsonObject.getString("vaccine_schedule_id").equals("null")) {
                    vaccineScheduleId = jsonObject.getInt("vaccine_schedule_id");
                    ageStart = jsonObject.getInt("age_start");
                    ageEnd = jsonObject.getInt("age_end");
                    frequency = jsonObject.getString("frequency");
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
        this.item = in.readString();
        this.frequency = in.readString();
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
        dest.writeString(this.item);
        dest.writeString(this.frequency);
    }
}
