package com.example.kinit.e_medicalrecord.Consultation.Activity;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Consultation.Bus.Bus_ConsultationROS;
import com.example.kinit.e_medicalrecord.Consultation.Class.ArrayAdapter_Strings;
import com.example.kinit.e_medicalrecord.Consultation.Class.Consultation;
import com.example.kinit.e_medicalrecord.Consultation.Class.Review_Of_Systems;
import com.example.kinit.e_medicalrecord.Consultation.Fragment.Review_Of_Systems_Dialog;
import com.example.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressBar;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.DatePickerFragment;
import com.example.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.General.Request.UrlString;
import com.example.kinit.e_medicalrecord.Profile.Class.Patient;
import com.example.kinit.e_medicalrecord.Profile.Class.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Consultation_Form extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    Patient patient;
    Viewer viewer;
    SimpleDateFormat simpleDateFormat;
    Consultation consultation;
    ArrayList<Review_Of_Systems> reviewOfSystems;
    ArrayAdapter_Strings arrayAdapterStrings;

    Custom_AlertDialog alertDialog;
    Custom_ProgressDialog progressDialog;
    Custom_ProgressBar progressBar;
    Review_Of_Systems_Dialog reviewOfSystemsDialog;

    //Widgets
    EditText et_physician, et_date, et_chiefComplaint, et_presentIllness, et_diagnosis;
    Button btn_save;
    ImageButton btn_add;
    Calendar calendar;
    DatePickerFragment datePickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_consultation_form);
        init();
    }

    void init() {
        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");
        if (intent.hasExtra("consultation")) {
            consultation = intent.getExtras().getParcelable("consultation");
        }

        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);
        progressBar = new Custom_ProgressBar(this);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Consultation Form");
        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_physician = (EditText) findViewById(R.id.et_physician);
        et_date = (EditText) findViewById(R.id.et_date);
        et_chiefComplaint = (EditText) findViewById(R.id.et_chiefComplaint);
        et_presentIllness = (EditText) findViewById(R.id.et_presentIllness);
        et_diagnosis = (EditText) findViewById(R.id.et_diagnosis);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_add = (ImageButton) findViewById(R.id.btn_add);
        btn_save.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        et_date.setOnClickListener(this);

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

        reviewOfSystemsDialog = new Review_Of_Systems_Dialog();

        getVaccines();
        if (consultation != null) {
            //fetchData();
        }
    }

    void setCalendar(Calendar calendar) {
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        et_date.setText(simpleDateFormat.format(calendar.getTime()));
    }

    boolean validateEditText(EditText editText, String text) {
        boolean isThereNoError = true;

        if (text.isEmpty()) {
            editText.setError(getString(R.string.required_field));
            isThereNoError = false;
        }

        return isThereNoError;
    }

    /*void onClickSave() {
        String strMedicalCondition = et_medicalCondition.getText().toString().trim();

        if (validateEditText(et_medicalCondition, strMedicalCondition)) {
            saveData(strMedicalCondition);
        }
    }*/

    void getVaccines() {
        try {
            progressBar.show();
            reviewOfSystems = new ArrayList<>();
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_CONSULTATION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response), jsonArray;
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    String code = jsonObject.getString("code");
                                    switch (code) {
                                        case "success":
                                            jsonArray = rootJsonArray.getJSONArray(1);
                                            arrayAdapterStrings = new ArrayAdapter_Strings(jsonArray);
                                            reviewOfSystems = arrayAdapterStrings.reviewOfSystems;

                                            /*int jsonArrayLength = jsonArray.length();
                                            for (int x = 0; x < jsonArrayLength; x++) {
                                                jsonObject = jsonArray.getJSONObject(x);
                                                reviewOfSystems.add(new Review_Of_Systems(jsonObject));
                                            }*/
                                            reviewOfSystemsDialog.setArrayList(arrayAdapterStrings);
                                            break;
                                        case "empty":
                                        case "error":
                                            break;
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
                            progressBar.show();
                            error.printStackTrace();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getVaccines");
                    params.put("device", "mobile");

                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressBar.hide();
            e.printStackTrace();
        }
    }

    void saveData(final String strMedicalCondition) {
        try {
            progressDialog.show("Saving...");
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_PAST_MEDICAL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    String code = jsonObject.getString("code");
                                    switch (code) {
                                        case "success":
                                            if (consultation == null) {
                                                Toast.makeText(getApplicationContext(), R.string.record_added, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), R.string.record_updated, Toast.LENGTH_SHORT).show();
                                            }
                                            intent = new Intent();
                                            intent.putExtra("result", true);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                            break;
                                        case "unauthorized":
                                            alertDialog.show("Error", getString(R.string.unauthorized_to_insert));
                                            break;
                                        case "empty":
                                            alertDialog.show("Error", getString(R.string.error_occured));
                                            break;
                                        default:
                                            alertDialog.show("Error", getString(R.string.error_occured));
                                            break;
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
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    if (consultation != null) {
                        params.put("action", "updatePastMedicalHistory");
                        params.put("id", String.valueOf(consultation.id));
                    } else {
                        params.put("action", "insertPastMedicalHistory");
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
                    params.put("medical_condition", strMedicalCondition);
                    params.put("date_time", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));

                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    /*void fetchData() {
        try {
            progressBar.show();
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_PAST_MEDICAL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response), jsonArray;
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    String code = jsonObject.getString("code");
                                    switch (code){
                                        case "success":
                                            jsonArray = rootJsonArray.getJSONArray(1);
                                            pastMedicalHistory = new Past_Medical_History(jsonArray.getJSONObject(0));
                                            setText();
                                            break;
                                        case "empty":
                                        case "error":
                                            break;
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
                            error.printStackTrace();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getPastMedicalHistoryData");
                    params.put("device", "mobile");
                    params.put("id", String.valueOf(pastMedicalHistory.id));

                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressBar.hide();
            e.printStackTrace();
        }
    }*/

    void setText() {
        //et_medicalCondition.setText(pastMedicalHistory.medicalCondition);
        //calendar = (Calendar) pastMedicalHistory.dateTime.clone();
        setCalendar(calendar);
    }

    void showDialog() {
        reviewOfSystemsDialog.show(getSupportFragmentManager(), "Review Of Systems Dialog");
    }

    @Subscribe
    public void onClickDialogOk(Bus_ConsultationROS consultationROS) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                //onClickSave();
                break;
            case R.id.btn_add:
                showDialog();
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
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusStation.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusStation.getBus().unregister(this);
    }
}
