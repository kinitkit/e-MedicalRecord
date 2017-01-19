package com.example.kinit.e_medicalrecord.Activities.Admission;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Classes.Admission.Admission;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressBar;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Admission_Form extends AppCompatActivity implements View.OnClickListener {

    static DatePickerFragment datePickerFragmentAdmitted, datePickerFragmentDischarged;
    //Classes
    Intent intent;
    Patient patient;
    Viewer viewer;
    Admission admission;
    SimpleDateFormat simpleDateFormat;
    Custom_AlertDialog alertDialog;
    Custom_ProgressDialog progressDialog;
    Custom_ProgressBar progressBar;
    //Widgets
    EditText et_physicianName, et_hospital, et_dateAdmitted, et_dateDischarged, et_admittingIpmression, et_procedures,
            et_futurePlan, et_finalDiagnosis;
    Button btn_save;
    Calendar calendarAdmitted, calendarDischarged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_admission);
        init();
    }

    void init() {
        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");
        if (intent.hasExtra("admission")) {
            admission = intent.getExtras().getParcelable("admission");
        }

        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);
        progressBar = new Custom_ProgressBar(this);

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
        datePickerFragmentAdmitted = new DatePickerFragment();
        datePickerFragmentDischarged = new DatePickerFragment();
        datePickerFragmentAdmitted.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (view.isShown()) {
                    calendarAdmitted.set(year, monthOfYear, dayOfMonth);
                    setCalendar(calendarAdmitted, 0);
                }
            }
        });
        datePickerFragmentDischarged.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (view.isShown()) {
                    calendarDischarged.set(year, monthOfYear, dayOfMonth);
                    setCalendar(calendarDischarged, 1);
                }
            }
        });

        if (admission != null) {
            fetchData();
        } else {
            setCalendar(calendarAdmitted, 0);
            setCalendar(calendarDischarged, 1);
        }
    }

    void onClick() {
        String strPhysicianName = et_physicianName.getText().toString().trim(), strHospital = et_hospital.getText().toString().trim(),
                strAdmittingImpression = et_admittingIpmression.getText().toString().trim(), strProcedures = et_procedures.getText().toString().trim(),
                strFuturePlan = et_futurePlan.getText().toString().trim(), strFinalDiagnosis = et_finalDiagnosis.getText().toString().trim();

        if (validateInput(et_physicianName, strPhysicianName) && validateInput(et_hospital, strHospital) && validateInput(et_admittingIpmression, strAdmittingImpression)
                && validateInput(et_procedures, strProcedures) && validateInput(et_futurePlan, strFuturePlan) && validateInput(et_finalDiagnosis, strFinalDiagnosis)) {
            sendData(strPhysicianName, strHospital, strAdmittingImpression, strProcedures, strFuturePlan, strFinalDiagnosis);
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
            progressDialog.show("Saving...");
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_ADMISSION,
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
                                        if (admission != null) {
                                            Toast.makeText(getApplicationContext(), getString(R.string.record_updated), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), R.string.record_added, Toast.LENGTH_SHORT).show();
                                        }
                                        Intent intent = new Intent();
                                        intent.putExtra("result", true);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    } else if (jsonObject.getString("code").equals("unauthorized")) {
                                        progressDialog.dismiss();
                                        alertDialog.show("Error", getString(R.string.unauthorized_to_insert));
                                    } else if (jsonObject.getString("code").equals("empty")) {
                                        if (admission != null) {
                                            alertDialog.show("Error", getString(R.string.not_available));
                                        } else {
                                            alertDialog.show("Error", getString(R.string.error_occured));
                                        }
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
                            } finally {
                                progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    if (admission != null) {
                        params.put("action", "updateAdmission");
                        params.put("id", String.valueOf(admission.id));
                    } else {
                        params.put("action", "insertAdmission");
                        if (viewer != null) {
                            params.put("user_data_id", String.valueOf(viewer.user_id));
                            params.put("medical_staff_id", String.valueOf(viewer.medicalStaff_id));
                        } else {
                            params.put("user_data_id", String.valueOf(patient.user_data_id));
                            params.put("medical_staff_id", "0");
                        }
                    }
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient.id));
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
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    void setCalendar(Calendar calendar, int selectedEditText) {
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        if (selectedEditText == 0) {
            et_dateAdmitted.setText(simpleDateFormat.format(calendar.getTime()));
        } else if (selectedEditText == 1) {
            et_dateDischarged.setText(simpleDateFormat.format(calendar.getTime()));
        }
    }

    void fetchData() {
        try {
            progressBar.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL_ADMISSION,
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
                                        setFields();
                                    } else {
                                        alertDialog.show("Error", getString(R.string.error_occured));
                                    }
                                } else if (jsonObject.has("exception")) {
                                    alertDialog.show("Error", jsonObject.getString("exception"));
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
                    params.put("action", "getAdmission");
                    params.put("device", "mobile");
                    params.put("id", String.valueOf(admission.id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressBar.hide();
            e.printStackTrace();
        }
    }

    void setFields() {
        et_physicianName.setText(admission.physicianName);
        et_hospital.setText(admission.hospital);
        this.calendarAdmitted.setTime(new Date(admission.calendarDateAdmitted.getTimeInMillis()));
        et_dateAdmitted.setText(admission.strDateAdmitted);
        this.calendarDischarged.setTime(new Date(admission.calendarDateDischarged.getTimeInMillis()));
        et_dateDischarged.setText(admission.strDateDischarged);
        et_admittingIpmression.setText(admission.admittingImpression);
        et_procedures.setText(admission.procedures);
        et_futurePlan.setText(admission.futurePlan);
        et_finalDiagnosis.setText(admission.finalDiagnosis);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                onClick();
                break;
            case R.id.et_dateAdmitted:
                datePickerFragmentAdmitted.show(getSupportFragmentManager(), "DatePicker");
                datePickerFragmentAdmitted.setCurrentDate(calendarAdmitted);
                datePickerFragmentAdmitted.setMaxCalendar(calendarDischarged);
                break;
            case R.id.et_dateDischarged:
                datePickerFragmentDischarged.show(getSupportFragmentManager(), "DatePicker1");
                datePickerFragmentDischarged.setCurrentDate(calendarDischarged);
                datePickerFragmentDischarged.setMinCalendar(calendarAdmitted);
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
