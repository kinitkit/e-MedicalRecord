package com.example.kinit.e_medicalrecord.Fragments.Family_History;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Adapters.RecyclerView.Family_History_ExpandableListAdapter;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Family_History.Bus_Family_History_Item;
import com.example.kinit.e_medicalrecord.BusStation.General.Bus_Resume_Fragment;
import com.example.kinit.e_medicalrecord.BusStation.General.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_Family_History extends Fragment {
    //View
    View rootView;

    int patient_id;
    boolean isPaused;
    String patient_name;
    Boolean[] familyHistory;
    List<String> familyMembers;
    List<String> listDataHeader;
    List<String> listDataChild;

    //ExpandableListView
    Family_History_ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;

    //List Medical Conditions
    String[] keys = {"anesthesia_problem_grandP", "anesthesia_problem_parent", "anesthesia_problem_sibling", "anesthesia_problem_child",
            "anxiety_panic_attacks_grandP", "anxiety_panic_attacks_parent", "anxiety_panic_attacks_sibling", "anxiety_panic_attacks_child",
            "arthritis_grandP", "arthritis_parent", "arthritis_sibling", "arthritis_child",
            "asthma_grandP", "asthma_parent", "asthma_sibling", "asthma_child",
            "ADHD_grandP", "ADHD_parent", "ADHD_sibling", "ADHD_child",
            "birth_defects_grandP", "birth_defects_parent", "birth_defects_sibling", "birth_defects_child",
            "blood_problem_grandP", "blood_problem_parent", "blood_problem_sibling", "blood_problem_child",
            "bone_grandP", "bone_parent", "bone_sibling", "bone_child",
            "breast_disease_grandP", "breast_disease_parent", "breast_disease_sibling", "breast_disease_child",
            "cancer_breast_grandP", "cancer_breast_parent", "cancer_breast_sibling", "cancer_breast_child",
            "cancer_colon_grandP", "cancer_colon_parent", "cancer_colon_sibling", "cancer_colon_child",
            "cancer_malenoma_grandP", "cancer_malenoma_parent", "cancer_malenoma_sibling", "cancer_malenoma_child",
            "cancer_ovary_grandP", "cancer_ovary_parent", "cancer_ovary_sibling", "cancer_ovary_child",
            "cancer_prostate_grandP", "cancer_prostate_parent", "cancer_prostate_sibling", "cancer_prostate_child",
            "chicken_pox_grandP", "chicken_pox_parent", "chicken_pox_sibling", "chicken_pox_child",
            "colon_problems_grandP", "colon_problems_parent", "colon_problems_sibling", "colon_problems_child",
            "depression_grandP", "depression_parent", "depression_sibling", "depression_child",
            "diabetes_1_grandP", "diabetes_1_parent", "diabetes_1_sibling", "diabetes_1_child",
            "diabetes_2_grandP", "diabetes_2_parent", "diabetes_2_sibling", "diabetes_2_child",
            "digestive_tract_problem_grandP", "digestive_tract_problem_parent", "digestive_tract_problem_sibling", "digestive_tract_problem_child",
            "ENT_grandP", "ENT_parent", "ENT_sibling", "ENT_child",
            "eating_disorder_grandP", "eating_disorder_parent", "eating_disorder_sibling", "eating_disorder_child",
            "eczema_grandP", "eczema_parent", "eczema_sibling", "eczema_child",
            "epilepsy_grandP", "epilepsy_parent", "epilepsy_sibling", "epilepsy_child",
            "fertility_problems_grandP", "fertility_problems_parent", "fertility_problems_sibling", "fertility_problems_child",
            "gallbladder_grandP", "gallbladder_parent", "gallbladder_sibling", "gallbladder_child",
            "gynecology_problems_grandP", "gynecology_problems_parent", "gynecology_problems_sibling", "gynecology_problems_child",
            "hay_fever_grandP", "hay_fever_parent", "hay_fever_sibling", "hay_fever_child",
            "migrane_grandP", "migrane_parent", "migrane_sibling", "migrane_child",
            "hearing_problem_grandP", "hearing_problem_parent", "hearing_problem_sibling", "hearing_problem_child",
            "heart_attack_grandP", "heart_attack_parent", "heart_attack_sibling", "heart_attack_child",
            "heart_murmur_grandP", "heart_murmur_parent", "heart_murmur_sibling", "heart_murmur_child",
            "heart_problem_grandP", "heart_problem_parent", "heart_problem_sibling", "heart_problem_child",
            "hepatitis_grandP", "hepatitis_parent", "hepatitis_sibling", "hepatitis_child",
            "hypertension_grandP", "hypertension_parent", "hypertension_sibling", "hypertension_child",
            "hyperlipidemia_grandP", "hyperlipidemia_parent", "hyperlipidemia_sibling", "hyperlipidemia_child"};

    String[] medicalConditions = {"Anesthesia Problem", "Anxiety / Panic Attacks", "Arthritis", "Asthma", "Attention Deficit Disorders",
            "Birth Defects", "Blood Problem / Clotting Disorder", "Bone / Joint Problems", "Breast Disease / Lumps (Benign)",
            "Cancer, Breast", "Cancer, Colon", "Cancer, Malenoma", "Cancer, Ovary", "Cancer, Prostate", "Chicken Pox",
            "Colitis or Colon Problems", "Depression (of a Serious State)", "Diabetes, Type 1 (Childhood Onset)",
            "Diabetes, Type 2 (Adult Onset)", "Digestive Tract Problem", "Ear / Nose / Throat Problems", "Eating Disorders",
            "Eczema", "Epilepsy (Convultions or Seizures)", "Fertility (Conception) Problems", "Gallbladder or Gallstones",
            "Gynecology Problems", "Hay Fever", "Headaches: Migranes or Frequent", "Hearing Problems: Loss", "Heart Attack", "Heart Murmur",
            "Heart Problem", "Hepatitis A, B, or C", "High Blood Pressure (Hypertension)", "High Cholesterol (Hyperlipidemia)"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            patient_id = bundle.getInt("patient_id");
            patient_name = bundle.getString("patient_name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_family_history, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        isPaused = false;
        familyHistory = new Boolean[keys.length];

        //ExpandableListView
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.elv_familyHistory);
        fetchData();
    }

    void fetchData() {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response);
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.getString("code").equals("successful")) {
                                    jsonObject = rootJsonArray.getJSONObject(1);
                                    for (int x = 0; x < keys.length; x++) {
                                        familyHistory[x] = "1".equals(jsonObject.getString(keys[x]));
                                    }
                                    loadToExpandableListView();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getFamilyHistoryData");
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

    void loadToExpandableListView() {
        prepareListData();
        expandableListAdapter = new Family_History_ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, familyHistory);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListViewItemClick();
    }

    //Populate ExpandableListView
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new ArrayList<>();
        familyMembers = new ArrayList<>();
        //Adding header and child data
        for (int x = 0; x < medicalConditions.length; x++) {
            listDataHeader.add(medicalConditions[x]);
        }
        this.listDataChild.add("");
    }

    void expandableListViewItemClick() {
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                BusStation.getBus().post(new Bus_Family_History_Item(patient_id, familyHistory[groupPosition * 4], familyHistory[(groupPosition * 4) + 1],
                        familyHistory[(groupPosition * 4) + 2], familyHistory[(groupPosition * 4) + 3], keys[groupPosition * 4],
                        keys[(groupPosition * 4) + 1], keys[(groupPosition * 4) + 2], keys[(groupPosition * 4) + 3]));
                isPaused = true;
                return false;
            }
        });
    }

    void setToolbarTitle() {
        BusStation.getBus().post(new Bus_ToolbarTitle("Family History", patient_name));
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

    @Subscribe
    public void receive_id(int patient_id) {
        this.patient_id = patient_id;
    }

    @Subscribe
    public void resume(Bus_Resume_Fragment busResumeFragment) {
        setToolbarTitle();
        fetchData();
    }
}
