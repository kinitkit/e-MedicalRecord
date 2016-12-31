package com.example.kinit.e_medicalrecord.Fragments.Medical_Prescription;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.RecyclerViewAdapter_SearchMyPhysician;
import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.RecyclerViewAdapter_Tagged_MedPrescription;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.My_Physician.Bus_Search_Physician;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.Medical_Prescription.Tagged_Physician_List;
import com.example.kinit.e_medicalrecord.Enum.My_Physician_Button_Mode;
import com.example.kinit.e_medicalrecord.R;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class Fragment_Search_Physician extends Fragment {
    //View
    View rootView;

    String name;
    int user_id, patient_id, position;

    ArrayList<Tagged_Physician_List> taggedPhysicianLists;

    //Widgets
    RecyclerView recyclerView_Content;
    RecyclerViewAdapter_Tagged_MedPrescription recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;

    //App
    Custom_ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_physician_medicalprescription, container, false);
        recyclerView_Content = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new Custom_ProgressDialog(getActivity());
    }

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_Tagged_MedPrescription(taggedPhysicianLists, My_Physician_Button_Mode.ADD);
        recyclerViewLayoutM_Content = new LinearLayoutManager(getActivity());
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
    }

    void fetchData() {

    }

    void addPhysician() {
        progressDialog.show("Loading...");
    }

    @Override
    public void onPause() {
        super.onPause();
        BusStation.getBus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusStation.getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rootView = null;
    }

    @Subscribe
    public void isSearchClicked(Bus_Search_Physician busSearchPhysician) {
        this.name = busSearchPhysician.name;
        this.user_id = busSearchPhysician.user_id;
        this.patient_id = busSearchPhysician.patient_id;
        fetchData();
    }

    @Subscribe
    public void onClickAdd() {

    }
}
