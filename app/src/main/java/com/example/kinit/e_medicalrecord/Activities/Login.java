package com.example.kinit.e_medicalrecord.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button btn_login;
    TextView link_signup;
    EditText et_username, et_password;

    Intent intent;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    int patient_id = 0, medicalStaff_id = 0, user_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    void init(){
        //SharedPreferences
        sharedPreferences = getSharedPreferences("com.example.kinit.e_medicalrecord", Context.MODE_PRIVATE);

        //EditText
        et_password = (EditText)findViewById(R.id.et_password);
        et_username = (EditText)findViewById(R.id.et_username);


        //ProgressDialog
        progressDialog = new ProgressDialog(this);
        //AlertDialog
        builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setPositiveButton("OK", null);

        //Login Button
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //Sign Up
        link_signup = (TextView)findViewById(R.id.link_signup);
        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), Register_MedicalStaff.class);
                startActivity(intent);
            }
        });
    }

    void login(){
        String username = et_username.getText().toString().trim(), password = et_password.getText().toString().trim();

        if(!validate_editText(et_username, username) && !validate_editText(et_password, password)){
            loginRequest(username, password);
        }
    }

    void loginRequest(final String username, final String password){
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try{
                                JSONArray jsonArray = new JSONArray(response);
                                final int numberOfItemsInResp = jsonArray.length();
                                JSONObject resp = (JSONObject)jsonArray.get(0);
                                if (resp.getString("code").equals("success")) {
                                    for(int x = 1; x < numberOfItemsInResp; x++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(x);
                                        if(jsonObject.getString("code").equals("user")){
                                            user_id = jsonObject.getInt("id");
                                        } else if(jsonObject.getString("code").equals("medicalStaff")){
                                            medicalStaff_id = jsonObject.getInt("id");
                                        } else if(jsonObject.getString("code").equals("patient")){
                                            patient_id = jsonObject.getInt("id");
                                        }
                                    }
                                    intent = new Intent(getBaseContext(), Profile.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("patient_id", patient_id);
                                    intent.putExtra("medicalStaff_id", medicalStaff_id);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("LOGIN_AUTHENTICATION", true);
                                    editor.putInt("user_id", user_id);
                                    editor.putInt("patient_id", patient_id);
                                    editor.putInt("medicalStaff_id", medicalStaff_id);
                                    editor.apply();
                                    startActivity(intent);

                                } else if(resp.getString("code").equals("fail")) {
                                    displayAlert("Incorrect Password", "The password you entered is incorrect. Please try again.");
                                }
                                else{
                                    displayAlert("Internal Error", "Something happened");
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                                displayAlert("Internal Error", e.getMessage());
                            } catch (Exception e){
                                e.printStackTrace();
                                displayAlert("Internal Error", e.getMessage());
                            } finally {
                                progressDialog.cancel();
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
                    params.put("action", "login");
                    params.put("device", "mobile");
                    params.put("username", username);
                    params.put("password", password);
                    return params;
                }
            };
            Custom_Singleton.getInstance(this.getApplicationContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressDialog.cancel();
            displayAlert("Error", "Som");
        }
    }

    boolean validate_editText(EditText editText, String data){
        boolean isThereError = false;
            if(data.isEmpty()){
                editText.setError("Required Field");
                isThereError = true;
            }
        return isThereError;
    }

    void displayAlert(String title, String msg){
        builder.setTitle(title);
        builder.setMessage(msg);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
