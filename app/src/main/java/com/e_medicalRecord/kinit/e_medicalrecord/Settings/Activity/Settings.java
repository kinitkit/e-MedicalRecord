package com.e_medicalRecord.kinit.e_medicalrecord.Settings.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.e_medicalRecord.kinit.e_medicalrecord.Profile.Class.User;
import com.e_medicalRecord.kinit.e_medicalrecord.R;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    TextView tv_personalInformation, tv_password, tv_patient; //tv_medicalStaff
    User user;
    boolean isInformationChanged, isImageChanged;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    void init() {
        isInformationChanged = false;
        isInformationChanged = false;
        user = getIntent().getExtras().getParcelable("user");
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_personalInformation = (TextView) findViewById(R.id.tv_personalInformation);
        //tv_medicalStaff = (TextView) findViewById(R.id.tv_medicalStaff);
        tv_password = (TextView) findViewById(R.id.tv_password);
        tv_patient = (TextView) findViewById(R.id.tv_patient);

        tv_personalInformation.setOnClickListener(this);
        //tv_medicalStaff.setOnClickListener(this);
        tv_password.setOnClickListener(this);
        tv_patient.setOnClickListener(this);

        if(user.medical_staff_id != 0){
            tv_patient.setVisibility(View.GONE);
        } else {
            tv_patient.setVisibility(View.VISIBLE);
        }
    }

    void setUpIntent() {
        intent.putExtra("user", user);
        startActivityForResult(intent, 1);
    }

    void setUpChanged(){
        Intent intent = new Intent();
        intent.putExtra("isInformationChanged", isInformationChanged);
        intent.putExtra("isImageChanged", isImageChanged);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_personalInformation:
                intent = new Intent(this, Settings_Personal_Information.class);
                setUpIntent();
                break;
            case R.id.tv_patient:
                intent = new Intent(this, Settings_Patient_Information.class);
                setUpIntent();
                break;
            //case R.id.tv_medicalStaff:
                //setUpIntent(2);
            //    break;
            case R.id.tv_password:
                intent = new Intent(this, Settings_Password.class);
                setUpIntent();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if(data.getBooleanExtra("isInformationChanged", false)){
                    user = data.getExtras().getParcelable("user");
                    isInformationChanged = true;
                }

                if(data.hasExtra("isImageChanged")){
                    if(data.getBooleanExtra("isImageChanged", false)){
                        isImageChanged = true;
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setUpChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
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
