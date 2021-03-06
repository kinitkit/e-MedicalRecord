package com.e_medicalRecord.kinit.e_medicalrecord.Settings.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_AlertDialog;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressDialog;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Classes.Dialogs.DatePickerFragment;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Request.UrlString;
import com.e_medicalRecord.kinit.e_medicalrecord.Profile.Class.User;
import com.e_medicalRecord.kinit.e_medicalrecord.R;

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
    EditText et_bday, et_height, et_weight, et_address, et_occupation, et_nationality, et_religion;
    Spinner spinner_civilStatus;
    ArrayAdapter arrayAdapter_civilStatus;
    Button btn_save;

    DatePickerFragment datePickerFragment;
    SimpleDateFormat simpleDateFormat;
    Calendar calendar;
    private boolean isInformationChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings_patient);
        init();
    }

    void init() {
        isInformationChanged = false;
        intent = getIntent();
        user = intent.getExtras().getParcelable("user");

        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);

        et_address = (EditText) findViewById(R.id.et_address);
        et_height = (EditText) findViewById(R.id.et_height);
        et_weight = (EditText) findViewById(R.id.et_weight);
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
        et_height.setText(user.height);
        et_weight.setText(user.weight);
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
            editText.requestFocus();
            isThereNoError = false;
        }

        return isThereNoError;
    }

    boolean validateHeightAndWeight(EditText editText, String text, String type) {
        boolean isThereNoError = true;
        double vald;

        if (!text.isEmpty()) {
            switch (type) {
                case "height":
                    vald = Double.parseDouble(text);
                    if (vald < 50 || vald > 500) {
                        editText.setError(getString(R.string.value_is_out_of_range));
                        editText.requestFocus();
                        isThereNoError = false;
                    }
                    break;
                case "weight":
                    vald = Double.parseDouble(text);
                    if (vald < 10 || vald > 300) {
                        editText.setError(getString(R.string.value_is_out_of_range));
                        editText.requestFocus();
                        isThereNoError = false;
                    }
                    break;
            }
        }

        return isThereNoError;

    }

    void onClickSave() {
        String strAddress = et_address.getText().toString().trim(), strNationality = et_nationality.getText().toString().trim(),
                strReligion = et_religion.getText().toString().trim(), strOccupation = et_occupation.getText().toString().trim(),
                strCivilStatus = spinner_civilStatus.getSelectedItem().toString(), strHeight = et_height.getText().toString(),
                strWeight = et_weight.getText().toString();

        if (validateEditText(et_address, strAddress) && validateEditText(et_address, strAddress) &&
                validateEditText(et_address, strAddress) && validateEditText(et_address, strAddress) &&
                validateHeightAndWeight(et_height, strHeight, "height") && validateHeightAndWeight(et_weight, strWeight, "weight")) {
            sendData(strAddress, strNationality, strReligion, strOccupation, strCivilStatus, strHeight, strWeight);
        }
    }

    void sendData(final String strAddress, final String strNationality, final String strReligion, final String strOccupation, final String strCivilStatus,
                  final String height, final String weight) {
        progressDialog.show("Saving...");
        try {
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_SETTINGS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    String code = jsonObject.getString("code");
                                    if (code.equals("success") || code.equals("empty")) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.record_updated), Toast.LENGTH_SHORT).show();
                                        user.address = strAddress;
                                        user.height = height;
                                        user.weight = weight;
                                        user.nationality = strNationality;
                                        user.religion = strReligion;
                                        user.occupation = strOccupation;
                                        user.civilStatus = strCivilStatus;
                                        user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                                        isInformationChanged = true;
                                        setUpChanged();
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
                    params.put("action", "updatePatientInformation");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(user.patient_id));
                    params.put("address", strAddress);
                    params.put("height", (height.equals("")) ? "0" : height);
                    params.put("weight", (weight.equals("")) ? "0" : weight);
                    params.put("nationality", strNationality);
                    params.put("religion", strReligion);
                    params.put("occupation", strOccupation);
                    params.put("civil_status", strCivilStatus);
                    params.put("birthday", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));

                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setUpChanged() {
        Intent intent = new Intent();
        intent.putExtra("user", user);
        intent.putExtra("isInformationChanged", isInformationChanged);
        setResult(RESULT_OK, intent);
        finish();
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
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        setUpChanged();
    }
}
