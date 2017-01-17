package com.example.kinit.e_medicalrecord.Activities;

import android.app.DatePickerDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Request.Connection;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.DatePickerFragment;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {
    Calendar calendar;
    Connection connection;
    Custom_ProgressDialog progressDialog;
    Custom_AlertDialog custom_alertDialog;
    AlertDialog.Builder builder;
    String jsonResponse;

    Toolbar toolbar;
    EditText et_username, et_password, et_cPassword, et_fName, et_mName, et_lName, et_license, et_address, et_eAddress, et_contactNo, et_occupation, et_nationality, et_religion, et_bday;
    Button btn_register;
    Spinner spinner_gender, spinner_type, spinner_user, spinner_civilStatus;
    ArrayAdapter arrayAdapter_gender, arrayAdapter_type, arrayAdapter_user, arrayAdapter_civilStatus;
    LinearLayout linearLayout_medicalStaff, linearLayout_patient;

    DatePickerFragment datePickerFragment;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    void init() {
        custom_alertDialog = new Custom_AlertDialog(Register.this);
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendar = Calendar.getInstance();
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                setCalendar(calendar);
            }
        });


        //ArrayAdapter
        arrayAdapter_gender = ArrayAdapter.createFromResource(this, R.array.spinner_gender_options, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter_type = ArrayAdapter.createFromResource(this, R.array.spinner_type_options, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter_user = ArrayAdapter.createFromResource(this, R.array.spinner_user_type, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter_civilStatus = ArrayAdapter.createFromResource(this, R.array.spinner_civil_status, R.layout.support_simple_spinner_dropdown_item);

        //Spinner
        spinner_gender = (Spinner)findViewById(R.id.spinner_gender);
        spinner_gender.setAdapter(arrayAdapter_gender);
        spinner_type = (Spinner)findViewById(R.id.spinner_type);
        spinner_type.setAdapter(arrayAdapter_type);
        spinner_user = (Spinner)findViewById(R.id.spinner_user);
        spinner_user.setAdapter(arrayAdapter_user);
        spinner_civilStatus = (Spinner)findViewById(R.id.spinner_civilStatus);
        spinner_civilStatus.setAdapter(arrayAdapter_civilStatus);

        //LinearLayout
        linearLayout_medicalStaff = (LinearLayout) findViewById(R.id.layout_medicalStaff);
        linearLayout_patient = (LinearLayout) findViewById(R.id.layout_patient);

        //Connection
        connection = new Connection(this);

        //ProgressDialog
        progressDialog = new Custom_ProgressDialog(this);
        //AlertDialog
        builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setPositiveButton("OK", null);

        //Spinner SetOnItemSelectedListener
        spinner_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedUser = parent.getItemAtPosition(position).toString();
                if(selectedUser.equals("Patient")){
                    linearLayout_medicalStaff.setVisibility(LinearLayout.GONE);
                    linearLayout_patient.setVisibility(LinearLayout.VISIBLE);
                }
                else{
                    linearLayout_patient.setVisibility(LinearLayout.GONE);
                    linearLayout_medicalStaff.setVisibility(LinearLayout.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //EditText
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_cPassword = (EditText) findViewById(R.id.et_cPassword);
        et_fName = (EditText) findViewById(R.id.et_fName);
        et_mName = (EditText) findViewById(R.id.et_mName);
        et_lName = (EditText) findViewById(R.id.et_lName);
        et_license = (EditText) findViewById(R.id.et_license);
        et_address = (EditText) findViewById(R.id.et_address);
        et_nationality = (EditText) findViewById(R.id.et_nationality);
        et_religion = (EditText) findViewById(R.id.et_religion);
        et_occupation = (EditText) findViewById(R.id.et_occupation);
        et_eAddress = (EditText) findViewById(R.id.et_eAddress);
        et_contactNo = (EditText) findViewById(R.id.et_contactNo);
        et_bday = (EditText) findViewById(R.id.et_bday);
        et_bday.setOnClickListener(this);
        setCalendar(calendar);

        //Button
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    void register() { //Registration Method
        boolean isThereErrorInEditText = false;
        final String fName = et_fName.getText().toString().trim(), mName = et_mName.getText().toString().trim(),
                lName = et_lName.getText().toString().trim(), username = et_username.getText().toString().trim(),
                password = et_password.getText().toString().trim(), cPassword = et_cPassword.getText().toString().trim(),
                license = et_license.getText().toString().trim(), address = et_address.getText().toString().trim(),
                eAddress = et_eAddress.getText().toString().trim(), contactNo = et_contactNo.getText().toString().trim(),
                user_type = spinner_user.getSelectedItem().toString(), civilStatus = spinner_civilStatus.getSelectedItem().toString(),
                occupation = et_occupation.getText().toString().trim(), nationality = et_nationality.getText().toString().trim(),
                religion = et_religion.getText().toString().trim(), bday = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        //Validate if texts have error
        if (validate_editText(et_fName, fName, "name") || validate_editText(et_mName, mName, "name") || validate_editText(et_lName, lName, "name") ||
                validate_editText(et_contactNo, contactNo, "none") || validate_editText(et_eAddress, eAddress, "email") ||
                validate_editText(et_username, username, "username") || validate_editText(et_password, password, "password") ||
                validate_editText(et_cPassword, cPassword, "password")) {
            if (user_type.equals("Medical Personnel")) {
                if (validate_editText(et_license, license, "none")) {
                    isThereErrorInEditText = true;
                }
            } else {
                if (validate_editText(et_occupation, occupation, "none") || validate_editText(et_nationality, nationality, "none") ||
                        validate_editText(et_religion, religion, "none") || validate_editText(et_address, address, "none")) {
                    isThereErrorInEditText = true;
                }
            }
            isThereErrorInEditText = true;
        }
        //Validate if passwords match
        if (!isThereErrorInEditText) {
            if (!password.equals(cPassword)) {
                et_cPassword.setError("Password does not match");
            } else {
                //Database manipulation
                final String gender = spinner_gender.getSelectedItem().toString(), medicalStaffType = spinner_type.getSelectedItem().toString();

                progressDialog.show("Processing...");
                //Start of JSON
                try {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL_REGISTRATION,
                            new Response.Listener<String>() { //Response Listener
                                @Override
                                public void onResponse(String response) {
                                    boolean isDuplicateUsername = false, isDuplicateEmail = false;
                                    try {
                                        Log.d("error", response);
                                        jsonResponse = "";
                                        JSONArray jsonArray = new JSONArray(response);
                                        final int numberOfItemsInResp = jsonArray.length();
                                        for (int x = 0; x < numberOfItemsInResp; x++) {
                                            JSONObject resp = (JSONObject) jsonArray.get(x);
                                            if (resp.has("code")) {
                                                String code = resp.getString("code");
                                                if (code.equals("duplicate_email"))
                                                    isDuplicateEmail = true;
                                                if (code.equals("duplicate_username"))
                                                    isDuplicateUsername = true;
                                                if (code.equals("reg_success"))
                                                    custom_alertDialog.show("Successful", "Registration is successful. You may now sign in.");
                                                else if (code.equals("reg_unsuccess"))
                                                    custom_alertDialog.show("Oops!", "Registration failed. Please, try again.");
                                            } else if (resp.has("error")) {
                                                custom_alertDialog.show("Error", resp.getString("error"));
                                            }
                                        }
                                    } catch (JSONException e) {
                                        custom_alertDialog.show("Internal Error", e.getMessage());
                                        //displayAlert("Internal Error", e.getMessage());
                                    } finally {
                                        if (isDuplicateUsername)
                                            et_username.setError("Username is already taken");
                                        if (isDuplicateEmail)
                                            et_eAddress.setError("Email is already taken");
                                        progressDialog.dismiss();
                                    }
                                }
                            },
                            new Response.ErrorListener() { //Response Error Listener
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    custom_alertDialog.show("Internal Error", error.getMessage());
                                    progressDialog.dismiss();
                                }
                            }) { //Get Parameters
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("action", "registerMedicalStaff");
                            params.put("device", "mobile");
                            params.put("user_type", user_type);
                            params.put("first_name", fName);
                            params.put("middle_name", mName);
                            params.put("last_name", lName);
                            params.put("gender", gender);
                            params.put("birthday", bday);
                            params.put("address", address);
                            params.put("occupation", occupation);
                            params.put("nationality", nationality);
                            params.put("civil_status", civilStatus);
                            params.put("religion", religion);
                            params.put("contact_number", contactNo);
                            params.put("email_address", eAddress);
                            params.put("username", username);
                            params.put("password", password);
                            if (user_type.equals("Medical Personnel")) {
                                params.put("medicalStaffType", medicalStaffType);
                                params.put("license_number", license);
                            }
                            return params;
                        }
                    };
                    Custom_Singleton.getInstance(Register.this).addToRequestQueue(stringRequest);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    custom_alertDialog.show("Internal Error", e.getMessage());
                }
            }
        }
    }

    boolean validate_editText(EditText editText, String text, String type) {
        boolean isThereError = false;

        if (!TextUtils.isEmpty(text)) {
            switch (type) {
                case "name":
                    if (text.length() >= 1) {
                        if (!text.matches("^([ \\u00c0-\\u01ffa-zA-Z'\\-])+$")) {
                            editText.setError("Invalid name");
                            isThereError = true;
                        }
                    } else {
                        editText.setError("Invalid name");
                        isThereError = true;
                    }
                    break;
                case "username":
                    if (text.length() >= 6 && text.length() <= 20) {
                        if (!text.matches("^[A-Za-z0-9]+([-|_][A-Za-z0-9]+)*$")) {
                            editText.setError("Use only letters (a-z), numbers, and -_");
                            isThereError = true;
                        }
                    } else {
                        editText.setError("Must be at least 6-20 characters long");
                        isThereError = true;
                    }
                    break;
                case "password":
                    if (text.length() >= 6 && text.length() <= 20) {
                        if (!text.matches("^([a-zA-Z0-9])+$")) {
                            editText.setError("Cannot contain special characters");
                            isThereError = true;
                        }
                    } else {
                        editText.setError("Password is short");
                        isThereError = true;
                    }
                    break;
                case "email":
                    if (!text.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
                        editText.setError("Email is invalid");
                        isThereError = true;
                    }
                    break;
            }
        } else {
            editText.setError("Required Field!");
            isThereError = true;
        }
        return isThereError;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                try {
                    this.finish();
                } catch (Exception ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_bday:
                datePickerFragment.show(getSupportFragmentManager(), "DatePicker");
                datePickerFragment.setCurrentDate(calendar);
                break;
        }
    }

    void setCalendar(Calendar calendar){
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        et_bday.setText(simpleDateFormat.format(calendar.getTime()));
    }
}