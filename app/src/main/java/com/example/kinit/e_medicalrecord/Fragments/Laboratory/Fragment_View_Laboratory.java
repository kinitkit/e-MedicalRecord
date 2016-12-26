package com.example.kinit.e_medicalrecord.Fragments.Laboratory;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Adapters.ListView.ListViewAdapter_Laboratory_Fields;
import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.RecyclerViewAdapter_LabResult_Container;
import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.RecyclerViewAdapter_LaboratoryFields;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Chemistry;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Fecalysis;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Hematology;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Urinalysis;
import com.example.kinit.e_medicalrecord.Enum.Laboratory_Tests;
import com.example.kinit.e_medicalrecord.R;

public class Fragment_View_Laboratory extends Fragment {
    //View
    View rootView;

    //Widgets
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;

    //Enum
    Laboratory_Tests enum_laboratoryTests;
    //Classes
    Lab_Chemistry labChemistry;
    Lab_Fecalysis labFecalysis;
    Lab_Hematology labHematology;
    Lab_Urinalysis labUrinalysis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        switch (enum_laboratoryTests) {
            case BLOOD_CHEMISTRY:
                rootView = inflater.inflate(R.layout.layout_view_chemistry, container, false);
                break;
            case FECALYSIS:
                rootView = inflater.inflate(R.layout.layout_view_fecalysis, container, false);
                break;
            case HEMATOLOGY:
                rootView = inflater.inflate(R.layout.layout_view_hematology, container, false);
                break;
            case URINALYSIS:
                rootView = inflater.inflate(R.layout.layout_view_urinalysis, container, false);
                break;
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        recyclerViewLayoutM_Content = new LinearLayoutManager(getActivity());

        switch (enum_laboratoryTests) {
            case BLOOD_CHEMISTRY:
                init_labChemistry();
                break;
            case FECALYSIS:
                break;
            case HEMATOLOGY:
                break;
            case URINALYSIS:
                break;
        }
    }

    public void setLab(Laboratory_Tests enum_laboratoryTests, Object object) {
        this.enum_laboratoryTests = enum_laboratoryTests;
        switch (enum_laboratoryTests) {
            case BLOOD_CHEMISTRY:
                this.labChemistry = (Lab_Chemistry) object;
                break;
            case FECALYSIS:
                this.labFecalysis = (Lab_Fecalysis) object;
                break;
            case HEMATOLOGY:
                this.labHematology = (Lab_Hematology) object;
                break;
            case URINALYSIS:
                this.labUrinalysis = (Lab_Urinalysis) object;
                break;
        }
    }

    void init_labChemistry() {
        loadDetails(labChemistry.physicianName, labChemistry.labName, labChemistry.datePerformed);

        RecyclerView recyclerView_LabTest = (RecyclerView) rootView.findViewById(R.id.recyclerView_labTest);
        RecyclerViewAdapter_LaboratoryFields recyclerViewAdapter_labTest = new RecyclerViewAdapter_LaboratoryFields();
        recyclerViewAdapter_labTest.addItem("FBS:", labChemistry.fbs);
        recyclerViewAdapter_labTest.addItem("Creatine:", labChemistry.creatine);
        recyclerViewAdapter_labTest.addItem("Triglycerides:", labChemistry.triglycerides);
        recyclerViewAdapter_labTest.addItem("HDL:", labChemistry.hdl);
        recyclerViewAdapter_labTest.addItem("LDL:", labChemistry.ldl);
        recyclerViewAdapter_labTest.addItem("Uric Acid:", labChemistry.uricAcid);
        recyclerViewAdapter_labTest.addItem("SGPT / ALAT:", labChemistry.sgpt_alat);
        recyclerViewAdapter_labTest.addItem("Sodium:", labChemistry.sodium);
        recyclerViewAdapter_labTest.addItem("Potassium:", labChemistry.potassium);
        recyclerViewAdapter_labTest.addItem("Calcium:", labChemistry.calcium);
        loadToRecyclerView(recyclerView_LabTest, recyclerViewAdapter_labTest);

        loadRemarks(labChemistry.remark);
    }

    void loadDetails(String physicianName, String labName, String datePerformed) {
        TextView tv_physicianName = (TextView) rootView.findViewById(R.id.tv_physicianName), tv_labName = (TextView) rootView.findViewById(R.id.tv_labName),
                tv_datePerformed = (TextView) rootView.findViewById(R.id.tv_date);

        tv_physicianName.setText(physicianName);
        tv_labName.setText(labName);
        tv_datePerformed.setText(datePerformed);
    }

    void loadToRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView.setAdapter(adapter);
    }

    void loadRemarks(String remarks) {
        TextView tv_remarks = (TextView) rootView.findViewById(R.id.tv_remarks);
        tv_remarks.setText(remarks);
    }

}
