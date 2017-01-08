package com.example.kinit.e_medicalrecord.Activities.Admission;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.kinit.e_medicalrecord.Classes.Admission.Admission;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.User.Patient;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Admission_View extends AppCompatActivity implements View.OnClickListener {

    //Classes
    Intent intent;
    Patient patient;
    Viewer viewer;
    Admission admission;
    Custom_ProgressDialog progressDialog;
    Custom_AlertDialog alertDialog;

    TextView tv_physicianName, tv_hospital, tv_dateAdmission, tv_dateDischarged, tv_admittingImpression,
            tv_procedures, tv_futurePlan, tv_finalDiagnosis;
    FloatingActionButton btn_tagged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission_view);
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

        progressDialog = new Custom_ProgressDialog(this);
        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");
        admission = intent.getExtras().getParcelable("admission");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admission");
        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_physicianName = (TextView) findViewById(R.id.tv_physicianName);
        tv_hospital = (TextView) findViewById(R.id.tv_hospital);
        tv_dateAdmission = (TextView) findViewById(R.id.tv_dateAdmission);
        tv_dateDischarged = (TextView) findViewById(R.id.tv_dateDischarged);
        tv_admittingImpression = (TextView) findViewById(R.id.tv_admittingImpression);
        tv_procedures = (TextView) findViewById(R.id.tv_procedures);
        tv_futurePlan = (TextView) findViewById(R.id.tv_futurePlan);
        tv_finalDiagnosis = (TextView) findViewById(R.id.tv_finalDiagnosis);
        btn_tagged = (FloatingActionButton) findViewById(R.id.btn_tagged);
        btn_tagged.setOnClickListener(this);

        if (viewer != null) {
            btn_tagged.setVisibility(View.GONE);
        }
        fetchData();
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
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    String code = jsonObject.getString("code");
                                    if (code.equals("success")) {
                                        jsonArray = rootJsonArray.getJSONArray(1);
                                        admission = new Admission(jsonArray.getJSONObject(0));
                                        setText();
                                    } else {
                                        alertDialog.show("Error", getString(R.string.error_occured));
                                    }
                                } else if (jsonObject.has("exception")) {
                                    alertDialog.show("Error", jsonObject.getString("exception"));
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
                    params.put("action", "getAdmission");
                    params.put("device", "mobile");
                    params.put("id", String.valueOf(admission.id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setText() {
        tv_physicianName.setText(admission.physicianName);
        tv_hospital.setText(admission.hospital);
        tv_dateAdmission.setText(admission.strDateAdmitted);
        tv_dateDischarged.setText(admission.strDateDischarged);
        tv_admittingImpression.setText(admission.admittingImpression);
        tv_procedures.setText(admission.procedures);
        tv_futurePlan.setText(admission.futurePlan);
        tv_finalDiagnosis.setText(admission.finalDiagnosis);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tagged:
                intent = new Intent(this, Tagged_Admission.class);
                intent.putExtra("admission_id", admission.id);
                intent.putExtra("patient", patient);
                startActivity(intent);
                break;
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
