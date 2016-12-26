package com.example.kinit.e_medicalrecord.Classes.Laboratory;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Lab_Urinalysis {
    public int id, userDataId;
    public String physicianName, labName, datePerformed, color, transparency, pusCells, rbc, epithCells,
            renalCells, mucusThreads, bacteria, yeastCells, amorphousSubs, uricAcid,
            calciumOxalate, triplePhosphate, pusCast, hyaline, fineGranular, coarseGranular, remark;
    public double reaction, specificGravity, urobilinogen;
    public boolean sugar, albumin, ketone, bilirubin, blood, bacteriaNit, leukocyte;
    public Calendar calendar = Calendar.getInstance();

    public void setDate(String date) {
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            this.datePerformed = new SimpleDateFormat("MMM dd, yyyy").format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Lab_Urinalysis() {
    }

    public Lab_Urinalysis(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            userDataId = jsonObject.getInt("user_data_id");
            physicianName = jsonObject.getString("physician_name");
            labName = jsonObject.getString("lab_name");
            setDate(jsonObject.getString("date_performed"));
            color = jsonObject.getString("color");
            transparency = jsonObject.getString("transparency");
            reaction = jsonObject.getDouble("reaction");
            specificGravity = jsonObject.getDouble("specific_gravity");
            pusCells = jsonObject.getString("pus_cells");
            rbc = jsonObject.getString("RBC");
            epithCells = jsonObject.getString("epith_cells");
            renalCells = jsonObject.getString("renal_cells");
            mucusThreads = jsonObject.getString("mucus_threads");
            bacteria = jsonObject.getString("bacteria");
            yeastCells = jsonObject.getString("yeast_cells");
            sugar = jsonObject.getString("sugar").equals("1");
            albumin = jsonObject.getString("albumin").equals("1");
            ketone = jsonObject.getString("ketone").equals("1");
            bilirubin = jsonObject.getString("bilirubin").equals("1");
            blood = jsonObject.getString("blood").equals("1");
            urobilinogen = jsonObject.getDouble("urobilinogen");
            bacteriaNit = jsonObject.getString("bacteriaNit").equals("1");
            leukocyte = jsonObject.getString("leukocyte").equals("1");
            amorphousSubs = jsonObject.getString("amorphous_subs");
            uricAcid = jsonObject.getString("uric_acid");
            triplePhosphate = jsonObject.getString("triple_phosphate");
            pusCast = jsonObject.getString("pus_cast");
            hyaline = jsonObject.getString("hyaline");
            fineGranular = jsonObject.getString("fine_granular");
            coarseGranular = jsonObject.getString("coarse_granular");
            calciumOxalate = jsonObject.getString("calcium_oxalate");
            remark = jsonObject.getString("remark");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
