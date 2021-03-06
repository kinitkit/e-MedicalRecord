package com.e_medicalRecord.kinit.e_medicalrecord.Allergy.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e_medicalRecord.kinit.e_medicalrecord.Allergy.Class.Allergy;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_AlertDialog;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressDialog;
import com.e_medicalRecord.kinit.e_medicalrecord.Profile.Class.Patient;
import com.e_medicalRecord.kinit.e_medicalrecord.R;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Allergy_Form extends AppCompatActivity implements View.OnClickListener {

    //Classes
    Patient patient;
    Allergy allergy;
    Custom_AlertDialog alertDialog;
    Custom_ProgressDialog progressDialog;

    //Widgets
    EditText et_allergicFrom, et_reaction, et_treatment;
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergy_form);
        init();
    }

    void init() {
        patient = getIntent().getExtras().getParcelable("patient");
        if (getIntent().hasExtra("allergy")) {
            allergy = getIntent().getExtras().getParcelable("allergy");
        }

        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);

        et_allergicFrom = (EditText) findViewById(R.id.et_allergicFrom);
        et_reaction = (EditText) findViewById(R.id.et_reaction);
        et_treatment = (EditText) findViewById(R.id.et_treatment);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(allergy != null) {
            getSupportActionBar().setTitle("Update Allergy");
            getSupportActionBar().setSubtitle(allergy.fr);
            et_allergicFrom.setText(allergy.fr);
            et_reaction.setText(allergy.reaction);
            et_treatment.setText(allergy.treatment);
        } else {
            getSupportActionBar().setTitle("Allergy Form");
            getSupportActionBar().setSubtitle(patient.name);
        }
    }

    void onClickSave() {
        String strAllergicFrom = et_allergicFrom.getText().toString().trim(), strReaction = et_reaction.getText().toString().trim(),
                strTreatment = et_treatment.getText().toString().trim();

        if (checkEditText(et_allergicFrom, strAllergicFrom) && checkEditText(et_reaction, strReaction) && checkEditText(et_treatment, strTreatment)) {
            sendData(strAllergicFrom, strReaction, strTreatment);
        }
    }

    boolean checkEditText(EditText editText, String text) {
        if (text.isEmpty()) {
            editText.setError("Please fill out this field.");
            return false;
        } else {
            return true;
        }
    }

    void sendData(final String strAllergicFrom, final String strReaction, final String strTreatment) {
        try {
            progressDialog.show("Saving...");
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_ALLERGY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("allergy", response);
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    if (jsonObject.getString("code").equals("success")) {
                                        progressDialog.dismiss();
                                        if (allergy != null) {
                                            Toast.makeText(getApplicationContext(), getString(R.string.record_updated), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), R.string.record_added, Toast.LENGTH_SHORT).show();
                                        }
                                        Intent intent = new Intent();
                                        intent.putExtra("result", true);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    } else {
                                        alertDialog.show("Error", jsonObject.getString("code"));
                                    }
                                } else if(jsonObject.has("exception")){
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
                            alertDialog.show("Error", error.getMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    if(allergy != null){
                        params.put("action", "updateAllergy");
                        params.put("id", String.valueOf(allergy.id));
                    } else {
                        params.put("action", "insertAllergy");
                        params.put("patient_id", String.valueOf(patient.id));
                    }
                    params.put("device", "mobile");
                    params.put("fr", strAllergicFrom);
                    params.put("reaction", strReaction);
                    params.put("treatment", strTreatment);
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
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
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
