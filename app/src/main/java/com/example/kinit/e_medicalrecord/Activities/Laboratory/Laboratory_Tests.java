package com.example.kinit.e_medicalrecord.Activities.Laboratory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.Classes.User.Patient;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.R;

public class Laboratory_Tests extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    //Classes
    Viewer viewer;
    Patient patient;

    //Widgets
    TextView tv_chemistry, tv_fecalysis, tv_hematology, tv_urinalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laboratory_tests);
        init();
    }

    void init() {
        patient = getIntent().getExtras().getParcelable("patient");
        viewer = getIntent().getExtras().getParcelable("viewer");

        tv_chemistry = (TextView) findViewById(R.id.tv_chemistry);
        tv_fecalysis = (TextView) findViewById(R.id.tv_fecalysis);
        tv_hematology = (TextView) findViewById(R.id.tv_hematology);
        tv_urinalysis = (TextView) findViewById(R.id.tv_urinalysis);
        tv_chemistry.setOnClickListener(this);
        tv_fecalysis.setOnClickListener(this);
        tv_hematology.setOnClickListener(this);
        tv_urinalysis.setOnClickListener(this);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Laboratory Tests");
        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_chemistry:
                setActivity(com.example.kinit.e_medicalrecord.Enum.Laboratory_Tests.BLOOD_CHEMISTRY.ordinal());
                break;
            case R.id.tv_fecalysis:
                setActivity(com.example.kinit.e_medicalrecord.Enum.Laboratory_Tests.FECALYSIS.ordinal());
                break;
            case R.id.tv_hematology:
                setActivity(com.example.kinit.e_medicalrecord.Enum.Laboratory_Tests.HEMATOLOGY.ordinal());
                break;
            case R.id.tv_urinalysis:
                setActivity(com.example.kinit.e_medicalrecord.Enum.Laboratory_Tests.URINALYSIS.ordinal());
                break;
        }
    }

    void setActivity(int ordinal){
        intent = new Intent(this, Laboratory_Results.class);
        intent.putExtra("patient", patient);
        intent.putExtra("viewer", viewer);
        intent.putExtra("ordinal", ordinal);
        startActivity(intent);
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
