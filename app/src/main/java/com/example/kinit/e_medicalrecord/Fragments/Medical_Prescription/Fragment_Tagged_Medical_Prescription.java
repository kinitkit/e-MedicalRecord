package com.example.kinit.e_medicalrecord.Fragments.Medical_Prescription;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.RecyclerViewAdapter_SearchMyPhysician;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.General.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Open_MedicalPrescription_Tagged;
import com.example.kinit.e_medicalrecord.BusStation.My_Physician.Bus_Search_Physician;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.My_Physician.Physician_List;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class Fragment_Tagged_Medical_Prescription extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    //View
    View rootView;

    //Primitive Data Types
    int patient_id;
    int position;

    //Classes
    ArrayList<Physician_List> physicianLists;
    Viewer viewer;
    Bus_Open_MedicalPrescription_Tagged busOpenMedicalPrescriptionTagged;

    //Widgets
    //RecyclerView
    RecyclerView recyclerView_Content;
    RecyclerViewAdapter_SearchMyPhysician recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;
    //SwipeRefreshLayout
    SwipeRefreshLayout swipeRefreshLayout;

    //App
    Custom_ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (viewer == null) {
            rootView = inflater.inflate(R.layout.fragment_fragment_tagged_medical_prescription, container, false);
        }
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        progressDialog = new Custom_ProgressDialog(getActivity());
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView_Content = (RecyclerView) rootView.findViewById(R.id.recyclerView);
    }

    @Override
    public void onRefresh() {

    }
}
