package com.e_medicalRecord.kinit.e_medicalrecord.Consultation.Class;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArrayAdapter_Strings {
    public ArrayList<String> arrayCategory, arrayGeneral, arraySkinBreast, arrayEyesEars, arrayCardio, arrayRespi, arrayGastro,
            arrayGenito, arrayMusculo, arrayNeuro, arrayAllergic;
    public ArrayList<Integer> arrayCategoryId, arrayGeneralId, arraySkinBreastId, arrayEyesEarsId, arrayCardioId, arrayRespiId,
            arrayGastroId, arrayGenitoId, arrayMusculoId, arrayNeuroId, arrayAllergicId;
    public ArrayList<Review_Of_Systems> reviewOfSystems;

    public ArrayAdapter_Strings(JSONArray jsonArray) {
        reviewOfSystems = new ArrayList<>();
        arrayCategory = new ArrayList<>();
        arrayGeneral = new ArrayList<>();
        arraySkinBreast = new ArrayList<>();
        arrayEyesEars = new ArrayList<>();
        arrayCardio = new ArrayList<>();
        arrayRespi = new ArrayList<>();
        arrayGastro = new ArrayList<>();
        arrayGenito = new ArrayList<>();
        arrayMusculo = new ArrayList<>();
        arrayNeuro = new ArrayList<>();
        arrayAllergic = new ArrayList<>();

        arrayCategoryId = new ArrayList<>();
        arrayGeneralId = new ArrayList<>();
        arraySkinBreastId = new ArrayList<>();
        arrayEyesEarsId = new ArrayList<>();
        arrayCardioId = new ArrayList<>();
        arrayRespiId = new ArrayList<>();
        arrayGastroId = new ArrayList<>();
        arrayGenitoId = new ArrayList<>();
        arrayMusculoId = new ArrayList<>();
        arrayNeuroId = new ArrayList<>();
        arrayAllergicId = new ArrayList<>();

        try {
            int jsonArrayLength = jsonArray.length();
            Review_Of_Systems reviewOfSystemsLone;
            JSONObject jsonObject;

            for (int x = 0; x < jsonArrayLength; x++) {
                jsonObject = jsonArray.getJSONObject(x);
                reviewOfSystems.add(new Review_Of_Systems(jsonObject));
                reviewOfSystemsLone = reviewOfSystems.get(reviewOfSystems.size() - 1);

                if (!arrayCategory.contains(reviewOfSystemsLone.category)) {
                    arrayCategory.add(reviewOfSystemsLone.category);
                    arrayCategoryId.add(reviewOfSystemsLone.rosCategoryId);
                }
                switch (reviewOfSystemsLone.rosCategoryId) {
                    case 1:
                        arrayGeneral.add(reviewOfSystemsLone.item);
                        arrayGeneralId.add(reviewOfSystemsLone.rosId);
                        break;
                    case 2:
                        arraySkinBreast.add(reviewOfSystemsLone.item);
                        arraySkinBreastId.add(reviewOfSystemsLone.rosId);
                        break;
                    case 3:
                        arrayEyesEars.add(reviewOfSystemsLone.item);
                        arrayEyesEarsId.add(reviewOfSystemsLone.rosId);
                        break;
                    case 4:
                        arrayCardio.add(reviewOfSystemsLone.item);
                        arrayCardioId.add(reviewOfSystemsLone.rosId);
                        break;
                    case 5:
                        arrayRespi.add(reviewOfSystemsLone.item);
                        arrayRespiId.add(reviewOfSystemsLone.rosId);
                        break;
                    case 6:
                        arrayGastro.add(reviewOfSystemsLone.item);
                        arrayGastroId.add(reviewOfSystemsLone.rosId);
                        break;
                    case 7:
                        arrayGenito.add(reviewOfSystemsLone.item);
                        arrayGenitoId.add(reviewOfSystemsLone.rosId);
                        break;
                    case 8:
                        arrayMusculo.add(reviewOfSystemsLone.item);
                        arrayMusculoId.add(reviewOfSystemsLone.rosId);
                        break;
                    case 9:
                        arrayNeuro.add(reviewOfSystemsLone.item);
                        arrayNeuroId.add(reviewOfSystemsLone.rosId);
                        break;
                    case 10:
                        arrayAllergic.add(reviewOfSystemsLone.item);
                        arrayAllergicId.add(reviewOfSystemsLone.rosId);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
