package com.example.kinit.e_medicalrecord.Profile.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.Admission.Activity.Admission_List;
import com.example.kinit.e_medicalrecord.Allergy.Activity.Allergy_List;
import com.example.kinit.e_medicalrecord.Consultation.Activity.Consultation_List;
import com.example.kinit.e_medicalrecord.Family_History.Activity.Family_History_List;
import com.example.kinit.e_medicalrecord.Laboratory.Activity.Laboratory_Tests;
import com.example.kinit.e_medicalrecord.Medical_Prescription.Activity.Medical_Prescription_List;
import com.example.kinit.e_medicalrecord.My_Physician.Activity.My_Physician;
import com.example.kinit.e_medicalrecord.Past_Medical_History.Activity.Past_Medical_History_List;
import com.example.kinit.e_medicalrecord.Social_History.Activity.Social_History_List;
import com.example.kinit.e_medicalrecord.Surgical_History.Activity.Surgical_History_List;
import com.example.kinit.e_medicalrecord.Vaccination.Activity.Vaccination_List;
import com.example.kinit.e_medicalrecord.Profile.Class.Patient;
import com.example.kinit.e_medicalrecord.Profile.Class.User;
import com.example.kinit.e_medicalrecord.Profile.Class.Viewer;
import com.example.kinit.e_medicalrecord.R;


public class Fragment_Medical_History extends Fragment {
    TextView tv_consultationResult, tv_labResult, tv_medPresc, tv_admissionHistory, tv_familyHistory, tv_pastMedicalHistory,
            tv_socialHistory, tv_surgicalHistory, tv_allergy, tv_myPhysicians, tv_vaccination;
    Intent myIntent;

    //User Class
    User user;
    Viewer viewer;
    Patient patient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_medical_record, container, false);
        init(rootView);

        return rootView;
    }

    public void init(View view) {

        //TextView initialization
        tv_consultationResult = (TextView) view.findViewById(R.id.tv_consultationResult);
        tv_labResult = (TextView) view.findViewById(R.id.tv_labResult);
        tv_medPresc = (TextView) view.findViewById(R.id.tv_medPresc);
        tv_admissionHistory = (TextView) view.findViewById(R.id.tv_admissionHistory);
        tv_familyHistory = (TextView) view.findViewById(R.id.tv_familyHistory);
        tv_pastMedicalHistory = (TextView) view.findViewById(R.id.tv_pastMedicalHistory);
        tv_socialHistory = (TextView) view.findViewById(R.id.tv_socialHistory);
        tv_surgicalHistory = (TextView) view.findViewById(R.id.tv_surgicalHistory);
        tv_allergy = (TextView) view.findViewById(R.id.tv_allergy);
        tv_myPhysicians = (TextView) view.findViewById(R.id.tv_myPhysicians);
        tv_vaccination = (TextView) view.findViewById(R.id.tv_vaccination);

        tv_consultationResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent = new Intent(getActivity(), Consultation_List.class);
                putExtra();
            }
        });
        tv_labResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent = new Intent(getActivity(), Laboratory_Tests.class);
                putExtra();
            }
        });

        tv_medPresc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent = new Intent(getActivity(), Medical_Prescription_List.class);
                putExtra();
            }
        });

        tv_admissionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent = new Intent(getActivity(), Admission_List.class);
                putExtra();
            }
        });
        tv_familyHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent = new Intent(getActivity(), Family_History_List.class);
                putExtra();
            }
        });
        tv_pastMedicalHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent = new Intent(getActivity(), Past_Medical_History_List.class);
                putExtra();
            }
        });
        tv_socialHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent = new Intent(getActivity(), Social_History_List.class);
                putExtra();
            }
        });
        tv_surgicalHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent = new Intent(getActivity(), Surgical_History_List.class);
                putExtra();
            }
        });
        tv_allergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent = new Intent(getActivity(), Allergy_List.class);
                putExtra();
            }
        });

        tv_vaccination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent = new Intent(getActivity(), Vaccination_List.class);
                putExtra();
            }
        });
        if (viewer == null) {
            tv_myPhysicians.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myIntent = new Intent(getActivity(), My_Physician.class);
                    putExtra();
                }
            });
        } else {
            tv_myPhysicians.setVisibility(View.GONE);
        }
    }


    void putExtra() {
        myIntent.putExtra("patient", patient);
        myIntent.putExtra("viewer", viewer);
        startActivity(myIntent);
    }

    public void setUser(User user, Viewer viewer) {
        this.user = user;
        this.viewer = viewer;
        patient = new Patient();
        patient.user_data_id = user.user_data_id;
        patient.name = user.getFullName();
        patient.id = user.patient_id;
    }
}
