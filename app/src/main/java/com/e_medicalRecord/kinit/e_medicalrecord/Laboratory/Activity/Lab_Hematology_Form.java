package com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_AlertDialog;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressBar;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressDialog;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.DatePickerFragment;
import com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Class.Lab_Hematology;
import com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Class.Laboratory;
import com.e_medicalRecord.kinit.e_medicalrecord.Profile.Class.Patient;
import com.e_medicalRecord.kinit.e_medicalrecord.Profile.Class.Viewer;
import com.e_medicalRecord.kinit.e_medicalrecord.R;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Lab_Hematology_Form extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Intent intent;
    //Classes
    Viewer viewer;
    Patient patient;
    Laboratory laboratory;
    Lab_Hematology labHematology;
    Custom_ProgressDialog progressDialog;
    Custom_AlertDialog alertDialog;
    Custom_ProgressBar progressBar;
    DatePickerFragment datePickerFragment;

    //Widgets
    AppCompatSpinner spinner_lab_search, spinner_bloodType;
    ArrayAdapter arrayAdapter_search, arrayAdapter_bloodType;
    ArrayList<EditText> editTexts;
    Button btn_save;

    //Utils
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_hematology_form);
        init();
    }

    void init() {
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");
        if (intent.hasExtra("laboratory")) {
            laboratory = intent.getExtras().getParcelable("laboratory");
            getSupportActionBar().setTitle("Update Hematology Test");
        } else {
            getSupportActionBar().setTitle("Hematology Form");
        }
        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);
        progressBar = new Custom_ProgressBar(this);

        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner_lab_search = (AppCompatSpinner) findViewById(R.id.spinner_lab_search);
        arrayAdapter_search = ArrayAdapter.createFromResource(this, R.array.spinner_lab_hematology, R.layout.support_simple_spinner_dropdown_item);
        spinner_lab_search.setAdapter(arrayAdapter_search);
        spinner_lab_search.setOnItemSelectedListener(this);
        spinner_bloodType = (AppCompatSpinner) findViewById(R.id.spinner_bloodType);
        arrayAdapter_bloodType = ArrayAdapter.createFromResource(this, R.array.spinner_blood_type, R.layout.support_simple_spinner_dropdown_item);
        spinner_bloodType.setAdapter(arrayAdapter_bloodType);
        spinner_bloodType.setFocusable(true);
        spinner_bloodType.setFocusableInTouchMode(true);

        editTexts = new ArrayList<>();
        editTexts.add((EditText) findViewById(R.id.et_physicianName));
        editTexts.add((EditText) findViewById(R.id.et_laboratory));
        editTexts.add((EditText) findViewById(R.id.et_date));
        editTexts.get(2).setOnClickListener(this);
        editTexts.add((EditText) findViewById(R.id.et_hemoglobin));
        editTexts.add((EditText) findViewById(R.id.et_hematocrit));
        editTexts.add((EditText) findViewById(R.id.et_rbc));
        editTexts.add((EditText) findViewById(R.id.et_wbc));
        editTexts.add((EditText) findViewById(R.id.et_platelet));
        editTexts.add((EditText) findViewById(R.id.et_reticulocytes));
        editTexts.add((EditText) findViewById(R.id.et_mcv));
        editTexts.add((EditText) findViewById(R.id.et_mch));
        editTexts.add((EditText) findViewById(R.id.et_mchc));
        editTexts.add((EditText) findViewById(R.id.et_esr));
        editTexts.add((EditText) findViewById(R.id.et_segmenter));
        editTexts.add((EditText) findViewById(R.id.et_stab));
        editTexts.add((EditText) findViewById(R.id.et_lymphocyte));
        editTexts.add((EditText) findViewById(R.id.et_monocyte));
        editTexts.add((EditText) findViewById(R.id.et_eosinophils));
        editTexts.add((EditText) findViewById(R.id.et_basophils));
        editTexts.add((EditText) findViewById(R.id.et_malarialSmear));
        editTexts.add((EditText) findViewById(R.id.et_bleedingTime));
        editTexts.add((EditText) findViewById(R.id.et_clottingTime));
        editTexts.add((EditText) findViewById(R.id.et_rh));
        editTexts.add((EditText) findViewById(R.id.et_remarks));

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        calendar = Calendar.getInstance();
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (view.isShown()) {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    setCalendar(calendar);
                }
            }
        });
        setCalendar(calendar);
        if (viewer != null && laboratory == null) {
            editTexts.get(0).setText(viewer.name);
        }
        if (laboratory != null) {
            fetchData();
        }
    }

    void verifyData() {
        int isLabTestEmpty = 1, isLabNameEmpty = 1;
        ArrayList<String> inps = new ArrayList<>();

        for (int x = 0; x < editTexts.size(); x++) {
            inps.add(editTexts.get(x).getText().toString().trim());
            if (x == 0 || x == 1) {
                if (inps.get(x).isEmpty()) {
                    editTexts.get(x).requestFocus();
                    editTexts.get(x).setError("Required Field!");
                } else {
                    isLabNameEmpty = 0;
                }
            } else if (x > 2) {
                if (!inps.get(x).isEmpty()) {
                    isLabTestEmpty = 0;
                }
            }
        }

        inps.add(22, spinner_bloodType.getSelectedItem().toString());

        if (isLabNameEmpty == 0 && isLabTestEmpty == 0) {
            insertData(inps);
        } else if (isLabTestEmpty == 1) {
            alertDialog.show("Error", "At least 1 field of the lab tests must be filled up.");
        }
    }

    void insertData(final ArrayList<String> inps) {
        progressDialog.show("Saving...");
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL_LABORATORY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("error", response);
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    if (jsonObject.getString("code").equals("successful")) {
                                        if(laboratory != null) {
                                            Toast.makeText(getApplicationContext(), R.string.record_updated, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), R.string.record_added, Toast.LENGTH_SHORT).show();
                                        }
                                        intent = new Intent();
                                        intent.putExtra("result", true);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    } else if (jsonObject.getString("code").equals("unauthorized")) {
                                        progressDialog.dismiss();
                                        alertDialog_Close(getString(R.string.unauthorized_to_insert));
                                    } else if (jsonObject.getString("code").equals("empty")) {
                                        progressDialog.dismiss();
                                        if(laboratory != null) {
                                            alertDialog_Close("This result has been deleted.");
                                        } else {
                                            alertDialog.show("Error", "Something happened. Please try again.");
                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        alertDialog.show("Error", "Something happened. Please try again.");
                                    }
                                } else if (jsonObject.has("exception")) {
                                    alertDialog.show("Error", jsonObject.getString("exception"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    if (laboratory != null) {
                        params.put("action", "updateHema");
                        params.put("id", String.valueOf(laboratory.lab_id));
                    } else {
                        params.put("action", "insertHema");
                    }
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient.id));
                    if (viewer == null) {
                        params.put("user_data_id", String.valueOf(patient.user_data_id));
                        params.put("medical_staff_id", "0");
                    } else {
                        params.put("user_data_id", String.valueOf(viewer.user_id));
                        params.put("medical_staff_id", String.valueOf(viewer.medicalStaff_id));
                    }
                    for (int x = 0; x < inps.size(); x++) {
                        if (x == 2) {
                            params.put("args[" + (x) + "]", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                        } else {
                            params.put("args[" + (x) + "]", inps.get(x));
                        }
                    }
                    return params;
                }
            };

            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    void fetchData() {
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
                                        labHematology = new Lab_Hematology(jsonObject);
                                        setToEditText();
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
                    params.put("id", String.valueOf(laboratory.lab_id));
                    params.put("table_name", Lab_Hematology.TABLE_NAME);
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressBar.hide();
            e.printStackTrace();
        }
    }

    void alertDialog_Close(String message){
        alertDialog.builder.setNegativeButton(null, null);
        alertDialog.builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show("Error", message);
    }

    void setToEditText() {
        editTexts.get(0).setText(labHematology.physician_name);
        editTexts.get(1).setText(labHematology.lab_name);
        setCalendar(labHematology.datePerformed);
        editTexts.get(3).setText(labHematology.hemoglobin);
        editTexts.get(4).setText(labHematology.hematrocit);
        editTexts.get(5).setText(labHematology.rbc);
        editTexts.get(6).setText(labHematology.wbc);
        editTexts.get(7).setText(labHematology.platelets);
        editTexts.get(8).setText(labHematology.reticulocytes);
        editTexts.get(9).setText(labHematology.mcv);
        editTexts.get(10).setText(labHematology.mch);
        editTexts.get(11).setText(labHematology.mchc);
        editTexts.get(12).setText(labHematology.esr);
        editTexts.get(13).setText(labHematology.segmenters);
        editTexts.get(14).setText(labHematology.stab);
        editTexts.get(15).setText(labHematology.lymphocytes);
        editTexts.get(16).setText(labHematology.monocytes);
        editTexts.get(17).setText(labHematology.eosinophils);
        editTexts.get(18).setText(labHematology.basophils);
        editTexts.get(19).setText(labHematology.malarialSmear);
        editTexts.get(20).setText(labHematology.bleedingTime);
        editTexts.get(21).setText(labHematology.clottingTime);
        spinner_bloodType.setSelection(arrayAdapter_bloodType.getPosition(labHematology.bloodType));
        editTexts.get(22).setText(labHematology.rh);
        editTexts.get(23).setText(labHematology.remarks);
    }

    void setCalendar(Calendar calendar) {
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        editTexts.get(2).setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 20)
            spinner_bloodType.requestFocus();
        else if (position == 2)
            editTexts.get(position).performClick();
        else if (position > 20)
            editTexts.get(position - 1).requestFocus();
        else
            editTexts.get(position).requestFocus();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                verifyData();
                break;
            case R.id.et_date:
                datePickerFragment.show(getSupportFragmentManager(), "DatePicker");
                datePickerFragment.setCurrentDate(calendar);
                break;
        }
    }
}
