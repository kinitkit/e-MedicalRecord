package com.example.kinit.e_medicalrecord.Fragments.Social_History;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Resume_Fragment;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Social_History_Item;
import com.example.kinit.e_medicalrecord.BusStation.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.Enum.Social_History_Type;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Social_History extends Fragment implements View.OnClickListener {
    //View
    View rootView;

    //Primitive Data types
    int patient_id;
    String patient_name;
    boolean isClickable, isPaused;

    //Widgets
    CardView cv_caffeine, cv_tobacco, cv_alcohol, cv_drug;
    TextView tv_caffeine_currentlyUse, tv_caffeine_previouslyUsed, tv_caffeine_frequency, tv_caffeine_length, tv_caffeine_stopped,
            tv_tobacco_currentlyUse, tv_tobacco_previouslyUsed, tv_tobacco_frequency, tv_tobacco_length, tv_tobacco_stopped,
            tv_alcohol_currentlyUse, tv_alcohol_previouslyUsed, tv_alcohol_frequency, tv_alcohol_length, tv_alcohol_stopped,
            tv_drugs_currentlyUse, tv_drugs_previouslyUsed, tv_drugs_frequency, tv_drugs_length, tv_drugs_stopped;

    //Classes
    Social_History_Data social_history_data;

    /*============================
        END OF GLOBAL VARIABLES
     =============================*/
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
        rootView = inflater.inflate(R.layout.fragment_social_history, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        social_history_data = new Social_History_Data();
        isClickable = false;
        isPaused = false;

        getSocialHistoryData();

        //TextView
        tv_caffeine_currentlyUse = (TextView) rootView.findViewById(R.id.tv_caffeine_currentlyUse);
        tv_caffeine_previouslyUsed = (TextView) rootView.findViewById(R.id.tv_caffeine_previouslyUsed);
        tv_caffeine_frequency = (TextView) rootView.findViewById(R.id.tv_caffeine_frequency);
        tv_caffeine_length = (TextView) rootView.findViewById(R.id.tv_caffeine_length);
        tv_caffeine_stopped = (TextView) rootView.findViewById(R.id.tv_caffeine_stopped);
        tv_tobacco_currentlyUse = (TextView) rootView.findViewById(R.id.tv_tobacco_currentlyUse);
        tv_tobacco_previouslyUsed = (TextView) rootView.findViewById(R.id.tv_tobacco_previouslyUsed);
        tv_tobacco_frequency = (TextView) rootView.findViewById(R.id.tv_tobacco_frequency);
        tv_tobacco_length = (TextView) rootView.findViewById(R.id.tv_tobacco_length);
        tv_tobacco_stopped = (TextView) rootView.findViewById(R.id.tv_tobacco_stopped);
        tv_alcohol_currentlyUse = (TextView) rootView.findViewById(R.id.tv_alcohol_currentlyUse);
        tv_alcohol_previouslyUsed = (TextView) rootView.findViewById(R.id.tv_alcohol_previouslyUsed);
        tv_alcohol_frequency = (TextView) rootView.findViewById(R.id.tv_alcohol_frequency);
        tv_alcohol_length = (TextView) rootView.findViewById(R.id.tv_alcohol_length);
        tv_alcohol_stopped = (TextView) rootView.findViewById(R.id.tv_alcohol_stopped);
        tv_drugs_currentlyUse = (TextView) rootView.findViewById(R.id.tv_drugs_currentlyUse);
        tv_drugs_previouslyUsed = (TextView) rootView.findViewById(R.id.tv_drugs_previouslyUsed);
        tv_drugs_frequency = (TextView) rootView.findViewById(R.id.tv_drugs_frequency);
        tv_drugs_length = (TextView) rootView.findViewById(R.id.tv_drugs_length);
        tv_drugs_stopped = (TextView) rootView.findViewById(R.id.tv_drugs_stopped);

        //CardView
        cv_caffeine = (CardView) rootView.findViewById(R.id.cv_caffeine);
        cv_tobacco = (CardView) rootView.findViewById(R.id.cv_tobacco);
        cv_alcohol = (CardView) rootView.findViewById(R.id.cv_alcohol);
        cv_drug = (CardView) rootView.findViewById(R.id.cv_drug);

        cv_caffeine.setOnClickListener(this);
        cv_tobacco.setOnClickListener(this);
        cv_alcohol.setOnClickListener(this);
        cv_drug.setOnClickListener(this);
    }

    void getSocialHistoryData() {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                //caffeine
                                social_history_data.setCaffeine("1".equalsIgnoreCase(jsonObject.getString("caffeine")));
                                social_history_data.setCaffeine_previously_used("1".equalsIgnoreCase(jsonObject.getString("caffeine_previously_used")));
                                social_history_data.setCaffeine_frequency(convertNullToEmptyString(jsonObject.getString("caffeine_frequency")));
                                social_history_data.setCaffeine_length(convertNullToNoData(jsonObject.getString("caffeine_length")));
                                social_history_data.setCaffeine_stopped_year(convertNullToNoData(jsonObject.getString("caffeine_stopped_year")));
                                //tobacco
                                social_history_data.setTobacco("1".equalsIgnoreCase(jsonObject.getString("tobacco")));
                                social_history_data.setTobacco_previously_used("1".equalsIgnoreCase(jsonObject.getString("tobacco_previously_used")));
                                social_history_data.setTobacco_frequency(convertNullToEmptyString(jsonObject.getString("tobacco_frequency")));
                                social_history_data.setTobacco_length(convertNullToNoData(jsonObject.getString("tobacco_length")));
                                social_history_data.setTobacco_stopped_year(convertNullToNoData(jsonObject.getString("tobacco_stopped_year")));
                                //alcohol
                                social_history_data.setAlcohol("1".equalsIgnoreCase(jsonObject.getString("alcohol")));
                                social_history_data.setAlcohol_previously_used("1".equalsIgnoreCase(jsonObject.getString("alcohol_previously_used")));
                                social_history_data.setAlcohol_frequency(convertNullToEmptyString(jsonObject.getString("alcohol_frequency")));
                                social_history_data.setAlcohol_length(convertNullToNoData(jsonObject.getString("alcohol_length")));
                                social_history_data.setAlcohol_stopped_year(convertNullToNoData(jsonObject.getString("alcohol_stopped_year")));
                                //drug
                                social_history_data.setDrugs("1".equalsIgnoreCase(jsonObject.getString("drugs")));
                                social_history_data.setDrugs_previously_used("1".equalsIgnoreCase(jsonObject.getString("drugs_previously_used")));
                                social_history_data.setDrugs_frequency(convertNullToEmptyString(jsonObject.getString("drugs_frequency")));
                                social_history_data.setDrugs_length(convertNullToNoData(jsonObject.getString("drugs_length")));
                                social_history_data.setDrugs_stopped_year(convertNullToNoData(jsonObject.getString("drugs_stopped_year")));
                                displayToTextView();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getSocialHistoryData");
                    params.put("device", "mobile");
                    params.put("user_id", String.valueOf(patient_id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        if(isClickable) {
            switch (v.getId()) {
                case R.id.cv_caffeine:
                    BusStation.getBus().post(new Bus_Social_History_Item(patient_id, "Caffeine: Coffee, Tea, Soda", Social_History_Type.CAFFEINE, social_history_data.isCaffeine(), social_history_data.isCaffeine_previously_used(),
                            social_history_data.getCaffeine_frequency(), social_history_data.getCaffeine_length(), social_history_data.getCaffeine_stopped_year()));
                    break;
                case R.id.cv_tobacco:
                    BusStation.getBus().post(new Bus_Social_History_Item(patient_id, "Tobacco", Social_History_Type.TOBACCO, social_history_data.isTobacco(), social_history_data.isTobacco_previously_used(),
                            social_history_data.getTobacco_frequency(), social_history_data.getTobacco_length(), social_history_data.getTobacco_stopped_year()));
                    break;
                case R.id.cv_alcohol:
                    BusStation.getBus().post(new Bus_Social_History_Item(patient_id, "Alcohol: Beer, Wine, Liquor", Social_History_Type.ALCOHOL, social_history_data.isAlcohol(), social_history_data.isAlcohol_previously_used(),
                            social_history_data.getAlcohol_frequency(), social_history_data.getAlcohol_length(), social_history_data.getAlcohol_stopped_year()));
                    break;
                case R.id.cv_drug:
                    BusStation.getBus().post(new Bus_Social_History_Item(patient_id, "Recreational/Street Drugs", Social_History_Type.DRUGS, social_history_data.isDrugs(), social_history_data.isDrugs_previously_used(),
                            social_history_data.getDrugs_frequency(), social_history_data.getDrugs_length(), social_history_data.getDrugs_stopped_year()));
                    break;
            }
        }
    }

    /*private void cardView_onClick(String header, Social_History_Type socialHistoryType, Boolean isCurrentLyUse, Boolean isPreviouslyUsed, String frequency, int length, int stopped) {
        Intent myIntent = new Intent(getActivity(), Edit_Social_History.class);
        myIntent.putExtra("header", header);
        myIntent.putExtra("patient_id", patient_id);
        myIntent.putExtra("socialType", socialHistoryType.ordinal());
        myIntent.putExtra("isCurrentLyUse", isCurrentLyUse);
        myIntent.putExtra("isPreviouslyUsed", isPreviouslyUsed);
        myIntent.putExtra("frequency", frequency);
        myIntent.putExtra("length", length);
        myIntent.putExtra("stopped", stopped);
        startActivity(myIntent);
    }*/

    void displayToTextView() {
        tv_caffeine_currentlyUse.setText(convertToYesNo(social_history_data.isCaffeine()));
        tv_caffeine_previouslyUsed.setText(convertToYesNo(social_history_data.isCaffeine_previously_used()));
        tv_caffeine_frequency.setText(social_history_data.getCaffeine_frequency());
        tv_caffeine_length.setText(String.valueOf(social_history_data.getCaffeine_length()));
        tv_caffeine_stopped.setText(String.valueOf(social_history_data.getCaffeine_stopped_year())); // End of Caffeine
        tv_tobacco_currentlyUse.setText(convertToYesNo(social_history_data.isTobacco()));
        tv_tobacco_previouslyUsed.setText(convertToYesNo(social_history_data.isTobacco_previously_used()));
        tv_tobacco_frequency.setText(social_history_data.getTobacco_frequency());
        tv_tobacco_length.setText(String.valueOf(social_history_data.getTobacco_length()));
        tv_tobacco_stopped.setText(String.valueOf(social_history_data.getTobacco_stopped_year())); // End of Tobacco
        tv_alcohol_currentlyUse.setText(convertToYesNo(social_history_data.isAlcohol()));
        tv_alcohol_previouslyUsed.setText(convertToYesNo(social_history_data.isAlcohol_previously_used()));
        tv_alcohol_frequency.setText(social_history_data.getAlcohol_frequency());
        tv_alcohol_length.setText(String.valueOf(social_history_data.getAlcohol_length()));
        tv_alcohol_stopped.setText(String.valueOf(social_history_data.getAlcohol_stopped_year())); // End of Alcohol
        tv_drugs_currentlyUse.setText(convertToYesNo(social_history_data.isDrugs()));
        tv_drugs_previouslyUsed.setText(convertToYesNo(social_history_data.isDrugs_previously_used()));
        tv_drugs_frequency.setText(social_history_data.getDrugs_frequency());
        tv_drugs_length.setText(String.valueOf(social_history_data.getDrugs_length()));
        tv_drugs_stopped.setText(String.valueOf(social_history_data.getDrugs_stopped_year())); //End of Drug
        isClickable = true;
    }

    String convertToYesNo(boolean str) {
        return (str) ? "Yes" : "No";
    }

    String convertNullToEmptyString(String str) {
        return (str.equals("null")) ? "No Data" : str;
    }

    int convertNullToNoData(String num) {
        return (num.equals("null")) ? 0 : Integer.valueOf(num);
    }

    void setToolbarTitle() {
        BusStation.getBus().post(new Bus_ToolbarTitle("Social History", patient_name));
    }

    @Override
    public void onPause(){
        super.onPause();
        isPaused = true;
        isClickable = false;
        BusStation.getBus().unregister(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        BusStation.getBus().register(this);
        setToolbarTitle();
        if(isPaused){
            Log.d("error", "paused");
            getSocialHistoryData();
            displayToTextView();
            isPaused = false;
        }
    }

    @Subscribe
    public void resume(Bus_Resume_Fragment resumeSurgicalHistor){
        setToolbarTitle();
        getSocialHistoryData();
        displayToTextView();
    }

    //Data Holder
    public class Social_History_Data {
        private boolean caffeine, tobacco, alcohol, drugs,
                caffeine_previously_used, tobacco_previously_used, alcohol_previously_used, drugs_previously_used;
        private String caffeine_frequency, tobacco_frequency, alcohol_frequency, drugs_frequency;
        private int caffeine_length, tobacco_length, alcohol_length, drugs_length,
                caffeine_stopped_year, tobacco_stopped_year, alcohol_stopped_year, drugs_stopped_year;

        public boolean isCaffeine() {
            return caffeine;
        }

        public void setCaffeine(boolean caffeine) {
            this.caffeine = caffeine;
        }

        public boolean isTobacco() {
            return tobacco;
        }

        public void setTobacco(boolean tobacco) {
            this.tobacco = tobacco;
        }

        public boolean isAlcohol() {
            return alcohol;
        }

        public void setAlcohol(boolean alcohol) {
            this.alcohol = alcohol;
        }

        public boolean isDrugs() {
            return drugs;
        }

        public void setDrugs(boolean drugs) {
            this.drugs = drugs;
        }

        public boolean isCaffeine_previously_used() {
            return caffeine_previously_used;
        }

        public void setCaffeine_previously_used(boolean caffeine_previously_used) {
            this.caffeine_previously_used = caffeine_previously_used;
        }

        public boolean isTobacco_previously_used() {
            return tobacco_previously_used;
        }

        public void setTobacco_previously_used(boolean tobacco_previously_used) {
            this.tobacco_previously_used = tobacco_previously_used;
        }

        public boolean isAlcohol_previously_used() {
            return alcohol_previously_used;
        }

        public void setAlcohol_previously_used(boolean alcohol_previously_used) {
            this.alcohol_previously_used = alcohol_previously_used;
        }

        public boolean isDrugs_previously_used() {
            return drugs_previously_used;
        }

        public void setDrugs_previously_used(boolean drugs_previously_used) {
            this.drugs_previously_used = drugs_previously_used;
        }

        public String getCaffeine_frequency() {
            return caffeine_frequency;
        }

        public void setCaffeine_frequency(String caffeine_frequency) {
            this.caffeine_frequency = caffeine_frequency;
        }

        public String getTobacco_frequency() {
            return tobacco_frequency;
        }

        public void setTobacco_frequency(String tobacco_frequency) {
            this.tobacco_frequency = tobacco_frequency;
        }

        public String getAlcohol_frequency() {
            return alcohol_frequency;
        }

        public void setAlcohol_frequency(String alcohol_frequency) {
            this.alcohol_frequency = alcohol_frequency;
        }

        public String getDrugs_frequency() {
            return drugs_frequency;
        }

        public void setDrugs_frequency(String drugs_frequency) {
            this.drugs_frequency = drugs_frequency;
        }

        public int getCaffeine_length() {
            return caffeine_length;
        }

        public void setCaffeine_length(int caffeine_length) {
            this.caffeine_length = caffeine_length;
        }

        public int getTobacco_length() {
            return tobacco_length;
        }

        public void setTobacco_length(int tobacco_length) {
            this.tobacco_length = tobacco_length;
        }

        public int getAlcohol_length() {
            return alcohol_length;
        }

        public void setAlcohol_length(int alcohol_length) {
            this.alcohol_length = alcohol_length;
        }

        public int getDrugs_length() {
            return drugs_length;
        }

        public void setDrugs_length(int drugs_length) {
            this.drugs_length = drugs_length;
        }

        public int getCaffeine_stopped_year() {
            return caffeine_stopped_year;
        }

        public void setCaffeine_stopped_year(int caffeine_stopped_year) {
            this.caffeine_stopped_year = caffeine_stopped_year;
        }

        public int getTobacco_stopped_year() {
            return tobacco_stopped_year;
        }

        public void setTobacco_stopped_year(int tobacco_stopped_year) {
            this.tobacco_stopped_year = tobacco_stopped_year;
        }

        public int getAlcohol_stopped_year() {
            return alcohol_stopped_year;
        }

        public void setAlcohol_stopped_year(int alcohol_stopped_year) {
            this.alcohol_stopped_year = alcohol_stopped_year;
        }

        public int getDrugs_stopped_year() {
            return drugs_stopped_year;
        }

        public void setDrugs_stopped_year(int drugs_stopped_year) {
            this.drugs_stopped_year = drugs_stopped_year;
        }
    }
}
