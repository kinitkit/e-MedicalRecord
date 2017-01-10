package com.example.kinit.e_medicalrecord.Activities.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.Classes.User.User;
import com.example.kinit.e_medicalrecord.R;

import org.w3c.dom.Text;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    TextView tv_personalInformation, tv_medicalStaff, tv_password, tv_patient;
    User user;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    void init() {

        user = getIntent().getExtras().getParcelable("user");
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_personalInformation = (TextView) findViewById(R.id.tv_personalInformation);
        tv_medicalStaff = (TextView) findViewById(R.id.tv_medicalStaff);
        tv_password = (TextView) findViewById(R.id.tv_password);
        tv_patient = (TextView) findViewById(R.id.tv_patient);

        tv_personalInformation.setOnClickListener(this);
        tv_medicalStaff.setOnClickListener(this);
        tv_password.setOnClickListener(this);
        tv_patient.setOnClickListener(this);
    }

    void setUpIntent() {
        intent.putExtra("user", user);
        startActivityForResult(intent, 1);
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
            case R.id.tv_medicalStaff:
                //setUpIntent(2);
                break;
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
                user = data.getExtras().getParcelable("user");
            }
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
