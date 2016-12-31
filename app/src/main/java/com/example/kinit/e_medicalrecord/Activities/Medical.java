package com.example.kinit.e_medicalrecord.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Laboratory.Bus_View_Lab_Test;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Drug;
import com.example.kinit.e_medicalrecord.BusStation.Family_History.Bus_Family_History_Item;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Medical_Prescription_Click;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Open_Add_Drug;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Open_Edit_MedicalPrescription;
import com.example.kinit.e_medicalrecord.BusStation.Laboratory.Bus_Open_New_Lab_Test;
import com.example.kinit.e_medicalrecord.BusStation.Past_Medical_History.Bus_Past_Medical_History_Item;
import com.example.kinit.e_medicalrecord.BusStation.Social_History.Bus_Social_History_Item;
import com.example.kinit.e_medicalrecord.BusStation.General.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.BusStation.General.Pop_BackStack;
import com.example.kinit.e_medicalrecord.BusStation.General.Bus_Resume_Fragment;
import com.example.kinit.e_medicalrecord.BusStation.Surgical_History.Bus_Surgical_History_Item;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.Enum.Medical_Transaction;
import com.example.kinit.e_medicalrecord.Enum.Mode;
import com.example.kinit.e_medicalrecord.Enum.Query_Type;
import com.example.kinit.e_medicalrecord.Fragments.Laboratory.Fragment_View_Lab_Result;
import com.example.kinit.e_medicalrecord.Fragments.Medical_Prescription.Fragment_Drug;
import com.example.kinit.e_medicalrecord.Fragments.Medical_Prescription.Fragment_Drug_List;
import com.example.kinit.e_medicalrecord.Fragments.Laboratory.Fragment_Lab_Chemistry;
import com.example.kinit.e_medicalrecord.Fragments.Laboratory.Fragment_Lab_Fecalysis;
import com.example.kinit.e_medicalrecord.Fragments.Laboratory.Fragment_Lab_Hematology;
import com.example.kinit.e_medicalrecord.Fragments.Laboratory.Fragment_Lab_Urinalysis;
import com.example.kinit.e_medicalrecord.Fragments.Laboratory.Fragment_Laboratory_Results;
import com.example.kinit.e_medicalrecord.Fragments.Medical_Prescription.Fragment_Medical_Prescription;
import com.example.kinit.e_medicalrecord.Fragments.Medical_Prescription.Fragment_New_MedicalPrescription;
import com.example.kinit.e_medicalrecord.Fragments.Family_History.Fragment_Update_Family_History;
import com.example.kinit.e_medicalrecord.Fragments.Past_Medical_History.Fragment_Update_Past_Medical_History;
import com.example.kinit.e_medicalrecord.Fragments.Social_History.Fragment_Update_Social_History;
import com.example.kinit.e_medicalrecord.Fragments.Surgery.Fragment_Update_Surgical_History;
import com.example.kinit.e_medicalrecord.Fragments.Family_History.Fragment_Family_History;
import com.example.kinit.e_medicalrecord.Fragments.Past_Medical_History.Fragment_Past_Medical_History;
import com.example.kinit.e_medicalrecord.Fragments.Social_History.Fragment_Social_History;
import com.example.kinit.e_medicalrecord.Fragments.Surgery.Fragment_Surgical_History;
import com.example.kinit.e_medicalrecord.R;
import com.squareup.otto.Subscribe;

public class Medical extends AppCompatActivity {
    //Primitive Data Types
    int patient_id;
    int user_id;
    String patient_name;

    //Widgets
    Toolbar toolbar;

    //App
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    //Fragment
    Fragment_New_MedicalPrescription fragmentNewMedicalPrescription;

