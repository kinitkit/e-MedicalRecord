package com.example.kinit.e_medicalrecord.Activities.Allergy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.User.Patient;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import java.util.HashMap;
import java.util.Map;

public class Allergy_Form extends AppCompatActivity implements View.OnClickListener {

    //Classes
    Patient patient;
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
        getSupportActionBar().setTitle("Allergy Form");
        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void onClickSave() {
        String strAllergicFrom = et_allergicFrom.getText().toString().trim(), strReaction = et_reaction.getText().toString().trim(),
                strTreatment = et_treatment.getText().toString().trim();

        if (checkEditText(et_allergicFrom, strAllergicFrom) && checkEditText(et_reaction, strReaction) && checkEditText(et_treatment, strTreatment)) {

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

    void sendData(final String strAllergicFrom, final String strReaction, String strTreatment) {
        try {
            progressDialog.show("Saving...");
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Intent intent = new Intent();
                            intent.putExtra("result", true);
                            setResult(RESULT_OK, intent);
                            finish();
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
                    params.put("action", "insertAllergy");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient.id));
                    params.put("fr", strAllergicFrom);
                    params.put("reaction", strReaction);
                    params.put("treatment", strReaction);
                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            progressDialog.dismiss();
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
