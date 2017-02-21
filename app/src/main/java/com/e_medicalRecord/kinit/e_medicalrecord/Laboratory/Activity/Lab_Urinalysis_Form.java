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
import android.widget.CheckBox;
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
import com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Class.Lab_Urinalysis;
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

public class Lab_Urinalysis_Form extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Intent intent;
    //Classes
    int dateIndex;

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
    ArrayList<CheckBox> checkBoxes;
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
        arrayAdapter_search = ArrayAdapter.createFromResource(this, R.array.spinner_lab_urinalysis, R.layout.support_simple_spinner_dropdown_item);
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
        editTexts.add((EditText) findViewById(R.id.et_transparency));
        editTexts.add((EditText) findViewById(R.id.et_reaction));
        editTexts.add((EditText) findViewById(R.id.et_gravity));
        editTexts.add((EditText) findViewById(R.id.et_urobilinogen));
        editTexts.add((EditText) findViewById(R.id.et_pusCells));
        editTexts.add((EditText) findViewById(R.id.et_rbc));
        editTexts.add((EditText) findViewById(R.id.et_epithCell));
        editTexts.add((EditText) findViewById(R.id.et_renalCell));
        editTexts.add((EditText) findViewById(R.id.et_mucusThreads));
        editTexts.add((EditText) findViewById(R.id.et_bacteria));
        editTexts.add((EditText) findViewById(R.id.et_yeastCell));
        editTexts.add((EditText) findViewById(R.id.et_amorphous));
        editTexts.add((EditText) findViewById(R.id.et_uricAcid));
        editTexts.add((EditText) findViewById(R.id.et_calciumOxalate));
        editTexts.add((EditText) findViewById(R.id.et_triplePhosphate));
        editTexts.add((EditText) findViewById(R.id.et_pusCast));
        editTexts.add((EditText) findViewById(R.id.et_hyaline));
        editTexts.add((EditText) findViewById(R.id.et_fineGranular));
        editTexts.add((EditText) findViewById(R.id.et_coarseGranular));
        editTexts.add((EditText) findViewById(R.id.et_remarks));

        checkBoxes = new ArrayList<>();
        checkBoxes.add((CheckBox) findViewById(R.id.cb_sugar));
        checkBoxes.add((CheckBox) findViewById(R.id.cb_albumin));
        checkBoxes.add((CheckBox) findViewById(R.id.cb_ketone));
        checkBoxes.add((CheckBox) findViewById(R.id.cb_bilirubin));
        checkBoxes.add((CheckBox) findViewById(R.id.cb_blood));
        checkBoxes.add((CheckBox) findViewById(R.id.cb_bacterialNit));
        checkBoxes.add((CheckBox) findViewById(R.id.cb_leukocyte));

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
        ArrayList<String> inps = new ArrayList<>();
        double valD;
        int valI;
        for (int x = 0; x < editTexts.size(); x++) {
            inps.add(editTexts.get(x).getText().toString().trim());
            if (x == 1 || x == 2 || x == 3) {
                if (inps.get(x).isEmpty()) {
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
                if (!inps.get(x).isEmpty()) {
                    switch (x) {
                        case 7:
                        case 10:
                        case 11:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                            valI = Integer.parseInt(inps.get(x));
                            if (valI < 0 || valI > 20) {
                                editTextError(editTexts.get(x), getString(R.string.value_is_out_of_range));
                                isThereNoError = 0;
                            }
                            break;
                        case 8:
                        case 9:
                            valD = Double.parseDouble(inps.get(x));
                            if (valD < 0 || valD > 10) {
                                editTextError(editTexts.get(x), getString(R.string.value_is_out_of_range));
                                isThereNoError = 0;
                            }
                            break;
                    }
                    isLabTestEmpty = 0;
                }
            }
        }

        if (isPathoEmpty == 0 && isMedTechEmpty == 0 && isLabNameEmpty == 0 && isLabTestEmpty == 0 && isThereNoError == 1) {
            int x = 9;
            for (CheckBox checkBox : checkBoxes) {
                inps.set(x, (checkBox.isChecked() ? "1" : "0"));
                ++x;
            }
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
                    for (int x = 0; x < inps.size(); x++) {
                        if (x == dateIndex) {
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
        editTexts.get(0).setText(labUrinalysis.physician_name);
        editTexts.get(1).setText(labUrinalysis.pathologist);
        editTexts.get(2).setText(labUrinalysis.medTech);
        editTexts.get(3).setText(labUrinalysis.lab_name);
        setCalendar(labUrinalysis.datePerformed);
        editTexts.get(5).setText(labUrinalysis.color);
        editTexts.get(6).setText(labUrinalysis.transparency);
        editTexts.get(7).setText(labUrinalysis.reaction);
        editTexts.get(8).setText(labUrinalysis.specificGravity);
        editTexts.get(9).setText(labUrinalysis.urobilinogen);
        editTexts.get(10).setText(labUrinalysis.pusCells);
        editTexts.get(11).setText(labUrinalysis.rbc);
        editTexts.get(12).setText(labUrinalysis.epithCells);
        editTexts.get(13).setText(labUrinalysis.renalCells);
        editTexts.get(14).setText(labUrinalysis.mucusThreads);
        editTexts.get(15).setText(labUrinalysis.bacteria);
        editTexts.get(16).setText(labUrinalysis.yeastCells);
        checkBoxes.get(0).setChecked(labUrinalysis.sugar.equals("1"));
        checkBoxes.get(0).setChecked(labUrinalysis.albumin.equals("1"));
        checkBoxes.get(0).setChecked(labUrinalysis.ketone.equals("1"));
        checkBoxes.get(0).setChecked(labUrinalysis.bilirubin.equals("1"));
        checkBoxes.get(0).setChecked(labUrinalysis.blood.equals("1"));
        checkBoxes.get(0).setChecked(labUrinalysis.bacteriaNit.equals("1"));
        checkBoxes.get(0).setChecked(labUrinalysis.leukocyte.equals("1"));
        editTexts.get(17).setText(labUrinalysis.amorphousSubs);
        editTexts.get(18).setText(labUrinalysis.uricAcid);
        editTexts.get(19).setText(labUrinalysis.calciumOxalate);
        editTexts.get(20).setText(labUrinalysis.triplePhosphate);
        editTexts.get(21).setText(labUrinalysis.pusCast);
        editTexts.get(22).setText(labUrinalysis.hyaline);
        editTexts.get(23).setText(labUrinalysis.fineGranular);
        editTexts.get(24).setText(labUrinalysis.coarseGranular);
        editTexts.get(25).setText(labUrinalysis.remarks);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == dateIndex) {
            editTexts.get(position).performClick();
        } else if ((position > 8 && position < 14) || position == 15 || position == 16) {
            checkBoxes.get(position - 15).requestFocus();
        } else if (position > 23) {
            editTexts.get(position - 7).requestFocus();
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
