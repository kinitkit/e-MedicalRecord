package com.example.kinit.e_medicalrecord.Classes.Laboratory;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Lab_Fecalysis extends Laboratory{
    public static final String TABLE_NAME = "lab_fecalysis";
    public int id;
    public String color, consistency, ascarisLumbricoides, trichurisTrichiura, enterobiusVermicularis,
            hookwormOva, giardiaLambia, blastocystisHominis, cyst, trophozoite, occultBlood, pusCells, rbc, fatGlobules, yeastCells,
            undigestedFood, starchGranules;

    public Lab_Fecalysis(JSONObject jsonObject) {
        super(jsonObject);
        try {
            this.id = jsonObject.getInt("id");
            color = jsonObject.getString("color");
            consistency = jsonObject.getString("consistency");
            ascarisLumbricoides = jsonObject.getString("ascaris_lumbricoides");
            trichurisTrichiura = jsonObject.getString("trichuris_trichiura");
            enterobiusVermicularis = jsonObject.getString("enterobius_vermicularis");
            hookwormOva = jsonObject.getString("hookworm_ova");
            giardiaLambia = jsonObject.getString("giardia_lambia");
            blastocystisHominis = jsonObject.getString("blastocystis_hominis");
            cyst = jsonObject.getString("cyst");
            trophozoite = jsonObject.getString("trophozoite");
            occultBlood = jsonObject.getString("occult_blood");
            pusCells = jsonObject.getString("pus_cells");
            rbc = jsonObject.getString("RBC");
            fatGlobules = jsonObject.getString("fat_globules");
            yeastCells = jsonObject.getString("yeast_cells");
            undigestedFood = jsonObject.getString("undigested_food");
            starchGranules = jsonObject.getString("starch_granules");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
