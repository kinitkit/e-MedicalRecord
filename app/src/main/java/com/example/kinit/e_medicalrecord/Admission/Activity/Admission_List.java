package com.example.kinit.e_medicalrecord.Admission.Activity;

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
import com.example.kinit.e_medicalrecord.General.Adapters.RecyclerView.RecyclerViewAdapter_Admission;
import com.example.kinit.e_medicalrecord.Admission.Bus.Bus_Admission_OnLongClick;
import com.example.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.Admission.Bus.Bus_Admission_OnClick;
import com.example.kinit.e_medicalrecord.Admission.Class.Admission;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressBar;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.General.Classes.General.NothingToShow;
import com.example.kinit.e_medicalrecord.Profile.Class.Patient;
import com.example.kinit.e_medicalrecord.Profile.Class.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.General.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Admission_List extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    Intent intent;
    //Classes
    Viewer viewer;
    Patient patient;
    Custom_ProgressDialog progressDialog;
    Custom_AlertDialog alertDialog;
    Custom_ProgressBar progressBar;
    ArrayList<Admission> admissions;

    //Widgets
    LinearLayout nothingToShow;
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
        setContentView(R.layout.activity_admission_list);
        init();
    }

    void init() {
        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admission List");
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
        admissions = new ArrayList<>();
        try {
            progressBar.show();
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_ADMISSION,
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
                                    if (jsonObject.getString("code").equals("success")) {
                                        jsonArray = rootJsonArray.getJSONArray(2);
                                        int jsonArrayLength = jsonArray.length();
                                        for (int x = 0; x < jsonArrayLength; x++) {
                                            jsonObject = jsonArray.getJSONObject(x);
                                            admissions.add(new Admission(jsonObject));
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
                                NothingToShow.showNothingToShow(admissions, recyclerView_Content, nothingToShow);
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
                    params.put("action", "getAdmissions");
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
            progressBar.hide();
        }
    }

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_Admission(admissions);
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
        progressDialog.dismiss();
    }

    void btn_initializer(boolean isButtonViewable) {
        if (isButtonViewable) {
            btn_add.show();
        } else {
            btn_add.hide();
        }
    }

    void action_AlertDialog(final Bus_Admission_OnLongClick busAdmissionOnLongClick) {
        final CharSequence actions[] = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Choose Action");
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        setActivityUpdate(busAdmissionOnLongClick);
                        break;
                    case 1:
                        alertDialog.builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteData(busAdmissionOnLongClick);
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

    void setActivityUpdate(Bus_Admission_OnLongClick busAdmissionOnLongClick) {
        intent = new Intent(this, Admission_Form.class);
        intent.putExtra("patient", patient);
        intent.putExtra("viewer", viewer);
        intent.putExtra("admission", busAdmissionOnLongClick.admission);
        startActivityForResult(intent, 1);
    }

    void deleteData(final Bus_Admission_OnLongClick busAdmissionOnLongClick) {
        progressDialog.show("Deleting...");
        try {
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_ADMISSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    if (jsonObject.getString("code").equals("successful")) {
                                        admissions.remove(busAdmissionOnLongClick.position);
                                        recyclerViewAdapter_Content.notifyItemRemoved(busAdmissionOnLongClick.position);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progressDialog.dismiss();
                                NothingToShow.showNothingToShow(admissions, recyclerView_Content, nothingToShow);
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
                    params.put("action", "deleteAdmission");
                    params.put("device", "mobile");
                    params.put("id", String.valueOf(busAdmissionOnLongClick.admission.id));
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
    public void onClickItem(Bus_Admission_OnClick busAdmissionOnClick) {
        intent = new Intent(this, Admission_View.class);
        intent.putExtra("patient", patient);
        intent.putExtra("viewer", viewer);
        intent.putExtra("admission", busAdmissionOnClick.admission);
        startActivity(intent);
    }

    @Subscribe
    public void onLongClickItem(Bus_Admission_OnLongClick busAdmissionOnLongClick) {
        if (viewer != null) {
            if (busAdmissionOnLongClick.admission.userDataId == viewer.user_id) {
                action_AlertDialog(busAdmissionOnLongClick);
            }
        } else {
            action_AlertDialog(busAdmissionOnLongClick);
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
                intent = new Intent(this, Admission_Form.class);
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
