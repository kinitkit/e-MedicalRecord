package com.example.kinit.e_medicalrecord.Consultation.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Consultation.Class.Consultation;
import com.example.kinit.e_medicalrecord.Consultation.Class.Consultation_ROS;
import com.example.kinit.e_medicalrecord.General.Adapters.RecyclerView.RecyclerViewAdapter_ReviewOfSystems;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressBar;
import com.example.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.General.Request.UrlString;
import com.example.kinit.e_medicalrecord.Profile.Class.Patient;
import com.example.kinit.e_medicalrecord.Profile.Class.Viewer;
import com.example.kinit.e_medicalrecord.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Consultation_View extends AppCompatActivity {

    //Classes
    Intent intent;
    Patient patient;
    Viewer viewer;
    Consultation consultation;

    ArrayList<Consultation_ROS> consultationRos_general, consultationRos_skinBreast, consultationRos_eyesEars,
            consultationRos_cardio, consultationRos_respi, consultationRos_gastro, consultationRos_genito,
            consultationRos_musculo, consultationRos_neuro, consultationRos_allergic;
    Custom_AlertDialog alertDialog;
    Custom_ProgressBar progressBar;

    //Widgets
    TextView tv_physician, tv_date, tv_chiefComplaint, tv_presentIllness, tv_height, tv_weight, tv_bloodPressure, tv_respirationRate,
            tv_temperature, tv_pulseRate, tv_diagnosis;
    LinearLayout linear_general, linear_skinBreast, linear_eyesEars, linear_cardio, linear_respi, linear_gastro, linear_genito,
            linear_musculo, linear_neuro, linear_allergic;
    RecyclerView recyclerView_general, recyclerView_skinBreast, recyclerView_eyesEars, recyclerView_cardio, recyclerView_respi,
            recyclerView_gastro, recyclerView_genito, recyclerView_musculo, recyclerView_neuro, recyclerView_allergic;
    RecyclerViewAdapter_ReviewOfSystems adapter_general, adapter_skinBreast, adapter_eyesEars, adapter_cardio, adapter_respi, adapter_gastro, adapter_genito,
            adapter_musculo, adapter_neuro, adapter_allergic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_consultation_view);
        init();
    }

    void init() {
        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");
        consultation = intent.getExtras().getParcelable("consultation");

        alertDialog = new Custom_AlertDialog(this);
        progressBar = new Custom_ProgressBar(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Consultation");
        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_physician = (TextView) findViewById(R.id.tv_physician);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_chiefComplaint = (TextView) findViewById(R.id.tv_chiefComplaint);
        tv_presentIllness = (TextView) findViewById(R.id.tv_presentIllness);
        tv_height = (TextView) findViewById(R.id.tv_height);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_bloodPressure = (TextView) findViewById(R.id.tv_bloodPressure);
        tv_respirationRate = (TextView) findViewById(R.id.tv_respirationRate);
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_pulseRate = (TextView) findViewById(R.id.tv_pulseRate);
        tv_diagnosis = (TextView) findViewById(R.id.tv_diagnosis);

        initLinearLayoutsAndRecyclerView();

        fetchData();
    }

    void initLinearLayoutsAndRecyclerView() {
        initArrayList();

        //LinearLayout Section
        linear_general = (LinearLayout) findViewById(R.id.linear_general);
        linear_skinBreast = (LinearLayout) findViewById(R.id.linear_skinBreast);
        linear_eyesEars = (LinearLayout) findViewById(R.id.linear_eyesEars);
        linear_cardio = (LinearLayout) findViewById(R.id.linear_cardio);
        linear_respi = (LinearLayout) findViewById(R.id.linear_respi);
        linear_gastro = (LinearLayout) findViewById(R.id.linear_gastro);
        linear_genito = (LinearLayout) findViewById(R.id.linear_genito);
        linear_musculo = (LinearLayout) findViewById(R.id.linear_musculo);
        linear_neuro = (LinearLayout) findViewById(R.id.linear_neuro);
        linear_allergic = (LinearLayout) findViewById(R.id.linear_allergic);

        //RecyclerView Section
        recyclerView_general = (RecyclerView) findViewById(R.id.recyclerView_general);
        recyclerView_skinBreast = (RecyclerView) findViewById(R.id.recyclerView_skinBreast);
        recyclerView_eyesEars = (RecyclerView) findViewById(R.id.recyclerView_eyesEars);
        recyclerView_cardio = (RecyclerView) findViewById(R.id.recyclerView_cardio);
        recyclerView_respi = (RecyclerView) findViewById(R.id.recyclerView_respi);
        recyclerView_gastro = (RecyclerView) findViewById(R.id.recyclerView_gastro);
        recyclerView_genito = (RecyclerView) findViewById(R.id.recyclerView_genito);
        recyclerView_musculo = (RecyclerView) findViewById(R.id.recyclerView_musculo);
        recyclerView_neuro = (RecyclerView) findViewById(R.id.recyclerView_neuro);
        recyclerView_allergic = (RecyclerView) findViewById(R.id.recyclerView_allergic);

        //RecyclerView Adapter Section
        adapter_general = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_general));
        adapter_skinBreast = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_skinBreast));
        adapter_eyesEars = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_eyesEars));
        adapter_cardio = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_cardio));
        adapter_respi = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_respi));
        adapter_gastro = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_gastro));
        adapter_genito = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_genito));
        adapter_musculo = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_musculo));
        adapter_neuro = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_neuro));
        adapter_allergic = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_allergic));

        //Set RecyclerView Adapter Section
        recyclerView_general.setAdapter(adapter_general);
        recyclerView_skinBreast.setAdapter(adapter_skinBreast);
        recyclerView_eyesEars.setAdapter(adapter_eyesEars);
        recyclerView_cardio.setAdapter(adapter_cardio);
        recyclerView_respi.setAdapter(adapter_respi);
        recyclerView_gastro.setAdapter(adapter_gastro);
        recyclerView_genito.setAdapter(adapter_genito);
        recyclerView_musculo.setAdapter(adapter_musculo);
        recyclerView_neuro.setAdapter(adapter_neuro);
        recyclerView_allergic.setAdapter(adapter_allergic);
        //
        //RecyclerView LayoutManager Section
        RecyclerView.LayoutManager layoutManager_general, layoutManager_SkinBreast, layoutManager_eyesEars,
                layoutManager_cardio, layoutManager_respi, layoutManager_gastro, layoutManager_genito,
                layoutManager_musculo, layoutManager_neuro, layoutManager_allergic;

        layoutManager_general = new LinearLayoutManager(this);
        layoutManager_SkinBreast = new LinearLayoutManager(this);
        layoutManager_eyesEars = new LinearLayoutManager(this);
        layoutManager_cardio = new LinearLayoutManager(this);
        layoutManager_respi = new LinearLayoutManager(this);
        layoutManager_gastro = new LinearLayoutManager(this);
        layoutManager_genito = new LinearLayoutManager(this);
        layoutManager_musculo = new LinearLayoutManager(this);
        layoutManager_neuro = new LinearLayoutManager(this);
        layoutManager_allergic = new LinearLayoutManager(this);

        recyclerView_general.setLayoutManager(layoutManager_general);
        recyclerView_skinBreast.setLayoutManager(layoutManager_SkinBreast);
        recyclerView_eyesEars.setLayoutManager(layoutManager_eyesEars);
        recyclerView_cardio.setLayoutManager(layoutManager_cardio);
        recyclerView_respi.setLayoutManager(layoutManager_respi);
        recyclerView_gastro.setLayoutManager(layoutManager_gastro);
        recyclerView_genito.setLayoutManager(layoutManager_genito);
        recyclerView_musculo.setLayoutManager(layoutManager_musculo);
        recyclerView_neuro.setLayoutManager(layoutManager_neuro);
        recyclerView_allergic.setLayoutManager(layoutManager_allergic);

        initArrayList();
    }

    void initArrayList() {
        consultationRos_general = new ArrayList<>();
        consultationRos_skinBreast = new ArrayList<>();
        consultationRos_eyesEars = new ArrayList<>();
        consultationRos_cardio = new ArrayList<>();
        consultationRos_respi = new ArrayList<>();
        consultationRos_gastro = new ArrayList<>();
        consultationRos_genito = new ArrayList<>();
        consultationRos_musculo = new ArrayList<>();
        consultationRos_neuro = new ArrayList<>();
        consultationRos_allergic = new ArrayList<>();
    }

    ArrayList<Consultation_ROS> getClone(ArrayList<Consultation_ROS> consultationRoses) {
        return (ArrayList<Consultation_ROS>) consultationRoses.clone();
    }

    void insertCheckWhichCategory(Consultation_ROS consultationRos) {
        switch (consultationRos.rosCategoryId) {
            case 1:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_general, adapter_general, linear_general);
                break;
            case 2:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_skinBreast, adapter_skinBreast, linear_skinBreast);
                break;
            case 3:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_eyesEars, adapter_eyesEars, linear_eyesEars);
                break;
            case 4:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_cardio, adapter_cardio, linear_cardio);
                break;
            case 5:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_respi, adapter_respi, linear_respi);
                break;
            case 6:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_gastro, adapter_gastro, linear_gastro);
                break;
            case 7:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_genito, adapter_genito, linear_genito);
                break;
            case 8:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_musculo, adapter_musculo, linear_musculo);
                break;
            case 9:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_neuro, adapter_neuro, linear_neuro);
                break;
            case 10:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_allergic, adapter_allergic, linear_allergic);
                break;
        }
    }

    void insertToArrayAndUpdateAdapter(Consultation_ROS consultationRos, ArrayList<Consultation_ROS> consultationRos_array,
                                       RecyclerViewAdapter_ReviewOfSystems consultationRos_adapter, LinearLayout consultationRos_linearLayout) {
        consultationRos_array.add(consultationRos);
        checkIfArrayIsEmpty(consultationRos_array, consultationRos_linearLayout);
        consultationRos_adapter.addItem(consultationRos);
    }

    void checkIfArrayIsEmpty(ArrayList<Consultation_ROS> consultationRos_array, LinearLayout consultationRos_linearLayout) {
        if (consultationRos_array.size() > 0) {
            consultationRos_linearLayout.setVisibility(View.VISIBLE);
        } else {
            consultationRos_linearLayout.setVisibility(View.GONE);
        }
    }

    void setToEditText(){
        tv_physician.setText(consultation.physicianName);
        tv_date.setText(consultation.strDateTime);
        tv_chiefComplaint.setText(consultation.chiefComplaint);
        tv_presentIllness.setText(consultation.presentIllness);
        tv_height.setText(String.valueOf(consultation.height));
        tv_weight.setText(String.valueOf(consultation.weight));
        tv_bloodPressure.setText(consultation.bloodPressure);
        tv_respirationRate.setText(String.valueOf(consultation.respirationRate));
        tv_temperature.setText(String.valueOf(consultation.temperature));
        tv_pulseRate.setText(String.valueOf(consultation.pulseRate));
        tv_diagnosis.setText(consultation.diagnosis);
    }

    void fetchData() {
        try {
            progressBar.show();
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_CONSULTATION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("view", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response), jsonArray;
                                JSONObject jsonObject;
                                int jsonArrayLength;

                                if (rootJsonArray.get(0) instanceof JSONObject) {
                                    jsonObject = rootJsonArray.getJSONObject(0);
                                    if (jsonObject.has("code")) {
                                        String code = jsonObject.getString("code");
                                        switch (code) {
                                            case "success":
                                                if (rootJsonArray.get(1) instanceof JSONArray) {
                                                    jsonArray = rootJsonArray.getJSONArray(1);
                                                    jsonObject = jsonArray.getJSONObject(0);
                                                    consultation = new Consultation(jsonObject);

                                                    if (rootJsonArray.get(2) instanceof JSONArray) {
                                                        jsonArray = rootJsonArray.getJSONArray(2);
                                                        jsonArrayLength = jsonArray.length();
                                                        for(int x = 0; x < jsonArrayLength; x++){
                                                            insertCheckWhichCategory(new Consultation_ROS(jsonArray.getJSONObject(x)));
                                                        }
                                                        setToEditText();
                                                    }
                                                }
                                                break;
                                            case "error":
                                                break;
                                        }
                                    } else if (jsonObject.has("exception")) {
                                        Log.d("exception", jsonObject.getString("exception"));
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
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getConsultation");
                    params.put("device", "mobile");
                    params.put("id", String.valueOf(consultation.id));

                    return params;
                }
            };

            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            progressBar.hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_with_tag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
