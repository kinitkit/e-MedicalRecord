package com.example.kinit.e_medicalrecord.Activities.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.User.User;
import com.example.kinit.e_medicalrecord.R;

public class Settings_Container extends AppCompatActivity {

    Intent intent;
    User user;
    int ordinal;
    Custom_AlertDialog alertDialog;
    Custom_ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        user = intent.getExtras().getParcelable("user");
        ordinal = intent.getIntExtra("ordinal", 0);

        switch (ordinal) {
            case 0:
                setContentView(R.layout.layout_settings_personal_information);
                break;
            case 1:
                setContentView(R.layout.layout_settings_patient);
                break;
            case 2:
                setContentView(R.layout.layout_settings_medical_staff_information);
                break;
            case 3:
                setContentView(R.layout.layout_settings_password);
                break;
        }
    }

    void init(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        alertDialog = new Custom_AlertDialog(this);
        progressDialog = new Custom_ProgressDialog(this);
    }

    void initPersonalInformation() {
        init("Change Personal Information");
        EditText et_fName = (EditText) findViewById(R.id.et_fName), et_mName = (EditText) findViewById(R.id.et_mName),
                et_lName = (EditText) findViewById(R.id.et_lName), et_contactNo = (EditText) findViewById(R.id.et_contactNo);
        ArrayAdapter arrayAdapter_gender = ArrayAdapter.createFromResource(this, R.array.spinner_gender_options, R.layout.support_simple_spinner_dropdown_item);
        Spinner spinner_gender = (Spinner) findViewById(R.id.spinner_gender);
        spinner_gender.setAdapter(arrayAdapter_gender);


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
