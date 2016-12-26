package com.example.kinit.e_medicalrecord.Fragments.Laboratory;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.General.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.BusStation.General.Pop_BackStack;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.DatePickerFragment;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Fragment_Lab_Chemistry extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    //View
    View rootView;

    //Primitive Data Types
    int patient_id, user_data_id;

    //Classes
    Custom_AlertDialog alertDialog;
    Viewer viewer;
    DatePickerFragment datePickerFragment;

    //Widgets
    AppCompatSpinner spinner_lab_search;
    ArrayAdapter arrayAdapter_search;
    ArrayList<EditText> editTexts;
    Button btn_save;

    //Utils
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            patient_id = bundle.getInt("patient_id", 0);
            user_data_id = bundle.getInt("user_data_id", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_lab_chemistry, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        alertDialog = new Custom_AlertDialog(getActivity());

        spinner_lab_search = (AppCompatSpinner) rootView.findViewById(R.id.spinner_lab_search);
        arrayAdapter_search = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_lab_chem, R.layout.support_simple_spinner_dropdown_item);
        spinner_lab_search.setAdapter(arrayAdapter_search);
        spinner_lab_search.setOnItemSelectedListener(this);

        editTexts = new ArrayList<>();
        editTexts.add((EditText) rootView.findViewById(R.id.et_physicianName));
        editTexts.add((EditText) rootView.findViewById(R.id.et_laboratory));
        editTexts.add((EditText) rootView.findViewById(R.id.et_date));
        editTexts.get(2).setOnClickListener(this);
        editTexts.add((EditText) rootView.findViewById(R.id.et_fbs));
        editTexts.add((EditText) rootView.findViewById(R.id.et_creatinine));
        editTexts.add((EditText) rootView.findViewById(R.id.et_cholesterol));
        editTexts.add((EditText) rootView.findViewById(R.id.et_triglycerides));
        editTexts.add((EditText) rootView.findViewById(R.id.et_hdl));
        editTexts.add((EditText) rootView.findViewById(R.id.et_ldl));
        editTexts.add((EditText) rootView.findViewById(R.id.et_uricAcid));
        editTexts.add((EditText) rootView.findViewById(R.id.et_sgpt));
        editTexts.add((EditText) rootView.findViewById(R.id.et_sodium));
        editTexts.add((EditText) rootView.findViewById(R.id.et_potassium));
        editTexts.add((EditText) rootView.findViewById(R.id.et_calcium));
        editTexts.add((EditText) rootView.findViewById(R.id.et_remarks));

        btn_save = (Button) rootView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        calendar = Calendar.getInstance();
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (view.isShown()) {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    setCalendar(calendar);
                }
            }
        });
        setCalendar(calendar);
        if (viewer != null) {
            editTexts.get(0).setFocusableInTouchMode(false);
            editTexts.get(0).setKeyListener(null);
            editTexts.get(0).setText(viewer.name);
        }
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        editTexts.get(position).requestFocus();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                verifyData();
                break;
            case R.id.et_date:
                datePickerFragment.show(getFragmentManager(), "DatePicker");
                datePickerFragment.setCurrentDate(calendar);
                break;
        }
    }

    void verifyData() {
        int isLabTestEmpty = 1, isLabNameEmpty = 1;
        String[] inps = new String[editTexts.size()];

        for (int x = 0; x < editTexts.size(); x++) {
            inps[x] = editTexts.get(x).getText().toString().trim();
            if (x == 0 || x == 1) {
                if (inps[x].isEmpty()) {
                    editTexts.get(x).requestFocus();
                    editTexts.get(x).setError("Required Field!");
                } else {
                    isLabNameEmpty = 0;
                }
            } else if (x > 2) {
                if (!inps[x].isEmpty()) {
                    isLabTestEmpty = 0;
                }
            }
        }

        if (isLabNameEmpty == 0 && isLabTestEmpty == 0) {
            insertData(inps);
        } else if (isLabTestEmpty == 1) {
            alertDialog.show("Error", "At least 1 field of the lab tests must be filled up.");
        }
    }

    void insertData(final String[] inps) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("error", response);
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    if (jsonObject.getString("code").equals("successful")) {
                                        BusStation.getBus().post(new Pop_BackStack());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "insertChem");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient_id));
                    if (viewer == null) {
                        params.put("user_data_id", String.valueOf(user_data_id));
                    } else {
                        params.put("user_data_id", String.valueOf(viewer.user_id));
                    }
                    for (int x = 0; x < inps.length; x++) {
                        if(x == 2){
                            params.put("args[" + (x) + "]", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                        } else {
                            params.put("args[" + (x) + "]", inps[x]);
                        }
                    }
                    return params;
                }
            };

            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setCalendar(Calendar calendar) {
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        editTexts.get(2).setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    public void onResume() {
        super.onResume();
        BusStation.getBus().post(new Bus_ToolbarTitle("New Blood Chemistry", null));
    }
}
