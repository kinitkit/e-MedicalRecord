package com.example.kinit.e_medicalrecord.Fragments.Medical_Prescription;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Adapters.RecyclerViewAdapter_Drug_List;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.Classes.Drug_List;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Drug_List extends Fragment {
    //View
    View rootView;

    //Primitive Data Types
    int medicalPrescription_id, patient_id;
    String physicianName, clinicName, date;

    //Classes
    Drug_List[] drugLists;

    //Widgets
    //RecyclerView
    RecyclerView recyclerView_Content;
    RecyclerView.Adapter recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;
    TextView tv_physicianName, tv_clinicName, tv_date;

    /*============================
        END OF GLOBAL VARIABLES
     =============================*/

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            patient_id = bundle.getInt("patient_id");
            medicalPrescription_id = bundle.getInt("medicalPrescription_id");
            physicianName = bundle.getString("physician_name");
            clinicName = bundle.getString("clinic_name");
            date = bundle.getString("date");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_drug_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        tv_physicianName = (TextView)  rootView.findViewById(R.id.tv_physicianName);
        tv_physicianName.setText(physicianName);
        tv_clinicName = (TextView)  rootView.findViewById(R.id.tv_clinicName);
        tv_clinicName.setText(clinicName);
        tv_date = (TextView)  rootView.findViewById(R.id.tv_date);
        tv_date.setText(date);

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
                                        drugLists = new Drug_List[jsonArrayLength];
                                        for (int x = 0; x < jsonArrayLength; x++) {
                                            drugLists[x] = new Drug_List();
                                            jsonObject = jsonArray.getJSONObject(x);
                                            drugLists[x].id = Integer.parseInt(jsonObject.getString("id"));
                                            drugLists[x].medical_prescription_id = Integer.parseInt(jsonObject.getString("medical_prescription_id"));
                                            drugLists[x].drug = jsonObject.getString("drug");
                                            drugLists[x].strength = jsonObject.getString("strength");
                                            drugLists[x].dosage = jsonObject.getString("dosage");
                                            drugLists[x].route = jsonObject.getString("route");
                                            drugLists[x].frequency = jsonObject.getString("frequency");
                                            drugLists[x].indication = jsonObject.getString("indication");
                                            drugLists[x].many = Integer.parseInt(jsonObject.getString("how_many"));
                                            drugLists[x].refill = Integer.parseInt(jsonObject.getString("refill"));
                                        }
                                        loadToRecyclerView();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getDrugList");
                    params.put("device", "mobile");
                    params.put("medical_prescription_id", String.valueOf(medicalPrescription_id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_Drug_List(drugLists);
        recyclerViewLayoutM_Content = new LinearLayoutManager(getActivity());
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
    }

    void setToolbarTitle() {
        BusStation.getBus().post(new Bus_ToolbarTitle("Drug List", null));
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbarTitle();
    }
}
