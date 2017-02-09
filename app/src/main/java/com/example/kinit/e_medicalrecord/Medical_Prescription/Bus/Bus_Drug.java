package com.example.kinit.e_medicalrecord.Medical_Prescription.Bus;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.kinit.e_medicalrecord.General.Enum.Query_Type;

import org.json.JSONObject;

public class Bus_Drug implements Parcelable {
    public static final Parcelable.Creator<Bus_Drug> CREATOR = new Parcelable.Creator<Bus_Drug>() {
        @Override
        public Bus_Drug createFromParcel(Parcel source) {
            return new Bus_Drug(source);
        }

        @Override
        public Bus_Drug[] newArray(int size) {
            return new Bus_Drug[size];
        }
    };
    public String drug, strength, amount, route, frequency, why, quantity, refill;
    public Query_Type queryType;
    public int position;

    public Bus_Drug(Query_Type queryType) {
        this.queryType = queryType;
    }

    public Bus_Drug(Query_Type queryType, int position, String drug, String strength, String amount, String route, String frequency, String why, String quantity) {
        this.drug = drug;
        this.strength = strength;
        this.amount = amount;
        this.route = route;
        this.frequency = frequency;
        this.why = why;
        this.quantity = quantity;
        this.queryType = queryType;
        this.position = position;
    }

    public Bus_Drug(String drug, String strength, String amount, String route, String frequency, String why, String quantity, String refill, Query_Type queryType, int position) {
        this.drug = drug;
        this.strength = strength;
        this.amount = amount;
        this.route = route;
        this.frequency = frequency;
        this.why = why;
        this.quantity = quantity;
        this.refill = refill;
        this.queryType = queryType;
        this.position = position;
    }

    public Bus_Drug(String drug, String strength, String amount, String route, String frequency, String why, String quantity, String refill) {
        this.drug = drug;
        this.strength = strength;
        this.amount = amount;
        this.route = route;
        this.frequency = frequency;
        this.why = why;
        this.quantity = quantity;
        this.refill = refill;
    }

    public Bus_Drug(JSONObject jsonObject) {
        try{
            this.drug = jsonObject.getString("drug");
            this.strength = jsonObject.getString("strength");
            this.amount = jsonObject.getString("dosage");
            this.route = jsonObject.getString("route");
            this.frequency = jsonObject.getString("frequency");
            this.why = jsonObject.getString("indication");
            this.quantity =jsonObject.getString("how_many");
            this.refill = jsonObject.getString("refill");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected Bus_Drug(Parcel in) {
        this.drug = in.readString();
        this.strength = in.readString();
        this.amount = in.readString();
        this.route = in.readString();
        this.frequency = in.readString();
        this.why = in.readString();
        this.quantity = in.readString();
        this.refill = in.readString();
        int tmpQueryType = in.readInt();
        this.queryType = tmpQueryType == -1 ? null : Query_Type.values()[tmpQueryType];
        this.position = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.drug);
        dest.writeString(this.strength);
        dest.writeString(this.amount);
        dest.writeString(this.route);
        dest.writeString(this.frequency);
        dest.writeString(this.why);
        dest.writeString(this.quantity);
        dest.writeString(this.refill);
        dest.writeInt(this.queryType == null ? -1 : this.queryType.ordinal());
        dest.writeInt(this.position);
    }
}
