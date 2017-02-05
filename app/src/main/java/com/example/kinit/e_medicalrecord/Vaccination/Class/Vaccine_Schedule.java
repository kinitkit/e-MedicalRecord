package com.example.kinit.e_medicalrecord.Vaccination.Class;

import android.os.Parcel;

import org.json.JSONObject;

public class Vaccine_Schedule extends Vaccine {
    public static final String VACCINE_SCHEDULE_ID = "vaccine_schedule_id", AGE_START_YEAR = "age_start_year",
            AGE_START_MONTH = "age_start_month", AGE_START_DAY = "age_start_day", AGE_END_YEAR = "age_end_year",
            AGE_END_MONTH = "age_end_month", AGE_END_DAY = "age_end_day", FREQUENCY_YEAR = "frequency_year",
            FREQUENCY_MONTH = "frequency_month", FREQUENCY_DAY = "frequency_day";

    public int vaccineScheduleId, ageStartYear, ageStartMonth, ageStartDay, ageEndYear, ageEndMonth, ageEndDay,
            frequencyYear, frequencyMonth, frequencyDay;

    public Vaccine_Schedule(JSONObject jsonObject) {
        super(jsonObject);
        try {
            vaccineScheduleId = jsonObject.getInt(VACCINE_SCHEDULE_ID);
            ageStartYear = jsonObject.getInt(AGE_START_YEAR);
            ageStartMonth = jsonObject.getInt(AGE_START_MONTH);
            ageStartDay = jsonObject.getInt(AGE_START_DAY);
            ageEndYear = jsonObject.getInt(AGE_END_YEAR);
            ageEndMonth = jsonObject.getInt(AGE_END_MONTH);
            ageEndDay = jsonObject.getInt(AGE_END_DAY);
            frequencyYear = jsonObject.getInt(FREQUENCY_YEAR);
            frequencyMonth = jsonObject.getInt(FREQUENCY_MONTH);
            frequencyDay = jsonObject.getInt(FREQUENCY_DAY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.vaccineScheduleId);
        dest.writeInt(this.vaccineId);
        dest.writeInt(this.ageStartYear);
        dest.writeInt(this.ageStartMonth);
        dest.writeInt(this.ageStartDay);
        dest.writeInt(this.ageEndYear);
        dest.writeInt(this.ageEndMonth);
        dest.writeInt(this.ageEndDay);
        dest.writeInt(this.frequencyYear);
        dest.writeInt(this.frequencyMonth);
        dest.writeInt(this.frequencyDay);
    }

    protected Vaccine_Schedule(Parcel in) {
        super(in);
        this.vaccineScheduleId = in.readInt();
        this.vaccineId = in.readInt();
        this.ageStartYear = in.readInt();
        this.ageStartMonth = in.readInt();
        this.ageStartDay = in.readInt();
        this.ageEndYear = in.readInt();
        this.ageEndMonth = in.readInt();
        this.ageEndDay = in.readInt();
        this.frequencyYear = in.readInt();
        this.frequencyMonth = in.readInt();
        this.frequencyDay = in.readInt();
    }

    public static final Creator<Vaccine_Schedule> CREATOR = new Creator<Vaccine_Schedule>() {
        @Override
        public Vaccine_Schedule createFromParcel(Parcel source) {
            return new Vaccine_Schedule(source);
        }

        @Override
        public Vaccine_Schedule[] newArray(int size) {
            return new Vaccine_Schedule[size];
        }
    };
}
