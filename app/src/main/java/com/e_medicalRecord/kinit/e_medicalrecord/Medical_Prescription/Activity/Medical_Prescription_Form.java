package com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView.RecyclerViewAdapter_MedicalPrescriptionDrugForm;
import com.e_medicalRecord.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_AlertDialog;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressBar;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressDialog;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.DatePickerFragment;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Request.UrlString;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Bus.Bus_DrugMedForm;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Bus.Bus_Drug_OnLongClick;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Bus.Bus_Medical_Prescription_LongClick;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Class.Drug;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Class.Medical_Prescription;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Fragment.Drug_Dialog;
import com.e_medicalRecord.kinit.e_medicalrecord.Profile.Class.Patient;
import com.e_medicalRecord.kinit.e_medicalrecord.Profile.Class.Viewer;
import com.e_medicalRecord.kinit.e_medicalrecord.R;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Medical_Prescription_Form extends AppCompatActivity implements View.OnClickListener {

    //Widgets
    EditText et_clinic, et_physicianName, et_prescriptionDate;
    Button btn_save;
    ImageButton btn_add;
    RecyclerView recyclerView_Content;
    RecyclerViewAdapter_MedicalPrescriptionDrugForm recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;

    //Classes
    ArrayList<Drug> drugs;
    Patient patient;
    Viewer viewer;
    DatePickerFragment datePickerFragment;
    Medical_Prescription medicalPrescription;
    Bus_Medical_Prescription_LongClick busMedicalPrescriptionLongClick;
    Bus_Drug_OnLongClick busDrugOnLongClick;
    Custom_ProgressDialog progressDialog;
    Custom_ProgressBar progressBar;
    Drug_Dialog drugDialog;

    //App
    Custom_AlertDialog alertDialog;
    Intent intent;

    //Utils
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_medical_prescription_form);
        init();
    }

    void init() {
        progressDialog = new Custom_ProgressDialog(this);
        progressBar = new Custom_ProgressBar(this);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");
        if (intent.hasExtra("busMedicalPrescriptionLongClick")) {
            busMedicalPrescriptionLongClick = intent.getExtras().getParcelable("busMedicalPrescriptionLongClick");
            getSupportActionBar().setTitle("Update Medical Prescription");
        } else {
            getSupportActionBar().setTitle("Medical Prescription Form");
        }
        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        alertDialog = new Custom_AlertDialog(this);
        drugs = new ArrayList<>();
        et_clinic = (EditText) findViewById(R.id.et_clinic);
        et_physicianName = (EditText) findViewById(R.id.et_physicianName);
        et_prescriptionDate = (EditText) findViewById(R.id.et_prescriptionDate);
        et_prescriptionDate.setOnClickListener(this);

        btn_add = (ImageButton) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        recyclerView_Content = (RecyclerView) findViewById(R.id.recyclerView);

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
        drugDialog = new Drug_Dialog();

        if (viewer != null) {
            et_physicianName.setFocusableInTouchMode(false);
            et_physicianName.setKeyListener(null);
            et_physicianName.setText(viewer.name);
        }

        if (busMedicalPrescriptionLongClick != null) {
            fetchDrugList();
        } else {
            loadToRecyclerView();
        }
    }

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_MedicalPrescriptionDrugForm((ArrayList<Drug>)drugs.clone());
        recyclerViewLayoutM_Content = new LinearLayoutManager(this);
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
    }


    void setCalendar(Calendar calendar) {
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        et_prescriptionDate.setText(simpleDateFormat.format(calendar.getTime()));
    }

    void setUpdate() {
        et_physicianName.setText(medicalPrescription.physicianName);
        et_clinic.setText(medicalPrescription.clinic_name);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(medicalPrescription.calendarStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        setCalendar(calendar);
        progressDialog.dismiss();
    }

    void alertDialog_showError(String code) {
        if (code.equals("medicalPrescriptionNotAvailable")) {
            alertDialog.show("Notice", "Medical Prescription is not available!");
        } else {
            alertDialog.show("Error", getString(R.string.error_occured));
        }
    }

    void showDialog(Bus_Drug_OnLongClick busDrugOnLongClick) {
        drugDialog.show(getSupportFragmentManager(), "Drug Dialog");
        if (busDrugOnLongClick != null) {
            drugDialog.setFields(busDrugOnLongClick);
        }
    }

    void insertDrug(Bus_DrugMedForm busDrug) {
        drugs.add(busDrug.drug);
        recyclerViewAdapter_Content.addItem(busDrug.drug);
    }

    void updateDrug(Bus_DrugMedForm busDrug) {
        drugs.set(busDrug.position, busDrug.drug);
        recyclerViewAdapter_Content.updateItem(busDrug.position, busDrug.drug);
    }

    void removeDrug(int position) {
        drugs.remove(position);
        recyclerViewAdapter_Content.removeItem(position);
    }

    void action_AlertDialog(final Bus_Drug_OnLongClick busDrugOnLongClick) {
        final CharSequence actions[] = {"Edit", "Delete"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Choose Action");
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showDialog(busDrugOnLongClick);
                        break;
                    case 1:
                        removeDrug(busDrugOnLongClick.position);
                        break;
                }
            }
        });
        builder.show();
    }

    void sendData(final String physicianName, final String clinicName) {
        progressDialog.show("Saving...");
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL_MEDICAL_PRESCRIPTION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("error", response);
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    if (jsonObject.getString("code").equals("successful")) {
                                        progressDialog.dismiss();
                                        if (busMedicalPrescriptionLongClick != null) {
                                            Toast.makeText(getApplicationContext(), R.string.record_updated, Toast.LENGTH_SHORT).show();
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
                                        progressDialog.dismiss();
                                        alertDialog.show("Error", getString(R.string.not_available));
                                    } else {
                                        progressDialog.dismiss();
                                        alertDialog.show("Error", getString(R.string.error_occured));
                                    }
                                } else if (jsonObject.has("exception")) {
                                    progressDialog.dismiss();
                                    alertDialog.show("Error", jsonObject.getString("exception"));
                                }
                            } catch (Exception e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    if (busMedicalPrescriptionLongClick != null) {
                        params.put("action", "updateMedPrescription");
                        params.put("medical_prescription_id", String.valueOf(busMedicalPrescriptionLongClick.medicalPrescription_id));
                    } else {
                        params.put("action", "insertNewMedPrescription");
                    }
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient.id));
                    params.put("clinic_name", clinicName);
                    params.put("physician_name", physicianName);
                    params.put("date_prescribed", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                    if (viewer != null) {
                        params.put("user_data_id", String.valueOf(viewer.user_id));
                        params.put("medical_staff_id", String.valueOf(viewer.medicalStaff_id));
                    } else {
                        params.put("user_data_id", String.valueOf(patient.user_data_id));
                        params.put("medical_staff_id", "0");
                    }
                    for (int x = 0; x < drugs.size(); x++) {
                        params.put("drug[" + (x) + "]", drugs.get(x).drug);
                        params.put("strength[" + (x) + "]", drugs.get(x).strength);
                        params.put("dosage[" + (x) + "]", drugs.get(x).dosage);
                        params.put("route[" + (x) + "]", drugs.get(x).route);
                        params.put("frequency[" + (x) + "]", drugs.get(x).frequency);
                        params.put("indication[" + (x) + "]", drugs.get(x).why);
                        params.put("many[" + (x) + "]", drugs.get(x).quantity);
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

    void fetchDrugList() {
        try {
            progressBar.show();
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_MEDICAL_PRESCRIPTION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response), jsonArray;
                                JSONObject jsonObject;
                                if (rootJsonArray.get(0) instanceof JSONObject) {
                                    jsonObject = rootJsonArray.getJSONObject(0);
                                    if (jsonObject.has("code")) {
                                        alertDialog_showError(jsonObject.getString("code"));
                                    } else if (jsonObject.has("exception")) {
                                        alertDialog_showError(jsonObject.getString("exception"));
                                    }
                                } else {
                                    jsonArray = rootJsonArray.getJSONArray(0);
                                    medicalPrescription = new Medical_Prescription(jsonArray.getJSONObject(0));
                                    jsonObject = rootJsonArray.getJSONObject(1);
                                    if (jsonObject.has("code")) {
                                        if (jsonObject.getString("code").equals("successful")) {
                                            jsonArray = rootJsonArray.getJSONArray(2);
                                            int jsonArrayLength = jsonArray.length();
                                            for (int x = 0; x < jsonArrayLength; x++) {
                                                jsonObject = jsonArray.getJSONObject(x);
                                                drugs.add(new Drug(jsonObject));
                                            }
                                            loadToRecyclerView();
                                        } else {
                                            alertDialog_showError(jsonObject.getString("code"));
                                        }
                                        setUpdate();
                                    } else {
                                        alertDialog_showError(jsonObject.getString("exception"));
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
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getDrugList");
                    params.put("device", "mobile");
                    params.put("medical_prescription_id", String.valueOf(busMedicalPrescriptionLongClick.medicalPrescription_id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressBar.hide();
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onClickDialogOk(Bus_DrugMedForm busDrugMedForm) {
        Log.d("error", "qwe");
        switch (busDrugMedForm.queryType) {
            case INSERT:
                insertDrug(busDrugMedForm);
                break;
            case UPDATE:
                updateDrug(busDrugMedForm);
                break;
        }
    }

    @Subscribe
    public void onLongClickDrug(Bus_Drug_OnLongClick busDrugOnLongClick) {
        this.busDrugOnLongClick = busDrugOnLongClick;
        action_AlertDialog(this.busDrugOnLongClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                showDialog(null);
                break;

            case R.id.btn_save:
                Custom_AlertDialog alertDialog = new Custom_AlertDialog(this);
                String clinic = et_clinic.getText().toString().trim(),
                        physicianName = et_physicianName.getText().toString().trim();
                if (drugs.size() > 0) {
                    if (!physicianName.isEmpty()) {
                        sendData(physicianName, clinic);
                    } else {
                        et_physicianName.setError("This field is required.");
                    }
                } else {
                    alertDialog.show("Ooops!", "Please add a drug/medication");
                }
                break;

            case R.id.et_prescriptionDate:
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
