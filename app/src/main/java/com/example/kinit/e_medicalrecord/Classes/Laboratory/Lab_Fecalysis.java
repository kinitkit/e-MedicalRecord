package com.example.kinit.e_medicalrecord.Classes.Laboratory;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Lab_Fecalysis {
    public int id, userDataId;
    public String physicianName, labName, datePerformed, color, consistency, microscopicExamination, ascarisLumbricoides, trichurisTrichiura, enterobiusVermicularis,
            hookwormOva, giardiaLambia, blastocystisHominis, cyst, trophozoite, occultBlood, pusCells, rbc, fatGlobules, yeastCells,
            undigestedFood, starchGranules, remark;
    public Calendar calendar = Calendar.getInstance();

    public void setDate(String date) {
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            this.datePerformed = new SimpleDateFormat("MMM dd, yyyy").format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Lab_Fecalysis() {
    }

    public Lab_Fecalysis(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            userDataId = jsonObject.getInt("user_data_id");
            physicianName = jsonObject.getString("physician_name");
            labName = jsonObject.getString("lab_name");
            setDate(jsonObject.getString("date_performed"));
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
            remark = jsonObject.getString("remark");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
