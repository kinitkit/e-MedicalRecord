package com.example.kinit.e_medicalrecord.Fragments.Past_Medical_History;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Resume_Fragment;
import com.example.kinit.e_medicalrecord.BusStation.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.BusStation.Pop_BackStack;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Enum.Medical_Transaction;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Fragment_Update_Past_Medical_History extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    //View
    View rootView;

    //Primitive Data Types
    int patient_id, year;
    String column_name, columnYr_name, medicalCondition;
    boolean isMedicalHistory;

    //Widgets
    CheckBox cb_medicalCondition;
    TextInputEditText et_medicalCondition;
    Button btn_save;

    //App
    ProgressDialog progressDialog;

    //Classes
    Custom_AlertDialog alertDialog;

    /*============================
        END OF GLOBAL VARIABLES
     =============================*/

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            medicalCondition = bundle.getString("medical_condition");
            year = bundle.getInt("year", 0);
            isMedicalHistory = bundle.getBoolean("isMedicalHistory", false);
            patient_id = bundle.getInt("patient_id", 0);
            column_name = bundle.getString("column_name");
            columnYr_name = bundle.getString("columnYr_name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_update_past_medical_history, container, false);
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

        cb_medicalCondition = (CheckBox) rootView.findViewById(R.id.cb_medicalCondition); //Initialize Widgets
        et_medicalCondition = (TextInputEditText) rootView.findViewById(R.id.et_medicalCondition_yr);
        btn_save = (Button) rootView.findViewById(R.id.btn_save);

        cb_medicalCondition.setChecked(isMedicalHistory); //Assign to Widgets
        cb_medicalCondition.setText(medicalCondition);
        et_medicalCondition.setText(String.valueOf(year));
        cb_medicalCondition.setOnCheckedChangeListener(this);
        btn_save.setOnClickListener(this);

        if (isMedicalHistory) {
            et_medicalCondition.setFocusableInTouchMode(true);
            et_medicalCondition.requestFocus();
        }
    }

    void saveData() {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.getString("code").equals("successful")) {
                                    Log.d("error", "pop1");
                                    BusStation.getBus().post(new Pop_BackStack(Medical_Transaction.UPDATE_PAST_MEDICAL_HISTORY));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            alertDialog.show("Error", "Please try again later.");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "editPastMedicalHistory");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient_id));
                    params.put("column_name", column_name);
                    params.put("isChecked", String.valueOf(cb_medicalCondition.isChecked()));
                    params.put("columnYr_name", columnYr_name);
                    params.put("year", String.valueOf(et_medicalCondition.getText()));
                    return params;
                }
            };
            Custom_Singleton.getInstance(this.getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            progressDialog.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                String yr_str = String.valueOf(et_medicalCondition.getText()).trim();
                if(!yr_str.isEmpty()) {
                    int yr = Integer.parseInt(String.valueOf(et_medicalCondition.getText()));
                    if ((cb_medicalCondition.isChecked() && yr >= 1900 && yr <= Calendar.getInstance().get(Calendar.YEAR)) ||
                            !cb_medicalCondition.isChecked()) {
                        progressDialog.setMessage("Processing...");
                        progressDialog.show();
                        saveData();
                    } else {
                        et_medicalCondition.setError("Invalid Year");
                    }
                } else {
                    et_medicalCondition.setError("Required Field");
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_medicalCondition:
                if (isChecked) {
                    et_medicalCondition.requestFocus();
                    et_medicalCondition.setFocusableInTouchMode(true);
                } else {
                    et_medicalCondition.setText("0");
                    et_medicalCondition.setFocusable(false);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BusStation.getBus().post(new Bus_ToolbarTitle("Update Past Medical History", medicalCondition));
    }
}
