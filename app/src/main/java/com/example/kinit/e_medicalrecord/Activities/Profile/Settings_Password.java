package com.example.kinit.e_medicalrecord.Activities.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
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

public class Settings_Password extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    User user;
    Custom_AlertDialog alertDialog;
    Custom_ProgressDialog progressDialog;

    //Widgets
    EditText et_currentPassword, et_password, et_cPassword;
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings_password);
        init();
    }

    void init() {
        intent = getIntent();
        user = intent.getExtras().getParcelable("user");

        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_currentPassword = (EditText) findViewById(R.id.et_currentPassword);
        et_password = (EditText) findViewById(R.id.et_password);
        et_cPassword = (EditText) findViewById(R.id.et_cPassword);
    }

    boolean validateEditText(EditText editText, String text) {
        boolean isThereNoError = true;

        if (!text.isEmpty()) {
            if (text.length() >= 6 && text.length() <= 20) {
                if (!text.matches("^([a-zA-Z0-9])+$")) {
                    editText.setError("Cannot contain special characters");
                    isThereNoError = false;
                }
            } else {
                editText.setError("Password is short");
                isThereNoError = false;
            }
        } else {
            editText.setError(getString(R.string.required_field));
            isThereNoError = false;
        }

        return isThereNoError;
    }

    void onClickSave() {
        String strCurrentPassword = et_currentPassword.getText().toString().trim(), strPassword = et_password.getText().toString().trim(),
                strConfirmPassword = et_cPassword.getText().toString().trim();

        if (validateEditText(et_currentPassword, strCurrentPassword) && validateEditText(et_password, strPassword) &&
                validateEditText(et_cPassword, strConfirmPassword)) {
            if (strPassword.equals(strConfirmPassword)) {
                sendData(strCurrentPassword, strPassword, strConfirmPassword);
            } else {
                et_cPassword.setError("Password does not match.");
            }
        }
    }

    void sendData(final String strCurrentPassword, final String strPassword, String strConfirmPassword) {
        try {
            progressDialog.show("Saving...");
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_SETTINGS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("error", response);
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    String code = jsonObject.getString("code");
                                    if (code.equals("success")) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.record_updated), Toast.LENGTH_SHORT).show();
                                        intent = new Intent();
                                        intent.putExtra("result", true);
                                        intent.putExtra("user", user);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    } else if (code.equals("empty")) {
                                        alertDialog.show("Error", "Password is incorrect.");
                                    } else {
                                        alertDialog.show("Error", getString(R.string.error_occured));
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

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "updatePassword");
                    params.put("device", "mobile");
                    params.put("user_id", String.valueOf(user.user_data_id));
                    params.put("current_password", strCurrentPassword);
                    params.put("password", strPassword);

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
}
