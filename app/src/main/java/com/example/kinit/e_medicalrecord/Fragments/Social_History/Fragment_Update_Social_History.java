package com.example.kinit.e_medicalrecord.Fragments.Social_History;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

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
import com.example.kinit.e_medicalrecord.Enum.Social_History_Type;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Fragment_Update_Social_History extends Fragment {
    //View
    View rootView;

    //Primitive Data Types
    boolean isCurrentLyUse, isPreviouslyUsed;
    String frequency, header;
    int length, stopped, patient_id;

    //Widgets
    TextView tv_header;
    TextInputEditText et_frequency, et_length, et_stopped;
    CheckBox cb_currentlyUse, cb_previouslyUsed;
    Button btn_save;

    //App
    ProgressDialog progressDialog;
    Custom_AlertDialog custom_alertDialog;

    //KeyListner
    KeyListener keyListener_frequency, keyListener_length, keyListener_stopped;
    //Enum
    Social_History_Type socialHistoryType;

    /*============================
        END OF GLOBAL VARIABLES
     =============================*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            patient_id = bundle.getInt("patient_id");
            header = bundle.getString("header");
            socialHistoryType = Social_History_Type.values()[bundle.getInt("socialType")];
            isCurrentLyUse = bundle.getBoolean("isCurrentLyUse", false);
            isPreviouslyUsed = bundle.getBoolean("isPreviouslyUsed", false);
            frequency = bundle.getString("frequency");
            length = bundle.getInt("length", 0);
            stopped = bundle.getInt("stopped", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_update_social_history, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        //Dialogs
        progressDialog = new ProgressDialog(getActivity());
        custom_alertDialog = new Custom_AlertDialog(getActivity());

        //TextView
        tv_header = (TextView) rootView.findViewById(R.id.tv_medicalCondition_header);
        //EditText
        et_frequency = (TextInputEditText) rootView.findViewById(R.id.et_frequency);
        et_length = (TextInputEditText) rootView.findViewById(R.id.et_length);
        et_stopped = (TextInputEditText) rootView.findViewById(R.id.et_stopped);
        //KeyListner
        keyListener_frequency = et_frequency.getKeyListener();
        keyListener_length = et_length.getKeyListener();
        keyListener_stopped = et_stopped.getKeyListener();
        //CheckBox
        cb_currentlyUse = (CheckBox) rootView.findViewById(R.id.cb_currentlyUse);
        cb_previouslyUsed = (CheckBox) rootView.findViewById(R.id.cb_previouslyUsed);

        cb_currentlyUse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkChanged();
            }
        });
        cb_previouslyUsed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkChanged();
            }
        });

        displayData();

        //Button
        btn_save = (Button) rootView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                String frequency = et_frequency.getText().toString().trim(), str_length = et_length.getText().toString(),
                        str_stopped = et_stopped.getText().toString();
                int length = 0, stopped = 0;
                if (cb_currentlyUse.isChecked() || cb_previouslyUsed.isChecked()) {
                    if (!frequency.isEmpty()) {
                        if (!str_length.isEmpty() && !str_length.equals("0")) {
                            length = Integer.parseInt(str_length);
                            if (!str_stopped.isEmpty() && !str_stopped.equals("0")) {
                                stopped = Integer.parseInt(str_stopped);
                                if (stopped >= 1900 && stopped <= Calendar.getInstance().get(Calendar.YEAR)) {
                                    sendData(frequency, length, stopped);
                                } else {
                                    et_stopped.setError("Invalid Year");
                                }

                            } else {
                                et_stopped.setError("Invalid Year");
                            }
                        } else {
                            et_length.setError("Invalid Length");
                        }
                    } else {
                        et_frequency.setError("Required field");
                    }
                } else {
                    sendData("", length, stopped);
                }
                progressDialog.dismiss();
            }
        });
    }

    void displayData() {
        tv_header.setText(header);
        cb_currentlyUse.setChecked(isCurrentLyUse);
        cb_previouslyUsed.setChecked(isPreviouslyUsed);
        if (isCurrentLyUse || isPreviouslyUsed) {
            et_frequency.setKeyListener(keyListener_frequency);
            et_frequency.setFocusable(true);
            et_length.setKeyListener(keyListener_length);
            et_stopped.setFocusable(true);
            et_stopped.setKeyListener(keyListener_stopped);
            et_stopped.setFocusable(true);

            et_frequency.setText(frequency);
            et_length.setText(String.valueOf(length));
            et_stopped.setText(String.valueOf(stopped));
        } else {
            et_frequency.setKeyListener(null);
            et_length.setKeyListener(null);
            et_stopped.setKeyListener(null);
        }
    }

    void checkChanged() {
        if (cb_currentlyUse.isChecked() || cb_previouslyUsed.isChecked()) {
            et_frequency.setKeyListener(keyListener_frequency);
            et_frequency.setFocusableInTouchMode(true);
            et_length.setKeyListener(keyListener_length);
            et_length.setFocusableInTouchMode(true);
            et_stopped.setKeyListener(keyListener_stopped);
            et_stopped.setFocusableInTouchMode(true);
        } else {
            et_frequency.setKeyListener(null);
            et_frequency.setFocusable(false);
            et_length.setKeyListener(null);
            et_length.setFocusable(false);
            et_stopped.setKeyListener(null);
            et_stopped.setFocusable(false);
        }
    }

    void sendData(final String frequency, final int length, final int stopped) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.getString("code").equals("successful")) {
                                    BusStation.getBus().post(new Pop_BackStack(Medical_Transaction.UPDATE_SOCIAL_HISTORY));
                                }
                            } catch (Exception e) {
                                custom_alertDialog.show("Error", "Try again.");
                            } finally {
                                progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            custom_alertDialog.show("Error", "Try again.");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "editSocailHistory");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient_id));
                    params.put("socialType", socialHistoryType.name());
                    params.put("currentlyUse", String.valueOf(cb_currentlyUse.isChecked()));
                    params.put("previouslyUsed", String.valueOf(cb_previouslyUsed.isChecked()));
                    if (cb_currentlyUse.isChecked() || cb_previouslyUsed.isChecked()) {
                        params.put("frequency", frequency);
                        params.put("length", String.valueOf(length));
                        params.put("stopped", String.valueOf(stopped));
                    } else {
                        params.put("frequency", "null");
                        params.put("length", "0");
                        params.put("stopped", "0");
                    }
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            custom_alertDialog.show("Error", "Try again.");
        } finally {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BusStation.getBus().post(new Bus_ToolbarTitle("Update Social History", header));
    }

}
