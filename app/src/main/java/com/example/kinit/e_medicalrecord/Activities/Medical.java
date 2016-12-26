package com.example.kinit.e_medicalrecord.Activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Family_History_Item;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Past_Medical_History_Item;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Social_History_Item;
import com.example.kinit.e_medicalrecord.BusStation.Pop_BackStack;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Resume_Fragment;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Surgical_History_Item;
import com.example.kinit.e_medicalrecord.Enum.Medical_Transaction;
import com.example.kinit.e_medicalrecord.Enum.Query_Type;
import com.example.kinit.e_medicalrecord.Fragments.Fragment_Update_Family_History;
import com.example.kinit.e_medicalrecord.Fragments.Fragment_Update_Past_Medical_History;
import com.example.kinit.e_medicalrecord.Fragments.Fragment_Update_Social_History;
import com.example.kinit.e_medicalrecord.Fragments.Fragment_Update_Surgical_History;
import com.example.kinit.e_medicalrecord.Fragments.Fragment_Family_History;
import com.example.kinit.e_medicalrecord.Fragments.Fragment_Medical_Result_Prescription_Laboratory;
import com.example.kinit.e_medicalrecord.Fragments.Fragment_Past_Medical_History;
import com.example.kinit.e_medicalrecord.Fragments.Fragment_Social_History;
import com.example.kinit.e_medicalrecord.Fragments.Fragment_Surgical_History;
import com.example.kinit.e_medicalrecord.R;
import com.squareup.otto.Subscribe;

public class Medical extends AppCompatActivity {
    //Primitive Data Types
    int patient_id;
    String patient_name;

    //Widgets
    Toolbar toolbar;

