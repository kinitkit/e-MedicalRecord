package com.example.kinit.e_medicalrecord.Fragments.Profile;


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
import com.example.kinit.e_medicalrecord.Adapters.RecyclerViewAdapter_Search;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Search_Item;
import com.example.kinit.e_medicalrecord.Classes.Search_Item;
import com.example.kinit.e_medicalrecord.Enum.Mode;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Search extends Fragment {
    //View
    View rootView;

    String name;
    int user_id;

    //Classes
    Search_Item search_item;
    Mode mode;

    //Widgets
    RecyclerView recyclerView_Content;
    RecyclerView.Adapter recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView_Content = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        search_item = new Search_Item();
    }

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_Search(search_item);
        recyclerViewLayoutM_Content = new LinearLayoutManager(getActivity());
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
    }

    void fetch_data() {
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
                                    if (jsonObject.getString("code").equals("success")) {
                                        JSONArray jsonArray = rootJsonArray.getJSONArray(1);
                                        int jsonArrayLength = rootJsonArray.getJSONArray(1).length();
                                        for (int x = 0; x < jsonArrayLength; x++) {
                                            jsonObject = jsonArray.getJSONObject(x);
                                            search_item.user_id.add(Integer.parseInt(jsonObject.getString("id")));
                                            search_item.setName(jsonObject.getString("first_name"),
                                                    jsonObject.getString("middle_name"), jsonObject.getString("last_name"));
                                            if(jsonObject.has("user_type")) {
                                                search_item.setMedicalType(jsonObject.getString("user_type"));
                                                search_item.medicalStaff_id.add(search_item.setId(jsonObject.getString("medicalStaff_id")));
                                                search_item.patient_id.add(0);
                                            } else {
                                                search_item.setMedicalType("null");
                                                search_item.medicalStaff_id.add(0);
                                                search_item.patient_id.add(search_item.setId(jsonObject.getString("patient_id")));
                                            }
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
                    params.put("action", "search");
                    params.put("device", "mobile");
                    params.put("user_id", String.valueOf(user_id));
                    params.put("name", name);
                    params.put("mode", mode.name());
                    return params;
                }
            };

            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
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

    @Subscribe
    public void isSearchClicked(Bus_Search_Item busSearchItem) {
        this.mode = busSearchItem.mode;
        this.name = busSearchItem.name;
        this.user_id = busSearchItem.user_id;
        search_item = new Search_Item();
        fetch_data();
    }
}
