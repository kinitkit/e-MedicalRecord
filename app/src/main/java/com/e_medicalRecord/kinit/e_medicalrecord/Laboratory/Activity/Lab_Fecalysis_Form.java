package com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Class.Lab_Fecalysis;
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

public class Lab_Fecalysis_Form extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Intent intent;
    //Classes
    int dateIndex;

    Viewer viewer;
    Patient patient;
    Laboratory laboratory;
    Lab_Fecalysis labFecalysis;
    Custom_ProgressDialog progressDialog;
    Custom_AlertDialog alertDialog;
    Custom_ProgressBar progressBar;
    DatePickerFragment datePickerFragment;

    //Widgets
    AppCompatSpinner spinner_lab_search;
    ArrayAdapter arrayAdapter_search;
    ArrayList<EditText> editTexts;
    Button btn_save;

    //Utils
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_lab_fecalysis_form);
        init();
    }

    void init() {
        dateIndex = 4;
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");
        if (intent.hasExtra("laboratory")) {
            laboratory = intent.getExtras().getParcelable("laboratory");
            getSupportActionBar().setTitle("Update Fecalysis Test");
        } else {
            getSupportActionBar().setTitle("Fecalysis Form");
        }
        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);
        progressBar = new Custom_ProgressBar(this);

        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner_lab_search = (AppCompatSpinner) findViewById(R.id.spinner_lab_search);
        arrayAdapter_search = ArrayAdapter.createFromResource(this, R.array.spinner_lab_fecalysis, R.layout.support_simple_spinner_dropdown_item);
        spinner_lab_search.setAdapter(arrayAdapter_search);
        spinner_lab_search.setOnItemSelectedListener(this);

        editTexts = new ArrayList<>();
        editTexts.add((EditText) findViewById(R.id.et_physicianName));
        editTexts.add((EditText) findViewById(R.id.et_pathologist));
        editTexts.add((EditText) findViewById(R.id.et_medTech));
        editTexts.add((EditText) findViewById(R.id.et_laboratory));
        editTexts.add((EditText) findViewById(R.id.et_date));
        editTexts.get(dateIndex).setOnClickListener(this);
        editTexts.add((EditText) findViewById(R.id.et_color));
        editTexts.add((EditText) findViewById(R.id.et_consistency));
        editTexts.add((EditText) findViewById(R.id.et_ascaris));
        editTexts.add((EditText) findViewById(R.id.et_trichuris));
        editTexts.add((EditText) findViewById(R.id.et_enterobius));
        editTexts.add((EditText) findViewById(R.id.et_hookwormOva));
        editTexts.add((EditText) findViewById(R.id.et_giardiaLambia));
        editTexts.add((EditText) findViewById(R.id.et_blastocystis));
        editTexts.add((EditText) findViewById(R.id.et_eHistolyticaCyst));
        editTexts.add((EditText) findViewById(R.id.et_eHistolyticaTroph));
        editTexts.add((EditText) findViewById(R.id.et_occultBlood));
        editTexts.add((EditText) findViewById(R.id.et_pusCells));
        editTexts.add((EditText) findViewById(R.id.et_redBloodCells));
        editTexts.add((EditText) findViewById(R.id.et_fatGlobules));
        editTexts.add((EditText) findViewById(R.id.et_yeastCells));
        editTexts.add((EditText) findViewById(R.id.et_undigestedFood));
        editTexts.add((EditText) findViewById(R.id.et_starchGranules));
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == dateIndex) {
            editTexts.get(position).performClick();
        } else {
            editTexts.get(position).requestFocus();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void verifyData() {
        int isLabTestEmpty = 1, isLabNameEmpty = 1;
        String[] inps = new String[editTexts.size()];

        for (int x = 0; x < editTexts.size(); x++) {
            inps[x] = editTexts.get(x).getText().toString().trim();
            if (x == 1 || x == 2 || x == 3) {
                if (inps[x].isEmpty()) {
                    editTexts.get(x).requestFocus();
                    editTexts.get(x).setError("Required Field!");
                } else {
                    isLabNameEmpty = 0;
                }
            } else if (x > 4) {
                if (!inps[x].isEmpty()) {
                    isLabTestEmpty = 0;
                }
            }
        }

        if (isLabNameEmpty == 0 && isLabTestEmpty == 0) {
            insertData(inps);
        } else if (isLabTestEmpty == 1) {
            alertDialog.show("Error", "At least 1 field of the lab tests must be filled up.");
        }
    }

    void insertData(final String[] inps) {
        try {
            progressDialog.show("Saving...");
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
                        params.put("action", "updateFecalysis");
                        params.put("id", String.valueOf(laboratory.lab_id));
                    } else {
                        params.put("action", "insertFecal");
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
                    for (int x = 0; x < inps.length; x++) {
                        if(x == 2){
                            params.put("args[" + (x) + "]", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                        } else {
                            params.put("args[" + (x) + "]", inps[x]);
                        }
                    }
                    return params;
                }
            };

            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
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
                                        labFecalysis = new Lab_Fecalysis(jsonObject);
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
                    params.put("table_name", Lab_Fecalysis.TABLE_NAME);
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
        editTexts.get(0).setText(labFecalysis.physician_name);
        editTexts.get(1).setText(labFecalysis.pathologist);
        editTexts.get(2).setText(labFecalysis.medTech);
        editTexts.get(3).setText(labFecalysis.lab_name);
        setCalendar(labFecalysis.datePerformed);
        editTexts.get(5).setText(labFecalysis.color);
        editTexts.get(6).setText(labFecalysis.consistency);
        editTexts.get(7).setText(labFecalysis.ascarisLumbricoides);
        editTexts.get(8).setText(labFecalysis.trichurisTrichiura);
        editTexts.get(9).setText(labFecalysis.enterobiusVermicularis);
        editTexts.get(10).setText(labFecalysis.hookwormOva);
        editTexts.get(11).setText(labFecalysis.giardiaLambia);
        editTexts.get(12).setText(labFecalysis.blastocystisHominis);
        editTexts.get(13).setText(labFecalysis.cyst);
        editTexts.get(14).setText(labFecalysis.trophozoite);
        editTexts.get(15).setText(labFecalysis.occultBlood);
        editTexts.get(16).setText(labFecalysis.pusCells);
        editTexts.get(17).setText(labFecalysis.rbc);
        editTexts.get(18).setText(labFecalysis.fatGlobules);
        editTexts.get(19).setText(labFecalysis.yeastCells);
        editTexts.get(20).setText(labFecalysis.undigestedFood);
        editTexts.get(21).setText(labFecalysis.starchGranules);
        editTexts.get(22).setText(labFecalysis.remarks);
    }

    void setCalendar(Calendar calendar) {
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        editTexts.get(dateIndex).setText(simpleDateFormat.format(calendar.getTime()));
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
