package com.example.kinit.e_medicalrecord.Activities.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.User.User;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Settings_Personal_Information extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    User user;
    Custom_AlertDialog alertDialog;
    Custom_ProgressDialog progressDialog;

    //Widgets
    EditText et_fName, et_mName, et_lName, et_contactNo;
    ArrayAdapter arrayAdapter_gender;
    Spinner spinner_gender;
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings_personal_information);

        init();
    }

    void init() {
        intent = getIntent();
        user = intent.getExtras().getParcelable("user");

        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);

        et_fName = (EditText) findViewById(R.id.et_fName);
        et_mName = (EditText) findViewById(R.id.et_mName);
        et_lName = (EditText) findViewById(R.id.et_lName);
        et_contactNo = (EditText) findViewById(R.id.et_contactNo);
        arrayAdapter_gender = ArrayAdapter.createFromResource(this, R.array.spinner_gender_options, R.layout.support_simple_spinner_dropdown_item);
        spinner_gender = (Spinner) findViewById(R.id.spinner_gender);
        spinner_gender.setAdapter(arrayAdapter_gender);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Personal Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setEditText();
    }

    void setEditText() {
        et_fName.setText(user.getFirstName());
        et_mName.setText(user.getMiddleName());
        et_lName.setText(user.getLastName());
        et_contactNo.setText(user.getContactNumber());
        spinner_gender.setSelection(arrayAdapter_gender.getPosition(user.getGender()));
    }

    boolean validateEditText(EditText editText, String text, String code) {
        boolean isThereNoError = true;
        if (!TextUtils.isEmpty(text)) {
            if (code.equals("name")) {
                if (text.length() >= 1) {
                    if (!text.matches("^([ \\u00c0-\\u01ffa-zA-Z'\\-])+$")) {
                        editText.setError("Invalid input.");
                        isThereNoError = false;
                    }
                } else {
                    editText.setError("Invalid input.");
                    isThereNoError = false;
                }
            }
        } else {
            editText.setError("This field is required.");
            isThereNoError = false;
        }

        return isThereNoError;
    }

    void onClickSave() {
        String strFname = et_fName.getText().toString().trim(), strMname = et_mName.getText().toString().trim(),
                strLname = et_lName.getText().toString().trim(), strContactNo = et_contactNo.getText().toString().trim(),
                strGender = spinner_gender.getSelectedItem().toString();

        if (validateEditText(et_fName, strFname, "name") || validateEditText(et_mName, strMname, "name") || validateEditText(et_lName, strLname, "name")
                || validateEditText(et_contactNo, strContactNo, "")) {
            sendData(strFname, strMname, strLname, strContactNo, strGender);
        }
    }

    void sendData(final String strFname, final String strMname, final String strLname, final String strContactNo, final String strGender) {
        try {
            progressDialog.show("Saving...");
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("error", response);
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if(jsonObject.has("code")){
                                    String code = jsonObject.getString("code");
                                    if(code.equals("success")){
                                        Toast.makeText(getApplicationContext(), getString(R.string.record_updated), Toast.LENGTH_SHORT).show();
                                        user.setFirstName(strFname);
                                        user.setMiddleName(strMname);
                                        user.setLastName(strLname);
                                        user.setContactNumber(strContactNo);
                                        user.setGender(strGender);
                                        intent = new Intent();
                                        intent.putExtra("result", true);
                                        intent.putExtra("user", user);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    } else {
                                        alertDialog.show("Error", getString(R.string.error_occured));
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
                            String message = null;
                            if (error instanceof NetworkError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (error instanceof ServerError) {
                                message = "The server could not be found. Please try again after some time!!";
                            } else if (error instanceof AuthFailureError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (error instanceof ParseError) {
                                message = "Parsing error! Please try again after some time!!";
                            } else if (error instanceof NoConnectionError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (error instanceof TimeoutError) {
                                message = "Connection TimeOut! Please check your internet connection.";
                            }
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "updatePersonalInformation");
                    params.put("device", "mobile");
                    params.put("user_id", String.valueOf(user.getUser_data_id()));
                    params.put("first_name", strFname);
                    params.put("middle_name", strMname);
                    params.put("last_name", strLname);
                    params.put("contact_number", strContactNo);
                    params.put("gender", strGender);

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
