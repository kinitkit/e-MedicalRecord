package com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView.CustomLinearLayoutManager;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView.RecyclerViewAdapter_LaboratoryFields;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_AlertDialog;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressBar;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressDialog;
import com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Class.Lab_Chemistry;
import com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Class.Lab_Fecalysis;
import com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Class.Lab_Hematology;
import com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Class.Lab_Urinalysis;
import com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Class.Laboratory;
import com.e_medicalRecord.kinit.e_medicalrecord.Profile.Class.Patient;
import com.e_medicalRecord.kinit.e_medicalrecord.Profile.Class.Viewer;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Enum.Laboratory_Tests;
import com.e_medicalRecord.kinit.e_medicalrecord.R;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.e_medicalRecord.kinit.e_medicalrecord.R.id.menu_tag;

public class Laboratory_Result_View extends AppCompatActivity {

    String table_name;
    Intent intent;
    //Enum
    Laboratory_Tests enum_laboratoryTests;
    //Classes
    Patient patient;
    Viewer viewer;
    Laboratory laboratory;
    Lab_Chemistry labChemistry;
    Lab_Fecalysis labFecalysis;
    Lab_Hematology labHematology;
    Lab_Urinalysis labUrinalysis;
    Custom_ProgressDialog progressDialog;
    Custom_AlertDialog alertDialog;
    Custom_ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        intent = getIntent();
        enum_laboratoryTests = Laboratory_Tests.values()[intent.getIntExtra("ordinal", 0)];
        switch (enum_laboratoryTests) {
            case BLOOD_CHEMISTRY:
                setContentView(R.layout.layout_view_chemistry);
                table_name = Lab_Chemistry.TABLE_NAME;
                break;
            case FECALYSIS:
                setContentView(R.layout.layout_view_fecalysis);
                table_name = Lab_Fecalysis.TABLE_NAME;
                break;
            case HEMATOLOGY:
                setContentView(R.layout.layout_view_hematology);
                table_name = Lab_Hematology.TABLE_NAME;
                break;
            case URINALYSIS:
                setContentView(R.layout.layout_view_urinalysis);
                table_name = Lab_Urinalysis.TABLE_NAME;
                break;
        }
        init();
    }

    void init() {
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");
        laboratory = intent.getExtras().getParcelable("laboratory");

        alertDialog = new Custom_AlertDialog(this);
        progressDialog = new Custom_ProgressDialog(this);
        progressBar = new Custom_ProgressBar(this);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fetchData(table_name, laboratory.lab_id);
    }

    void fetchData(final String tableName, final int id) {
        try {
            progressBar.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL_LABORATORY,
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
                                                labChemistry = new Lab_Chemistry(jsonObject);
                                                init_labChemistry();
                                                break;
                                            case FECALYSIS:
                                                labFecalysis = new Lab_Fecalysis(jsonObject);
                                                init_labFecalysis();
                                                break;
                                            case HEMATOLOGY:
                                                labHematology = new Lab_Hematology(jsonObject);
                                                init_labHematology();
                                                break;
                                            case URINALYSIS:
                                                labUrinalysis = new Lab_Urinalysis(jsonObject);
                                                init_labUrinalysis();
                                                break;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progressBar.hide();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.hide();
                            error.printStackTrace();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getTestResult");
                    params.put("device", "mobile");
                    params.put("id", String.valueOf(id));
                    params.put("table_name", tableName);
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressBar.hide();
            e.printStackTrace();
        }
    }

    void loadToRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    void loadDetails(String physicianName, String pathologist, String medTech, String labName, String datePerformed) {
        TextView tv_physicianName = (TextView) findViewById(R.id.tv_physicianName), tv_pathologist = (TextView) findViewById(R.id.tv_pathologist),
                tv_medTech = (TextView) findViewById(R.id.tv_medTech), tv_labName = (TextView) findViewById(R.id.tv_labName),
                tv_datePerformed = (TextView) findViewById(R.id.tv_date);

        tv_physicianName.setText(physicianName);
        tv_pathologist.setText(pathologist);
        tv_medTech.setText(medTech);
        tv_labName.setText(labName);
        tv_datePerformed.setText(datePerformed);
    }

    void loadRemarks(String remarks) {
        TextView tv_remarks = (TextView) findViewById(R.id.tv_remarks);
        tv_remarks.setText(remarks);
    }

    void init_labChemistry() {
        getSupportActionBar().setSubtitle("Blood Chemistry");
        loadDetails(labChemistry.physician_name, labChemistry.pathologist, labChemistry.medTech, labChemistry.lab_name,
                labChemistry.strDatePerformed);

        RecyclerView recyclerView_LabTest = (RecyclerView) findViewById(R.id.recyclerView_labTest);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_labTest = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_labTest.addItem("FBS:", labChemistry.fbs, "4.2 - 6.4 mmol/L");
        rvAdapter_labTest.addItem("Creatine:", labChemistry.creatine, "59 - 104 umol/L");
        rvAdapter_labTest.addItem("Triglycerides:", labChemistry.triglycerides, "0.41 - 1.88 mmol/L");
        rvAdapter_labTest.addItem("HDL:", labChemistry.hdl, "1.15 - 1.68 mmol/L");
        rvAdapter_labTest.addItem("LDL:", labChemistry.ldl, "0.00 - 3.40 mmol/L");
        rvAdapter_labTest.addItem("Uric Acid:", labChemistry.uricAcid, "0.201 - 0.413 mmol/L");
        rvAdapter_labTest.addItem("SGPT / ALAT:", labChemistry.sgpt_alat, "0 - 41 U/L");
        rvAdapter_labTest.addItem("Sodium:", labChemistry.sodium, "136 - 145 mmol/L");
        rvAdapter_labTest.addItem("Potassium:", labChemistry.potassium, "3.5 - 5.1 mmol/L");
        rvAdapter_labTest.addItem("Calcium:", labChemistry.calcium, "2.15-2.55 mmol/L");
        loadToRecyclerView(recyclerView_LabTest, rvAdapter_labTest);

        loadRemarks(labChemistry.remarks);
    }

    void init_labFecalysis() {
        getSupportActionBar().setSubtitle("Fecalysis");
        loadDetails(labFecalysis.physician_name, labFecalysis.pathologist, labFecalysis.medTech, labFecalysis.lab_name,
                labFecalysis.strDatePerformed);

        RecyclerView recyclerView_physicalCharacteristics = (RecyclerView) findViewById(R.id.recyclerView_physicalCharacteristics);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_physicalCharacteristics = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_physicalCharacteristics.addItem("Color:", labFecalysis.color, null);
        rvAdapter_physicalCharacteristics.addItem("Consistency:", labFecalysis.consistency, null);

        RecyclerView recyclerView_microscopicExamination = (RecyclerView) findViewById(R.id.recyclerView_microscopicExamination);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_microscopicExamination = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_microscopicExamination.addItem("Ascaris Lumbricoides:", labFecalysis.ascarisLumbricoides, "/hpf");
        rvAdapter_microscopicExamination.addItem("Trichuris trichiura:", labFecalysis.trichurisTrichiura, "/hpf");
        rvAdapter_microscopicExamination.addItem("Enterobius Vermicularis:", labFecalysis.enterobiusVermicularis, "/hpf");
        rvAdapter_microscopicExamination.addItem("Hookworm Ova:", labFecalysis.hookwormOva, "/hpf");
        rvAdapter_microscopicExamination.addItem("Giardia Lambia:", labFecalysis.giardiaLambia, "/hpf");
        rvAdapter_microscopicExamination.addItem("Blastocystis Hominis:", labFecalysis.blastocystisHominis, "/hpf");
        rvAdapter_microscopicExamination.addItem("Entamoeba Histolytica Cyst:", labFecalysis.cyst, "/hpf");
        rvAdapter_microscopicExamination.addItem("Entamoeba Histolytica Troph:", labFecalysis.trophozoite, "/hpf");
        rvAdapter_microscopicExamination.addItem("Occult Blood:", labFecalysis.occultBlood, null);
        rvAdapter_microscopicExamination.addItem("Pus Cells:", labFecalysis.pusCells, "/hpf");
        rvAdapter_microscopicExamination.addItem("Red Blood Cells:", labFecalysis.rbc, "/hpf");
        rvAdapter_microscopicExamination.addItem("Fat Globules:", labFecalysis.fatGlobules, null);
        rvAdapter_microscopicExamination.addItem("Yeast Cells:", labFecalysis.yeastCells, null);
        rvAdapter_microscopicExamination.addItem("Undigested Food:", labFecalysis.undigestedFood, null);
        rvAdapter_microscopicExamination.addItem("Starch Granules:", labFecalysis.starchGranules, null);

        loadToRecyclerView(recyclerView_physicalCharacteristics, rvAdapter_physicalCharacteristics);
        loadToRecyclerView(recyclerView_microscopicExamination, rvAdapter_microscopicExamination);

        loadRemarks(labFecalysis.remarks);
    }

    void init_labHematology() {
        getSupportActionBar().setSubtitle("Hematology");
        loadDetails(labHematology.physician_name, labHematology.pathologist, labHematology.medTech, labHematology.lab_name,
                labHematology.strDatePerformed);

        RecyclerView recyclerView_LabTest = (RecyclerView) findViewById(R.id.recyclerView_labTest);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_labTest = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_labTest.addItem("Hemoglobin:", labHematology.hemoglobin, "F: (120 - 140); M: (140 - 170) g/L");
        rvAdapter_labTest.addItem("Hematocrit:", labHematology.hematrocit, "F: (0.38 - 0.48); M: (0.40 - 0.50) g/L");
        rvAdapter_labTest.addItem("RBC:", labHematology.rbc, "F: (4.0 - 5.0); M: (4.5 - 6.0) x 10 12/L");
        rvAdapter_labTest.addItem("WBC:", labHematology.wbc, "5.0 - 10.0 x 10 9/L");
        rvAdapter_labTest.addItem("Platelets:", labHematology.platelets, "150 - 450 x 10 3/uL");
        rvAdapter_labTest.addItem("Reticulocytes:", labHematology.reticulocytes, "0.005 - 0.015");
        rvAdapter_labTest.addItem("MCV:", labHematology.mcv, "79.9 - 92.2");
        rvAdapter_labTest.addItem("MCH:", labHematology.mch, "25.7 - 32.2");
        rvAdapter_labTest.addItem("MCHC:", labHematology.mchc, "32.2 - 36.5");
        rvAdapter_labTest.addItem("ESR (Sedimentation Rate):", labHematology.esr, "F: (0 - 20); M (0 - 10) mm/hr");
        rvAdapter_labTest.addItem("Segmenters:", labHematology.segmenters, "0.50 - 0.70");
        rvAdapter_labTest.addItem("Stab:", labHematology.stab, "0.01 - 0.06");
        rvAdapter_labTest.addItem("Lymphocytes:", labHematology.lymphocytes, "0.20 - 0.40");
        rvAdapter_labTest.addItem("Monocytes:", labHematology.monocytes, "0.02 - 0.07");
        rvAdapter_labTest.addItem("Eosinophils:", labHematology.eosinophils, "0.01 - 0.05");
        rvAdapter_labTest.addItem("Basophils:", labHematology.basophils, "0.00 - 0.01");
        rvAdapter_labTest.addItem("Malarial Smear:", labHematology.malarialSmear, null);
        rvAdapter_labTest.addItem("Bleeding Time:", labHematology.bleedingTime, "1 - 3 mins.");
        rvAdapter_labTest.addItem("Clotting Time:", labHematology.clottingTime, "2 - 6 mins.");
        rvAdapter_labTest.addItem("Blood Type:", labHematology.bloodType, null);
        rvAdapter_labTest.addItem("Rh:", labHematology.rh, null);
        loadToRecyclerView(recyclerView_LabTest, rvAdapter_labTest);

        loadRemarks(labHematology.remarks);
    }

    void init_labUrinalysis() {
        getSupportActionBar().setSubtitle("Urinalysis");
        loadDetails(labUrinalysis.physician_name, labUrinalysis.pathologist, labUrinalysis.medTech, labUrinalysis.lab_name,
                labUrinalysis.strDatePerformed);

        RecyclerView recyclerView_physical = (RecyclerView) findViewById(R.id.recyclerView_physical);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_physical = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_physical.addItem("Color:", labUrinalysis.color, null);
        rvAdapter_physical.addItem("Transparency:", labUrinalysis.transparency, null);
        rvAdapter_physical.addItem("Reaction:", labUrinalysis.reaction, null);
        rvAdapter_physical.addItem("Specific Gravity:", labUrinalysis.specificGravity, "1.016 - 1.022");

        RecyclerView recyclerView_microscopic = (RecyclerView) findViewById(R.id.recyclerView_microscopic);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_microscopic = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_microscopic.addItem("Pus Cells:", labUrinalysis.pusCells, "/hpf");
        rvAdapter_microscopic.addItem("RBC:", labUrinalysis.rbc, "/hpf");
        rvAdapter_microscopic.addItem("Epith Cells:", labUrinalysis.epithCells, null);
        rvAdapter_microscopic.addItem("Renal Cells:", labUrinalysis.renalCells, null);
        rvAdapter_microscopic.addItem("Mucus Threads:", labUrinalysis.mucusThreads, null);
        rvAdapter_microscopic.addItem("Bacteria:", labUrinalysis.bacteria, null);
        rvAdapter_microscopic.addItem("Yeast Cells:", labUrinalysis.yeastCells, null);

        RecyclerView recyclerView_chemical = (RecyclerView) findViewById(R.id.recyclerView_chemical);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_chemical = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_chemical.addItem("Sugar:", labUrinalysis.sugar.equals("1") ? "Positive" : "Negative", "Negative");
        rvAdapter_chemical.addItem("Albumin:", labUrinalysis.albumin.equals("1") ? "Positive" : "Negative", "Negative");
        rvAdapter_chemical.addItem("Ketone:", labUrinalysis.ketone.equals("1") ? "Positive" : "Negative", "Negative");
        rvAdapter_chemical.addItem("Bilirubin:", labUrinalysis.bilirubin.equals("1") ? "Positive" : "Negative", "Negative");
        rvAdapter_chemical.addItem("Blood:", labUrinalysis.blood.equals("1") ? "Positive" : "Negative", "Negative");
        rvAdapter_chemical.addItem("Urobilinogen:", labUrinalysis.urobilinogen, "0.1 - 1.0");
        rvAdapter_chemical.addItem("BacteriaNit:", labUrinalysis.bacteriaNit.equals("1") ? "Positive" : "Negative", "Negative");
        rvAdapter_chemical.addItem("Leukocyte:", labUrinalysis.leukocyte.equals("1") ? "Positive" : "Negative", "Negative");

        RecyclerView recyclerView_crystals = (RecyclerView) findViewById(R.id.recyclerView_crystals);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_crystals = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_crystals.addItem("Amorphous Subs:", labUrinalysis.amorphousSubs, null);
        rvAdapter_crystals.addItem("Uric Acid:", labUrinalysis.uricAcid, null);
        rvAdapter_crystals.addItem("Calcium Oxalate:", labUrinalysis.calciumOxalate, null);
        rvAdapter_crystals.addItem("Triple Phosphate:", labUrinalysis.triplePhosphate, null);

        RecyclerView recyclerView_casts = (RecyclerView) findViewById(R.id.recyclerView_casts);
        RecyclerViewAdapter_LaboratoryFields rvAdapter_casts = new RecyclerViewAdapter_LaboratoryFields();
        rvAdapter_casts.addItem("Pus Cast:", labUrinalysis.pusCast, "/lpf");
        rvAdapter_casts.addItem("Hyaline:", labUrinalysis.hyaline, "/lpf");
        rvAdapter_casts.addItem("Fine Granular:", labUrinalysis.fineGranular, "/lpf");
        rvAdapter_casts.addItem("Coarse Granular:", labUrinalysis.coarseGranular, "/lpf");

        loadToRecyclerView(recyclerView_physical, rvAdapter_physical);
        loadToRecyclerView(recyclerView_microscopic, rvAdapter_microscopic);
        loadToRecyclerView(recyclerView_chemical, rvAdapter_chemical);
        loadToRecyclerView(recyclerView_crystals, rvAdapter_crystals);
        loadToRecyclerView(recyclerView_casts, rvAdapter_casts);

        loadRemarks(labUrinalysis.remarks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (viewer == null) {
            getMenuInflater().inflate(R.menu.menu_with_tag, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case menu_tag:
                Intent intent = new Intent(this, Tagged_Laboratory.class);
                intent.putExtra("patient", patient);
                intent.putExtra("lab_id", laboratory.lab_id);
                startActivity(intent);
                break;
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
