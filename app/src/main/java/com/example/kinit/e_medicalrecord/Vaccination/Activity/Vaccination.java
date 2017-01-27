package com.example.kinit.e_medicalrecord.Vaccination.Activity;

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
import com.example.kinit.e_medicalrecord.General.Adapters.RecyclerView.RecyclerViewAdapter_Vaccination;
import com.example.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.Vaccination.Bus.Bus_Vaccination_OnLongClick;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressBar;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.General.Classes.General.NothingToShow;
import com.example.kinit.e_medicalrecord.General.Classes.Notification.Custom_Notification;
import com.example.kinit.e_medicalrecord.Profile.Class.Patient;
import com.example.kinit.e_medicalrecord.Profile.Class.Viewer;
import com.example.kinit.e_medicalrecord.Vaccination.Class.Vaccine;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.General.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Vaccination extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    int age;
    Intent intent;
    //Classes
    Viewer viewer;
    Patient patient;
    ArrayList<Vaccine> vaccines;
    ArrayList<com.example.kinit.e_medicalrecord.Vaccination.Class.Vaccination> vaccinations;

    Custom_AlertDialog alertDialog;
    Custom_ProgressDialog progressDialog;
    Custom_Notification notification;
    Custom_ProgressBar progressBar;

    //Widgets
    //RecyclerView
    RecyclerView recyclerView_Content;
    RecyclerView.Adapter recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;
    //FAB
    FloatingActionButton btn_add;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout nothingToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_vaccination);
        init();
    }

    void init() {
        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vaccination List");
        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nothingToShow = (LinearLayout) findViewById(R.id.nothingToShow);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);
        notification = new Custom_Notification(this);
        progressBar = new Custom_ProgressBar(this);

        btn_add = (FloatingActionButton) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        recyclerView_Content = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewLayoutM_Content = new LinearLayoutManager(this);

        fetchData();
    }

    void btn_initializer(boolean isButtonViewable) {
        if (isButtonViewable) {
            btn_add.setVisibility(View.VISIBLE);
        } else {
            btn_add.setVisibility(View.GONE);
        }
    }

    void fetchData() {
        try {
            progressBar.show();
            vaccines = new ArrayList<>();
            vaccinations = new ArrayList<>();
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_VACCINATION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                boolean isButtonViewable = true;
                                JSONArray rootJsonArray = new JSONArray(response), jsonArray;
                                JSONObject jsonObject;
                                int jsonArrayLength;
                                if (rootJsonArray.get(0) instanceof JSONArray) {
                                    jsonArray = rootJsonArray.getJSONArray(0);
                                    jsonObject = jsonArray.getJSONObject(0);
                                    if (jsonObject.has("isMyPhysician")) {
                                        isButtonViewable = (viewer != null) ? jsonObject.getString("isMyPhysician").equals("1") : true;
                                    }
                                    if (rootJsonArray.get(1) instanceof JSONArray) {
                                        jsonArray = rootJsonArray.getJSONArray(1);
                                        jsonArrayLength = jsonArray.length();
                                        for (int x = 0; x < jsonArrayLength; x++) {
                                            jsonObject = jsonArray.getJSONObject(x);
                                            vaccines.add(new Vaccine(jsonObject));
                                        }
                                        if (rootJsonArray.get(2) instanceof JSONArray) {
                                            jsonArray = rootJsonArray.getJSONArray(2);
                                            jsonArrayLength = jsonArray.length();
                                            for (int x = 0; x < jsonArrayLength; x++) {
                                                jsonObject = jsonArray.getJSONObject(x);
                                                vaccinations.add(new com.example.kinit.e_medicalrecord.Vaccination.Class.Vaccination(jsonObject));
                                            }
                                            if (rootJsonArray.get(3) instanceof JSONObject) {
                                                jsonObject = rootJsonArray.getJSONObject(3);
                                                age = jsonObject.getInt("age");
                                                setNotification();
                                                btn_initializer(isButtonViewable);
                                                loadToRecyclerView();
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                loadToRecyclerView();
                            } finally {
                                progressBar.hide();
                                NothingToShow.showNothingToShow(vaccinations, recyclerView_Content, nothingToShow);
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
                    params.put("action", "getVaccinationHistory");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient.id));
                    params.put("medical_staff_id", (viewer != null) ? String.valueOf(viewer.medicalStaff_id) : "0");
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            progressBar.hide();
        }
    }

    void deleteData(final Bus_Vaccination_OnLongClick busVaccinationOnLongClick) {
        try {
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_VACCINATION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    if (jsonObject.getString("code").equals("successful")) {
                                        vaccinations.remove(busVaccinationOnLongClick.position);
                                        recyclerViewAdapter_Content.notifyItemRemoved(busVaccinationOnLongClick.position);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progressDialog.dismiss();
                                NothingToShow.showNothingToShow(vaccinations, recyclerView_Content, nothingToShow);
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
                    params.put("action", "deleteVaccination");
                    params.put("device", "mobile");
                    params.put("id", String.valueOf(busVaccinationOnLongClick.vaccination.id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_Vaccination(vaccines, vaccinations, age);
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
    }

    void setNotification() {
        int count = 0;
        for (com.example.kinit.e_medicalrecord.Vaccination.Class.Vaccination vaccination : vaccinations) {
            if (vaccination.vaccine.vaccineScheduleId != 0) {
                if (age >= vaccination.vaccine.ageStart && age <= vaccination.vaccine.ageEnd) {
                    ++count;
                }
            }
        }
        if (count > 0) {
            notification.showVaccination("Vaccination", count + ((count == 1) ? " vaccination is " : " vaccinations are ")
                    + "about to due.");
        }
    }

    void action_AlertDialog(final Bus_Vaccination_OnLongClick busVaccinationOnLongClick) {
        final CharSequence actions[] = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Choose Action");
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        setActivityUpdate(busVaccinationOnLongClick);
                        break;
                    case 1:
                        alertDialog.builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteData(busVaccinationOnLongClick);
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

    void setActivityUpdate(Bus_Vaccination_OnLongClick busVaccinationOnLongClick) {
        intent = new Intent(this, Vaccination_Form.class);
        intent.putExtra("patient", patient);
        intent.putExtra("viewer", viewer);
        intent.putExtra("vaccination", busVaccinationOnLongClick.vaccination);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                intent = new Intent(this, Vaccination_Form.class);
                intent.putExtra("patient", patient);
                intent.putExtra("viewer", viewer);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Subscribe
    public void onLongClickItem(Bus_Vaccination_OnLongClick busVaccinationOnLongClick) {
        action_AlertDialog(busVaccinationOnLongClick);
    }

    @Override
    public void onRefresh() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        fetchData();
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
