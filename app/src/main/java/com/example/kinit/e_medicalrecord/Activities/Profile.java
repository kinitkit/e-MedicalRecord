package com.example.kinit.e_medicalrecord.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Classes.User;
import com.example.kinit.e_medicalrecord.Fragments.Fragment_Medical_History;
import com.example.kinit.e_medicalrecord.Fragments.Fragment_Profile_BasicInfo;
import com.example.kinit.e_medicalrecord.Interfaces.Profile_Communicator;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile extends AppCompatActivity implements Profile_Communicator {
    Intent intent;
    SharedPreferences sharedPreferences;

    //Fragment
    Fragment_Profile_BasicInfo fragment_profile;
    Fragment_Medical_History fragment_medicalInfo;

    AlertDialog.Builder builder;

    boolean isPatient = false, isMedicalStaff = false;
    String recentCode = "";

    User user;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initIntent();
        getUserData();
    }

    public void initIntent(){
        user = new User();

        //Intent
        Intent intent = getIntent();
        user.setUser_data_id(intent.getIntExtra("user_id", 0));
        user.setPatient_id(intent.getIntExtra("patient_id", 0));
        user.setMedical_staff_id(intent.getIntExtra("medicalStaff_id", 0));
        if (user.getPatient_id() != 0) {
            isPatient = true;
        }
        if (user.getMedical_staff_id() != 0) {
            isMedicalStaff = true;
        }
    }

    void init() {
        //Fragment
        fragment_profile = new Fragment_Profile_BasicInfo();
        fragment_medicalInfo = new Fragment_Medical_History();

        //Fragment Pass Data
        fragment_profile.setUser(user);
        fragment_medicalInfo.setUser(user);

        //SharedPreferences
        sharedPreferences = getSharedPreferences("com.example.kinit.e_medicalrecord", Context.MODE_PRIVATE);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(user.getFullName());

        //ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        //TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
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

    void getUserData() {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("error", response);
                                JSONArray jsonArray = new JSONArray(response);
                                final int numberOfItemsInResp = jsonArray.length();
                                for (int x = 0; x < numberOfItemsInResp; x++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                                    if (recentCode.isEmpty()) {
                                        recentCode = jsonObject.getString("code");
                                    } else {
                                        switch (recentCode) {
                                            case "user_data":
                                                user.setUsername(jsonObject.getString("username"));
                                                user.setPassword(jsonObject.getString("password"));
                                                user.setFirstName(jsonObject.getString("first_name"));
                                                user.setMiddleName(jsonObject.getString("middle_name"));
                                                user.setLastName(jsonObject.getString("last_name"));
                                                user.setGender(jsonObject.getString("gender"));
                                                user.setBirthday(jsonObject.getString("birthday"));
                                                user.setContactNumber(jsonObject.getString("contact_number"));
                                                user.setAddress(jsonObject.getString("address"));
                                                user.setEmailAddress(jsonObject.getString("email_address"));
                                                user.setImage(jsonObject.getString("image"));
                                                user.setActive(Boolean.valueOf(jsonObject.getString("active")));
                                                break;
                                            case "patient_data":
                                                user.setOccupation(jsonObject.getString("occupation"));
                                                break;
                                            case "medicalStaff_data":
                                                user.setLicenseNumber(jsonObject.getString("license_number"));
                                                user.setMedical_staff_type(jsonObject.getString("user_type"));
                                                break;
                                        }
                                        recentCode = "";
                                    }
                                }
                                init();
                            } catch (JSONException e) {
                                Log.d("error", e.getMessage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.d("error", error.getStackTrace().toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getUserData");
                    params.put("device", "mobile");
                    params.put("user_id", Integer.toString(user.getUser_data_id()));
                    if (user.getMedical_staff_id() != 0)
                        params.put("medicalStaff_id", Integer.toString(user.getMedical_staff_id()));
                    if (user.getPatient_id() != 0)
                        params.put("patient_id", Integer.toString(user.getPatient_id()));
                    return params;
                }
            };
            Custom_Singleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }
    }

    //Start ViewPager Setter
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(fragment_profile, "About");
        viewPagerAdapter.addFragment(fragment_medicalInfo, "Medical History");
        this.viewPager.setAdapter(viewPagerAdapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //End ViewPager Setter

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_edit:
                break;

            case R.id.menu_logout:
                intent = new Intent(getBaseContext(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("LOGIN_AUTHENTICATION", false);
                editor.putInt("user_id", 0);
                editor.putInt("patient_id", 0);
                editor.putInt("medicalStaff_id", 0);
                editor.apply();
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void pass_userInfo(User user) {

    }

    void displayAlert(String title, String msg) {
        builder.setTitle(title);
        builder.setMessage(msg);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
