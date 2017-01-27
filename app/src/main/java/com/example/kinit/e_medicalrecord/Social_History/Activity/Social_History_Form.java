package com.example.kinit.e_medicalrecord.Social_History.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressBar;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Social_History.Class.Social_History;
import com.example.kinit.e_medicalrecord.Profile.Class.Patient;
import com.example.kinit.e_medicalrecord.Profile.Class.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.General.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Social_History_Form extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    Intent intent;
    Patient patient;
    Viewer viewer;
    Social_History socialHistory;

    Custom_AlertDialog alertDialog;
    Custom_ProgressDialog progressDialog;
    Custom_ProgressBar progressBar;

    //Widgets
    EditText et_substance, et_frequency, et_length, et_stopped;
    CheckBox cb_currentlyUse, cb_previouslyUsed;
    Button btn_save;
    //KeyListner
    KeyListener keyListener_frequency, keyListener_length, keyListener_stopped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_social_history_form);
        init();
    }

    void init() {
        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");
        if (intent.hasExtra("socialHistory")) {
            socialHistory = intent.getExtras().getParcelable("socialHistory");
        }

        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);
        progressBar = new Custom_ProgressBar(this);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Past Medical History Form");
        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_substance = (EditText) findViewById(R.id.et_substance);
        et_frequency = (EditText) findViewById(R.id.et_frequency);
        et_length = (EditText) findViewById(R.id.et_length);
        et_stopped = (EditText) findViewById(R.id.et_stopped);
        cb_currentlyUse = (CheckBox) findViewById(R.id.cb_currentlyUse);
        cb_previouslyUsed = (CheckBox) findViewById(R.id.cb_previouslyUsed);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        cb_currentlyUse.setOnCheckedChangeListener(this);
        cb_previouslyUsed.setOnCheckedChangeListener(this);
        //KeyListner
        keyListener_frequency = et_frequency.getKeyListener();
        keyListener_length = et_length.getKeyListener();
        keyListener_stopped = et_stopped.getKeyListener();

        if (socialHistory != null) {
            fetchData();
        }
    }

    boolean validateEditText(EditText editText, String text, String type) {
        boolean isThereNoError = true;
        Log.d("error", text);
        if (text.isEmpty()) {
            if (editText.getId() == R.id.et_stopped) {
                if (cb_previouslyUsed.isChecked()) {
                    editText.requestFocus();
                    editText.setError(getString(R.string.required_field));
                    isThereNoError = false;
                }
            } else {
                editText.requestFocus();
                editText.setError(getString(R.string.required_field));
                isThereNoError = false;
            }
        } else if (type != null) {
            if (text.equals("0") || text.equals("")) {
                if (editText.getId() == R.id.et_stopped) {
                    if (cb_previouslyUsed.isChecked()) {
                        editText.requestFocus();
                        editText.setError("Invalid year");
                        isThereNoError = false;
                    }
                } else {
                    editText.requestFocus();
                    editText.setError("Invalid year");
                    isThereNoError = false;
                }
            }
        }

        return isThereNoError;
    }

    void checkChanged() {
        if (cb_currentlyUse.isChecked() || cb_previouslyUsed.isChecked()) {
            et_frequency.setKeyListener(keyListener_frequency);
            et_frequency.setFocusableInTouchMode(true);
            et_length.setKeyListener(keyListener_length);
            et_length.setFocusableInTouchMode(true);
            if (cb_previouslyUsed.isChecked()) {
                et_stopped.setKeyListener(keyListener_stopped);
                et_stopped.setFocusableInTouchMode(true);
            } else if (!cb_previouslyUsed.isChecked()) {
                et_stopped.setKeyListener(null);
                et_stopped.setFocusable(false);
                et_stopped.setText("");
            }
        } else {
            et_frequency.setKeyListener(null);
            et_frequency.setFocusable(false);
            et_frequency.setText("");
            et_length.setKeyListener(null);
            et_length.setFocusable(false);
            et_length.setText("");
            et_stopped.setKeyListener(null);
            et_stopped.setFocusable(false);
            et_stopped.setText("");
        }
    }

    void onClickSave() {
        String substance = et_substance.getText().toString().trim(), frequency = et_frequency.getText().toString().trim(),
                str_length = et_length.getText().toString().trim(), str_stopped = et_stopped.getText().toString().trim();
        int length, stopped;

        if (validateEditText(et_substance, substance, null)) {
            if (cb_currentlyUse.isChecked() || cb_previouslyUsed.isChecked()) {
                if (validateEditText(et_frequency, frequency, null)) {
                    if (validateEditText(et_length, str_length, "") || validateEditText(et_stopped, str_stopped, "")) {
                        length = Integer.parseInt(str_length);
                        stopped = (!str_stopped.equals("")) ? Integer.parseInt(str_stopped) : 0;
                        if(cb_previouslyUsed.isChecked()) {
                            if (stopped >= 1900 && stopped <= Calendar.getInstance().get(Calendar.YEAR)) {
                                saveData(substance, frequency, length, stopped);
                            } else {
                                et_stopped.requestFocus();
                                et_stopped.setError("Invalid year");
                            }
                        } else {
                            saveData(substance, frequency, length, stopped);
                        }
                    }
                }
            } else {
                alertDialog.show("Error", "Must be currently used or previously used.");
            }
        }
    }

    void saveData(final String substance, final String frequency, final int length, final int stopped) {
        try {
            progressDialog.show("Saving...");
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_SOCIAL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    String code = jsonObject.getString("code");
                                    switch (code) {
                                        case "success":
                                            if (socialHistory == null) {
                                                Toast.makeText(getApplicationContext(), R.string.record_added, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), R.string.record_updated, Toast.LENGTH_SHORT).show();
                                            }
                                            intent = new Intent();
                                            intent.putExtra("result", true);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                            break;
                                        case "unauthorized":
                                            alertDialog.show("Error", getString(R.string.unauthorized_to_insert));
                                            break;
                                        case "empty":
                                            alertDialog.show("Error", getString(R.string.error_occured));
                                            break;
                                        default:
                                            alertDialog.show("Error", getString(R.string.error_occured));
                                            break;
                                    }
                                } else if (jsonObject.has("exception")) {
                                    alertDialog.show("Error", jsonObject.getString("exception"));
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
                            progressDialog.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    if (socialHistory != null) {
                        params.put("action", "updateSocialHistory");
                        params.put("id", String.valueOf(socialHistory.id));
                    } else {
                        params.put("action", "insertSocialHistory");
                        if (viewer != null) {
                            params.put("user_data_id", String.valueOf(viewer.user_id));
                            params.put("medical_staff_id", String.valueOf(viewer.medicalStaff_id));
                        } else {
                            params.put("user_data_id", String.valueOf(patient.user_data_id));
                            params.put("medical_staff_id", "0");
                        }
                    }
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient.id));
                    params.put("substance", substance);
                    params.put("currently_use", (cb_currentlyUse.isChecked()) ? "1" : "0");
                    params.put("previously_used", (cb_previouslyUsed.isChecked()) ? "1" : "0");
                    params.put("frequency", frequency);
                    params.put("length", String.valueOf(length));
                    params.put("stopped_year", String.valueOf(stopped));

                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    void fetchData() {
        try {
            progressBar.show();
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_SOCIAL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response), jsonArray;
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    String code = jsonObject.getString("code");
                                    switch (code) {
                                        case "success":
                                            jsonArray = rootJsonArray.getJSONArray(1);
                                            socialHistory = new Social_History(jsonArray.getJSONObject(0));
                                            setText();
                                            break;
                                        case "empty":
                                        case "error":
                                            break;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progressBar.hide();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getSocialHistory");
                    params.put("device", "mobile");
                    params.put("id", String.valueOf(socialHistory.id));

                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressBar.hide();
            e.printStackTrace();
        }
    }

    void setText() {
        et_substance.setText(socialHistory.substance);
        cb_currentlyUse.setChecked(socialHistory.currentlyUse);
        cb_previouslyUsed.setChecked(socialHistory.previouslyUsed);
        et_frequency.setText(socialHistory.frequency);
        et_length.setText(String.valueOf(socialHistory.length));
        et_stopped.setText(String.valueOf(socialHistory.stoppedYear));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                onClickSave();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
