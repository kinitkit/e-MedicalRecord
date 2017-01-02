package com.example.kinit.e_medicalrecord.Activities.Medical_Prescription;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.RecyclerViewAdapter_MedicalPrescription;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Medical_Prescription_Click;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Medical_Prescription_LongClick;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.User.Patient;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.Enum.Mode;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Medical_Prescription extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    Intent intent;
    //Classes
    ArrayList<com.example.kinit.e_medicalrecord.Classes.Medical_Prescription.Medical_Prescription> medicalPrescriptions;
    Viewer viewer;
    Patient patient;
    Custom_ProgressDialog progressDialog;
    Custom_AlertDialog alertDialog;

    //Widgets
    //RecyclerView
    RecyclerView recyclerView_Content;
    RecyclerView.Adapter recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;
    //FAB
    FloatingActionButton btn_add;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_prescription);
        init();
    }

    void init() {
        intent = getIntent();
        patient = new Patient();
        patient.id = intent.getIntExtra("patient_id", 0);
        patient.name = intent.getStringExtra("patient_name");
        patient.user_data_id = intent.getIntExtra("user_id", 0);

        if (intent.hasExtra("viewer_user_id")) {
            viewer = new Viewer();
            viewer.name = intent.getStringExtra("viewer_name");
            viewer.user_id = intent.getIntExtra("viewer_user_id", 0);
            viewer.patient_id = intent.getIntExtra("viewer_patient_id", 0);
            viewer.medicalStaff_id = intent.getIntExtra("viewer_medicalStaff_id", 0);
            viewer.mode = Mode.values()[intent.getIntExtra("viewer_ordinal", 0)];
        }

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Medical Prescription List");
        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);

        btn_add = (FloatingActionButton) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        recyclerView_Content = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewLayoutM_Content = new LinearLayoutManager(this);

        fetchData();
    }

    void fetchData() {
        medicalPrescriptions = new ArrayList<>();
        try {
            progressDialog.show("Loading...");
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                boolean isButtonViewable = true;
                                JSONArray rootJsonArray = new JSONArray(response), jsonArray;
                                JSONObject jsonObject;
                                if (rootJsonArray.get(0) instanceof JSONArray) {
                                    jsonArray = rootJsonArray.getJSONArray(0);
                                    jsonObject = jsonArray.getJSONObject(0);
                                    if (jsonObject.has("isMyPhysician")) {
                                        isButtonViewable = (viewer != null) ? jsonObject.getString("isMyPhysician").equals("1") : true;
                                    }
                                    jsonObject = rootJsonArray.getJSONObject(1);
                                    if (jsonObject.getString("code").equals("successful")) {
                                        jsonArray = rootJsonArray.getJSONArray(2);
                                        int jsonArrayLength = jsonArray.length();
                                        for (int x = 0; x < jsonArrayLength; x++) {
                                            jsonObject = jsonArray.getJSONObject(x);
                                            medicalPrescriptions.add(new com.example.kinit.e_medicalrecord.Classes.Medical_Prescription.Medical_Prescription(jsonObject));
                                        }
                                        loadToRecyclerView();
                                        btn_initializer(isButtonViewable);
                                    } else if (jsonObject.getString("code").equals("empty")) {
                                        loadToRecyclerView();
                                        btn_initializer(isButtonViewable);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
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
                    params.put("action", "getMedicalPrescription");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient.id));
                    params.put("medical_staff_id", (viewer != null) ? String.valueOf(viewer.medicalStaff_id) : "0");
                    params.put("user_data_id", String.valueOf((viewer != null) ? viewer.user_id : patient.user_data_id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    void btn_initializer(boolean isButtonViewable) {
        if (isButtonViewable) {
            btn_add.setVisibility(View.VISIBLE);
        } else {
            btn_add.setVisibility(View.GONE);
        }
    }

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_MedicalPrescription(medicalPrescriptions);
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
        progressDialog.dismiss();
    }



    void action_AlertDialog(final Bus_Medical_Prescription_LongClick busMedicalPrescriptionLongClick) {
        final CharSequence actions[] = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Choose Action");
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        updateItem(busMedicalPrescriptionLongClick);
                        break;
                    case 1:
                        alertDialog.builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteData(busMedicalPrescriptionLongClick);
                                    }
                                });
                        alertDialog.show("Delete", "This item will be permanently deleted.");
                        break;
                }
            }
        });
        builder.show();
    }

    void updateItem(Bus_Medical_Prescription_LongClick busMedicalPrescriptionLongClick) {
        intent = new Intent(this, Medical_Prescription_Form.class);
        intent.putExtra("patient", patient);
        intent.putExtra("viewer", viewer);
        intent.putExtra("busMedicalPrescriptionLongClick", busMedicalPrescriptionLongClick);
        startActivityForResult(intent, 1);
    }

    void deleteData(final Bus_Medical_Prescription_LongClick busMedicalPrescriptionLongClick) {
        progressDialog.show("Deleting...");
        try {
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try{
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if(jsonObject.getString("code").equals("successful")){
                                    medicalPrescriptions.remove(busMedicalPrescriptionLongClick.position);
                                    recyclerViewAdapter_Content.notifyItemRemoved(busMedicalPrescriptionLongClick.position);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
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
                    params.put("action", "deleteMedPrescription");
                    params.put("device", "mobile");
                    params.put("medical_prescription_id", String.valueOf(busMedicalPrescriptionLongClick.medicalPrescription_id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onClickItem(Bus_Medical_Prescription_Click busMedicalPrescriptionClick){
        intent = new Intent(this, Medical_Prescription_View.class);
        intent.putExtra("patient", patient);
        intent.putExtra("viewer", viewer);
        intent.putExtra("busMedicalPrescriptionClick", busMedicalPrescriptionClick);
        startActivity(intent);
    }

    @Subscribe
    public void onLongClickItem(Bus_Medical_Prescription_LongClick busMedicalPrescriptionLongClick) {
        if(viewer != null){
            if(viewer.user_id == busMedicalPrescriptionLongClick.user_data_id) {
                action_AlertDialog(busMedicalPrescriptionLongClick);
            }
        } else {
            action_AlertDialog(busMedicalPrescriptionLongClick);
        }
    }

    @Override
    public void onRefresh() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        fetchData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                intent = new Intent(this, Medical_Prescription_Form.class);
                intent.putExtra("patient", patient);
                intent.putExtra("viewer", viewer);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                fetchData();
            }
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