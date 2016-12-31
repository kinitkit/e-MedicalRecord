package com.example.kinit.e_medicalrecord.Fragments.Medical_Prescription;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.General.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.User.Patient;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.Fragments.Laboratory.Fragment_Laboratory_Results;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Medical_Prescription_Container extends Fragment {
    //View
    View rootView;
    ViewPager viewPager;

    //Primitive Data Types
    int medicalPrescription_id, patient_id;
    String physicianName, clinicName, date;

    //Classes
    Viewer viewer;
    Patient patient;
    Custom_ProgressDialog progressDialog;

    Fragment_Drug_List fragmentDrugList;
    Fragment_Tagged_Medical_Prescription fragmentTaggedMedicalPrescription;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            patient_id = bundle.getInt("patient_id");
            medicalPrescription_id = bundle.getInt("medicalPrescription_id");
            physicianName = bundle.getString("physician_name");
            clinicName = bundle.getString("clinic_name");
            date = bundle.getString("date");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_medical_prescription_container, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        Bundle args = new Bundle();
        args.putInt("medicalPrescription_id", medicalPrescription_id);
        args.putInt("patient_id", patient_id);
        args.putString("physician_name", physicianName);
        args.putString("clinic_name", clinicName);
        args.putString("date", date);
        fragmentDrugList = new Fragment_Drug_List();
        fragmentDrugList.setArguments(args);

        fragmentTaggedMedicalPrescription = new Fragment_Tagged_Medical_Prescription();

        initializeViewPager();
    }

    void initializeViewPager() {
        //ViewPager
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        if (viewPager != null) {
            setupViewPager();
        }
        //TabLayout
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(fragmentDrugList, "Medical Prescription");
        viewPagerAdapter.addFragment(fragmentTaggedMedicalPrescription, "Tagged Physicians");
        this.viewPager.setAdapter(viewPagerAdapter);
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    void setToolbarTitle() {
        BusStation.getBus().post(new Bus_ToolbarTitle("Medical Prescription", patient.name));
    }

    @Override
    public void onResume() {
        super.onResume();
        BusStation.getBus().register(this);
        setToolbarTitle();
    }

    @Override
    public void onPause() {
        super.onPause();
        BusStation.getBus().unregister(this);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
