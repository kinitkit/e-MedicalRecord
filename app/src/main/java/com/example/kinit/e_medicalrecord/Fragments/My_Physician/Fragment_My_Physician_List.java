package com.example.kinit.e_medicalrecord.Fragments.My_Physician;


import android.os.Bundle;
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
import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.RecyclerViewAdapter_SearchMyPhysician;
import com.example.kinit.e_medicalrecord.Classes.My_Physician.Physician_List;
import com.example.kinit.e_medicalrecord.Enum.My_Physician_Button_Mode;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment_My_Physician_List extends Fragment {
    //View
    View rootView;

    //Primitive Data Types
    int patient_id;

    //Classes
    ArrayList<Physician_List> physicianLists;

    //Widgets
    //RecyclerView
    RecyclerView recyclerView_Content;
    RecyclerView.Adapter recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_physician_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        recyclerView_Content = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        fetchData();
    }

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_SearchMyPhysician(physicianLists, My_Physician_Button_Mode.REMOVE);
        recyclerViewLayoutM_Content = new LinearLayoutManager(getActivity());
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    void fetchData() {
        physicianLists = new ArrayList<>();
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
                                    if(jsonObject.getString("code").equals("success")){
                                        JSONArray jsonArray = rootJsonArray.getJSONArray(1);
                                        int jsonArrayLength = jsonArray.length();
                                        for(int x = 0; x < jsonArrayLength; x++){
                                            physicianLists.add(new Physician_List(jsonArray.getJSONObject(x)));
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
                    params.put("action", "getMyPhysicianList");
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

}
