package com.example.kinit.e_medicalrecord.Fragments.Laboratory;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kinit.e_medicalrecord.Adapters.RecyclerViewAdapter_LabResult_Container;
import com.example.kinit.e_medicalrecord.Classes.Lab_Chemistry;
import com.example.kinit.e_medicalrecord.Classes.Lab_Fecalysis;
import com.example.kinit.e_medicalrecord.Classes.Lab_Hematology;
import com.example.kinit.e_medicalrecord.Classes.Lab_Urinalysis;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class Fragment_Laboratory_Result_Container extends Fragment {
    //View
    View rootView;

    //Widget
    RecyclerView recyclerView;
    RecyclerViewAdapter_LabResult_Container recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;

    //Classes
    ArrayList<Lab_Chemistry> labChemistries;
    ArrayList<Lab_Fecalysis> labFecalysises;
    ArrayList<Lab_Hematology> labHematologies;
    ArrayList<Lab_Urinalysis> labUrinalysises;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_laboratory_result_container, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        loadToRecyclerView();
    }

    public void setLabChemistries(ArrayList<Lab_Chemistry> labChemistries){
        this.labChemistries = labChemistries;
        recyclerViewAdapter_Content = new RecyclerViewAdapter_LabResult_Container();
        recyclerViewAdapter_Content.setLabChemistries(labChemistries);
    }
    public void setLabFecalysises(ArrayList<Lab_Fecalysis> labFecalysises){
        this.labFecalysises = labFecalysises;
        recyclerViewAdapter_Content = new RecyclerViewAdapter_LabResult_Container();
        recyclerViewAdapter_Content.setLabFecalysis(labFecalysises);
    }
    public void setLabHematologies(ArrayList<Lab_Hematology> labHematologies){
        this.labHematologies = labHematologies;
        recyclerViewAdapter_Content = new RecyclerViewAdapter_LabResult_Container();
        recyclerViewAdapter_Content.setLabHematologies(labHematologies);
    }
    public void setLabUrinalysises(ArrayList<Lab_Urinalysis> labUrinalysises){
        this.labUrinalysises = labUrinalysises;
        recyclerViewAdapter_Content = new RecyclerViewAdapter_LabResult_Container();
        recyclerViewAdapter_Content.setLabUrinalysises(labUrinalysises);
    }

    void loadToRecyclerView() {
        recyclerViewLayoutM_Content = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView.setAdapter(recyclerViewAdapter_Content);
    }

}
