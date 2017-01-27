package com.example.kinit.e_medicalrecord.Allergy.Class;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Allergy implements Parcelable {
    public static final Parcelable.Creator<Allergy> CREATOR = new Parcelable.Creator<Allergy>() {
        @Override
        public Allergy createFromParcel(Parcel source) {
            return new Allergy(source);
        }

        @Override
        public Allergy[] newArray(int size) {
            return new Allergy[size];
        }
    };
    public int id, patient_id;
    public String fr, reaction, treatment;

    public Allergy(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            patient_id = jsonObject.getInt("patient_id");
            fr = jsonObject.getString("fr");
            reaction = jsonObject.getString("reaction");
            treatment = jsonObject.getString("treatment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Allergy() {
    }

    protected Allergy(Parcel in) {
        this.id = in.readInt();
        this.patient_id = in.readInt();
        this.fr = in.readString();
        this.reaction = in.readString();
        this.treatment = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.patient_id);
        dest.writeString(this.fr);
        dest.writeString(this.reaction);
        dest.writeString(this.treatment);
    }
}
