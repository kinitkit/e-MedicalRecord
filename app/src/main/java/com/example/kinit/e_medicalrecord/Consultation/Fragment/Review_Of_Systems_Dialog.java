package com.example.kinit.e_medicalrecord.Consultation.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.kinit.e_medicalrecord.Consultation.Bus.Bus_ConsultationROS;
import com.example.kinit.e_medicalrecord.Consultation.Class.ArrayAdapter_Strings;
import com.example.kinit.e_medicalrecord.Consultation.Class.Consultation_ROS;
import com.example.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.General.Enum.Query_Type;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class Review_Of_Systems_Dialog extends DialogFragment implements AdapterView.OnItemSelectedListener {
    AlertDialog builder;
    Spinner spinner_category, spinner_examination;
    EditText et_remarks;

    ArrayAdapter<String> arrayAdapter_category, arrayAdapter_examination;
    ArrayAdapter_Strings arrayAdapterStrings;
    Query_Type queryType;
    Consultation_ROS consultationRos;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_review_of_systems, null);
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

        spinner_category = (Spinner) view.findViewById(R.id.spinner_category);
        spinner_examination = (Spinner) view.findViewById(R.id.spinner_examination);
        et_remarks = (EditText) view.findViewById(R.id.et_remarks);

        arrayAdapter_category = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayAdapterStrings.arrayCategory);
        setArrayAdapterExamination(arrayAdapterStrings.arrayGeneral);
        spinner_category.setAdapter(arrayAdapter_category);
        spinner_category.setOnItemSelectedListener(this);

        return builder;
    }

    public void setArrayList(ArrayAdapter_Strings arrayAdapterStrings) {
        this.arrayAdapterStrings = arrayAdapterStrings;
    }

    void setArrayAdapterExamination(ArrayList<String> arrayList) {
        arrayAdapter_examination = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayList);
        spinner_examination.setAdapter(arrayAdapter_examination);
    }

    boolean validateEditText(EditText editText, String text) {
        boolean isThereNoError = true;

        if (text.isEmpty()) {
            editText.setError(getString(R.string.required_field));
            isThereNoError = false;
        }

        return isThereNoError;
    }

    void onClickOk() {
        String strRemarks = et_remarks.getText().toString().trim();

        if (validateEditText(et_remarks, strRemarks)) {
            if (consultationRos == null) {
                queryType = Query_Type.INSERT;
                consultationRos = new Consultation_ROS(strRemarks, getRosId(), spinner_examination.getSelectedItem().toString(),
                        getCategoryId(), spinner_category.getSelectedItem().toString());
            } else {
                queryType = Query_Type.UPDATE;
                consultationRos.remarks = strRemarks;
                consultationRos.rosId = getRosId();
                consultationRos.item = spinner_examination.getSelectedItem().toString();
                consultationRos.rosCategoryId = getCategoryId();
                consultationRos.category = spinner_category.getSelectedItem().toString();
            }
            sendData();
        }
    }

    void sendData() {
        BusStation.getBus().post(new Bus_ConsultationROS(queryType, 0, consultationRos));
        builder.dismiss();
    }

    int getRosId() {
        switch (arrayAdapter_category.getPosition(spinner_category.getSelectedItem().toString())) {
            case 0:
                return arrayAdapterStrings.arrayGeneralId.get(arrayAdapter_examination.getPosition(spinner_examination.getSelectedItem().toString()));
            case 1:
                return arrayAdapterStrings.arraySkinBreastId.get(arrayAdapter_examination.getPosition(spinner_examination.getSelectedItem().toString()));
            case 2:
                return arrayAdapterStrings.arrayEyesEarsId.get(arrayAdapter_examination.getPosition(spinner_examination.getSelectedItem().toString()));
            case 3:
                return arrayAdapterStrings.arrayCardioId.get(arrayAdapter_examination.getPosition(spinner_examination.getSelectedItem().toString()));
            case 4:
                return arrayAdapterStrings.arrayRespiId.get(arrayAdapter_examination.getPosition(spinner_examination.getSelectedItem().toString()));
            case 5:
                return arrayAdapterStrings.arrayGastroId.get(arrayAdapter_examination.getPosition(spinner_examination.getSelectedItem().toString()));
            case 6:
                return arrayAdapterStrings.arrayGenitoId.get(arrayAdapter_examination.getPosition(spinner_examination.getSelectedItem().toString()));
            case 7:
                return arrayAdapterStrings.arrayMusculoId.get(arrayAdapter_examination.getPosition(spinner_examination.getSelectedItem().toString()));
            case 8:
                return arrayAdapterStrings.arrayNeuroId.get(arrayAdapter_examination.getPosition(spinner_examination.getSelectedItem().toString()));
            case 9:
                return arrayAdapterStrings.arrayAllergicId.get(arrayAdapter_examination.getPosition(spinner_examination.getSelectedItem().toString()));
        }
        return 0;
    }

    int getCategoryId() {
        return arrayAdapterStrings.arrayCategoryId.get(arrayAdapter_category.getPosition(spinner_category.getSelectedItem().toString()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (arrayAdapter_category.getPosition(spinner_category.getSelectedItem().toString())) {
            case 0:
                setArrayAdapterExamination(arrayAdapterStrings.arrayGeneral);
                break;
            case 1:
                setArrayAdapterExamination(arrayAdapterStrings.arraySkinBreast);
                break;
            case 2:
                setArrayAdapterExamination(arrayAdapterStrings.arrayEyesEars);
                break;
            case 3:
                setArrayAdapterExamination(arrayAdapterStrings.arrayCardio);
                break;
            case 4:
                setArrayAdapterExamination(arrayAdapterStrings.arrayRespi);
                break;
            case 5:
                setArrayAdapterExamination(arrayAdapterStrings.arrayGastro);
                break;
            case 6:
                setArrayAdapterExamination(arrayAdapterStrings.arrayGenito);
                break;
            case 7:
                setArrayAdapterExamination(arrayAdapterStrings.arrayMusculo);
                break;
            case 8:
                setArrayAdapterExamination(arrayAdapterStrings.arrayNeuro);
                break;
            case 9:
                setArrayAdapterExamination(arrayAdapterStrings.arrayAllergic);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
