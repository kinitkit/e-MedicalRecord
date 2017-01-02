package com.example.kinit.e_medicalrecord.Fragments.Laboratory;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.RecyclerViewAdapter_LaboratoryFields;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Chemistry;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Fecalysis;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Hematology;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Urinalysis;
import com.example.kinit.e_medicalrecord.Enum.Laboratory_Tests;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_View_Laboratory extends Fragment {
    //View
    View rootView;

    String table_name;
    int lab_id;

    //Enum
    Laboratory_Tests enum_laboratoryTests;
    //Classes
    Lab_Chemistry labChemistry;
    Lab_Fecalysis labFecalysis;
    Lab_Hematology labHematology;
    Lab_Urinalysis labUrinalysis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        switch (enum_laboratoryTests) {
            case BLOOD_CHEMISTRY:
                rootView = inflater.inflate(R.layout.layout_view_chemistry, container, false);
                break;
            case FECALYSIS:
                rootView = inflater.inflate(R.layout.layout_view_fecalysis, container, false);
                break;
            case HEMATOLOGY:
                rootView = inflater.inflate(R.layout.layout_view_hematology, container, false);
                break;
            case URINALYSIS:
                rootView = inflater.inflate(R.layout.layout_view_urinalysis, container, false);
                break;
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        fetchData(table_name, lab_id);
    }

    public void setLab(Laboratory_Tests enum_laboratoryTests, Object object) {
        this.enum_laboratoryTests = enum_laboratoryTests;
        switch (enum_laboratoryTests) {
            case BLOOD_CHEMISTRY:
                this.labChemistry = (Lab_Chemistry) object;
                table_name = Lab_Chemistry.TABLE_NAME;
                lab_id = labChemistry.id;
                break;
            case FECALYSIS:
                this.labFecalysis = (Lab_Fecalysis) object;
                table_name = Lab_Fecalysis.TABLE_NAME;
                lab_id = labFecalysis.id;
                break;
            case HEMATOLOGY:
                this.labHematology = (Lab_Hematology) object;
                table_name = Lab_Hematology.TABLE_NAME;
                lab_id = labHematology.id;
                break;
            case URINALYSIS:
                this.labUrinalysis = (Lab_Urinalysis) object;
                table_name = Lab_Urinalysis.TABLE_NAME;
                lab_id = labUrinalysis.id;
                break;
        }
    }

    void fetchData(final String tableName, final int id) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("error", response);
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    if (jsonObject.getString("code").equals("successful")) {
                                        rootJsonArray = rootJsonArray.getJSONArray(1);
                                        jsonObject = rootJsonArray.getJSONObject(0);
                                        switch (enum_laboratoryTests) {
                                            case BLOOD_CHEMISTRY:
                                                //labChemistry.setValues(jsonObject);
                                                init_labChemistry();
                                                break;
                                            case FECALYSIS:
                                                labFecalysis.setValues(jsonObject);
                                                init_labFecalysis();
                                                break;
                                            case HEMATOLOGY:
                                                labHematology.setValues(jsonObject);
                                                init_labHematology();
                                                break;
                                            case URINALYSIS:
                                                labUrinalysis.setValues(jsonObject);
                                                init_labUrinalysis();
                                                break;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getLabTest");
                    params.put("device", "mobile");
                    params.put("id", String.valueOf(id));
                    params.put("table_name", tableName);
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }

    }

    void loadDetails(String physicianName, String labName, String datePerformed) {
        TextView tv_physicianName = (TextView) rootView.findViewById(R.id.tv_physicianName), tv_labName = (TextView) rootView.findViewById(R.id.tv_labName),
                tv_datePerformed = (TextView) rootView.findViewById(R.id.tv_date);

        tv_physicianName.setText(physicianName);
        tv_labName.setText(labName);
        tv_datePerformed.setText(datePerformed);
    }

    void loadToRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    void loadRemarks(String remarks) {
        TextView tv_remarks = (TextView) rootView.findViewById(R.id.tv_remarks);
        tv_remarks.setText(remarks);
    }

    void init_labChemistry() {
        //loadDetails(labChemistry.physicianName, labChemistry.labName, labChemistry.datePerformed);

        RecyclerView recyclerView_LabTest = (RecyclerView) rootView.findViewById(R.id.recyclerView_labTest);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_labTest = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_labTest.addItem("FBS:", labChemistry.fbs);
        rvAdapter_labTest.addItem("Creatine:", labChemistry.creatine);
        rvAdapter_labTest.addItem("Triglycerides:", labChemistry.triglycerides);
        rvAdapter_labTest.addItem("HDL:", labChemistry.hdl);
        rvAdapter_labTest.addItem("LDL:", labChemistry.ldl);
        rvAdapter_labTest.addItem("Uric Acid:", labChemistry.uricAcid);
        rvAdapter_labTest.addItem("SGPT / ALAT:", labChemistry.sgpt_alat);
        rvAdapter_labTest.addItem("Sodium:", labChemistry.sodium);
        rvAdapter_labTest.addItem("Potassium:", labChemistry.potassium);
        rvAdapter_labTest.addItem("Calcium:", labChemistry.calcium);
        loadToRecyclerView(recyclerView_LabTest, rvAdapter_labTest);

        //loadRemarks(labChemistry.remark);
    }

    void init_labFecalysis() {
        loadDetails(labFecalysis.physicianName, labFecalysis.labName, labFecalysis.datePerformed);

        RecyclerView recyclerView_physicalCharacteristics = (RecyclerView) rootView.findViewById(R.id.recyclerView_physicalCharacteristics);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_physicalCharacteristics = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_physicalCharacteristics.addItem("Color:", labFecalysis.color);
        rvAdapter_physicalCharacteristics.addItem("Consistency:", labFecalysis.consistency);

        RecyclerView recyclerView_microscopicExamination = (RecyclerView) rootView.findViewById(R.id.recyclerView_microscopicExamination);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_microscopicExamination = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_microscopicExamination.addItem("Ascaris Lumbricoides:", labFecalysis.ascarisLumbricoides);
        rvAdapter_microscopicExamination.addItem("Trichuris trichiura:", labFecalysis.trichurisTrichiura);
        rvAdapter_microscopicExamination.addItem("Enterobius Vermicularis:", labFecalysis.enterobiusVermicularis);
        rvAdapter_microscopicExamination.addItem("Hookworm Ova:", labFecalysis.hookwormOva);
        rvAdapter_microscopicExamination.addItem("Giardia Lambia:", labFecalysis.giardiaLambia);
        rvAdapter_microscopicExamination.addItem("Blastocystis Hominis:", labFecalysis.blastocystisHominis);
        rvAdapter_microscopicExamination.addItem("Entamoeba Histolytica Cyst:", labFecalysis.cyst);
        rvAdapter_microscopicExamination.addItem("Entamoeba Histolytica Troph:", labFecalysis.trophozoite);
        rvAdapter_microscopicExamination.addItem("Occult Blood:", labFecalysis.occultBlood);
        rvAdapter_microscopicExamination.addItem("Pus Cells:", labFecalysis.pusCells);
        rvAdapter_microscopicExamination.addItem("Red Blood Cells:", labFecalysis.rbc);
        rvAdapter_microscopicExamination.addItem("Fat Globules:", labFecalysis.fatGlobules);
        rvAdapter_microscopicExamination.addItem("Yeast Cells:", labFecalysis.yeastCells);
        rvAdapter_microscopicExamination.addItem("Undigested Food:", labFecalysis.undigestedFood);
        rvAdapter_microscopicExamination.addItem("Starch Granules:", labFecalysis.starchGranules);

        loadToRecyclerView(recyclerView_physicalCharacteristics, rvAdapter_physicalCharacteristics);
        loadToRecyclerView(recyclerView_microscopicExamination, rvAdapter_microscopicExamination);

        loadRemarks(labFecalysis.remark);
    }

    void init_labHematology() {
        loadDetails(labHematology.physicianName, labHematology.labName, labHematology.datePerformed);

        RecyclerView recyclerView_LabTest = (RecyclerView) rootView.findViewById(R.id.recyclerView_labTest);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_labTest = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_labTest.addItem("Hemoglobin:", labHematology.hemoglobin);
        rvAdapter_labTest.addItem("Hematocrit:", labHematology.hematrocit);
        rvAdapter_labTest.addItem("RBC:", labHematology.rbc);
        rvAdapter_labTest.addItem("WBC:", labHematology.wbc);
        rvAdapter_labTest.addItem("Platelets:", labHematology.platelets);
        rvAdapter_labTest.addItem("Reticulocytes:", labHematology.reticulocytes);
        rvAdapter_labTest.addItem("MCV:", labHematology.mcv);
        rvAdapter_labTest.addItem("MCH:", labHematology.mch);
        rvAdapter_labTest.addItem("MCHC:", labHematology.mchc);
        rvAdapter_labTest.addItem("ESR (Sedimentation Rate):", labHematology.esr);
        rvAdapter_labTest.addItem("Segmenters:", labHematology.segmenters);
        rvAdapter_labTest.addItem("Stab:", labHematology.stab);
        rvAdapter_labTest.addItem("Lymphocytes:", labHematology.lymphocytes);
        rvAdapter_labTest.addItem("Monocytes:", labHematology.monocytes);
        rvAdapter_labTest.addItem("Eosinophils:", labHematology.eosinophils);
        rvAdapter_labTest.addItem("Basophils:", labHematology.basophils);
        rvAdapter_labTest.addItem("Malarial Smear:", labHematology.malarialSmear);
        rvAdapter_labTest.addItem("Bleeding Time:", labHematology.bleedingTime);
        rvAdapter_labTest.addItem("Clotting Time:", labHematology.clottingTime);
        rvAdapter_labTest.addItem("Blood Type:", labHematology.bloodType);
        rvAdapter_labTest.addItem("Rh:", labHematology.rh);
        loadToRecyclerView(recyclerView_LabTest, rvAdapter_labTest);

        loadRemarks(labHematology.remark);
    }

    void init_labUrinalysis() {
        loadDetails(labUrinalysis.physicianName, labUrinalysis.labName, labUrinalysis.datePerformed);

        RecyclerView recyclerView_physical = (RecyclerView) rootView.findViewById(R.id.recyclerView_physical);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_physical = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_physical.addItem("Color:", labUrinalysis.color);
        rvAdapter_physical.addItem("Transparency:", labUrinalysis.transparency);
        rvAdapter_physical.addItem("Reaction:", labUrinalysis.reaction);
        rvAdapter_physical.addItem("Specific Gravity:", labUrinalysis.specificGravity);

        RecyclerView recyclerView_microscopic = (RecyclerView) rootView.findViewById(R.id.recyclerView_microscopic);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_microscopic = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_microscopic.addItem("Pus Cells:", labUrinalysis.pusCells);
        rvAdapter_microscopic.addItem("RBC:", labUrinalysis.rbc);
        rvAdapter_microscopic.addItem("Epith Cells:", labUrinalysis.epithCells);
        rvAdapter_microscopic.addItem("Renal Cells:", labUrinalysis.renalCells);
        rvAdapter_microscopic.addItem("Mucus Threads:", labUrinalysis.mucusThreads);
        rvAdapter_microscopic.addItem("Bacteria:", labUrinalysis.bacteria);
        rvAdapter_microscopic.addItem("Yeast Cells:", labUrinalysis.yeastCells);

        RecyclerView recyclerView_chemical = (RecyclerView) rootView.findViewById(R.id.recyclerView_chemical);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_chemical = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_chemical.addItem("Sugar:", labUrinalysis.sugar);
        rvAdapter_chemical.addItem("Albumin:", labUrinalysis.albumin);
        rvAdapter_chemical.addItem("Ketone:", labUrinalysis.ketone);
        rvAdapter_chemical.addItem("Bilirubin:", labUrinalysis.bilirubin);
        rvAdapter_chemical.addItem("Blood:", labUrinalysis.blood);
        rvAdapter_chemical.addItem("Urobilinogen:", labUrinalysis.urobilinogen);
        rvAdapter_chemical.addItem("BacteriaNit:", labUrinalysis.bacteriaNit);
        rvAdapter_chemical.addItem("Leukocyte:", labUrinalysis.leukocyte);

        RecyclerView recyclerView_crystals = (RecyclerView) rootView.findViewById(R.id.recyclerView_crystals);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_crystals = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_crystals.addItem("Amorphous Subs:", labUrinalysis.amorphousSubs);
        rvAdapter_crystals.addItem("Uric Acid:", labUrinalysis.uricAcid);
        rvAdapter_crystals.addItem("Calcium Oxalate:", labUrinalysis.calciumOxalate);
        rvAdapter_crystals.addItem("Triple Phosphate:", labUrinalysis.triplePhosphate);

        RecyclerView recyclerView_casts = (RecyclerView) rootView.findViewById(R.id.recyclerView_casts);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_casts = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_casts.addItem("Pus Cast:", labUrinalysis.pusCast);
        rvAdapter_casts.addItem("Hyaline:", labUrinalysis.hyaline);
        rvAdapter_casts.addItem("Fine Granular:", labUrinalysis.fineGranular);
        rvAdapter_casts.addItem("Coarse Granular:", labUrinalysis.coarseGranular);

        loadToRecyclerView(recyclerView_physical, rvAdapter_physical);
        loadToRecyclerView(recyclerView_microscopic, rvAdapter_microscopic);
        loadToRecyclerView(recyclerView_chemical, rvAdapter_chemical);
        loadToRecyclerView(recyclerView_crystals, rvAdapter_crystals);
        loadToRecyclerView(recyclerView_casts, rvAdapter_casts);

        loadRemarks(labUrinalysis.remark);
    }


}
