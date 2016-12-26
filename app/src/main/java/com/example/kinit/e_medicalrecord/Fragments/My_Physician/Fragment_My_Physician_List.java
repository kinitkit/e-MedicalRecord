package com.example.kinit.e_medicalrecord.Fragments.My_Physician;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kinit.e_medicalrecord.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_My_Physician_List extends Fragment {


    public Fragment_My_Physician_List() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_physician_list, container, false);
    }

}
