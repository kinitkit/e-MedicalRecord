package com.example.kinit.e_medicalrecord.My_Physician.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.General.Adapters.RecyclerView.RecyclerViewAdapter_SearchMyPhysician;
import com.example.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.My_Physician.Bus.Bus_Add_Physician;
import com.example.kinit.e_medicalrecord.My_Physician.Bus.Bus_Search_Physician;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressBar;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.General.Classes.General.NothingToShow;
import com.example.kinit.e_medicalrecord.My_Physician.Class.Physician_List;
import com.example.kinit.e_medicalrecord.General.Enum.My_Physician_Button_Mode;
import com.example.kinit.e_medicalrecord.General.Exception.Codes;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.General.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment_Search_Physician extends Fragment {
    //View
    View rootView;

    String name;
    int user_id, patient_id, position;

    ArrayList<Physician_List> physicianLists;

    //Widgets
    RecyclerView recyclerView_Content;
    RecyclerViewAdapter_SearchMyPhysician recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;

    //App
    Custom_ProgressDialog progressDialog;
    Custom_ProgressBar progressBar;
    LinearLayout nothingToShow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_physician, container, false);
        recyclerView_Content = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nothingToShow = (LinearLayout) rootView.findViewById(R.id.nothingToShow);
        progressDialog = new Custom_ProgressDialog(getActivity());
        progressBar = new Custom_ProgressBar(getActivity());
    }

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_SearchMyPhysician(physicianLists, My_Physician_Button_Mode.ADD);
        recyclerViewLayoutM_Content = new LinearLayoutManager(getActivity());
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
    }

    void fetchData() {
        physicianLists = new ArrayList<>();
        try {
            progressBar.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL_MY_PHYSICIAN,
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
                                            physicianLists.add(new Physician_List(jsonArray.getJSONObject(x)));
                                        }
                                        loadToRecyclerView();
                                    } else if (jsonObject.getString("code").equals("empty")) {
                                        loadToRecyclerView();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progressBar.hide();
                                NothingToShow.showNothingToShow(physicianLists, recyclerView_Content, nothingToShow);
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
                    params.put("action", "searchMyPhysician");
                    params.put("device", "mobile");
                    params.put("user_id", String.valueOf(user_id));
                    params.put("name", name);
                    params.put("patient_id", String.valueOf(patient_id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            progressBar.hide();
        }
    }

    void addPhysician(final int medicalStaffId) {
        progressDialog.show("Loading...");
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL_MY_PHYSICIAN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("exception")) {
                                    if (jsonObject.getString("exception").contains(Codes.SQLSTATE23000)) {
                                        Toast.makeText(getActivity(), "This physician is already in your list", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (jsonObject.has("code")) {
                                    if (jsonObject.getString("code").equals("success")) {
                                        recyclerViewAdapter_Content.removeItem(position);
                                        Toast.makeText(getActivity(), "Physician added", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progressDialog.dismiss();
                                NothingToShow.showNothingToShow(physicianLists, recyclerView_Content, nothingToShow);
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
                    params.put("action", "addMyPhysician");
                    params.put("device", "mobile");
                    params.put("medical_staff_id", String.valueOf(medicalStaffId));
                    params.put("patient_id", String.valueOf(patient_id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
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

    @Subscribe
    public void isSearchClicked(Bus_Search_Physician busSearchPhysician) {
        this.name = busSearchPhysician.name;
        this.user_id = busSearchPhysician.user_id;
        this.patient_id = busSearchPhysician.patient_id;
        fetchData();
    }

    @Subscribe
    public void onClickAdd(Bus_Add_Physician busAddPhysician) {
        this.position = busAddPhysician.position;
        addPhysician(busAddPhysician.physicianList.medicalStaffId);
    }
}
