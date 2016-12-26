package com.example.kinit.e_medicalrecord.Fragments.Surgery;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Adapters.RecyclerViewAdapter_Surgery;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Resume_Fragment;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Surgical_History_Item;
import com.example.kinit.e_medicalrecord.BusStation.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.Surgical_History;
import com.example.kinit.e_medicalrecord.Enum.Medical_Transaction;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Surgical_History extends Fragment implements View.OnClickListener {
    //View
    View rootView;

    //Primitive Data Types
    int patient_id;
    String patient_name;

    //Classes
    Surgical_History surgical_history;

    //Dialog
    Custom_AlertDialog alertDialog;

    //Widgets
    //FAB
    FloatingActionButton btn_add;
    //RecyclerView
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_surgical_history, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        alertDialog = new Custom_AlertDialog(getActivity());

        btn_add = (FloatingActionButton) rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
    }

    void fetchData() {
        surgical_history = new Surgical_History();
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.getString("code").equals("successful")) {
                                    JSONArray jsonArray = rootJsonArray.getJSONArray(1);
                                    int jsonArrayLength = rootJsonArray.getJSONArray(1).length();
                                    for (int x = 0; x < jsonArrayLength; x++) {
                                        jsonObject = jsonArray.getJSONObject(x);
                                        surgical_history.setSurgicalIdItem(jsonObject.getInt("id"));
                                        surgical_history.setSurgicalTitleItem(jsonObject.getString("surgery_title"));
                                        surgical_history.setSurgicalDateItem(jsonObject.getString("date_performed"));
                                        surgical_history.setSurgicalAttachName(jsonObject.getString("first_name"), jsonObject.getString("middle_name"), jsonObject.getString("last_name"));
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
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getSurgicalHistoryData");
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
        //RecyclerView
        recyclerView_Content = (RecyclerView) rootView.findViewById(R.id.rv_surgical_history);
        recyclerViewAdapter_Content = new RecyclerViewAdapter_Surgery(surgical_history, patient_id);
        recyclerViewLayoutM_Content = new LinearLayoutManager(getActivity());
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
        setupItemTouchHelper();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                BusStation.getBus().post(new Bus_Surgical_History_Item(Medical_Transaction.INSERT_SURGICAL_HISTORY, patient_id));
                break;
        }
    }

    void setupItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                RecyclerViewAdapter_Surgery adapter_surgery = (RecyclerViewAdapter_Surgery) recyclerView_Content.getAdapter();
                deleteItemConfirmation(swipedPosition, adapter_surgery);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView_Content);
    }

    void deleteItemConfirmation(final int position, final RecyclerViewAdapter_Surgery adapter_surgery) {
        alertDialog.builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItem(position, adapter_surgery);
            }
        });
        alertDialog.builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter_surgery.remove(position, false);
                dialog.dismiss();
            }
        });
        alertDialog.show("Confirm delete", "Are you sure you want to permanently delete the item?");
    }

    void deleteItem(final int position, final RecyclerViewAdapter_Surgery adapter_surgery) {
        alertDialog.dismiss();
        alertDialog.setPositiveButton();
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.has("error")) {
                                    adapter_surgery.remove(position, false);
                                    alertDialog.show("Error", jsonObject.getString("error"));
                                } else if (jsonObject.has("code")) {
                                    if (jsonObject.getString("code").equals("successful")) {
                                        adapter_surgery.remove(position, true);
                                    } else {
                                        adapter_surgery.remove(position, false);
                                        alertDialog.show("Error", "Please try again.");
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            alertDialog.show("Error", error.getMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "updateSurgicalHistory");
                    params.put("device", "mobile");
                    params.put("id", String.valueOf(surgical_history.getSurgicalIdItem(position)));
                    params.put("transaction", "delete");
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            alertDialog.show("Error", e.getMessage());
        }
    }

    void setToolbarTitle() {
        BusStation.getBus().post(new Bus_ToolbarTitle("Surgical History", patient_name));
    }

    @Override
    public void onResume() {
        super.onResume();
        BusStation.getBus().register(this);
        setToolbarTitle();
        surgical_history = new com.example.kinit.e_medicalrecord.Classes.Surgical_History();
        fetchData();
    }

    @Override
    public void onPause() {
        super.onPause();
        BusStation.getBus().unregister(this);
    }

    @Subscribe
    public void resume(Bus_Resume_Fragment resumeSurgicalHistory) {
        setToolbarTitle();
        fetchData();
    }

}
