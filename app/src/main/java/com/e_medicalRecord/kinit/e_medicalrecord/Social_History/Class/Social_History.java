package com.e_medicalRecord.kinit.e_medicalrecord.Social_History.Class;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Social_History implements Parcelable {
    public static final Parcelable.Creator<Social_History> CREATOR = new Parcelable.Creator<Social_History>() {
        @Override
        public Social_History createFromParcel(Parcel source) {
            return new Social_History(source);
        }

        @Override
        public Social_History[] newArray(int size) {
            return new Social_History[size];
        }
    };
    public int id, patientId, userDataId, length, stoppedYear;
    public String substance, frequency;
    public boolean currentlyUse, previouslyUsed;

    public Social_History(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            patientId = jsonObject.getInt("patient_id");
            userDataId = jsonObject.getInt("user_data_id");
            substance = jsonObject.getString("substance");
            currentlyUse = jsonObject.getString("currently_use").equals("1");
            previouslyUsed = jsonObject.getString("previously_used").equals("1");
            frequency = jsonObject.getString("frequency");
            length = jsonObject.getInt("length");
            stoppedYear = jsonObject.getInt("stopped_year");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Social_History(Parcel in) {
        this.id = in.readInt();
        this.patientId = in.readInt();
        this.userDataId = in.readInt();
        this.length = in.readInt();
        this.stoppedYear = in.readInt();
        this.substance = in.readString();
        this.frequency = in.readString();
        this.currentlyUse = in.readByte() != 0;
        this.previouslyUsed = in.readByte() != 0;
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
        dest.writeInt(this.length);
        dest.writeInt(this.stoppedYear);
        dest.writeString(this.substance);
        dest.writeString(this.frequency);
        dest.writeByte(this.currentlyUse ? (byte) 1 : (byte) 0);
        dest.writeByte(this.previouslyUsed ? (byte) 1 : (byte) 0);
    }
}
