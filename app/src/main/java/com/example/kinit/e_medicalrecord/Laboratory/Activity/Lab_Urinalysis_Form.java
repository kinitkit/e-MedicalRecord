package com.example.kinit.e_medicalrecord.Laboratory.Activity;

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
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressBar;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.DatePickerFragment;
import com.example.kinit.e_medicalrecord.Laboratory.Class.Lab_Urinalysis;
import com.example.kinit.e_medicalrecord.Laboratory.Class.Laboratory;
import com.example.kinit.e_medicalrecord.Profile.Class.Patient;
import com.example.kinit.e_medicalrecord.Profile.Class.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.General.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Lab_Urinalysis_Form extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Intent intent;
    //Classes
    Viewer viewer;
    Patient patient;
    Laboratory laboratory;
    Lab_Urinalysis labUrinalysis;
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
        setContentView(R.layout.activity_lab_urinalysis_form);
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
            getSupportActionBar().setTitle("Update Blood Chemistry Test");
        } else {
            getSupportActionBar().setTitle("Blood Chemistry Form");
        }
        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);
        progressBar = new Custom_ProgressBar(this);

        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner_lab_search = (AppCompatSpinner) findViewById(R.id.spinner_lab_search);
        arrayAdapter_search = ArrayAdapter.createFromResource(this, R.array.spinner_lab_urinalysis, R.layout.support_simple_spinner_dropdown_item);
        spinner_lab_search.setAdapter(arrayAdapter_search);
        spinner_lab_search.setOnItemSelectedListener(this);

        editTexts = new ArrayList<>();
        editTexts.add((EditText) findViewById(R.id.et_physicianName));
        editTexts.add((EditText) findViewById(R.id.et_laboratory));
        editTexts.add((EditText) findViewById(R.id.et_date));
        editTexts.get(2).setOnClickListener(this);
        editTexts.add((EditText) findViewById(R.id.et_color));
        editTexts.add((EditText) findViewById(R.id.et_transparency));
        editTexts.add((EditText) findViewById(R.id.et_reaction));
        editTexts.add((EditText) findViewById(R.id.et_gravity));
        editTexts.add((EditText) findViewById(R.id.et_pusCells));
        editTexts.add((EditText) findViewById(R.id.et_rbc));
        editTexts.add((EditText) findViewById(R.id.et_epithCell));
        editTexts.add((EditText) findViewById(R.id.et_renalCell));
        editTexts.add((EditText) findViewById(R.id.et_mucusThreads));
        editTexts.add((EditText) findViewById(R.id.et_bacteria));
        editTexts.add((EditText) findViewById(R.id.et_yeastCell));
        editTexts.add((EditText) findViewById(R.id.et_sugar));
        editTexts.add((EditText) findViewById(R.id.et_albumin));
        editTexts.add((EditText) findViewById(R.id.et_ketone));
        editTexts.add((EditText) findViewById(R.id.et_bilirubin));
        editTexts.add((EditText) findViewById(R.id.et_blood));
        editTexts.add((EditText) findViewById(R.id.et_urobilinogen));
        editTexts.add((EditText) findViewById(R.id.et_bacteriaNit));
        editTexts.add((EditText) findViewById(R.id.et_leukocyte));
        editTexts.add((EditText) findViewById(R.id.et_amorphous));
        editTexts.add((EditText) findViewById(R.id.et_uricAcid));
        editTexts.add((EditText) findViewById(R.id.et_calciumOxalate));
        editTexts.add((EditText) findViewById(R.id.et_triplePhosphate));
        editTexts.add((EditText) findViewById(R.id.et_pusCast));
        editTexts.add((EditText) findViewById(R.id.et_hyaline));
        editTexts.add((EditText) findViewById(R.id.et_fineGranular));
        editTexts.add((EditText) findViewById(R.id.et_coarseGranular));
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
        if (viewer != null) {
            editTexts.get(0).setFocusableInTouchMode(false);
            editTexts.get(0).setKeyListener(null);
            editTexts.get(0).setText(viewer.name);
        }

        if (laboratory != null) {
            fetchData();
        }
    }

    void verifyData() {
        int isLabTestEmpty = 1, isLabNameEmpty = 1;
        String[] inps = new String[editTexts.size()];

        for (int x = 0; x < editTexts.size(); x++) {
            inps[x] = editTexts.get(x).getText().toString().trim();
            if (x == 0 || x == 1) {
                if (inps[x].isEmpty()) {
                    editTexts.get(x).requestFocus();
                    editTexts.get(x).setError("Required Field!");
                } else {
                    isLabNameEmpty = 0;
                }
            } else if (x > 2) {
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

    void setCalendar(Calendar calendar) {
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        editTexts.get(2).setText(simpleDateFormat.format(calendar.getTime()));
    }

    void insertData(final String[] inps) {
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
                    progressDialog.dismiss();
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    if (laboratory != null) {
                        params.put("action", "updateUri");
                        params.put("id", String.valueOf(laboratory.lab_id));
                    } else {
                        params.put("action", "insertUri");
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
                        if (x == 2) {
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
                                        labUrinalysis = new Lab_Urinalysis(jsonObject);
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
                    params.put("table_name", Lab_Urinalysis.TABLE_NAME);
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
        editTexts.get(0).setText(labUrinalysis.physician_name);
        editTexts.get(1).setText(labUrinalysis.lab_name);
        setCalendar(labUrinalysis.datePerformed);
        editTexts.get(3).setText(labUrinalysis.color);
        editTexts.get(4).setText(labUrinalysis.transparency);
        editTexts.get(5).setText(labUrinalysis.reaction);
        editTexts.get(6).setText(labUrinalysis.specificGravity);
        editTexts.get(7).setText(labUrinalysis.pusCells);
        editTexts.get(8).setText(labUrinalysis.rbc);
        editTexts.get(9).setText(labUrinalysis.epithCells);
        editTexts.get(10).setText(labUrinalysis.renalCells);
        editTexts.get(11).setText(labUrinalysis.mucusThreads);
        editTexts.get(12).setText(labUrinalysis.bacteria);
        editTexts.get(13).setText(labUrinalysis.yeastCells);
        editTexts.get(14).setText(labUrinalysis.sugar);
        editTexts.get(15).setText(labUrinalysis.albumin);
        editTexts.get(16).setText(labUrinalysis.ketone);
        editTexts.get(17).setText(labUrinalysis.bilirubin);
        editTexts.get(18).setText(labUrinalysis.blood);
        editTexts.get(19).setText(labUrinalysis.urobilinogen);
        editTexts.get(20).setText(labUrinalysis.bacteriaNit);
        editTexts.get(21).setText(labUrinalysis.leukocyte);
        editTexts.get(22).setText(labUrinalysis.amorphousSubs);
        editTexts.get(23).setText(labUrinalysis.uricAcid);
        editTexts.get(24).setText(labUrinalysis.calciumOxalate);
        editTexts.get(25).setText(labUrinalysis.triplePhosphate);
        editTexts.get(26).setText(labUrinalysis.pusCast);
        editTexts.get(27).setText(labUrinalysis.hyaline);
        editTexts.get(28).setText(labUrinalysis.fineGranular);
        editTexts.get(29).setText(labUrinalysis.coarseGranular);
        editTexts.get(30).setText(labUrinalysis.remarks);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 2) {
            editTexts.get(position).performClick();
        } else {
            editTexts.get(position).requestFocus();
        }

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
