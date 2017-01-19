package com.example.kinit.e_medicalrecord.Activities.Allergy;

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
import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.RecyclerViewAdapter_Allergy;
import com.example.kinit.e_medicalrecord.BusStation.Allergy.Bus_Allergy;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressBar;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
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

public class Allergy extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    //Classes
    Patient patient;
    Viewer viewer;
    Custom_ProgressDialog progressDialog;
    Custom_AlertDialog alertDialog;
    Custom_ProgressBar progressBar;
    ArrayList<com.example.kinit.e_medicalrecord.Classes.Allergy.Allergy> allergies;

    //Widgets
    LinearLayout nothingToShow;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView_Content;
    RecyclerView.Adapter recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;
    FloatingActionButton btn_add;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_allergy);
        init();
    }

    void init() {
        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Allergy List");
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

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_Allergy(allergies);
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
        progressDialog.dismiss();
    }

    void fetchData() {
        allergies = new ArrayList<>();
        try {
            progressBar.show();
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_ALLERGY,
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
                                    if (jsonObject.has("code")) {
                                        if (jsonObject.getString("code").equals("successful")) {
                                            jsonArray = rootJsonArray.getJSONArray(1);
                                            int jsonArrayLength = jsonArray.length();
                                            for (int x = 0; x < jsonArrayLength; x++) {
                                                jsonObject = jsonArray.getJSONObject(x);
                                                allergies.add(new com.example.kinit.e_medicalrecord.Classes.Allergy.Allergy(jsonObject));
                                            }
                                            loadToRecyclerView();
                                            btn_initializer(isButtonViewable);
                                        } else {
                                            loadToRecyclerView();
                                            btn_initializer(isButtonViewable);
                                        }
                                    } else if(jsonObject.has("exception")) {
                                        alertDialog.show("Error", jsonObject.getString("exception"));
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
                    params.put("action", "getAllergies");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient.id));
                    params.put("medical_staff_id", (viewer != null) ? String.valueOf(viewer.medicalStaff_id) : "0");
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressBar.hide();
        }
    }

    void action_AlertDialog(final Bus_Allergy busAllergy) {
        final CharSequence actions[] = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Choose Action");
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        updateItem(busAllergy);
                        break;
                    case 1:
                        alertDialog.builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteData(busAllergy);
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

    void deleteData(final Bus_Allergy busAllergy) {
        progressDialog.show("Deleting...");
        try {
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_ALLERGY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {

                                    if (jsonObject.getString("code").equals("successful")) {
                                        allergies.remove(busAllergy.position);
                                        recyclerViewAdapter_Content.notifyItemRemoved(busAllergy.position);
                                    } else {
                                        alertDialog.show("Error", "An error occurred while we were trying to update your data.");
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
                    params.put("action", "deleteAllergy");
                    params.put("device", "mobile");
                    params.put("id", String.valueOf(busAllergy.allergy.id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    void updateItem(Bus_Allergy busAllergy) {
        intent = new Intent(this, Allergy_Form.class);
        intent.putExtra("patient", patient);
        intent.putExtra("viewer", viewer);
        intent.putExtra("allergy", busAllergy.allergy);
        startActivityForResult(intent, 1);
    }

    void btn_initializer(boolean isButtonViewable) {
        if (isButtonViewable) {
            btn_add.setVisibility(View.VISIBLE);
        } else {
            btn_add.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onLongClickItem(Bus_Allergy busAllergy) {
        if (viewer == null) {
            action_AlertDialog(busAllergy);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                intent = new Intent(this, Allergy_Form.class);
                intent.putExtra("patient", patient);
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
