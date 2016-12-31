package com.example.kinit.e_medicalrecord.Fragments.Medical_Prescription;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.RecyclerViewAdapter_MedicalPrescription;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.General.Bus_Finish_Activity;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Open_Edit_MedicalPrescription;
import com.example.kinit.e_medicalrecord.BusStation.General.Bus_Resume_Fragment;
import com.example.kinit.e_medicalrecord.BusStation.General.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.Medical_Prescription.Medical_Prescription;
import com.example.kinit.e_medicalrecord.Classes.User.Patient;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Medical_Prescription extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    //View
    View rootView;

    //Classes
    Medical_Prescription[] medicalPrescriptions;
    Viewer viewer;
    Patient patient;
    Custom_ProgressDialog progressDialog;

    //Widgets
    //RecyclerView
    RecyclerView recyclerView_Content;
    RecyclerView.Adapter recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;
    //FAB
    FloatingActionButton btn_add;
    SwipeRefreshLayout swipeRefreshLayout;

    /*============================
        END OF GLOBAL VARIABLES
     =============================*/

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_medical_prescription, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        progressDialog = new Custom_ProgressDialog(getActivity());

        btn_add = (FloatingActionButton) rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        recyclerView_Content = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerViewLayoutM_Content = new LinearLayoutManager(getActivity());

        fetchData();
    }

    void btn_initializer(boolean isbuttonViewable){
        if (isbuttonViewable) {
            btn_add.setVisibility(View.VISIBLE);
        } else {
            btn_add.setVisibility(View.GONE);
        }
    }

    void fetchData() {
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
                                        medicalPrescriptions = new Medical_Prescription[jsonArrayLength];
                                        for (int x = 0; x < jsonArrayLength; x++) {
                                            jsonObject = jsonArray.getJSONObject(x);
                                            //medicalPrescriptions[x] = new Medical_Prescription();
                                            //medicalPrescriptions[x].medicalPrescription_id = Integer.parseInt(jsonObject.getString("id"));
                                            //medicalPrescriptions[x].patient_id = Integer.parseInt(jsonObject.getString("patient_id"));
                                            //medicalPrescriptions[x].user_data_id = Integer.parseInt(jsonObject.getString("user_data_id"));
                                            //medicalPrescriptions[x].clinic_name = jsonObject.getString("clinic_name");
                                            //medicalPrescriptions[x].physicianName = jsonObject.getString("physician_name");
                                            //medicalPrescriptions[x].setDateAndTime(jsonObject.getString("date_prescribed"));
                                        }
                                        loadToRecyclerView();btn_initializer(isButtonViewable);
                                    } else if(jsonObject.getString("code").equals("empty")){
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
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    void loadToRecyclerView() {
        //recyclerViewAdapter_Content = new RecyclerViewAdapter_MedicalPrescription(medicalPrescriptions);
        //recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        //recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
        //progressDialog.dismiss();
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    void setToolbarTitle() {
        BusStation.getBus().post(new Bus_ToolbarTitle("Medical Prescription", patient.name));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                BusStation.getBus().post(new Bus_Open_Edit_MedicalPrescription());
                break;
        }
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

    @Subscribe
    public void resume(Bus_Resume_Fragment resumeMedicalPrescription) {
        setToolbarTitle();
        fetchData();
    }

    @Override
    public void onRefresh() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        fetchData();
    }
}
