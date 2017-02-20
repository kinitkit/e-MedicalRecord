package com.e_medicalRecord.kinit.e_medicalrecord.Vaccination.Class;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Vaccine implements Parcelable {

    public static final String VACCINE_ID = "vaccine_id", ITEM = "item";
    public int vaccineId;
    public String item;

    public Vaccine(JSONObject jsonObject) {
        try {
            vaccineId = jsonObject.getInt(VACCINE_ID);
            item = jsonObject.getString(ITEM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final Creator<Vaccine> CREATOR = new Creator<Vaccine>() {
        @Override
        public Vaccine createFromParcel(Parcel in) {
            return new Vaccine(in);
        }

        @Override
        public Vaccine[] newArray(int size) {
            return new Vaccine[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.vaccineId);
        dest.writeString(this.item);
    }

    protected Vaccine(Parcel in) {
        this.vaccineId = in.readInt();
        this.item = in.readString();
    }

}
