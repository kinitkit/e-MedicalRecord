package com.example.kinit.e_medicalrecord.Activities;

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
import com.example.kinit.e_medicalrecord.Classes.Connection;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    Connection connection;
    ProgressDialog progressDialog;
    Custom_AlertDialog custom_alertDialog;
    AlertDialog.Builder builder;
    String jsonResponse;

    Toolbar toolbar;
    EditText et_username, et_password, et_cPassword, et_fName, et_mName, et_lName, et_license, et_address, et_eAddress, et_contactNo;
    Button btn_register;
    Spinner spinner_gender, spinner_type, spinner_month, spinner_day, spinner_year, spinner_user;
    ArrayAdapter arrayAdapter_gender, arrayAdapter_type, arrayAdapter_month, arrayAdapter_user;
    ArrayAdapter<String> arrayAdapter_day, arrayAdapter_year;
    ArrayList<String> arrayList_month, arrayList_day, arrayList_year;
    LinearLayout linearLayout;

    int spinner_year_previous = -1;
    String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    void init(){
        custom_alertDialog = new Custom_AlertDialog(Register.this);
        //Toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ArrayList
        arrayList_month = new ArrayList<>();
        arrayList_day = new ArrayList<>();
        arrayList_year = new ArrayList<>();
        initialize_date();

        //ArrayAdapter
        arrayAdapter_gender = ArrayAdapter.createFromResource(this, R.array.spinner_gender_options, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter_type = ArrayAdapter.createFromResource(this, R.array.spinner_type_options, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter_user= ArrayAdapter.createFromResource(this, R.array.spinner_user_type, R.layout.support_simple_spinner_dropdown_item);

        arrayAdapter_month = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayList_month);
        arrayAdapter_day = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayList_day);
        arrayAdapter_year = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayList_year);

        //Spinner
        spinner_gender = (Spinner)findViewById(R.id.spinner_gender);
        spinner_gender.setAdapter(arrayAdapter_gender);
        spinner_type = (Spinner)findViewById(R.id.spinner_type);
        spinner_type.setAdapter(arrayAdapter_type);
        spinner_user = (Spinner)findViewById(R.id.spinner_user);
        spinner_user.setAdapter(arrayAdapter_user);

        spinner_month = (Spinner)findViewById(R.id.spinner_month);
        spinner_month.setAdapter(arrayAdapter_month);
        spinner_day = (Spinner)findViewById(R.id.spinner_day);
        spinner_day.setAdapter(arrayAdapter_day);
        spinner_year = (Spinner)findViewById(R.id.spinner_year);
        spinner_year.setAdapter(arrayAdapter_year);

        //LinearLayout
        linearLayout = (LinearLayout)findViewById(R.id.layout_medicalStaff);

        //Connection
        connection = new Connection(this);

        //ProgressDialog
        progressDialog = new ProgressDialog(this);
        //AlertDialog
        builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setPositiveButton("OK", null);

        //Spinner SetOnItemSelectedListener
        spinner_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedUser = parent.getItemAtPosition(position).toString();
                if(selectedUser.equals("Patient")){
                    linearLayout.setVisibility(LinearLayout.GONE);
                }
                else{
                    linearLayout.setVisibility(LinearLayout.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedYear = Integer.parseInt(parent.getItemAtPosition(position).toString());
                if(spinner_year_previous != selectedYear) {
                    checkYear(selectedYear);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedYear = Integer.parseInt(spinner_year.getSelectedItem().toString()),
                        selectedMonth = parent.getSelectedItemPosition();
                checkMonth(selectedMonth, selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //EditText
        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        et_cPassword = (EditText)findViewById(R.id.et_cPassword);
        et_fName = (EditText)findViewById(R.id.et_fName);
        et_mName = (EditText)findViewById(R.id.et_mName);
        et_lName = (EditText)findViewById(R.id.et_lName);
        et_license = (EditText)findViewById(R.id.et_license);
        et_address = (EditText)findViewById(R.id.et_address);
        et_eAddress = (EditText)findViewById(R.id.et_eAddress);
        et_contactNo = (EditText)findViewById(R.id.et_contactNo);

        //Button
        btn_register = (Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    void register(){ //Registration Method
        boolean isThereErrorInEditText = false;
        final String fName = et_fName.getText().toString().trim(), mName = et_mName.getText().toString().trim(),
                lName = et_lName.getText().toString().trim(), username = et_username.getText().toString().trim(),
                password = et_password.getText().toString().trim(), cPassword= et_cPassword.getText().toString().trim(),
                license = et_license.getText().toString().trim(), address = et_address.getText().toString().trim(),
                eAddress = et_eAddress.getText().toString().trim(), contactNo = et_contactNo.getText().toString().trim(),
                user_type = spinner_user.getSelectedItem().toString();

        //Validate if texts have error
        if(validate_editText(et_fName, fName, "name") || validate_editText(et_mName, mName, "name") || validate_editText(et_lName, lName, "name") ||
                validate_editText(et_address, address, "none") ||
                validate_editText(et_contactNo, contactNo, "none") || validate_editText(et_eAddress, eAddress, "email") ||
                 validate_editText(et_username, username, "username") || validate_editText(et_password, password, "password") ||
                validate_editText(et_cPassword, cPassword, "password")){
            if(user_type.equals("Medical Personnel")){
                if(validate_editText(et_license, license, "none")) {
                    isThereErrorInEditText = true;
                }
            }
            isThereErrorInEditText = true;
        }
        //Validate if passwords match
        if(!isThereErrorInEditText){
            if(!password.equals(cPassword)){
                et_cPassword.setError("Password does not match");
            }
            else {
                //Database manipulation
                final String gender = spinner_gender.getSelectedItem().toString(), medicalStaffType = spinner_type.getSelectedItem().toString(),
                        bday = spinner_year.getSelectedItem().toString()  +"-"+ Integer.toString(spinner_month.getSelectedItemPosition()+1) +"-"+
                                Integer.toString(spinner_day.getSelectedItemPosition() + 1);


                progressDialog.setMessage("Processing...");
                progressDialog.show();

                //Start of JSON
                try {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                            new Response.Listener<String>() { //Response Listener
                                @Override
                                public void onResponse(String response) {
                                    boolean isDuplicateUsername = false, isDuplicateEmail = false;
                                    try{
                                        Log.d("error", response);
                                        jsonResponse = "";
                                        JSONArray jsonArray = new JSONArray(response);
                                        final int numberOfItemsInResp = jsonArray.length();
                                        for(int x = 0; x < numberOfItemsInResp; x++){
                                            JSONObject resp = (JSONObject)jsonArray.get(x);
                                            String code = resp.getString("code");
                                            if(code.equals("duplicate_email"))
                                                isDuplicateEmail = true;
                                            if(code.equals("duplicate_username"))
                                                isDuplicateUsername = true;
                                            if(code.equals("reg_success"))
                                                custom_alertDialog.show("Successful", "Registration is successful. You may now sign in.");
                                            else if(code.equals("reg_unsuccess"))
                                                custom_alertDialog.show("Oops!", "Registration failed. Please, try again.");
                                            //displayAlert("Successful", "Registration is successful. You may now sign in.");
                                            //displayAlert("Oops!", "Registration failed. Please, try again later.");
                                        }
                                    } catch (JSONException e) {
                                        custom_alertDialog.show("Internal Error", e.getMessage());
                                        //displayAlert("Internal Error", e.getMessage());
                                    }
                                    finally {
                                        if(isDuplicateUsername)
                                            et_username.setError("Username is already taken");
                                        if(isDuplicateEmail)
                                            et_eAddress.setError("Email is already taken");
                                        progressDialog.cancel();
                                    }
                                }
                            },
                            new Response.ErrorListener() { //Response Error Listener
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    custom_alertDialog.show("Internal Error", error.getMessage());
                                    //displayAlert("Database Error", error.getMessage());
                                    progressDialog.cancel();
                                }
                            }) { //Get Parameters
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("action", "registerMedicalStaff");
                            params.put("device", "mobile");
                            params.put("user_type", user_type);
                            params.put("first_name", fName);
                            params.put("middle_name", mName);
                            params.put("last_name", lName);
                            params.put("gender", gender);
                            params.put("birthday", bday);
                            params.put("address", address);
                            params.put("contact_number", contactNo);
                            params.put("email_address", eAddress);
                            params.put("username", username);
                            params.put("password", password);
                            if(user_type.equals("Medical Personnel")) {
                                params.put("user_type", medicalStaffType);
                                params.put("license_number", license);
                            }
                            return params;
                        }
                    };
                    Custom_Singleton.getInstance(Register.this).addToRequestQueue(stringRequest);
                }
                catch (Exception e){
                    progressDialog.cancel();
                    e.printStackTrace();
                    custom_alertDialog.show("Internal Error", e.getMessage());
                    //displayAlert("Internal Error", e.getStackTrace().toString());
                }
            }
        }
    }

    void displayAlert(String title, String msg){
        builder.setTitle(title);
        builder.setMessage(msg);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    boolean validate_editText(EditText editText, String text, String type){
        boolean isThereError = false;

        if(!TextUtils.isEmpty(text)){
            switch (type){
                case "name":
                    if(text.length() >= 1) {
                        if (!text.matches("^([ \\u00c0-\\u01ffa-zA-Z'\\-])+$")) {
                            editText.setError("Invalid name");
                            isThereError = true;
                        }
                    } else{
                        editText.setError("Invalid name");
                        isThereError = true;
                    }
                    break;
                case "username":
                    if(text.length() >= 6 && text.length() <= 20) {
                        if (!text.matches("^[A-Za-z0-9]+([-|_][A-Za-z0-9]+)*$")) {
                            editText.setError("Use only letters (a-z), numbers, and -_");
                            isThereError = true;
                        }
                    } else{
                        editText.setError("Must be at least 6-20 characters long");
                        isThereError = true;
                    }
                    break;
                case "password":
                    if(text.length() >= 6 && text.length() <= 20) {
                        if (!text.matches("^([a-zA-Z0-9])+$")) {
                            editText.setError("Cannot contain special characters");
                            isThereError = true;
                        }
                    } else{
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
        } else{
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
                }
                catch (Exception ex){
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void initialize_date(){
        arrayList_month.clear();
        arrayList_day.clear();
        arrayList_year.clear();

        for(int x = 0; x < month.length; x++){
            arrayList_month.add(month[x]);
        }

        for(int x = 1; x <= 31; x++) {
            arrayList_day.add(Integer.toString(x));
        }

        for(int x = 1900; x <= Calendar.getInstance().get(Calendar.YEAR); x++){
            arrayList_year.add(Integer.toString(x));
        }
    }

    void checkYear(int year){

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int monthSelected = spinner_month.getSelectedItemPosition(), currentMonth = Calendar.getInstance().get(Calendar.MONTH);

        if(year == currentYear){
            if(currentMonth != 11) {
                configure_month(currentMonth + 1);

                if (monthSelected > currentMonth) {
                    spinner_month.setSelection(0);
                } else{
                    spinner_month.setSelection(monthSelected);
                }
            }
        } else{
            configure_month(month.length);
            spinner_month.setSelection(monthSelected);
        }
    }

    void checkMonth(int selectedMonth, int selectedYear){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR), currentMonth = Calendar.getInstance().get(Calendar.MONTH),
                currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH), selectedDay = Integer.parseInt(spinner_day.getSelectedItem().toString());
        switch (selectedMonth){
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                checkDay(selectedYear, selectedMonth, selectedDay, currentYear, currentMonth, currentDay, 31);
                break;
            case 3:
            case 5:
            case 8:
            case 10:
                checkDay(selectedYear, selectedMonth, selectedDay, currentYear, currentMonth, currentDay, 30);
                break;
            case 1:
                if(isLeapYear(selectedYear)){
                    checkDay(selectedYear, selectedMonth, selectedDay, currentYear, currentMonth, currentDay, 29);
                } else {
                    checkDay(selectedYear, selectedMonth, selectedDay, currentYear, currentMonth, currentDay, 28);
                }
                break;
        }
    }

    void configure_month(int counter){
        arrayList_month.clear();
        for(int x = 0; x < counter; x++){
            arrayList_month.add(this.month[x]);
        }
        arrayAdapter_month = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayList_month);
        spinner_month.setAdapter(arrayAdapter_month);
    }

    void checkDay(int selectedYear, int selectedMonth, int selectedDay, int currentYear, int currentMonth, int currentDay, int dayCount){

        if(selectedYear == currentYear && selectedMonth == currentMonth){

            configure_day(currentDay);
            if(selectedDay > currentDay){
                spinner_day.setSelection(0);
            } else{
                spinner_day.setSelection(selectedDay - 1);
            }
        } else {
            configure_day(dayCount);
            if(selectedDay > spinner_day.getCount()){
                spinner_day.setSelection(0);
            } else{
                spinner_day.setSelection(selectedDay - 1);
            }
        }
    }

    void configure_day(int counter){
        arrayList_day.clear();
        for(int x = 1; x <= counter; x++){
            arrayList_day.add(Integer.toString(x));
        }
        arrayAdapter_day = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayList_day);
        spinner_day.setAdapter(arrayAdapter_day);
    }

    boolean isLeapYear(int year){
        if(year % 4 == 0){
            if(year % 100 == 0){
                if(year % 400 == 0){
                    return true;
                }
            }
            else{
                return true;
            }
        }
        return false;
    }
}