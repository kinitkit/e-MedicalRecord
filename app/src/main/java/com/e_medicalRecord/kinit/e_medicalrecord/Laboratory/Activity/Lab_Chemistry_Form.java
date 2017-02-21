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
import com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Class.Lab_Chemistry;
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

public class Lab_Chemistry_Form extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Intent intent;
    //Classes
    int dateIndex;

    Viewer viewer;
    Patient patient;
    Laboratory laboratory;
    Lab_Chemistry labChemistry;
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
        setContentView(R.layout.activity_lab_chemistry_form);
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
        arrayAdapter_search = ArrayAdapter.createFromResource(this, R.array.spinner_lab_chem, R.layout.support_simple_spinner_dropdown_item);
        spinner_lab_search.setAdapter(arrayAdapter_search);
        spinner_lab_search.setOnItemSelectedListener(this);

        editTexts = new ArrayList<>();
        editTexts.add((EditText) findViewById(R.id.et_physicianName));
        editTexts.add((EditText) findViewById(R.id.et_pathologist));
        editTexts.add((EditText) findViewById(R.id.et_medTech));
        editTexts.add((EditText) findViewById(R.id.et_laboratory));
        editTexts.add((EditText) findViewById(R.id.et_date));
        editTexts.get(dateIndex).setOnClickListener(this);
        editTexts.add((EditText) findViewById(R.id.et_fbs));
        editTexts.add((EditText) findViewById(R.id.et_creatinine));
        editTexts.add((EditText) findViewById(R.id.et_cholesterol));
        editTexts.add((EditText) findViewById(R.id.et_triglycerides));
        editTexts.add((EditText) findViewById(R.id.et_hdl));
        editTexts.add((EditText) findViewById(R.id.et_ldl));
        editTexts.add((EditText) findViewById(R.id.et_uricAcid));
        editTexts.add((EditText) findViewById(R.id.et_sgpt));
        editTexts.add((EditText) findViewById(R.id.et_sodium));
        editTexts.add((EditText) findViewById(R.id.et_potassium));
        editTexts.add((EditText) findViewById(R.id.et_calcium));
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
        int isLabTestEmpty = 1, isLabNameEmpty = 1, isPathoEmpty = 1, isMedTechEmpty = 1, isThereNoError = 1;
        String[] inps = new String[editTexts.size()];
        double valD;
        for (int x = 0; x < editTexts.size(); x++) {
            inps[x] = editTexts.get(x).getText().toString().trim();
            if (x == 1 || x == 2 || x == 3) {
                if (inps[x].isEmpty()) {
                    editTextError(editTexts.get(x), getString(R.string.required_field));
                } else {
                    switch (x) {
                        case 1:
                            isPathoEmpty = 0;
                            break;
                        case 2:
                            isMedTechEmpty = 0;
                            break;
                        case 3:
                            isLabNameEmpty = 0;
                            break;
                    }
                }
            } else if (x > dateIndex) {
                if (!inps[x].isEmpty()) {
                    switch (x) {
                        case 5:
                        case 7:
                        case 13:
                        case 14:
                            valD = Double.parseDouble(inps[x]);
                            if (valD < 0 || valD > 20) {
                                editTextError(editTexts.get(x), getString(R.string.value_is_out_of_range));
                                isThereNoError = 0;
                            }
                            break;
                        case 6:
                            valD = Double.parseDouble(inps[x]);
                            if (valD < 0 || valD > 130) {
                                editTextError(editTexts.get(x), getString(R.string.value_is_out_of_range));
                                isThereNoError = 0;
                            }
                            break;
                        case 8:
                        case 9:
                        case 10:
                            valD = Double.parseDouble(inps[x]);
                            if (valD < 0 || valD > 5) {
                                editTextError(editTexts.get(x), getString(R.string.value_is_out_of_range));
                                isThereNoError = 0;
                            }
                            break;
                        case 11:
                            valD = Double.parseDouble(inps[x]);
                            if (valD < 0 || valD > 90) {
                                editTextError(editTexts.get(x), getString(R.string.value_is_out_of_range));
                                isThereNoError = 0;
                            }
                            break;
                        case 12:
                            valD = Double.parseDouble(inps[x]);
                            if (valD < 0 || valD > 200) {
                                editTextError(editTexts.get(x), getString(R.string.value_is_out_of_range));
                                isThereNoError = 0;
                            }
                    }
                    isLabTestEmpty = 0;
                }
            }
        }

        if (isPathoEmpty == 0 && isMedTechEmpty == 0 && isLabNameEmpty == 0 && isLabTestEmpty == 0 && isThereNoError == 1) {
            insertData(inps);
        } else if (isLabTestEmpty == 1) {
            alertDialog.show("Error", "At least 1 field of the lab tests must be filled up.");
        }
    }

    void editTextError(EditText editText, String message) {
        editText.requestFocus();
        editText.setError(message);
    }

    void setCalendar(Calendar calendar) {
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        editTexts.get(dateIndex).setText(simpleDateFormat.format(calendar.getTime()));
    }

    void alertDialog_Close(String message) {
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
        editTexts.get(0).setText(labChemistry.physician_name);
        editTexts.get(1).setText(labChemistry.pathologist);
        editTexts.get(2).setText(labChemistry.medTech);
        editTexts.get(3).setText(labChemistry.lab_name);
        setCalendar(labChemistry.datePerformed);
        editTexts.get(5).setText(labChemistry.fbs);
        editTexts.get(6).setText(labChemistry.creatine);
        editTexts.get(7).setText(labChemistry.cholesterol);
        editTexts.get(8).setText(labChemistry.triglycerides);
        editTexts.get(9).setText(labChemistry.hdl);
        editTexts.get(10).setText(labChemistry.ldl);
        editTexts.get(11).setText(labChemistry.uricAcid);
        editTexts.get(12).setText(labChemistry.sgpt_alat);
        editTexts.get(13).setText(labChemistry.sodium);
        editTexts.get(14).setText(labChemistry.potassium);
        editTexts.get(15).setText(labChemistry.calcium);
        editTexts.get(16).setText(labChemistry.remarks);
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
                                        if (laboratory != null) {
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
                                        if (laboratory != null) {
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
                        params.put("action", "updateChem");
                        params.put("id", String.valueOf(laboratory.lab_id));
                    } else {
                        params.put("action", "insertChem");
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
                        if (x == dateIndex) {
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
                                        labChemistry = new Lab_Chemistry(jsonObject);
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
                    params.put("table_name", Lab_Chemistry.TABLE_NAME);
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressBar.hide();
            e.printStackTrace();
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
