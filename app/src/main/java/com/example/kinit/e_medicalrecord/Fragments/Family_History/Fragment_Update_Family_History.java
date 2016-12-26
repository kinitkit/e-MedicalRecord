package com.example.kinit.e_medicalrecord.Fragments.Family_History;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.BusStation.Pop_BackStack;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Enum.Medical_Transaction;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Update_Family_History extends Fragment implements View.OnClickListener {
    //View
    View rootView;

    //Primitive Data Types
    int patient_id;
    boolean isGrandP, isParent, isSibling, isChild;
    String key_grandP, key_parent, key_sibling, key_child, patient_name;

    //Widgets
    CheckBox cb_grandP, cb_parent, cb_sibling, cb_child;
    Button btn_save;

    //Classes
    ProgressDialog progressDialog;
    Custom_AlertDialog alertDialog;

    /*============================
        END OF GLOBAL VARIABLES
     =============================*/

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            patient_name = bundle.getString("patient_name");
            patient_id = bundle.getInt("patient_id", 0);
            isGrandP = bundle.getBoolean("isGrandP", false);
            isParent = bundle.getBoolean("isParent", false);
            isSibling = bundle.getBoolean("isSibling", false);
            isChild = bundle.getBoolean("isChild", false);
            key_grandP = bundle.getString("key_grandP");
            key_parent = bundle.getString("key_parent");
            key_sibling = bundle.getString("key_sibling");
            key_child = bundle.getString("key_child");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_update_family_history, container, false);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        progressDialog = new ProgressDialog(getActivity());
        alertDialog = new Custom_AlertDialog(getActivity());
        //CheckBox
        cb_grandP = (CheckBox) rootView.findViewById(R.id.cb_grandP);
        cb_parent = (CheckBox) rootView.findViewById(R.id.cb_parent);
        cb_sibling = (CheckBox) rootView.findViewById(R.id.cb_sibling);
        cb_child = (CheckBox) rootView.findViewById(R.id.cb_child);

        //Set CheckBox Check
        cb_grandP.setChecked(isGrandP);
        cb_parent.setChecked(isParent);
        cb_sibling.setChecked(isSibling);
        cb_child.setChecked(isChild);

        //Button
        btn_save = (Button)rootView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                editFamilyHistory();
                break;
        }
    }

    void editFamilyHistory(){
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if(jsonObject.getString("code").equals("successful")){
                                    BusStation.getBus().post(new Pop_BackStack(Medical_Transaction.UPDATE_FAMILY_HISTORY));
                                } else {
                                    alertDialog.show("Error", "Please try again.");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                progressDialog.cancel();
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
                    params.put("action", "editFamilyHistoryData");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient_id));
                    params.put("key_grandP", key_grandP);
                    params.put("key_parent", key_parent);
                    params.put("key_sibling", key_sibling);
                    params.put("key_child", key_child);
                    params.put("isGrandP", String.valueOf(cb_grandP.isChecked()));
                    params.put("isParent", String.valueOf(cb_parent.isChecked()));
                    params.put("isSibling", String.valueOf(cb_sibling.isChecked()));
                    params.put("isChild", String.valueOf(cb_child.isChecked()));
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e){
            progressDialog.cancel();
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BusStation.getBus().post(new Bus_ToolbarTitle("Update Family History", patient_name));
    }

}
