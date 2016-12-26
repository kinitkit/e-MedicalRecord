package com.example.kinit.e_medicalrecord.Fragments.Medical_History;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.Activities.My_Physician;
import com.example.kinit.e_medicalrecord.Classes.User.User;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.Enum.Medical_Transaction;
import com.example.kinit.e_medicalrecord.Activities.Medical;
import com.example.kinit.e_medicalrecord.R;


public class Fragment_Medical_History extends Fragment {
    TextView tv_consultationResult, tv_labResult, tv_medPresc, tv_admissionHistory, tv_familyHistory, tv_pastMedicalHistory,
            tv_socialHistory, tv_surgicalHistory, tv_allergy, tv_myPhysicians;
    Intent myIntent;

    //User Class
    User user;
    Viewer viewer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_medical_record, container, false);
        init(rootView);

        return rootView;
    }

    public void init(View view){

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

        tv_consultationResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tv_labResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivityInitializer(Medical_Transaction.LAB_RESULT.ordinal());
            }
        });
        tv_medPresc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivityInitializer(Medical_Transaction.MEDICAL_PRESCRIPTION.ordinal());
            }
        });
        tv_admissionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tv_familyHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivityInitializer(Medical_Transaction.FAMILY_HISTORY.ordinal());
            }
        });
        tv_pastMedicalHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivityInitializer(Medical_Transaction.PAST_MEDICAL_HISTORY.ordinal());
            }
        });
        tv_socialHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivityInitializer(Medical_Transaction.SOCIAL_HISTORY.ordinal());
            }
        });
        tv_surgicalHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivityInitializer(Medical_Transaction.SURGICAL_HISTORY.ordinal());
            }
        });
        tv_allergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tv_myPhysicians.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), My_Physician.class);
                intent.putExtra("patient_id", user.getPatient_id());
                intent.putExtra("patient_name", user.getFullName());
                intent.putExtra("user_id", user.getUser_data_id());
                startActivity(intent);
            }
        });
    }

    void newActivityInitializer(int ordinal){
        myIntent = new Intent(getActivity(), Medical.class);
        myIntent.putExtra("patient_id", user.getPatient_id());
        myIntent.putExtra("patient_name", user.getFullName());
        myIntent.putExtra("user_id", user.getUser_data_id());
        myIntent.putExtra("ordinal", ordinal);
        viewer_putExtra();
        startActivity(myIntent);
    }

    void viewer_putExtra() {
        if(viewer != null) {
            myIntent.putExtra("viewer_name", viewer.name);
            myIntent.putExtra("viewer_patient_id", viewer.patient_id);
            myIntent.putExtra("viewer_user_id", viewer.user_id);
            myIntent.putExtra("viewer_medicalStaff_id", viewer.medicalStaff_id);
            myIntent.putExtra("viewer_ordinal", viewer.mode.ordinal());
        }
    }

    public void setUser(User user, Viewer viewer){
        this.user = user;
        this.viewer = viewer;
    }
}
