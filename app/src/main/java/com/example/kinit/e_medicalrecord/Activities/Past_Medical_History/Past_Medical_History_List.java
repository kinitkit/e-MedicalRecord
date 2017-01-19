package com.example.kinit.e_medicalrecord.Activities.Past_Medical_History;

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
import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.RecyclerViewAdapter_PastMedicalHistory;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Past_Medical_History.Bus_PastMedicalHistory_OnLongClick;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressBar;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.General.NothingToShow;
import com.example.kinit.e_medicalrecord.Classes.Past_Medical_History.Past_Medical_History;
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

public class Past_Medical_History_List extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    Intent intent;
    //Classes
    Viewer viewer;
    Patient patient;
    ArrayList<Past_Medical_History> pastMedicalHistories;

    Custom_AlertDialog alertDialog;
    Custom_ProgressDialog progressDialog;
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
        setContentView(R.layout.activity_past_medical_history);
        init();
    }

    void init() {
        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Past Medical History");
        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nothingToShow = (LinearLayout) findViewById(R.id.nothingToShow);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);
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

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_PastMedicalHistory(pastMedicalHistories);
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
    }

    void fetchData() {
        try {
            pastMedicalHistories = new ArrayList<>();
            progressBar.show();
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_PAST_MEDICAL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try{
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
                                            pastMedicalHistories.add(new Past_Medical_History(jsonObject));
                                        }
                                    }
                                }
                                loadToRecyclerView();
                                btn_initializer(isButtonViewable);
                            } catch (Exception e){
                                e.printStackTrace();
                            } finally {
                                progressBar.hide();
                                NothingToShow.showNothingToShow(pastMedicalHistories, recyclerView_Content, nothingToShow);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            progressBar.hide();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getPastMedicalHistoriesData");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient.id));
                    params.put("medical_staff_id", (viewer != null) ? String.valueOf(viewer.medicalStaff_id) : "0");

                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressBar.hide();
            e.printStackTrace();
        }
    }

    void action_AlertDialog(final Bus_PastMedicalHistory_OnLongClick busPastMedicalHistoryOnLongClick) {
        final CharSequence actions[] = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Choose Action");
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        setActivityUpdate(busPastMedicalHistoryOnLongClick);
                        break;
                    case 1:
                        alertDialog.builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteData(busPastMedicalHistoryOnLongClick);
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

    void deleteData(final Bus_PastMedicalHistory_OnLongClick busPastMedicalHistoryOnLongClick) {
        progressDialog.show("Deleting...");
        try {
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_PAST_MEDICAL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    if (jsonObject.getString("code").equals("success")) {
                                        pastMedicalHistories.remove(busPastMedicalHistoryOnLongClick.position);
                                        recyclerViewAdapter_Content.notifyItemRemoved(busPastMedicalHistoryOnLongClick.position);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progressDialog.dismiss();
                                NothingToShow.showNothingToShow(pastMedicalHistories, recyclerView_Content, nothingToShow);
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
                    params.put("action", "deletePastMedicalHistory");
                    params.put("device", "mobile");
                    params.put("id", String.valueOf(busPastMedicalHistoryOnLongClick.pastMedicalHistory.id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    void setActivityUpdate(Bus_PastMedicalHistory_OnLongClick busPastMedicalHistoryOnLongClick) {
        intent = new Intent(this, Past_Medical_History_Form.class);
        intent.putExtra("patient", patient);
        intent.putExtra("viewer", viewer);
        intent.putExtra("pastMedicalHistory", busPastMedicalHistoryOnLongClick.pastMedicalHistory);
        startActivityForResult(intent, 1);
    }

    @Subscribe
    public void onLongClickItem(Bus_PastMedicalHistory_OnLongClick busPastMedicalHistoryOnLongClick) {
        if (viewer != null) {
            if (busPastMedicalHistoryOnLongClick.pastMedicalHistory.userDataId == viewer.user_id) {
                action_AlertDialog(busPastMedicalHistoryOnLongClick);
            }
        } else {
            action_AlertDialog(busPastMedicalHistoryOnLongClick);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                intent = new Intent(this, Past_Medical_History_Form.class);
                intent.putExtra("patient", patient);
                intent.putExtra("viewer", viewer);
                startActivityForResult(intent, 1);
                break;
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
