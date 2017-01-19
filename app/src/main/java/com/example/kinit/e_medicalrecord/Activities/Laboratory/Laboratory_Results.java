package com.example.kinit.e_medicalrecord.Activities.Laboratory;

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
import android.view.Window;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.RecyclerViewAdapter_LabResult_Container;
import com.example.kinit.e_medicalrecord.BusStation.Allergy.Bus_Allergy;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Laboratory.Bus_Laboratory_OnClick;
import com.example.kinit.e_medicalrecord.BusStation.Laboratory.Bus_Laboratory_OnLongClick;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressBar;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.General.NothingToShow;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Chemistry;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Fecalysis;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Hematology;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Urinalysis;
import com.example.kinit.e_medicalrecord.Enum.Laboratory_Tests;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Laboratory;
import com.example.kinit.e_medicalrecord.Classes.User.Patient;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Laboratory_Results extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    Intent intent;
    String table_name;
    //Classes
    ArrayList<Laboratory> laboratories;
    Viewer viewer;
    Patient patient;
    Custom_ProgressDialog progressDialog;
    Custom_AlertDialog alertDialog;
    Custom_ProgressBar progressBar;
    LinearLayout nothingToShow;
    Laboratory_Tests laboratoryTests;

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
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_laboratory_results);
        init();
    }

    void init() {
        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");
        laboratoryTests = Laboratory_Tests.values()[intent.getIntExtra("ordinal", 0)];

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbarTitle();
        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);
        progressBar = new Custom_ProgressBar(this);
        nothingToShow = (LinearLayout) findViewById(R.id.nothingToShow);

        btn_add = (FloatingActionButton) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        recyclerView_Content = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewLayoutM_Content = new LinearLayoutManager(this);

        fetchData();
    }

    void fetchData() {
        laboratories = new ArrayList<>();
        try {
            progressBar.show();
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_LABORATORY,
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
                                            laboratories.add(new Laboratory(jsonObject));
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
                            } finally {
                                progressBar.hide();
                                NothingToShow.showNothingToShow(laboratories, recyclerView_Content, nothingToShow);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    progressBar.hide();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getLabResult");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient.id));
                    params.put("medical_staff_id", (viewer != null) ? String.valueOf(viewer.medicalStaff_id) : "0");
                    params.put("user_data_id", String.valueOf((viewer != null) ? viewer.user_id : patient.user_data_id));
                    params.put("table_name", table_name);
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            progressBar.hide();
        }
    }

    void deleteData(final Bus_Laboratory_OnLongClick busLaboratoryOnLongClick) {
        progressDialog.show("Deleting...");
        try {
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_LABORATORY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.getString("code").equals("successful")) {
                                    laboratories.remove(busLaboratoryOnLongClick.position);
                                    recyclerViewAdapter_Content.notifyItemRemoved(busLaboratoryOnLongClick.position);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progressDialog.dismiss();
                                NothingToShow.showNothingToShow(laboratories, recyclerView_Content, nothingToShow);
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
                    params.put("action", "deleteLabTest");
                    params.put("device", "mobile");
                    params.put("id", String.valueOf(busLaboratoryOnLongClick.laboratory.lab_id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
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
        recyclerViewAdapter_Content = new RecyclerViewAdapter_LabResult_Container(laboratories);
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
        progressDialog.dismiss();
    }

    void setToolbarTitle() {
        switch (laboratoryTests) {
            case BLOOD_CHEMISTRY:
                getSupportActionBar().setTitle("Blood Chemistry List");
                table_name = Lab_Chemistry.TABLE_NAME;
                break;
            case FECALYSIS:
                getSupportActionBar().setTitle("Fecalysis List");
                table_name = Lab_Fecalysis.TABLE_NAME;
                break;
            case HEMATOLOGY:
                getSupportActionBar().setTitle("Hematology List");
                table_name = Lab_Hematology.TABLE_NAME;
                break;
            case URINALYSIS:
                getSupportActionBar().setTitle("Urinalysis List");
                table_name = Lab_Urinalysis.TABLE_NAME;
                break;
        }
    }

    void action_AlertDialog(final Bus_Laboratory_OnLongClick busLaboratoryOnLongClick) {
        final CharSequence actions[] = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Choose Action");
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        updateItem(busLaboratoryOnLongClick);
                        break;
                    case 1:
                        alertDialog.builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteData(busLaboratoryOnLongClick);
                                    }
                                });
                        alertDialog.builder.setNegativeButton("Cancel", null);
                        alertDialog.show("Delete", "This item will be permanently deleted.");
                        break;
                }
            }
        });
        builder.show();
    }

    void openForm() {
        switch (laboratoryTests) {
            case BLOOD_CHEMISTRY:
                intent = new Intent(this, Lab_Chemistry_Form.class);
                setActivity();
                break;
            case FECALYSIS:
                intent = new Intent(this, Lab_Fecalysis_Form.class);
                setActivity();
                break;
            case HEMATOLOGY:
                intent = new Intent(this, Lab_Hematology_Form.class);
                setActivity();
                break;
            case URINALYSIS:
                intent = new Intent(this, Lab_Urinalysis_Form.class);
                setActivity();
                break;
        }
    }

    void setActivity() {
        intent.putExtra("patient", patient);
        intent.putExtra("viewer", viewer);
        startActivityForResult(intent, 1);
    }

    void setActivityUpdate(Bus_Laboratory_OnLongClick busLaboratoryOnLongClick) {
        intent.putExtra("patient", patient);
        intent.putExtra("viewer", viewer);
        intent.putExtra("laboratory", busLaboratoryOnLongClick.laboratory);
        startActivityForResult(intent, 1);
    }

    void updateItem(Bus_Laboratory_OnLongClick busLaboratoryOnLongClick) {
        switch (laboratoryTests) {
            case BLOOD_CHEMISTRY:
                intent = new Intent(this, Lab_Chemistry_Form.class);
                setActivityUpdate(busLaboratoryOnLongClick);
                break;
            case FECALYSIS:
                intent = new Intent(this, Lab_Fecalysis_Form.class);
                setActivityUpdate(busLaboratoryOnLongClick);
                break;
            case HEMATOLOGY:
                intent = new Intent(this, Lab_Hematology_Form.class);
                setActivityUpdate(busLaboratoryOnLongClick);
                break;
            case URINALYSIS:
                intent = new Intent(this, Lab_Urinalysis_Form.class);
                setActivityUpdate(busLaboratoryOnLongClick);
                break;
        }
    }

    @Subscribe
    public void onClickItem(Bus_Laboratory_OnClick busLaboratoryOnClick) {
        intent = new Intent(this, Laboratory_Result_View.class);
        intent.putExtra("patient", patient);
        intent.putExtra("viewer", viewer);
        intent.putExtra("laboratory", busLaboratoryOnClick.laboratory);
        intent.putExtra("ordinal", laboratoryTests.ordinal());
        startActivity(intent);
    }

    @Subscribe
    public void onLongClickItem(Bus_Laboratory_OnLongClick busLaboratoryOnLongClick) {
        if (viewer != null) {
            if (busLaboratoryOnLongClick.laboratory.user_data_id == viewer.user_id) {
                action_AlertDialog(busLaboratoryOnLongClick);
            }
        } else {
            action_AlertDialog(busLaboratoryOnLongClick);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                openForm();
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
    public void onRefresh() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        fetchData();
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
