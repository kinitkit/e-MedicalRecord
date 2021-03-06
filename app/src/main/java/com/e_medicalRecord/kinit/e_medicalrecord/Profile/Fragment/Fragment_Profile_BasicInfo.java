package com.e_medicalRecord.kinit.e_medicalrecord.Profile.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e_medicalRecord.kinit.e_medicalrecord.Profile.Class.User;
//import com.e_medicalRecord.kinit.e_medicalrecord.Interfaces.Profile_Communicator;
import com.e_medicalRecord.kinit.e_medicalrecord.R;


public class Fragment_Profile_BasicInfo extends Fragment {
    //Interface
    //Profile_Communicator communicator;

    //TextView
    TextView tv_profession, tv_license, tv_name, tv_gender, tv_birthday, tv_occupation, tv_address, tv_contactNumber, tv_email, tv_nationality,
            tv_civilStatus, tv_religion, tv_height, tv_weight;
    //CardView
    CardView cardView_medicalProfession;

    //User Class
    User user;

    //View
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_patient_profile, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Cardview
        cardView_medicalProfession = (CardView) rootView.findViewById(R.id.cardView_medicalProfession);
        //TextView
        tv_name = (TextView) rootView.findViewById(R.id.tv_name);
        tv_gender = (TextView) rootView.findViewById(R.id.tv_gender);
        tv_birthday = (TextView) rootView.findViewById(R.id.tv_birthday);
        tv_height = (TextView) rootView.findViewById(R.id.tv_height);
        tv_weight = (TextView) rootView.findViewById(R.id.tv_weight);
        tv_occupation = (TextView) rootView.findViewById(R.id.tv_occupation);
        tv_address = (TextView) rootView.findViewById(R.id.tv_address);
        tv_contactNumber = (TextView) rootView.findViewById(R.id.tv_contactNumber);
        tv_email = (TextView) rootView.findViewById(R.id.tv_email);
        tv_civilStatus = (TextView) rootView.findViewById(R.id.tv_civilStatus);
        tv_nationality = (TextView) rootView.findViewById(R.id.tv_nationality);
        tv_religion = (TextView) rootView.findViewById(R.id.tv_religion);

        if (user.patient_id != 0) {
            cardView_medicalProfession.setVisibility(View.GONE);

        } else {
            tv_nationality.setVisibility(View.GONE);
            tv_civilStatus.setVisibility(View.GONE);
            tv_religion.setVisibility(View.GONE);
        }
        if (user.medical_staff_id != 0) {
            cardView_medicalProfession.setVisibility(View.VISIBLE);
            tv_profession = (TextView) rootView.findViewById(R.id.tv_profession);
            tv_license = (TextView) rootView.findViewById(R.id.tv_license);
            tv_height.setVisibility(View.GONE);
            tv_weight.setVisibility(View.GONE);
            tv_birthday.setVisibility(View.GONE);
            tv_occupation.setVisibility(View.GONE);
            tv_address.setVisibility(View.GONE);
        }
        setTextViewText();
    }

    public void setUser(User user) {
        this.user = user;

        if (tv_name != null) {
            setTextViewText();
        }
    }

    void setTextViewText() {
        if (user.medical_staff_id != 0) {
            tv_profession.setText(user.medical_staff_type);
            tv_license.setText(user.licenseNumber);
        } else {
            tv_birthday.setText(user.birthday);

            if (user.height.equals("0")) {
                tv_height.setVisibility(View.GONE);
            } else {
                tv_height.setText(String.format("%s cm", user.height));
            }

            if (user.weight.equals("0")) {
                tv_weight.setVisibility(View.GONE);
            } else {
                tv_weight.setText(String.format("%s kg", user.weight));
            }

            tv_address.setText(user.address);
            tv_civilStatus.setText(user.civilStatus);
            tv_nationality.setText(user.nationality);
            tv_religion.setText(user.religion);
        }
        tv_name.setText(user.getFullName());
        tv_gender.setText(user.gender);
        tv_occupation.setText(user.occupation);
        tv_contactNumber.setText(user.contactNumber);
        tv_email.setText(user.emailAddress);
    }
}
