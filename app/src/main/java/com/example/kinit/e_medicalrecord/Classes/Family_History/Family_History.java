package com.example.kinit.e_medicalrecord.Classes.Family_History;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Family_History implements Parcelable {
    public static final Parcelable.Creator<Family_History> CREATOR = new Parcelable.Creator<Family_History>() {
        @Override
        public Family_History createFromParcel(Parcel source) {
            return new Family_History(source);
        }

        @Override
        public Family_History[] newArray(int size) {
            return new Family_History[size];
        }
    };
    public int id, patientId, userDataId;
    public String medicalCondition;
    public boolean grandP, parent, sibling, child;

    public Family_History(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            patientId = jsonObject.getInt("patient_id");
            userDataId = jsonObject.getInt("user_data_id");
            medicalCondition = jsonObject.getString("medical_condition");
            grandP = jsonObject.getString("grandP").equals("1");
            parent = jsonObject.getString("parent").equals("1");
            sibling = jsonObject.getString("sibling").equals("1");
            child = jsonObject.getString("child").equals("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Family_History(Parcel in) {
        this.id = in.readInt();
        this.patientId = in.readInt();
        this.userDataId = in.readInt();
        this.medicalCondition = in.readString();
        this.grandP = in.readByte() != 0;
        this.parent = in.readByte() != 0;
        this.sibling = in.readByte() != 0;
        this.child = in.readByte() != 0;
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
        dest.writeByte(this.grandP ? (byte) 1 : (byte) 0);
        dest.writeByte(this.parent ? (byte) 1 : (byte) 0);
        dest.writeByte(this.sibling ? (byte) 1 : (byte) 0);
        dest.writeByte(this.child ? (byte) 1 : (byte) 0);
    }
}
