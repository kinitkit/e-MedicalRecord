package com.example.kinit.e_medicalrecord.Fragments.Laboratory;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.RecyclerViewAdapter_Tagged_MedPrescription;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.Medical_Prescription.Tagged_Physician_List;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.Enum.My_Physician_Button_Mode;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment_Tagged_Laboratory extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    //View
    View rootView;

    //Primitive Data Types
    int lab_id, position;
    ArrayList<Tagged_Physician_List> taggedPhysicianLists;
    Viewer viewer;

    //Widgets
    //RecyclerView
    RecyclerView recyclerView_Content;
    RecyclerViewAdapter_Tagged_MedPrescription recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;
    //SwipeRefreshLayout
    SwipeRefreshLayout swipeRefreshLayout;

    //App
    Custom_ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fragment_tagged_laboratory, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        progressDialog = new Custom_ProgressDialog(getActivity());
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView_Content = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        fetchData();
    }

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_Tagged_MedPrescription(taggedPhysicianLists, My_Physician_Button_Mode.REMOVE);
        recyclerViewLayoutM_Content = new LinearLayoutManager(getActivity());
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
        progressDialog.dismiss();
    }

    public void setLab_id(int lab_id){
        this.lab_id = lab_id;
    }

    void fetchData() {
        taggedPhysicianLists = new ArrayList<>();
        progressDialog.show("Loading...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("error", response);
                        try {
                            JSONArray rootJsonArray = new JSONArray(response);
                            JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                            if (jsonObject.has("code")) {
                                if (jsonObject.getString("code").equals("success")) {
                                    JSONArray jsonArray = rootJsonArray.getJSONArray(1);
                                    int jsonArrayLength = jsonArray.length();
                                    for (int x = 0; x < jsonArrayLength; x++) {
                                        taggedPhysicianLists.add(new Tagged_Physician_List(jsonArray.getJSONObject(x)));
                                    }
                                    loadToRecyclerView();
                                }
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

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "laboratoryTaggedPhysician");
                params.put("device", "mobile");
                params.put("lab_id", String.valueOf(lab_id));
                return params;
            }
        };
        Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    void removePhysician(final int id) {
        try {
            progressDialog.show("Loading...");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("error", response);
                            try {
                                taggedPhysicianLists.remove(position);
                                recyclerViewAdapter_Content.notifyItemRemoved(position);
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
                    params.put("action", "removeMedicalPrescriptionPhysician");
                    params.put("device", "mobile");
                    params.put("id", String.valueOf(id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
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
    public void onPause() {
        super.onPause();
        BusStation.getBus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusStation.getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rootView = null;
    }

}
