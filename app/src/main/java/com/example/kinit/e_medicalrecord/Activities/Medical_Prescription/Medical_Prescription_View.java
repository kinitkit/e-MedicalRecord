package com.example.kinit.e_medicalrecord.Activities.Medical_Prescription;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.RecyclerViewAdapter_Drug_List;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Medical_Prescription_Click;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.Medical_Prescription.*;
import com.example.kinit.e_medicalrecord.Classes.Medical_Prescription.Medical_Prescription;
import com.example.kinit.e_medicalrecord.Classes.User.Patient;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Medical_Prescription_View extends AppCompatActivity implements View.OnClickListener {

    Intent intent;

    //Classes
    Patient patient;
    Viewer viewer;
    Bus_Medical_Prescription_Click busMedicalPrescriptionClick;
    Drug_List[] drugLists;
    Custom_ProgressDialog progressDialog;
    Custom_AlertDialog alertDialog;
    com.example.kinit.e_medicalrecord.Classes.Medical_Prescription.Medical_Prescription medicalPrescription;

    //Widgets
    //RecyclerView
    RecyclerView recyclerView_Content;
    RecyclerView.Adapter recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;
    TextView tv_physicianName, tv_clinicName, tv_date;
    FloatingActionButton btn_tagged;

    /*============================
        END OF GLOBAL VARIABLES
     =============================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_prescription_view);
        init();
    }

    void init() {
        alertDialog = new Custom_AlertDialog(this);
        alertDialog.builder.setNegativeButton(null, null);
        alertDialog.builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new Custom_ProgressDialog(this);
        progressDialog.show("Loading...");
        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");
        busMedicalPrescriptionClick = intent.getExtras().getParcelable("busMedicalPrescriptionClick");

        fetchData();

        tv_physicianName = (TextView) findViewById(R.id.tv_physicianName);
        tv_clinicName = (TextView) findViewById(R.id.tv_clinicName);
        tv_date = (TextView) findViewById(R.id.tv_date);


        btn_tagged = (FloatingActionButton) findViewById(R.id.btn_tagged);
        btn_tagged.setOnClickListener(this);

        if (viewer != null) {
            btn_tagged.setVisibility(View.GONE);
        }

        recyclerView_Content = (RecyclerView) findViewById(R.id.recyclerView);
    }

    void fetchData() {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response), jsonArray;
                                JSONObject jsonObject;
                                if (rootJsonArray.get(0) instanceof JSONObject) {
                                    jsonObject = rootJsonArray.getJSONObject(0);
                                    if (jsonObject.has("code")) {
                                        alertDialog_showError(jsonObject.getString("code"));
                                    } else if (jsonObject.has("exception")) {
                                        alertDialog_showError(jsonObject.getString("exception"));
                                    }
                                } else {
                                    jsonArray = rootJsonArray.getJSONArray(0);
                                    medicalPrescription = new Medical_Prescription(jsonArray.getJSONObject(0));
                                    jsonObject = rootJsonArray.getJSONObject(1);
                                    if (jsonObject.has("code")) {
                                        if (jsonObject.getString("code").equals("successful")) {
                                            jsonArray = rootJsonArray.getJSONArray(2);
                                            int jsonArrayLength = jsonArray.length();
                                            drugLists = new Drug_List[jsonArrayLength];
                                            for (int x = 0; x < jsonArrayLength; x++) {
                                                jsonObject = jsonArray.getJSONObject(x);
                                                drugLists[x] = new Drug_List(jsonObject);
                                            }
                                            loadToRecyclerView();
                                        } else {
                                            alertDialog_showError(jsonObject.getString("code"));
                                        }
                                    } else {
                                        alertDialog_showError(jsonObject.getString("exception"));
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

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getDrugList");
                    params.put("device", "mobile");
                    params.put("medical_prescription_id", String.valueOf(busMedicalPrescriptionClick.medicalPrescription_id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_Drug_List(drugLists);
        recyclerViewLayoutM_Content = new LinearLayoutManager(this);
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
        setTextViewTexts();
    }

    void setTextViewTexts() {
        tv_physicianName.setText(medicalPrescription.physicianName);
        tv_clinicName.setText(medicalPrescription.clinic_name);
        tv_date.setText(medicalPrescription.date);
        getSupportActionBar().setTitle("Medical Prescription");
        getSupportActionBar().setSubtitle(patient.name);
        progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tagged:
                intent = new Intent(this, Tagged_Medical_Prescription.class);
                intent.putExtra("medical_prescription_id", medicalPrescription.medicalPrescription_id);
                startActivity(intent);
                break;
        }
    }

    void alertDialog_showError(String code) {
        if (code.equals("medicalPrescriptionNotAvailable")) {
            alertDialog.show("Notice", "Medical Prescription is not available!");
        } else {
            alertDialog.show("Error", "An error occured while we were trying to get the data. Please try again");
        }
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
