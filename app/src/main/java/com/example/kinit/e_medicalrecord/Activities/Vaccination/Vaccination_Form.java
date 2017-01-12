package com.example.kinit.e_medicalrecord.Activities.Vaccination;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.kinit.e_medicalrecord.Classes.Vaccination.Vaccine;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vaccination_Form extends AppCompatActivity implements View.OnClickListener {

    //Classes
    ArrayList<Vaccine> vaccines;
    List<String> spinnerArray;
    Intent intent;
    Patient patient;
    Viewer viewer;
    SimpleDateFormat simpleDateFormat;
    Custom_AlertDialog alertDialog;
    Custom_ProgressDialog progressDialog;

    //Widgets
    Spinner spinner_vaccine;
    ArrayAdapter arrayAdapter;
    EditText et_provider, et_date, et_place;
    Button btn_save;
    Calendar calendar;
    DatePickerFragment datePickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_form);
        init();
    }

    void init() {
        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");
        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);

        fetchVaccine();

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vaccination Form");
        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_provider = (EditText) findViewById(R.id.et_provider);
        et_date = (EditText) findViewById(R.id.et_date);
        et_place = (EditText) findViewById(R.id.et_place);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
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

    void onClickSave() {
        String strProvider = et_provider.getText().toString().trim(), strPlace = et_place.getText().toString().trim();

        if (validateEditText(et_provider, strProvider) && validateEditText(et_place, strPlace)) {
            saveData(strProvider, strPlace);
        }
    }

    void saveData(final String strProvider, final String strPlace) {
        try {
            progressDialog.show("Saving...");
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL,
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
                                            Toast.makeText(getApplicationContext(), R.string.record_added, Toast.LENGTH_SHORT).show();
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
                    params.put("action", "insertVaccinationHistory");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient.id));
                    if (viewer != null) {
                        params.put("user_data_id", String.valueOf(viewer.user_id));
                        params.put("medical_staff_id", String.valueOf(viewer.medicalStaff_id));
                    } else {
                        params.put("user_data_id", String.valueOf(patient.user_data_id));
                        params.put("medical_staff_id", "0");
                    }
                    params.put("vaccination_id", String.valueOf(vaccines.get(spinner_vaccine.getSelectedItemPosition()).vaccineId));
                    params.put("provider_name", strProvider);
                    params.put("date_taken", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                    params.put("place_taken", strPlace);

                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    void fetchVaccine() {
        try {
            vaccines = new ArrayList<>();
            spinnerArray = new ArrayList<>();
            progressDialog.show("Loading vaccines...");
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("error", response);
                                JSONArray rootJsonArray = new JSONArray(response), jsonArray;
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");
                                if (code.equals("success")) {
                                    jsonArray = rootJsonArray.getJSONArray(1);
                                    int jsonArrayLength = jsonArray.length();
                                    for (int x = 0; x < jsonArrayLength; x++) {
                                        jsonObject = jsonArray.getJSONObject(x);
                                        spinnerArray.add(jsonObject.getString("item"));
                                        vaccines.add(new Vaccine(jsonObject));
                                    }
                                    arrayAdapter = new ArrayAdapter<>(Vaccination_Form.this, R.layout.support_simple_spinner_dropdown_item, spinnerArray);
                                    spinner_vaccine = (Spinner) findViewById(R.id.spinner_vaccine);
                                    spinner_vaccine.setAdapter(arrayAdapter);
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
                    params.put("action", "getAllVaccine");
                    params.put("device", "mobile");

                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                onClickSave();
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
}
