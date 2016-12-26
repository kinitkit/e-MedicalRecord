package com.example.kinit.e_medicalrecord.Fragments.Past_Medical_History;


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
import com.example.kinit.e_medicalrecord.Adapters.RecyclerViewAdapter_PastMedicalHistory;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Resume_Fragment;
import com.example.kinit.e_medicalrecord.BusStation.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.Classes.Past_Medical_History;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Past_Medical_History extends Fragment {
    //Views
    View rootView;

    //Primitive Data Types
    int patient_id;
    String patient_name;
    boolean isPaused;

    //Classes
    Past_Medical_History past_medical_history;

    //Widgets
    RecyclerView recyclerView_Content;
    RecyclerView.Adapter recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;

    /*============================
        END OF GLOBAL VARIABLES
     =============================*/

    @Override
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
        rootView = inflater.inflate(R.layout.fragment_past_medical_history, container, false);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init(){
        isPaused = false;

        past_medical_history = new Past_Medical_History();
        fetchData();
    }

    void fetchData(){
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("error", response);
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if(jsonObject.getString("code").equals("successful")){
                                    jsonObject = jsonArray.getJSONObject(1);
                                    int counter = 0;
                                    for (int x = 0; x < past_medical_history.length(); x++){
                                        past_medical_history.setMedicalCondition_data(x, "1".equals(jsonObject.getString(past_medical_history.getKey(counter))));
                                        ++counter;
                                        past_medical_history.setYear(x, jsonObject.getInt(past_medical_history.getKey(counter)));
                                        ++counter;
                                    }
                                    loadToRecyclerView();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getPastMedicalHistoryData");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient_id));
                    return params;
                }
            };

            Custom_Singleton.getInstance(this.getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void loadToRecyclerView(){
        //RecyclerView
        recyclerView_Content = (RecyclerView)rootView.findViewById(R.id.rv_pastMedicalHistory);
        recyclerViewAdapter_Content = new RecyclerViewAdapter_PastMedicalHistory(past_medical_history, patient_id);
        recyclerViewLayoutM_Content = new LinearLayoutManager(getActivity());
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
    }

    void setToolbarTitle() {
        BusStation.getBus().post(new Bus_ToolbarTitle("Past Medical History", patient_name));
    }

    @Override
    public void onPause(){
        super.onPause();
        isPaused = true;
        BusStation.getBus().unregister(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(isPaused){
            fetchData();
            isPaused = false;
        }
        BusStation.getBus().register(this);
        setToolbarTitle();
    }

    @Subscribe
    public void resume(Bus_Resume_Fragment busResumeFragment){
        setToolbarTitle();
        fetchData();
    }

}
