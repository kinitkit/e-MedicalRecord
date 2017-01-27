package com.example.kinit.e_medicalrecord.Medical_Prescription.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.kinit.e_medicalrecord.Medical_Prescription.Bus.Bus_Drug;
import com.example.kinit.e_medicalrecord.General.Enum.Query_Type;
import com.example.kinit.e_medicalrecord.R;

public class Drug_Form extends AppCompatActivity implements View.OnClickListener {

    //Widgets
    EditText et_drug, et_strength, et_amount, et_why, et_many, et_refill;
    Spinner spinner_route, spinner_frequency;
    ArrayAdapter arrayAdapterRoute, arrayAdapterFrequency;
    Button btn_add;

    //Classes
    Bus_Drug busDrug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_form);
        init();
    }

    void init() {
        busDrug = getIntent().getExtras().getParcelable("busDrug");
        et_drug = (EditText) findViewById(R.id.et_drug);
        et_strength = (EditText) findViewById(R.id.et_strength);
        et_amount = (EditText) findViewById(R.id.et_amount);
        et_why = (EditText) findViewById(R.id.et_why);
        et_many = (EditText) findViewById(R.id.et_many);
        et_refill = (EditText) findViewById(R.id.et_refill);

        spinner_route = (Spinner) findViewById(R.id.spinner_route);
        arrayAdapterRoute = ArrayAdapter.createFromResource(this, R.array.spinner_route, R.layout.support_simple_spinner_dropdown_item);
        spinner_route.setAdapter(arrayAdapterRoute);

        spinner_frequency = (Spinner) findViewById(R.id.spinner_frequency);
        arrayAdapterFrequency = ArrayAdapter.createFromResource(this, R.array.spinner_frequency, R.layout.support_simple_spinner_dropdown_item);
        spinner_frequency.setAdapter(arrayAdapterFrequency);

        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        if (busDrug.queryType != null) {
            if (busDrug.queryType == Query_Type.UPDATE) {
                //setText();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                onClick_add();
                break;
        }
    }

    void onClick_add() {
        String drug = et_drug.getText().toString().trim(), strength = et_strength.getText().toString().trim(),
                amount = et_amount.getText().toString().trim(), why = et_why.getText().toString().trim(),
                many = et_many.getText().toString().trim(),
                refill = et_refill.getText().toString().trim(), route = spinner_route.getSelectedItem().toString(),
                frequency = spinner_frequency.getSelectedItem().toString();

        many = validateNumber(many);
        refill = validateNumber(refill);

        if (!validateEditText(et_drug, drug) && !validateEditText(et_strength, strength) && !validateEditText(et_amount, amount) &&
                !validateEditText(et_many, many)) {
            if (busDrug.queryType != null) {
                if (busDrug.queryType == Query_Type.UPDATE) {
                    //BusStation.getBus().post(new Bus_Drug(drug, strength, amount, route, frequency, why, many, refill, busDrug.queryType, busDrug.position));
                } else if (busDrug.queryType == Query_Type.INSERT) {
                    Intent intent = new Intent();
                    intent.putExtra("busDrug", new Bus_Drug(drug, strength, amount, route, frequency, why, many, refill));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
            //BusStation.getBus().post(new Bus_Drug(drug, strength, amount, route, frequency, why, many, refill));
        }
    }

    boolean validateEditText(EditText editText, String inp) {
        boolean isThereError = false;

        if (TextUtils.isEmpty(inp)) {
            editText.setError("Required Field");
            isThereError = true;
        }

        return isThereError;
    }

    String validateNumber(String inp) {
        return (inp.isEmpty()) ? "0" : String.valueOf(Integer.parseInt(inp));
    }
}