    //App
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);
        init();
    }

    void init() {
        //FragmentManager
        fragmentManager = getSupportFragmentManager();
        int ordinal = getIntent().getIntExtra("ordinal", 0);
        patient_id = getIntent().getIntExtra("patient_id", 0);
        patient_name = getIntent().getStringExtra("patient_name");
        assignFragment(Medical_Transaction.values()[ordinal]);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(patient_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    void assignFragment(Medical_Transaction transaction) {
        Bundle args = new Bundle();
        args.putInt("patient_id", patient_id);
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (transaction) {
            case LAB_RESULT:
            case MEDICAL_PRESCRIPTION:
                Fragment_Medical_Result_Prescription_Laboratory fragmentMedicalResultPrescriptionLaboratory = new Fragment_Medical_Result_Prescription_Laboratory();
                args.putInt("ordinal", transaction.ordinal());
                fragmentMedicalResultPrescriptionLaboratory.setArguments(args);
                fragmentTransaction.add(R.id.frameLayout, fragmentMedicalResultPrescriptionLaboratory).commit();
                break;
            case FAMILY_HISTORY:
                Fragment_Family_History fragmentFamilyHistory = new Fragment_Family_History();
                fragmentFamilyHistory.setArguments(args);
                fragmentTransaction.add(R.id.frameLayout, fragmentFamilyHistory).commit();
                //BusStation.getBus().post(patient_id);
                break;
            case SURGICAL_HISTORY:
                Fragment_Surgical_History fragmentSurgicalHistory = new Fragment_Surgical_History();
                fragmentSurgicalHistory.setArguments(args);
                fragmentTransaction.add(R.id.frameLayout, fragmentSurgicalHistory).commit();
                break;
            case PAST_MEDICAL_HISTORY:
                Fragment_Past_Medical_History fragmentPastMedicalHistory = new Fragment_Past_Medical_History();
                fragmentPastMedicalHistory.setArguments(args);
                fragmentTransaction.add(R.id.frameLayout, fragmentPastMedicalHistory).commit();
                break;
            case SOCIAL_HISTORY:
                Fragment_Social_History fragmentSocialHistory = new Fragment_Social_History();
                fragmentSocialHistory.setArguments(args);
                fragmentTransaction.add(R.id.frameLayout, fragmentSocialHistory).commit();
                break;
        }
    }

    void popFragmentBackStack() {
        fragmentManager.popBackStack();
    }

    @Override
    public void onStart() {
        super.onStart();
        BusStation.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusStation.getBus().unregister(this);
    }

    @Subscribe
    public void pop(Pop_BackStack popBackStack) {
        popFragmentBackStack();
        BusStation.getBus().post(new Bus_Resume_Fragment());
    }

    @Subscribe
    public void onClickSurgicalHistory(Bus_Surgical_History_Item surgicalHistoryItem) { //Executes when the user presses an item or the FAB
        Bundle args = new Bundle();
        switch (surgicalHistoryItem.getTransaction()) {
            case UPDATE_SURGICAL_HISTORY:
                args.putInt("ordinal", Query_Type.UPDATE.ordinal());
                args.putInt("id", surgicalHistoryItem.getId());
                args.putInt("year", surgicalHistoryItem.getYear());
                args.putString("title", surgicalHistoryItem.getTitle());
                break;
            case INSERT_SURGICAL_HISTORY:
                args.putInt("ordinal", Query_Type.INSERT.ordinal());
                args.putInt("patient_id", surgicalHistoryItem.getPatient_id());
                break;
        }
        Fragment_Update_Surgical_History fragmentUpdateSurgicalHistory = new Fragment_Update_Surgical_History();
        fragmentUpdateSurgicalHistory.setArguments(args);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragmentUpdateSurgicalHistory);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Subscribe
    public void onClickFamilyHistory(Bus_Family_History_Item busFamilyHistoryItem) {
        Bundle args = new Bundle();
        args.putInt("patient_id", busFamilyHistoryItem.getPatient_id());
        args.putBoolean("isGrandP", busFamilyHistoryItem.isGrandP());
        args.putBoolean("isParent", busFamilyHistoryItem.isParent());
        args.putBoolean("isSibling", busFamilyHistoryItem.isSibling());
        args.putBoolean("isChild", busFamilyHistoryItem.isChild());
        args.putString("key_grandP", busFamilyHistoryItem.getKey_grandP());
        args.putString("key_parent", busFamilyHistoryItem.getKey_parent());
        args.putString("key_sibling", busFamilyHistoryItem.getKey_sibling());
        args.putString("key_child", busFamilyHistoryItem.getKey_child());
        Fragment_Update_Family_History fragmentUpdateFamilyHistory = new Fragment_Update_Family_History();
        fragmentUpdateFamilyHistory.setArguments(args);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragmentUpdateFamilyHistory);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Subscribe
    public void onClickPastMedicalHistory(Bus_Past_Medical_History_Item busPastMedicalHistoryItem) {
        Bundle args = new Bundle();
        args.putString("medical_condition", busPastMedicalHistoryItem.getMedicalCondition());
        args.putInt("year", busPastMedicalHistoryItem.getYear());
        args.putBoolean("isMedicalHistory", busPastMedicalHistoryItem.isMedicalHistory());
        args.putInt("patient_id", busPastMedicalHistoryItem.getPatient_id());
        args.putString("column_name", busPastMedicalHistoryItem.getColumn_name());
        args.putString("columnYr_name", busPastMedicalHistoryItem.getColumnYr_name());
        Fragment_Update_Past_Medical_History fragmentUpdatePastMedicalHistory = new Fragment_Update_Past_Medical_History();
        fragmentUpdatePastMedicalHistory.setArguments(args);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragmentUpdatePastMedicalHistory);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Subscribe
    public void onClickSocialHistory(Bus_Social_History_Item busSocialHistoryItem) {
        Bundle args = new Bundle();
        args.putString("header", busSocialHistoryItem.getHeader());
        args.putInt("patient_id", busSocialHistoryItem.getPatient_id());
        args.putInt("socialType", busSocialHistoryItem.getSocialHistoryType().ordinal());
        args.putBoolean("isCurrentLyUse", busSocialHistoryItem.isCurrentLyUse());
        args.putBoolean("isPreviouslyUsed", busSocialHistoryItem.isPreviouslyUsed());
        args.putString("frequency", busSocialHistoryItem.getFrequency());
        args.putInt("length", busSocialHistoryItem.getLength());
        args.putInt("stopped", busSocialHistoryItem.getStopped());
        Fragment_Update_Social_History fragmentUpdatePastMedicalHistory = new Fragment_Update_Social_History();
        fragmentUpdatePastMedicalHistory.setArguments(args);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragmentUpdatePastMedicalHistory);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
