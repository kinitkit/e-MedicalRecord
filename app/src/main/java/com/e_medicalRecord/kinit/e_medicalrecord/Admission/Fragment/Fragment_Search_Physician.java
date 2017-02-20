package com.e_medicalRecord.kinit.e_medicalrecord.Admission.Fragment;


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
import com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView.RecyclerViewAdapter_Tagged_MedPrescription;
import com.e_medicalRecord.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Bus.Bus_Add_Physician;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Bus.Bus_Search_Tagged_MedicalPrescription;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressBar;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressDialog;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.General.NothingToShow;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Class.Tagged_Physician_List;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Enum.My_Physician_Button_Mode;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Exception.Codes;
import com.e_medicalRecord.kinit.e_medicalrecord.R;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Request.UrlString;
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
    int user_id, position;
    int admission_id;

    ArrayList<Tagged_Physician_List> taggedPhysicianLists;

    //Widgets
    RecyclerView recyclerView_Content;
    RecyclerViewAdapter_Tagged_MedPrescription recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;

    //App
    Custom_ProgressDialog progressDialog;
    Custom_ProgressBar progressBar;
    LinearLayout nothingToShow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_physician_admissioin, container, false);
        recyclerView_Content = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new Custom_ProgressDialog(getActivity());
        progressBar = new Custom_ProgressBar(getActivity());
        nothingToShow = (LinearLayout) rootView.findViewById(R.id.nothingToShow);
    }

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_Tagged_MedPrescription(taggedPhysicianLists, My_Physician_Button_Mode.ADD);
        recyclerViewLayoutM_Content = new LinearLayoutManager(getActivity());
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
    }

    void fetchData() {
        taggedPhysicianLists = new ArrayList<>();

        try {
            progressBar.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL_ADMISSION,
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
                                    } else if (jsonObject.getString("code").equals("empty")) {
                                        loadToRecyclerView();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progressBar.hide();
                                NothingToShow.showNothingToShow(taggedPhysicianLists, recyclerView_Content, nothingToShow);
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
                    params.put("action", "searchAdmissionPhysicians");
                    params.put("device", "mobile");
                    params.put("user_id", String.valueOf(user_id));
                    params.put("name", name);
                    params.put("admission_id", String.valueOf(admission_id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            progressBar.hide();
        }
    }

    void addPhysician(final int medical_staff_id) {
        progressDialog.show("Loading...");
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL_ADMISSION,
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
                                        taggedPhysicianLists.remove(position);
                                        recyclerViewAdapter_Content.notifyItemRemoved(position);
                                        Toast.makeText(getActivity(), "Physician added", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progressDialog.dismiss();
                                NothingToShow.showNothingToShow(taggedPhysicianLists, recyclerView_Content, nothingToShow);
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
                    params.put("action", "addAdmissionPhysician");
                    params.put("device", "mobile");
                    params.put("medical_staff_id", String.valueOf(medical_staff_id));
                    params.put("admission_id", String.valueOf(admission_id));
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
    public void isSearchClicked(Bus_Search_Tagged_MedicalPrescription busSearchTaggedMedicalPrescription) {
        this.name = busSearchTaggedMedicalPrescription.name;
        this.user_id = busSearchTaggedMedicalPrescription.user_id;
        this.admission_id = busSearchTaggedMedicalPrescription.id;
        fetchData();
    }

    @Subscribe
    public void onClickAdd(Bus_Add_Physician busAddPhysician) {
        this.position = busAddPhysician.position;
        addPhysician(busAddPhysician.taggedPhysicianList.medical_staff_id);
    }

}
