package com.example.kinit.e_medicalrecord.Profile.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Login.Login;
import com.example.kinit.e_medicalrecord.Settings.Activity.Settings;
import com.example.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.General.BusStation.Bus_Mode;
import com.example.kinit.e_medicalrecord.Profile.Bus.Bus_Search_Item;
import com.example.kinit.e_medicalrecord.Profile.Bus.Bus_Search_OnClick;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Profile.Class.User;
import com.example.kinit.e_medicalrecord.Profile.Class.Viewer;
import com.example.kinit.e_medicalrecord.General.Enum.Mode;
import com.example.kinit.e_medicalrecord.Profile.Fragment.Fragment_Search;
import com.example.kinit.e_medicalrecord.Profile.Fragment.Fragment_Profile;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.General.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    //Data Holder
    Intent intent;
    SharedPreferences sharedPreferences;

    //Primitive Data Types
    boolean isPatient = false, isMedicalStaff = false;
    String recentCode = "";
    Bitmap profPic;

    //Fragment
    Fragment_Profile fragment_profile;

    //App
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Custom_ProgressDialog progressDialog;

    //Classes
    Viewer viewer;
    User user;
    Mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_profile);
        progressDialog = new Custom_ProgressDialog(this);
        initIntent();
        getUserData();
    }

    public void initIntent() {
        user = new User();

        //Intent
        Intent intent = getIntent();
        user.user_data_id = intent.getIntExtra("user_id", 0);
        user.patient_id = intent.getIntExtra("patient_id", 0);
        user.medical_staff_id = intent.getIntExtra("medicalStaff_id", 0);
        if (user.patient_id != 0) {
            mode = Mode.PATIENT;
            isPatient = true;
        }
        if (user.medical_staff_id != 0) {
            mode = Mode.MEDICAL_STAFF;
            isMedicalStaff = true;
        }
        if (intent.hasExtra("viewer_user_id")) {
            viewer = new Viewer();
            viewer.name = intent.getStringExtra("viewer_name");
            viewer.user_id = intent.getIntExtra("viewer_user_id", 0);
            viewer.patient_id = intent.getIntExtra("viewer_patient_id", 0);
            viewer.medicalStaff_id = intent.getIntExtra("viewer_medicalStaff_id", 0);
            viewer.mode = Mode.values()[intent.getIntExtra("viewer_ordinal", 0)];
        }
    }

    void init() {
        //SharedPreferences
        sharedPreferences = getSharedPreferences("com.example.kinit.e_medicalrecord", Context.MODE_PRIVATE);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(user.getFullName());
        if (viewer != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Fragment
        fragment_profile = new Fragment_Profile();
        fragment_profile.setUser(user, mode, viewer, profPic);

        //FragmentManager
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragment_profile).commit();
    }

    void getUserData() {
        progressDialog.show("Loading...");
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL_USER_INFORMATION,
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
                                                user.username = jsonObject.getString("username");
                                                user.firstName = jsonObject.getString("first_name");
                                                user.middleName = jsonObject.getString("middle_name");
                                                user.lastName = jsonObject.getString("last_name");
                                                user.gender = jsonObject.getString("gender");
                                                user.contactNumber = jsonObject.getString("contact_number");
                                                user.emailAddress = jsonObject.getString("email_address");
                                                user.image = jsonObject.getString("image");
                                                user.active = jsonObject.getString("active").equals("1");
                                                break;
                                            case "patient_data":
                                                user.address = jsonObject.getString("address");
                                                user.setBirthday(jsonObject.getString("birthday"));
                                                user.occupation = jsonObject.getString("occupation");
                                                user.civilStatus = jsonObject.getString("civil_status");
                                                user.nationality = jsonObject.getString("nationality");
                                                user.religion = jsonObject.getString("religion");
                                                break;
                                            case "medicalStaff_data":
                                                user.licenseNumber = jsonObject.getString("license_number");
                                                user.medical_staff_type = jsonObject.getString("user_type");
                                                break;
                                        }
                                        recentCode = "";
                                    }
                                }
                                //if (!user.image.equals("null")) {
                                    //Log.d("error", user.image);
                                    //imgRqst(UrlString.getImageUrl(user.image));
                                //} else {
                                    init();
                                //}
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getUserData");
                    params.put("device", "mobile");
                    params.put("user_id", Integer.toString(user.user_data_id));
                    if (user.medical_staff_id != 0)
                        params.put("medicalStaff_id", Integer.toString(user.medical_staff_id));
                    if (user.patient_id != 0)
                        params.put("patient_id", Integer.toString(user.patient_id));
                    return params;
                }
            };
            Custom_Singleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    void imgRqst(String url) {
        try {
            ImageRequest imageRequest = new ImageRequest(url,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            profPic = response;
                            init();
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            Custom_Singleton.getInstance(this).addToRequestQueue(imageRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (viewer == null) {
            if (user.medical_staff_id != 0 && user.patient_id != 0) {
                getMenuInflater().inflate(R.menu.menu_profile_with_mode, menu);
            } else {
                getMenuInflater().inflate(R.menu.menu_profile, menu);
            }

            MenuItem searchItem = menu.findItem(R.id.menu_search);
            MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    fragmentManager.popBackStack();
                    return true;
                }
            });

            final SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    BusStation.getBus().post(new Bus_Search_Item(query, user.user_data_id, mode));
                    searchView.clearFocus();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_search:
                Fragment_Search fragmentSearch = new Fragment_Search();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frameLayout, fragmentSearch);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case R.id.menu_mode:
                Mode_AlertDialog();
                break;

            case R.id.menu_edit:
                intent = new Intent(this, Settings.class);
                intent.putExtra("user", user);
                startActivity(intent);
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

            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void Mode_AlertDialog() {
        final CharSequence modes[] = {"Patient", "Medical Staff"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Choose Mode");
        builder.setItems(modes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "You are now in patient mode", Toast.LENGTH_SHORT).show();
                        if (mode != Mode.PATIENT) {
                            mode = Mode.PATIENT;
                            BusStation.getBus().post(new Bus_Mode(mode));
                        }
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "You are now in medical staff mode", Toast.LENGTH_SHORT).show();
                        if (mode != Mode.MEDICAL_STAFF) {
                            mode = Mode.MEDICAL_STAFF;
                            BusStation.getBus().post(new Bus_Mode(mode));
                        }

                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        BusStation.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusStation.getBus().unregister(this);
    }

    @Subscribe
    public void onClickSearchItem(Bus_Search_OnClick busSearchOnClick){
        Intent intent = new Intent(this, Profile.class);
        intent.putExtra("user_id", busSearchOnClick.searchUser.userId);
        intent.putExtra("patient_id", busSearchOnClick.searchUser.patientId);
        intent.putExtra("medicalStaff_id", busSearchOnClick.searchUser.medicalStaffId);
        intent.putExtra("viewer_name", user.getFullName());
        intent.putExtra("viewer_patient_id", user.patient_id);
        intent.putExtra("viewer_user_id", user.user_data_id);
        intent.putExtra("viewer_medicalStaff_id", user.medical_staff_id);
        intent.putExtra("viewer_ordinal", mode.ordinal());
        startActivity(intent);
    }

    /*@Subscribe
    public void searchItemOnClick(Bus_Search_Item_OnClick busSearchItemOnClick) {
        Intent intent = new Intent(this, Profile.class);
        intent.putExtra("user_id", busSearchItemOnClick.user_id);
        intent.putExtra("patient_id", busSearchItemOnClick.patient_id);
        intent.putExtra("medicalStaff_id", busSearchItemOnClick.medicalStaff_id);
        intent.putExtra("viewer_name", user.getFullName());
        intent.putExtra("viewer_patient_id", user.patient_id);
        intent.putExtra("viewer_user_id", user.user_data_id);
        intent.putExtra("viewer_medicalStaff_id", user.medical_staff_id);
        intent.putExtra("viewer_ordinal", mode.ordinal());
        startActivity(intent);
    }*/
}
