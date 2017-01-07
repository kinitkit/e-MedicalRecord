package com.example.kinit.e_medicalrecord.Activities.Admission;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.DatePickerFragment;
import com.example.kinit.e_medicalrecord.Classes.User.Patient;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Admission_Form extends AppCompatActivity implements View.OnClickListener {

    //Primitive Data Types
    int selectedEditText;
    //Classes
    Patient patient;
    Viewer viewer;
    DatePickerFragment datePickerFragment;

    SimpleDateFormat simpleDateFormat;
    Custom_AlertDialog alertDialog;
    Custom_ProgressDialog progressDialog;
    //Widgets
    EditText et_physicianName, et_hospital, et_dateAdmitted, et_dateDischarged, et_admittingIpmression, et_procedures,
            et_futurePlan, et_finalDiagnosis;
    Button btn_save;
    Calendar calendarAdmitted, calendarDischarged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission);
        init();
    }

    void init() {
        patient = getIntent().getExtras().getParcelable("patient");
        viewer = getIntent().getExtras().getParcelable("viewer");

        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);

        et_physicianName = (EditText) findViewById(R.id.et_physicianName);
        et_hospital = (EditText) findViewById(R.id.et_hospital);
        et_dateAdmitted = (EditText) findViewById(R.id.et_dateAdmitted);
        et_dateDischarged = (EditText) findViewById(R.id.et_dateDischarged);
        et_admittingIpmression = (EditText) findViewById(R.id.et_admittingIpmression);
        et_procedures = (EditText) findViewById(R.id.et_procedures);
        et_futurePlan = (EditText) findViewById(R.id.et_futurePlan);
        et_finalDiagnosis = (EditText) findViewById(R.id.et_finalDiagnosis);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        et_dateAdmitted.setOnClickListener(this);
        et_dateDischarged.setOnClickListener(this);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admission Form");
        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendarAdmitted = Calendar.getInstance();
        calendarDischarged = Calendar.getInstance();
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                setCalendar(year, monthOfYear, dayOfMonth);
            }
        });
    }

    void onClick() {
        String strPhysicianName = et_physicianName.getText().toString().trim(), strHospital = et_hospital.getText().toString().trim(),
                strAdmittingIpmression = et_admittingIpmression.getText().toString().trim(), strProcedures = et_procedures.getText().toString().trim(),
                strFuturePlan = et_futurePlan.getText().toString().trim(), strFinalDiagnosis = et_finalDiagnosis.getText().toString().trim();

        if (validateInput(et_physicianName, strPhysicianName) && validateInput(et_hospital, strHospital) && validateInput(et_admittingIpmression, strAdmittingIpmression)
                && validateInput(et_procedures, strProcedures) && validateInput(et_futurePlan, strFuturePlan) && validateInput(et_finalDiagnosis, strFinalDiagnosis)) {
            sendData(strPhysicianName, strHospital, strAdmittingIpmression, strProcedures, strFuturePlan, strFinalDiagnosis);
        }
    }

    boolean validateInput(EditText editText, String txt) {
        if (txt.isEmpty()) {
            editText.setError("This field is required");
            return false;
        } else {
            return true;
        }
    }

    void sendData(final String strPhysicianName, final String strHospital, final String strAdmittingIpmression, final String strProcedures, final String strFuturePlan, final String strFinalDiagnosis) {
        try {
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("error", response);
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    if (jsonObject.getString("code").equals("success")) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), R.string.record_added, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.putExtra("result", true);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    } else if (jsonObject.getString("code").equals("unauthorized")) {
                                        progressDialog.dismiss();
                                        alertDialog.show("Error", getString(R.string.unauthorized_to_insert));
                                    } else if (jsonObject.getString("code").equals("empty")) {
                                        //if(laboratory != null) {
                                        //    alertDialog_Close("This result has been deleted.");
                                        // else {
                                            alertDialog.show("Error", getString(R.string.error_occured));
                                        //}
                                    } else {
                                        progressDialog.dismiss();
                                        alertDialog.show("Error", getString(R.string.error_occured));
                                    }
                                } else if (jsonObject.has("exception")) {
                                    progressDialog.dismiss();
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
                    params.put("action", "insertAdmission");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient.id));
                    if (viewer != null) {
                        params.put("user_data_id", String.valueOf(viewer.user_id));
                        params.put("medical_staff_id", String.valueOf(viewer.medicalStaff_id));
                    } else {
                        params.put("user_data_id", String.valueOf(patient.user_data_id));
                        params.put("medical_staff_id", "0");
                    }
                    params.put("physician_name", strPhysicianName);
                    params.put("hospital", strHospital);
                    params.put("date_admitted", new SimpleDateFormat("yyyy-MM-dd").format(calendarAdmitted.getTime()));
                    params.put("date_discharged", new SimpleDateFormat("yyyy-MM-dd").format(calendarDischarged.getTime()));
                    params.put("admitting_impression", strAdmittingIpmression);
                    params.put("procedures", strProcedures);
                    params.put("future_plan", strFuturePlan);
                    params.put("final_diagnosis", strFinalDiagnosis);
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    void setCalendar(int year, int monthOfYear, int dayOfMonth) {
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        if (selectedEditText == 0) {
            calendarAdmitted.set(year, monthOfYear, dayOfMonth);
            et_dateAdmitted.setText(simpleDateFormat.format(calendarAdmitted.getTime()));
        } else if (selectedEditText == 1) {
            calendarDischarged.set(year, monthOfYear, dayOfMonth);
            et_dateDischarged.setText(simpleDateFormat.format(calendarDischarged.getTime()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                onClick();
                break;
            case R.id.et_dateAdmitted:
                selectedEditText = 0;
                datePickerFragment.show(getSupportFragmentManager(), "DatePicker");
                datePickerFragment.setCurrentDate(calendarAdmitted);
                break;
            case R.id.et_dateDischarged:
                selectedEditText = 1;
                datePickerFragment.show(getSupportFragmentManager(), "DatePicker");
                datePickerFragment.setCurrentDate(calendarDischarged);
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
