package com.example.kinit.e_medicalrecord.Classes.Surgical_History;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Surgical_History {
    String[] month = { "", "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "August", "September", "October", "November", "December"};

    static List<String> surgicalAttachName;
    static List<String> surgicalTitle;
    static List<String> surgicalDate;
    static List<Integer> surgicalId;
    static List<Calendar> calendars;

    public Surgical_History() {
        surgicalTitle = new ArrayList<>();
        surgicalDate = new ArrayList<>();
        surgicalId = new ArrayList<>();
        surgicalAttachName = new ArrayList<>();
        calendars = new ArrayList<>();
    }

    public void setSurgicalTitleItem(String surgicalTitle) {
        Surgical_History.surgicalTitle.add(surgicalTitle);
    }

    public String getSurgicalTitleItem(int position) {
        return surgicalTitle.get(position);
    }

    public void setSurgicalDateItem(String surgicalDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(surgicalDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        calendars.add(calendar);
        Log.d("error", calendar.getTime().toString());
        Surgical_History.surgicalDate.add(simpleDateFormat.format(calendar.getTime()));
    }

    public String getSurgicalDateItem(int position) {
        return surgicalDate.get(position);
    }

    public void setSurgicalIdItem(int surgicalId) {
        Surgical_History.surgicalId.add(surgicalId);
    }

    public int getSurgicalIdItem(int position) {
        return surgicalId.get(position);
    }

    public void setSurgicalAttachName(String fName, String mName, String lName) {
        surgicalAttachName.add(fName + " " + mName + " " + lName);
    }

    public String getSurgicalAttachName(int position) {
        return surgicalAttachName.get(position);
    }

    public Calendar getCalendar(int position){
        return calendars.get(position);
    }

    public int getSize() {
        return surgicalTitle.size();
    }

    public void removeItem(int position) {
        surgicalId.remove(position);
        surgicalTitle.remove(position);
        surgicalDate.remove(position);
    }

}
