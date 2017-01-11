package com.example.kinit.e_medicalrecord.Classes.Vaccination;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Vaccine implements Parcelable {
    public static final Parcelable.Creator<Vaccine> CREATOR = new Parcelable.Creator<Vaccine>() {
        @Override
        public Vaccine createFromParcel(Parcel source) {
            return new Vaccine(source);
        }

        @Override
        public Vaccine[] newArray(int size) {
            return new Vaccine[size];
        }
    };
    public int vaccine_id, vaccine_schedule_id, age_start, age_end;
    public String item, frequency;

    public Vaccine(JSONObject jsonObject) {
        try {
            vaccine_id = jsonObject.getInt("vaccine_id");
            item = jsonObject.getString("item");
            if (jsonObject.has("vaccine_schedule_id")) {
                vaccine_schedule_id = jsonObject.getInt("vaccine_schedule_id");
                age_start = jsonObject.getInt("age_start");
                age_end = jsonObject.getInt("age_end");
                frequency = jsonObject.getString("frequency");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Vaccine(Parcel in) {
        this.vaccine_id = in.readInt();
        this.vaccine_schedule_id = in.readInt();
        this.age_start = in.readInt();
        this.age_end = in.readInt();
        this.item = in.readString();
        this.frequency = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.vaccine_id);
        dest.writeInt(this.vaccine_schedule_id);
        dest.writeInt(this.age_start);
        dest.writeInt(this.age_end);
        dest.writeString(this.item);
        dest.writeString(this.frequency);
    }
}
