package com.example.kinit.e_medicalrecord.Fragments.Laboratory;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Open_New_Lab_Test;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Resume_Fragment;
import com.example.kinit.e_medicalrecord.BusStation.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.Classes.Lab_Chemistry;
import com.example.kinit.e_medicalrecord.Classes.Lab_Fecalysis;
import com.example.kinit.e_medicalrecord.Classes.Lab_Hematology;
import com.example.kinit.e_medicalrecord.Classes.Lab_Urinalysis;
import com.example.kinit.e_medicalrecord.Classes.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_Laboratory_Results extends Fragment implements View.OnClickListener {
    //View
    View rootView;
    ViewPager viewPager;

    //Primitive Data Types
    final CharSequence laboratories[] = {"Blood Chemistry", "Fecalysis", "Hematology", "Urinalysis"};
    int patient_id;
    String patient_name;

    //Widgets
    //FAB
    FloatingActionButton btn_add;

    //App
    AlertDialog.Builder builder;

    //Classes
    Viewer viewer;
    ArrayList<Lab_Chemistry> labChemistries;
    ArrayList<Lab_Fecalysis> labFecalysises;
    ArrayList<Lab_Hematology> labHematologies;
    ArrayList<Lab_Urinalysis> labUrinalysises;

    Fragment_Laboratory_Result_Container labContainer_chemistry, labContainer_fecalysis, labContainer_hematology, labContainer_urinalysis;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            patient_id = bundle.getInt("patient_id", 0);
            patient_name = bundle.getString("patient_name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_laboratory_results, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        labChemistries = new ArrayList<>();
        labFecalysises = new ArrayList<>();
        labHematologies = new ArrayList<>();
        labUrinalysises = new ArrayList<>();
        fetchData();

        builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        btn_add = (FloatingActionButton) rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
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
        if(!labChemistries.isEmpty()){
            labContainer_chemistry = new Fragment_Laboratory_Result_Container();
            labContainer_chemistry.setLabChemistries(labChemistries);
            viewPagerAdapter.addFragment(labContainer_chemistry, "Blood Chemistry");
        }
        if(!labFecalysises.isEmpty()){
            labContainer_fecalysis = new Fragment_Laboratory_Result_Container();
            labContainer_fecalysis.setLabFecalysises(labFecalysises);
            viewPagerAdapter.addFragment(labContainer_fecalysis, "Fecalysis");
        }
        if(!labHematologies.isEmpty()){
            labContainer_hematology = new Fragment_Laboratory_Result_Container();
            labContainer_hematology.setLabHematologies(labHematologies);
            viewPagerAdapter.addFragment(labContainer_hematology, "Hematology");
        }
        if(!labUrinalysises.isEmpty()){
            labContainer_urinalysis = new Fragment_Laboratory_Result_Container();
            labContainer_urinalysis.setLabUrinalysises(labUrinalysises);
            viewPagerAdapter.addFragment(labContainer_urinalysis, "Urinalysis");
        }
        this.viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                showLabTypes();
                break;
        }
    }

    void showLabTypes() {
        builder.setTitle("Select a Test");
        builder.setItems(laboratories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    default:
                        BusStation.getBus().post(new Bus_Open_New_Lab_Test(which));
                        break;
                }
            }
        });
        builder.show();
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    void setToolbarTitle() {
        BusStation.getBus().post(new Bus_ToolbarTitle("Laboratory Results", patient_name));
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

    void fetchData() {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                int rootJsonArrayLength = rootJsonArray.length();
                                for (int x = 0; x < rootJsonArrayLength; x++) {
                                    Object jsonTokenizer = new JSONTokener(rootJsonArray.get(x).toString()).nextValue();
                                    if (jsonTokenizer instanceof JSONArray) {
                                        JSONArray jsonArray = rootJsonArray.getJSONArray(x);
                                        int jsonArrayLength = jsonArray.length();
                                        for (int y = 0; y < jsonArrayLength; y++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(y);
                                            if (jsonObject.has("table_name")) {
                                                switch (jsonObject.getString("table_name")) {
                                                    case "Blood Chemistry":
                                                        labChemistries.add(new Lab_Chemistry(jsonObject));
                                                        break;
                                                    case "Fecalysis":
                                                        labFecalysises.add(new Lab_Fecalysis(jsonObject));
                                                        break;
                                                    case "Hematology":
                                                        labHematologies.add(new Lab_Hematology(jsonObject));
                                                        break;
                                                    case "Urinalysis":
                                                        labUrinalysises.add(new Lab_Urinalysis(jsonObject));
                                                        break;
                                                }
                                            }
                                        }
                                    }
                                }
                                initializeViewPager();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getLabResults");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient_id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void resume(Bus_Resume_Fragment busResumeFragment) {
        setToolbarTitle();
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
