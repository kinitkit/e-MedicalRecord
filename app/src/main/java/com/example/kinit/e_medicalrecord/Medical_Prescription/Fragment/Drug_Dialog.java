package com.example.kinit.e_medicalrecord.Medical_Prescription.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.General.Enum.Query_Type;
import com.example.kinit.e_medicalrecord.Medical_Prescription.Bus.Bus_DrugMedForm;
import com.example.kinit.e_medicalrecord.Medical_Prescription.Bus.Bus_Drug_OnLongClick;
import com.example.kinit.e_medicalrecord.Medical_Prescription.Class.Drug;
import com.example.kinit.e_medicalrecord.R;


public class Drug_Dialog extends DialogFragment {
    AlertDialog builder;

    //Widgets
    EditText et_drug, et_strength, et_amount, et_why, et_quantity;
    Spinner spinner_route, spinner_frequency;
    ArrayAdapter arrayAdapterRoute, arrayAdapterFrequency;
    Query_Type queryType;
    int position;
    Drug drug;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_drug_dialog, null);
        builder = new AlertDialog.Builder(getActivity())
                .setTitle("Review of Systems")
                .setView(view)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null)
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickOk();
                    }
                });
            }
        });

        et_drug = (EditText) view.findViewById(R.id.et_drug);
        et_strength = (EditText) view.findViewById(R.id.et_strength);
        et_amount = (EditText) view.findViewById(R.id.et_amount);
        et_why = (EditText) view.findViewById(R.id.et_why);
        et_quantity = (EditText) view.findViewById(R.id.et_many);

        spinner_route = (Spinner) view.findViewById(R.id.spinner_route);
        arrayAdapterRoute = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_route, R.layout.support_simple_spinner_dropdown_item);
        spinner_route.setAdapter(arrayAdapterRoute);

        spinner_frequency = (Spinner) view.findViewById(R.id.spinner_frequency);
        arrayAdapterFrequency = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_frequency, R.layout.support_simple_spinner_dropdown_item);
        spinner_frequency.setAdapter(arrayAdapterFrequency);

        if (drug != null) {
            et_drug.setText(drug.drug);
            et_strength.setText(drug.strength);
            et_amount.setText(drug.dosage);
            et_why.setText(drug.why);
            et_quantity.setText(drug.quantity);
            spinner_route.setSelection(arrayAdapterRoute.getPosition(drug.route));
            spinner_frequency.setSelection(arrayAdapterFrequency.getPosition(drug.frequency));
        } else {
            position = 0;
            queryType = Query_Type.INSERT;
            et_drug.setText("");
            et_strength.setText("");
            et_amount.setText("");
            et_why.setText("");
            et_quantity.setText("");
            spinner_route.setSelection(0);
            spinner_frequency.setSelection(0);
        }

        return builder;
    }

    void onClickOk() {
        String drug = et_drug.getText().toString().trim(), strength = et_strength.getText().toString().trim(),
                amount = et_amount.getText().toString().trim(), why = et_why.getText().toString().trim(),
                quantity = et_quantity.getText().toString().trim(), route = spinner_route.getSelectedItem().toString(),
                frequency = spinner_frequency.getSelectedItem().toString();

        quantity = validateNumber(quantity);

        if (!validateEditText(et_drug, drug) && !validateEditText(et_strength, strength) && !validateEditText(et_amount, amount) &&
                !validateEditText(et_quantity, quantity)) {
            sendData(drug, strength, amount, route, frequency, why, quantity);
        }
    }

    void sendData(String drug, String strength, String amount, String route, String frequency, String why, String quantity) {
        BusStation.getBus().post(new Bus_DrugMedForm(queryType, position, new Drug(drug, strength, amount, route, frequency, why, quantity)));
        builder.dismiss();
        this.drug = null;
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

    public void setFields(Bus_Drug_OnLongClick busDrugOnLongClick) {
        this.drug = busDrugOnLongClick.drug;
        this.position =busDrugOnLongClick.position;
        this.queryType = Query_Type.UPDATE;
    }
}
