package com.example.kinit.e_medicalrecord.Fragments.Surgery;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.BusStation.Pop_BackStack;
import com.example.kinit.e_medicalrecord.Classes.Viewer;
import com.example.kinit.e_medicalrecord.Classes.DatePickerFragment;
import com.example.kinit.e_medicalrecord.Enum.Medical_Transaction;
import com.example.kinit.e_medicalrecord.Enum.Query_Type;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Fragment_Update_Surgical_History extends Fragment implements View.OnClickListener {
    //View
    View rootView;

    //Primitive Data Types
    String surgeryTitle, surgeryDate;
    int surgeryId, patient_id, user_id;
    String patient_name, attachName;

    //Enum
    Query_Type queryType;

    //Widgets
    TextInputEditText et_surgeryTitle, et_surgeryDate;
    TextView tv_attach;
    Button btn_save;
    LinearLayout linearLayout;

    //Classes
    Viewer viewer;
    DatePickerFragment datePicker;

    //Utils
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    /*============================
        END OF GLOBAL VARIABLES
     =============================*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            queryType = Query_Type.values()[bundle.getInt("ordinal")];
            patient_name = bundle.getString("patient_name");
            switch (queryType) {
                case INSERT:
                    patient_id = bundle.getInt("patient_id");
                    user_id = bundle.getInt("user_id");
                    break;
                case UPDATE:
                    surgeryId = bundle.getInt("id");
                    surgeryTitle = bundle.getString("title");
                    this.calendar.setTime(new Date(bundle.getString("date")));
                    attachName = bundle.getString("attachName");
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_update_surgical_history, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        //Button, TextView, and EditText
        et_surgeryTitle = (TextInputEditText) rootView.findViewById(R.id.et_surgeryTitle);
        et_surgeryDate = (TextInputEditText) rootView.findViewById(R.id.et_surgeryDate);
        et_surgeryDate.setOnClickListener(this);
        setCalendar(calendar);

        tv_attach = (TextView) rootView.findViewById(R.id.tv_attach);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout);

        datePicker = new DatePickerFragment();
        datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if(view.isShown()) {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    setCalendar(calendar);
                    Log.d("error", calendar.getTime().toString());
                }
            }
        });

        btn_save = (Button) rootView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        if (queryType == Query_Type.UPDATE) {
            //Set Text of EditText
            et_surgeryTitle.setText(surgeryTitle);
            linearLayout.setVisibility(View.VISIBLE);
            tv_attach.setText(attachName);
        }
        et_surgeryTitle.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                String title = et_surgeryTitle.getText().toString().trim(), date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                if (isTitleApplicable(title)) {
                    saveData(title, date);
                }
                break;
            case R.id.et_surgeryDate:
                datePicker.show(getFragmentManager(), "DatePicker");
                datePicker.setCurrentDate(calendar);
                break;
        }
    }

    void setCalendar(Calendar calendar){
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        et_surgeryDate.setText(simpleDateFormat.format(calendar.getTime()));
    }

    void saveData(final String title, final String datePerformed) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    if (jsonObject.getString("code").equals("successful")) {
                                        BusStation.getBus().post(new Pop_BackStack(Medical_Transaction.UPDATE_SURGICAL_HISTORY));
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

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    switch (queryType) {
                        case INSERT:
                            params.put("action", "insertSurgicalHistory");
                            params.put("patient_id", String.valueOf(patient_id));
                            if (viewer != null) {
                                params.put("user_data_id", String.valueOf(viewer.user_id));
                            } else {
                                params.put("user_data_id", String.valueOf(user_id));
                            }
                            break;
                        case UPDATE:
                            params.put("action", "updateSurgicalHistory");
                            params.put("id", String.valueOf(surgeryId));
                            params.put("transaction", "update");
                            break;
                    }
                    params.put("device", "mobile");
                    params.put("title", title);
                    params.put("date_performed", datePerformed);
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isTitleApplicable(String title) {
        if (title.length() > 0) {
            return true;
        } else {
            et_surgeryTitle.setError("Required Field!");
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (queryType) {
            case INSERT:
                BusStation.getBus().post(new Bus_ToolbarTitle("New Surgery", patient_name));
                break;
            case UPDATE:
                BusStation.getBus().post(new Bus_ToolbarTitle("Update Surgery", surgeryTitle));
                break;
        }
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }
}
