package com.example.kinit.e_medicalrecord.Laboratory.Class;

import org.json.JSONObject;

public class Lab_Urinalysis extends Laboratory {
    public static final String TABLE_NAME = "lab_urinalysis";
    public int id;
    public String color, transparency, pusCells, rbc, epithCells,
            renalCells, mucusThreads, bacteria, yeastCells, amorphousSubs, uricAcid,
            calciumOxalate, triplePhosphate, pusCast, hyaline, fineGranular, coarseGranular, reaction, specificGravity, urobilinogen, sugar, albumin, ketone, bilirubin, blood, bacteriaNit, leukocyte;

    public Lab_Urinalysis(JSONObject jsonObject) {
        super(jsonObject);
        try {
            color = jsonObject.getString("color");
            transparency = jsonObject.getString("transparency");
            reaction = jsonObject.getString("reaction");
            specificGravity = jsonObject.getString("specific_gravity");
            pusCells = jsonObject.getString("pus_cells");
            rbc = jsonObject.getString("RBC");
            epithCells = jsonObject.getString("epith_cells");
            renalCells = jsonObject.getString("renal_cells");
            mucusThreads = jsonObject.getString("mucus_threads");
            bacteria = jsonObject.getString("bacteria");
            yeastCells = jsonObject.getString("yeast_cells");
            sugar = jsonObject.getString("sugar");
            albumin = jsonObject.getString("albumin");
            ketone = jsonObject.getString("ketone");
            bilirubin = jsonObject.getString("bilirubin");
            blood = jsonObject.getString("blood");
            urobilinogen = jsonObject.getString("urobilinogen");
            bacteriaNit = jsonObject.getString("bacteriaNit");
            leukocyte = jsonObject.getString("leukocyte");
            amorphousSubs = jsonObject.getString("amorphous_subs");
            uricAcid = jsonObject.getString("uric_acid");
            triplePhosphate = jsonObject.getString("triple_phosphate");
            pusCast = jsonObject.getString("pus_cast");
            hyaline = jsonObject.getString("hyaline");
            fineGranular = jsonObject.getString("fine_granular");
            coarseGranular = jsonObject.getString("coarse_granular");
            calciumOxalate = jsonObject.getString("calcium_oxalate");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
