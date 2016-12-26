package com.example.kinit.e_medicalrecord.Fragments.Medical_History;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.kinit.e_medicalrecord.Classes.User;
import com.example.kinit.e_medicalrecord.Classes.Viewer;
import com.example.kinit.e_medicalrecord.Enum.Medical_Transaction;
import com.example.kinit.e_medicalrecord.Activities.Medical;
import com.example.kinit.e_medicalrecord.R;


public class Fragment_Medical_History extends Fragment {

    LinearLayout layout_medResult, layout_labResult, layout_medPresc, layout_family, layout_surgery, layout_pastMedical, layout_social,
            layout_allergy;
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
        //Layout initialization
        layout_medResult = (LinearLayout)view.findViewById(R.id.layout_medResult);
        layout_labResult = (LinearLayout)view.findViewById(R.id.layout_labResult);
        layout_medPresc = (LinearLayout)view.findViewById(R.id.layout_medPresc);
        layout_family = (LinearLayout)view.findViewById(R.id.layout_family);
        layout_surgery = (LinearLayout)view.findViewById(R.id.layout_surgery);
        layout_pastMedical = (LinearLayout)view.findViewById(R.id.layout_pastMedical);
        layout_social = (LinearLayout)view.findViewById(R.id.layout_social);
        layout_allergy = (LinearLayout)view.findViewById(R.id.layout_allergy);

        //Set onClick for each textViews
        layout_medResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        layout_labResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivityInitializer(Medical_Transaction.LAB_RESULT.ordinal());
            }
        });
        layout_medPresc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivityInitializer(Medical_Transaction.MEDICAL_PRESCRIPTION.ordinal());
            }
        });
        layout_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivityInitializer(Medical_Transaction.FAMILY_HISTORY.ordinal());
            }
        });
        layout_surgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivityInitializer(Medical_Transaction.SURGICAL_HISTORY.ordinal());
            }
        });
        layout_pastMedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivityInitializer(Medical_Transaction.PAST_MEDICAL_HISTORY.ordinal());
            }
        });
        layout_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivityInitializer(Medical_Transaction.SOCIAL_HISTORY.ordinal());
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
