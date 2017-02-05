package com.example.kinit.e_medicalrecord.Medical_Prescription.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.General.Adapters.RecyclerView.RecyclerViewAdapter_Drug_List;
import com.example.kinit.e_medicalrecord.Medical_Prescription.Bus.Bus_Medical_Prescription_Click;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressBar;
import com.example.kinit.e_medicalrecord.Medical_Prescription.Class.Drug_List;
import com.example.kinit.e_medicalrecord.Medical_Prescription.Class.Medical_Prescription;
import com.example.kinit.e_medicalrecord.Profile.Class.Patient;
import com.example.kinit.e_medicalrecord.Profile.Class.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.General.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.kinit.e_medicalrecord.R.id.menu_tag;

public class Medical_Prescription_View extends AppCompatActivity {

    Intent intent;

    //Classes
    Patient patient;
    Viewer viewer;
    Bus_Medical_Prescription_Click busMedicalPrescriptionClick;
    Drug_List[] drugLists;
    Custom_AlertDialog alertDialog;
    Custom_ProgressBar progressBar;
    Medical_Prescription medicalPrescription;

    //Widgets
    //RecyclerView
    RecyclerView recyclerView_Content;
    RecyclerView.Adapter recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;
    TextView tv_physicianName, tv_clinicName, tv_date;

    /*============================
        END OF GLOBAL VARIABLES
     =============================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_medical_prescription_view);
        init();
    }

    void init() {
        alertDialog = new Custom_AlertDialog(this);
        alertDialog.builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = new Custom_ProgressBar(this);

        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");
        busMedicalPrescriptionClick = intent.getExtras().getParcelable("busMedicalPrescriptionClick");

        fetchData();

        tv_physicianName = (TextView) findViewById(R.id.tv_physicianName);
        tv_clinicName = (TextView) findViewById(R.id.tv_clinicName);
        tv_date = (TextView) findViewById(R.id.tv_date);

        recyclerView_Content = (RecyclerView) findViewById(R.id.recyclerView);
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
    }

    void fetchData() {
        try {
            progressBar.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL_MEDICAL_PRESCRIPTION,
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
                    params.put("action", "getDrugList");
                    params.put("device", "mobile");
                    params.put("medical_prescription_id", String.valueOf(busMedicalPrescriptionClick.medicalPrescription_id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            progressBar.hide();
        }
    }

    void alertDialog_showError(String code) {
        if (code.equals("medicalPrescriptionNotAvailable")) {
            alertDialog.show("Notice", "Medical Prescription is not available!");
        } else {
            alertDialog.show("Error", getString(R.string.error_occured));
        }
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
                Intent intent = new Intent(this, Tagged_Medical_Prescription.class);
                intent.putExtra("medical_prescription_id", medicalPrescription.medicalPrescription_id);
                intent.putExtra("patient", patient);
                startActivity(intent);
                break;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
