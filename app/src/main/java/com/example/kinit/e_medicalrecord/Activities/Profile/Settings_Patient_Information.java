package com.example.kinit.e_medicalrecord.Activities.Profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.DatePickerFragment;
import com.example.kinit.e_medicalrecord.Classes.User.User;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Settings_Patient_Information extends AppCompatActivity implements View.OnClickListener {

    //Classes
    Intent intent;
    User user;
    Custom_AlertDialog alertDialog;
    Custom_ProgressDialog progressDialog;

    //Widgets
    EditText et_bday, et_address, et_occupation, et_nationality, et_religion;
    Spinner spinner_civilStatus;
    ArrayAdapter arrayAdapter_civilStatus;
    Button btn_save;

    DatePickerFragment datePickerFragment;
    SimpleDateFormat simpleDateFormat;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings_patient);
        init();
    }

    void init() {
        intent = getIntent();
        user = intent.getExtras().getParcelable("user");

        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);

        et_address = (EditText) findViewById(R.id.et_address);
        et_nationality = (EditText) findViewById(R.id.et_nationality);
        et_religion = (EditText) findViewById(R.id.et_religion);
        et_occupation = (EditText) findViewById(R.id.et_occupation);
        et_bday = (EditText) findViewById(R.id.et_bday);
        et_bday.setOnClickListener(this);
        arrayAdapter_civilStatus = ArrayAdapter.createFromResource(this, R.array.spinner_civil_status, R.layout.support_simple_spinner_dropdown_item);
        spinner_civilStatus = (Spinner) findViewById(R.id.spinner_civilStatus);
        spinner_civilStatus.setAdapter(arrayAdapter_civilStatus);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        calendar = Calendar.getInstance();
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                setCalendar(calendar);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setEditText();
    }

    void setEditText() {
        et_address.setText(user.address);
        et_nationality.setText(user.nationality);
        et_religion.setText(user.religion);
        et_occupation.setText(user.occupation);
        et_bday.setText(user.birthday);
        calendar = user.calendarBirthday;
        spinner_civilStatus.setSelection(arrayAdapter_civilStatus.getPosition(user.civilStatus));
    }

    void setCalendar(Calendar calendar) {
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        et_bday.setText(simpleDateFormat.format(calendar.getTime()));
    }

    boolean validateEditText(EditText editText, String text) {
        boolean isThereNoError = true;

        if (text.isEmpty()) {
            editText.setError(getString(R.string.required_field));
            isThereNoError = false;
        }

        return isThereNoError;
    }

    void onClickSave() {
        String strAddress = et_address.getText().toString().trim(), strNationality = et_nationality.getText().toString().trim(),
                strReligion = et_religion.getText().toString().trim(), strOccupation = et_occupation.getText().toString().trim(),
                strCivilStatus = spinner_civilStatus.getSelectedItem().toString();

        if (validateEditText(et_address, strAddress) && validateEditText(et_address, strAddress) &&
                validateEditText(et_address, strAddress) && validateEditText(et_address, strAddress)) {
            sendData(strAddress, strNationality, strReligion, strOccupation, strCivilStatus);
        }
    }

    void sendData(final String strAddress, final String strNationality, final String strReligion, final String strOccupation, final String strCivilStatus) {
        progressDialog.show("Saving...");
        try {
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
                                        user.address = strAddress;
                                        user.nationality = strNationality;
                                        user.religion = strReligion;
                                        user.occupation = strOccupation;
                                        user.civilStatus = strCivilStatus;
                                        user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
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

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "updatePatientInformation");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(user.patient_id));
                    params.put("address", strAddress);
                    params.put("nationality", strNationality);
                    params.put("religion", strReligion);
                    params.put("occupation", strOccupation);
                    params.put("civil_status", strCivilStatus);
                    params.put("birthday", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));

                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                onClickSave();
                break;
            case R.id.et_bday:
                datePickerFragment.show(getSupportFragmentManager(), "DatePicker");
                datePickerFragment.setCurrentDate(calendar);
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