    //Viewer
    Viewer viewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);
        init();
    }

    void init() {
        //FragmentManager
        fragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        int ordinal = intent.getIntExtra("ordinal", 0);
        patient_id = intent.getIntExtra("patient_id", 0);
        patient_name = intent.getStringExtra("patient_name");
        user_id = intent.getIntExtra("user_id", 0);
        if (intent.hasExtra("viewer_user_id")) {
            viewer = new Viewer();
            viewer.name = intent.getStringExtra("viewer_name");
            viewer.user_id = intent.getIntExtra("viewer_user_id", 0);
            viewer.patient_id = intent.getIntExtra("viewer_patient_id", 0);
            viewer.medicalStaff_id = intent.getIntExtra("viewer_medicalStaff_id", 0);
            viewer.mode = Mode.values()[intent.getIntExtra("viewer_ordinal", 0)];
        }

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        assignFragment(Medical_Transaction.values()[ordinal]);
    }

    void assignFragment(Medical_Transaction transaction) {
        Bundle args = new Bundle();
        args.putInt("patient_id", patient_id);
        args.putString("patient_name", patient_name);
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (transaction) {
            case LAB_RESULT:
                Fragment_Laboratory_Results fragmentLaboratoryResults = new Fragment_Laboratory_Results();
                fragmentLaboratoryResults.setArguments(args);
                fragmentTransaction.add(R.id.frameLayout, fragmentLaboratoryResults).commit();
                fragmentLaboratoryResults.setViewer(viewer);
                break;
            case MEDICAL_PRESCRIPTION:
                Fragment_Medical_Prescription fragmentMedicalPrescription = new Fragment_Medical_Prescription();
                fragmentMedicalPrescription.setArguments(args);
                fragmentTransaction.add(R.id.frameLayout, fragmentMedicalPrescription).commit();
                fragmentMedicalPrescription.setViewer(viewer);
                break;
            case FAMILY_HISTORY:
                Fragment_Family_History fragmentFamilyHistory = new Fragment_Family_History();
                fragmentFamilyHistory.setArguments(args);
                fragmentTransaction.add(R.id.frameLayout, fragmentFamilyHistory).commit();
                break;
            case SURGICAL_HISTORY:
                Fragment_Surgical_History fragmentSurgicalHistory = new Fragment_Surgical_History();
                fragmentSurgicalHistory.setArguments(args);
                fragmentSurgicalHistory.setViewer(viewer);
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
    public void setTitle(Bus_ToolbarTitle busToolbarTitle) {
        getSupportActionBar().setTitle(busToolbarTitle.title);
        getSupportActionBar().setSubtitle(busToolbarTitle.subtitle);
    }

    @Subscribe
    public void pop(Pop_BackStack popBackStack) {
        popFragmentBackStack();
        BusStation.getBus().post(new Bus_Resume_Fragment());
    }

    @Override
    public void onBackPressed() {
        int count = fragmentManager.getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            pop(new Pop_BackStack());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                int count = fragmentManager.getBackStackEntryCount();
                if (count == 0) {
                    this.finish();
                } else {
                    pop(new Pop_BackStack());
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onClickSurgicalHistory(Bus_Surgical_History_Item surgicalHistoryItem) { //Executes when the user presses an item or the FAB
        Bundle args = new Bundle();
        args.putString("patient_name", patient_name);
        switch (surgicalHistoryItem.getTransaction()) {
            case UPDATE_SURGICAL_HISTORY:
                args.putInt("ordinal", Query_Type.UPDATE.ordinal());
                args.putInt("id", surgicalHistoryItem.getId());
                args.putString("date", surgicalHistoryItem.getDate());
                args.putString("title", surgicalHistoryItem.getTitle());
                args.putString("attachName", surgicalHistoryItem.getAttachName());
                break;
            case INSERT_SURGICAL_HISTORY:
                args.putInt("ordinal", Query_Type.INSERT.ordinal());
                args.putInt("patient_id", surgicalHistoryItem.getPatient_id());
                args.putInt("user_id", user_id);
                break;
        }
        Fragment_Update_Surgical_History fragmentUpdateSurgicalHistory = new Fragment_Update_Surgical_History();
        fragmentUpdateSurgicalHistory.setArguments(args);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragmentUpdateSurgicalHistory);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentUpdateSurgicalHistory.setViewer(viewer);
    }

    @Subscribe
    public void onClickFamilyHistory(Bus_Family_History_Item busFamilyHistoryItem) {
        Bundle args = new Bundle();
        args.putInt("patient_id", busFamilyHistoryItem.getPatient_id());
        args.putString("patient_name", patient_name);
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

    //Medical Prescription
    @Subscribe
    public void onClickMedicalPrescription(Bus_Medical_Prescription_Click busMedicalPrescriptionItem) {
        Bundle args = new Bundle();
        args.putInt("medicalPrescription_id", busMedicalPrescriptionItem.medicalPrescription_id);
        args.putInt("patient_id", busMedicalPrescriptionItem.patient_id);
        args.putString("physician_name", busMedicalPrescriptionItem.physicianName);
        args.putString("clinic_name", busMedicalPrescriptionItem.clinicName);
        args.putString("date", busMedicalPrescriptionItem.date);
        Fragment_Drug_List fragmentDrugList = new Fragment_Drug_List();
        fragmentDrugList.setArguments(args);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragmentDrugList);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Subscribe
    public void onClickOpenEditMedicalPrescription(Bus_Open_Edit_MedicalPrescription busOpenEditMedicalPrescription) {
        Bundle args = new Bundle();
        args.putInt("patient_id", patient_id);
        args.putInt("user_id", user_id);
        fragmentNewMedicalPrescription = new Fragment_New_MedicalPrescription();
        fragmentNewMedicalPrescription.setArguments(args);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragmentNewMedicalPrescription);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentNewMedicalPrescription.setViewer(viewer);
    }

    @Subscribe
    public void onClickOpenAddDrug(Bus_Open_Add_Drug busOpenAddDrug) {
        Bundle args = new Bundle();
        Fragment_Drug fragmentDrug = new Fragment_Drug();
        if (busOpenAddDrug.queryType != null) {
            if (busOpenAddDrug.queryType == Query_Type.UPDATE) {
                args.putInt("ordinal", busOpenAddDrug.queryType.ordinal());
                fragmentDrug.setArguments(args);
                fragmentDrug.set_drug(busOpenAddDrug);
            }
        }

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragmentDrug);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Subscribe
    public void onClickAddDrug(Bus_Drug busDrug) {
        if (busDrug.queryType == null) {
            fragmentNewMedicalPrescription.insertDrug(busDrug);
            popFragmentBackStack();
        } else if (busDrug.queryType == Query_Type.UPDATE) {
            fragmentNewMedicalPrescription.updateDrug(busDrug);
            popFragmentBackStack();
        }
    }
    //End Medical Prescription

    //Start Laboratory
    @Subscribe
    public void onClickNewLabTest(Bus_Open_New_Lab_Test busOpenNewLabTest) {
        Bundle args = new Bundle();
        args.putInt("patient_id", patient_id);
        args.putInt("user_data_id", user_id);
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (busOpenNewLabTest.laboratoryTests) {
            case BLOOD_CHEMISTRY:
                Fragment_Lab_Chemistry fragmentLabChemistry = new Fragment_Lab_Chemistry();
                fragmentLabChemistry.setArguments(args);
                fragmentLabChemistry.setViewer(viewer);
                fragmentTransaction.add(R.id.frameLayout, fragmentLabChemistry);
                break;
            case FECALYSIS:
                Fragment_Lab_Fecalysis fragment_lab_fecalysis = new Fragment_Lab_Fecalysis();
                fragment_lab_fecalysis.setArguments(args);
                fragment_lab_fecalysis.setViewer(viewer);
                fragmentTransaction.add(R.id.frameLayout, fragment_lab_fecalysis);
                break;
            case HEMATOLOGY:
                Fragment_Lab_Hematology fragmentLabHematology = new Fragment_Lab_Hematology();
                fragmentLabHematology.setArguments(args);
                fragmentLabHematology.setViewer(viewer);
                fragmentTransaction.add(R.id.frameLayout, fragmentLabHematology);
                break;
            case URINALYSIS:
                Fragment_Lab_Urinalysis fragmentLabUrinalysis = new Fragment_Lab_Urinalysis();
                fragmentLabUrinalysis.setArguments(args);
                fragmentLabUrinalysis.setViewer(viewer);
                fragmentTransaction.add(R.id.frameLayout, fragmentLabUrinalysis);
                break;
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Subscribe
    public void onClickViewLabTest(Bus_View_Lab_Test busViewLabTest) {
        Bundle args = new Bundle();
        args.putInt("patient_id", patient_id);
        args.putInt("user_data_id", user_id);
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment_View_Lab_Result fragmentViewLabResult = new Fragment_View_Lab_Result();
        fragmentViewLabResult.setArguments(args);
        fragmentViewLabResult.setLabResultAndViewer(busViewLabTest.enum_laboratoryTests, busViewLabTest.object, viewer);
        fragmentTransaction.add(R.id.frameLayout, fragmentViewLabResult);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
