package com.example.kinit.e_medicalrecord.Fragments.Medical_Prescription;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Drug;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Open_Add_Drug;
import com.example.kinit.e_medicalrecord.Enum.Query_Type;
import com.example.kinit.e_medicalrecord.R;

public class Fragment_Drug extends Fragment implements View.OnClickListener {
    //View
    View rootView;

    //Widgets
    EditText et_drug, et_strength, et_amount, et_why, et_many, et_refill;
    Spinner spinner_route, spinner_frequency;
    ArrayAdapter arrayAdapterRoute, arrayAdapterFrequency;
    Button btn_add;

    //Classes
    Bus_Open_Add_Drug busOpenAddDrug;
    Query_Type queryType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            queryType = Query_Type.values()[bundle.getInt("ordinal")];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_drug, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        et_drug = (EditText) rootView.findViewById(R.id.et_drug);
        et_strength = (EditText) rootView.findViewById(R.id.et_strength);
        et_amount = (EditText) rootView.findViewById(R.id.et_amount);
        et_why = (EditText) rootView.findViewById(R.id.et_why);
        et_many = (EditText) rootView.findViewById(R.id.et_many);
        et_refill = (EditText) rootView.findViewById(R.id.et_refill);

        spinner_route = (Spinner) rootView.findViewById(R.id.spinner_route);
        arrayAdapterRoute = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_route, R.layout.support_simple_spinner_dropdown_item);
        spinner_route.setAdapter(arrayAdapterRoute);

        spinner_frequency = (Spinner) rootView.findViewById(R.id.spinner_frequency);
        arrayAdapterFrequency = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_frequency, R.layout.support_simple_spinner_dropdown_item);
        spinner_frequency.setAdapter(arrayAdapterFrequency);

        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        if(queryType != null) {
            if(queryType == Query_Type.UPDATE) {
                setText();
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
            if(queryType != null) {
                if (queryType == Query_Type.UPDATE) {
                    BusStation.getBus().post(new Bus_Drug(drug, strength, amount, route, frequency, why, many, refill, queryType, busOpenAddDrug.position));
                }
            } else {
                BusStation.getBus().post(new Bus_Drug(drug, strength, amount, route, frequency, why, many, refill));
            }
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

    public void set_drug(Bus_Open_Add_Drug busOpenAddDrug) {
        this.busOpenAddDrug = busOpenAddDrug;
    }

    void setText() {
        et_drug.setText(busOpenAddDrug.drug);
        et_strength.setText(busOpenAddDrug.strength);
        et_amount.setText(busOpenAddDrug.amount);
        et_why.setText(busOpenAddDrug.why);
        et_many.setText(busOpenAddDrug.many);
        et_refill.setText(busOpenAddDrug.refill);
        spinner_route.setSelection(arrayAdapterRoute.getPosition(busOpenAddDrug.route));
        spinner_frequency.setSelection(arrayAdapterFrequency.getPosition(busOpenAddDrug.frequency));
        btn_add.setText("UPDATE");
    }
}
