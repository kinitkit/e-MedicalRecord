package com.example.kinit.e_medicalrecord.Fragments.Medical_Prescription;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Adapters.RecyclerViewAdapter_MedicalPrescription;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Open_Edit_MedicalPrescription;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Resume_Fragment;
import com.example.kinit.e_medicalrecord.BusStation.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.Classes.Medical_Prescription;
import com.example.kinit.e_medicalrecord.Classes.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Medical_Prescription extends Fragment implements View.OnClickListener{
    //View
    View rootView;

    //Primitive Data Types
    int patient_id;
    String patient_name;

    //Classes
    Medical_Prescription[] medicalPrescriptions;
    Viewer viewer;

    //Widgets
    //RecyclerView
    RecyclerView recyclerView_Content;
    RecyclerView.Adapter recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;
    //FAB
    FloatingActionButton btn_add;

    /*============================
        END OF GLOBAL VARIABLES
     =============================*/

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            patient_id = bundle.getInt("patient_id");
            patient_name = bundle.getString("patient_name");
        }
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
        btn_add = (FloatingActionButton) rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        recyclerView_Content = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        fetchData();
    }

    void fetchData() {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    if (jsonObject.getString("code").equals("successful")) {
                                        JSONArray jsonArray = rootJsonArray.getJSONArray(1);
                                        int jsonArrayLength = rootJsonArray.getJSONArray(1).length();
                                        medicalPrescriptions = new Medical_Prescription[jsonArrayLength];

                                        for (int x = 0; x < jsonArrayLength; x++) {
                                            jsonObject = jsonArray.getJSONObject(x);
                                            medicalPrescriptions[x] = new Medical_Prescription();
                                            medicalPrescriptions[x].medicalPrescription_id = Integer.parseInt(jsonObject.getString("id"));
                                            medicalPrescriptions[x].patient_id = Integer.parseInt(jsonObject.getString("patient_id"));
                                            medicalPrescriptions[x].user_data_id = Integer.parseInt(jsonObject.getString("user_data_id"));
                                            medicalPrescriptions[x].clinic_name = jsonObject.getString("clinic_name");
                                            medicalPrescriptions[x].physicianName = jsonObject.getString("physician_name");
                                            medicalPrescriptions[x].setDateAndTime(jsonObject.getString("date_prescribed"));
                                        }
                                        loadToRecyclerView();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getMedicalPrescription");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient_id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_MedicalPrescription(medicalPrescriptions);
        recyclerViewLayoutM_Content = new LinearLayoutManager(getActivity());
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    void setToolbarTitle() {
        BusStation.getBus().post(new Bus_ToolbarTitle("Medical Prescription", patient_name));
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
    public void onResume(){
        super.onResume();
        BusStation.getBus().register(this);
        setToolbarTitle();
    }
    @Override
    public void onPause(){
        super.onPause();
        BusStation.getBus().unregister(this);
    }

    @Subscribe
    public void resume(Bus_Resume_Fragment resumeMedicalPrescription){
        setToolbarTitle();
        fetchData();
    }
}
